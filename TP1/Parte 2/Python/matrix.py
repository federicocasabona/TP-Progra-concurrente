import random
import pprint
import threading

TAM = 5

a = []
b = []
cs = []  # secuencial
cc = []  # concurrente

def suma_filas(num_fila: int):
    """
    :param num_fila: entre 0 y 4 para este caso
    :return: None
    """
    for i in range(TAM):
        cc[num_fila][i] = a[num_fila][i] + b[num_fila][i]


# carga de valores en matrices
for i in range(TAM):
    a.append([random.randint(-32, 32) for _ in range(TAM)])
    b.append([random.randint(-32, 32) for _ in range(TAM)])
    cc.append([0 for _ in range(TAM)])  # La creo con valores para tener la estructura y poder hacer cc[x][y]

# algoritmo secuencial
for i in range(TAM):
    fila_nueva = []
    for j in range(TAM):
        fila_nueva.append(a[i][j] + b[i][j])
    cs.append(fila_nueva)

# algoritmo concurrente


hilos = [threading.Thread(target=suma_filas, args=(i,), name=f"suma_fila {i}") for i in range(TAM)]

for i in hilos:
    i.start()
    i.join()

# Python tiene algo llamado GIL (global interpreter lock), que significa solo un hilo puede estar haciendo operaciones.
# El beneficio de performance de tener threads en python solo se ve cuando está esperando por IO o por una conexion,
# para estos casos donde es solo hacer operaciones no hay diferencia


print("Matriz A:")
pprint.pp(a, )
print("\nMatriz B:")
pprint.pp(b)
print("\nMatriz CS (Secuencial):")
pprint.pp(cs)
print("\nMatriz CC (Concurrente):")
pprint.pp(cc)
print(f"\nLas matrices CC y CS son iguales? {cc == cs}")