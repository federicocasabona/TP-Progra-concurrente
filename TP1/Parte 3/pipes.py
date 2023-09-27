import multiprocessing
import os
import sys
import pprint


def calculate_stats(wordlist):
    res = {
        "Total Caracteres": 0,
        "Total Letras": 0,
        "Total Digitos": 0,
        "Palabra mas Corta": None,
        "Palabra mas Larga": None,
    }

    for i in wordlist:
        res["Total Caracteres"] += len(i)
        res["Total Letras"] += amount_letters(i)
        res["Total Digitos"] += amount_digits(i)

    wordlist_by_len = sorted(wordlist, key=len)
    res["Palabra mas Corta"] = wordlist_by_len[0]
    res["Palabra mas Larga"] = wordlist_by_len[-1]

    return res


def amount_digits(word):
    res = 0
    for i in word:
        if i.isdigit():
            res += 1
    return res


def amount_letters(word):
    res = 0
    for i in word:
        if i.isalpha():
            res += 1
    return res


def main():
    CHILD = 0

    child_pipe, parent_pipe = multiprocessing.Pipe(duplex=True)

    process = os.fork()
    if process < 0:
        sys.exit("Error al crear el nuevo proceso")

    if process == CHILD:
        child_done = False
        words = []
        while not child_done:
            rec = child_pipe.recv()
            if rec.lower() == "close":
                child_done = True
            else:
                words.append(rec)
        child_pipe.send(calculate_stats(words))

        parent_pipe.close()
        child_pipe.close()
        os._exit(0)

    else:
        parent_done = False
        while not parent_done:
            inp = input(">>>")
            parent_pipe.send(inp)
            if inp.lower() == "close":
                parent_done = True
        stats = parent_pipe.recv()
        pprint.pp(stats)
        parent_pipe.close()
        child_pipe.close()
        os.wait()

if __name__ == "__main__":
    main()
