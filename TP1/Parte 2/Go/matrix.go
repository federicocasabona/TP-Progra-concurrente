package main

import (
	"fmt"
	"math/rand"
	"sync"
)

const SIZE int = 5

var a [SIZE][SIZE]int
var b [SIZE][SIZE]int
var sc [SIZE][SIZE]int
var ss [SIZE][SIZE]int

func main() {
	for row := 0; row < SIZE; row++ {
		for column := 0; column < SIZE; column++ {
			a[row][column] = rand.Intn(64) - 32
			b[row][column] = rand.Intn(64) - 32
		}
	}
	printMatrix(a, "A")
	printMatrix(b, "B")

	concurrentSum()
	sequentialSum()

	printMatrix(sc, "SC")
	printMatrix(ss, "SS")
}

func sequentialSum() {
	for row := 0; row < SIZE; row++ {
		for column := 0; column < SIZE; column++ {
			ss[row][column] = a[row][column] + b[row][column]
		}
	}
}

func concurrentSum() {
	var wg sync.WaitGroup
	for row := 0; row < SIZE; row++ {
		wg.Add(1)
		go rowSum(row, &wg)
	}
	wg.Wait()

}

func rowSum(row int, wg *sync.WaitGroup) {
	defer wg.Done()
	for column := 0; column < SIZE; column++ {
		sc[row][column] = a[row][column] + b[row][column]
	}
}

func printMatrix(matrix [SIZE][SIZE]int, name string) {
	fmt.Printf("Matrix %s:\n", name)
	for row := 0; row < SIZE; row++ {
		for column := 0; column < SIZE; column++ {
			fmt.Printf("%3d\t", matrix[row][column])
		}
		fmt.Println("")
	}
	fmt.Println("")
}
