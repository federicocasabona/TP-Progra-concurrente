import os
import sys
import time


def pids_proceso(letra):
    print(f"Proceso {letra}. PID {os.getpid()}, PID PADRE {os.getppid()} \n")

# tuve que crear un archivo para ver el pid para el pstree porque aca en el colab
# ni con sys.stdout.flush() me manda los prints a la terminal hasta que termina de ejecutar
os.system("rm PID_*")
with open(f"PID_{os.getpid()}", "w") as f:
  pass

pid_ab = os.fork()

if pid_ab < 0:
    sys.exit("Error al crear el nuevo proceso")

if pid_ab:  # A
    pid_ac = os.fork()
    if pid_ac:  # A
        pids_proceso("A")
        print(os.wait())  # A waitfor B
        print(os.wait())  # A waitfor C
    else:  # C
        pid_cf = os.fork()
        if pid_cf:  # C
            pids_proceso("C")
            os.wait()  # C waitfor F
            os._exit(os.EX_OK)
        else:  # F
            pids_proceso("F")
            time.sleep(30)
            os._exit(os.EX_OK)

else:  # B
    pid_bd = os.fork()
    if pid_bd:  # B
        pid_be = os.fork()
        if pid_be:  # B
            pids_proceso("B")
            print(os.wait())  # B waitfor D
            print(os.wait())  # B waitfor E
            os._exit(os.EX_OK)
        else:  # E
            pid_eg = os.fork()
            if pid_eg:  # E
                pid_eh = os.fork()
                if pid_eh:  # E
                    pids_proceso("E")
                    print(os.wait())  # E waitfor G
                    print(os.wait())  # E waitfor G
                    os._exit(os.EX_OK)
                else:
                    pids_proceso("H")
                    time.sleep(30)
                    os._exit(os.EX_OK)
            else:  # G
                pids_proceso("G")
                time.sleep(30)
                os._exit(os.EX_OK)
    else:  # D
        pids_proceso("D")
        time.sleep(30)
        os._exit(os.EX_OK)
