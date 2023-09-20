import java.util.Random;

public class MatricesAleatorias
{
    private static int rows = 5;
    private static int columns = 5;
    private static int max = 32;
    private static int min = -32;

    public static void main(String[] args) throws InterruptedException
    {

        int[][] matrixA = generateRadomMatrix(rows, columns, min, max);
        int[][] matrixB = generateRadomMatrix(rows, columns, min, max);
        int[][] matrixC = addArraysSecuencial(matrixA,matrixB);
        int[][] matrixD = addArraysWithThreads(matrixA,matrixB);

        System.out.println("matrix A:");
        printMatrix(matrixA);

        System.out.println("\nmatrix B:");
        printMatrix(matrixB);

        boolean matrixAreEquals = arraysAreEquals(matrixC,matrixD);
        if(matrixAreEquals)
        {
            System.out.println("\nmatrix CC:");
            printMatrix(matrixD);
        }
        else
        {
            System.out.println("\nLas matrices no son iguales.");
        }

    }


    public static int[][] generateRadomMatrix(int rows, int columns, int min, int max)
    {
        int[][] matrix = new int[rows][columns];
        Random rand = new Random();

        for (int i = 0; i < rows; i++)
        {
            for (int j = 0; j < columns; j++)
            {
                matrix[i][j] = rand.nextInt((max - min + 1)) + min;
            }
        }

        return matrix;
    }

    public static void printMatrix(int[][] matrix)
    {
        int padding = 5;
        for (int i = 0; i < matrix.length; i++)
        {
            for (int j = 0; j < matrix[i].length; j++)
            {
                String number = String.format("%" + padding + "d", matrix[i][j]);
                System.out.print(number + " ");
            }
            System.out.println();
        }
    }

    public static int[][] addArraysSecuencial(int[][] matrixA, int[][] matrixB)
    {
        int rows = matrixA.length;
        int columns = matrixA[0].length;
        int[][] result = new int[rows][columns];

        for (int i = 0; i < rows; i++)
        {
            for (int j = 0; j < columns; j++)
            {
                result[i][j] = matrixA[i][j] + matrixB[i][j];
            }
        }

        return result;
    }

    public static int[][] addArraysWithThreads (int[][]matrixA, int[][] matrixB) throws InterruptedException
    {
        int[][] arrayAdd = new int[rows][columns];

        Thread[] threads = new Thread[rows];

        for (int i = 0; i < rows; i++)
        {
            final int row = i;
            threads[i] = new Thread(() ->
            {
                for (int j = 0; j < columns; j++)
                {
                    arrayAdd[row][j] = matrixA[row][j] + matrixB[row][j];
                }
            }
            );
            threads[i].start();
        }

        for (Thread thread : threads)
        {
            try
            {
                thread.join();
            }
            catch (InterruptedException e)
            {
                e.printStackTrace();
            }
        }
        return arrayAdd;
    }

     public static boolean arraysAreEquals(int[][] matrixA, int[][] matrixB)
     {
        if (matrixA.length != matrixB.length || matrixA[0].length != matrixB[0].length)
        {
            return false;
        }
        for (int i = 0; i < matrixA.length; i++)
        {
            for (int j = 0; j < matrixA[0].length; j++)
            {
                if (matrixA[i][j] != matrixB[i][j])
                {
                    return false;
                }
            }
        }
        return true;
    }
}