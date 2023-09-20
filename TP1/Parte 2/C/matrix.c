#include <stdio.h>
#include <stdlib.h>
#include <time.h>
#include <pthread.h>

#define MAX 32
#define MIN -32
#define ROWS 5
#define COLUMNS 5
#define THREADS 5

typedef struct p
{
  int *A;
  int *B;
  int *result;
  int columns;
}parameters;

int GenerateRandomNumber(int min, int max);
int** GenerateMatrix(int rows, int columns);
void PrintMatrix(int **matrix, int rows, int columns, char *name);
void FillMatrix(int** matrix, int rows, int columns);
void SumSecuencial(int** A, int** B, int** result, int rows, int columns);
void SumConcurrent(int** A, int** B, int** result, int rows, int columns, int threads);
void CompareMatrix(int **A, int **b, int rows, int columns);
void* SumRow(void *arguments);
void FreeMatrix(int **matrix, int rows);

int main(int argc, char *argv[])
{
    srand(time(NULL));

    int **A = GenerateMatrix(ROWS, COLUMNS);
    int **B = GenerateMatrix(ROWS, COLUMNS);
    int **CS = GenerateMatrix(ROWS, COLUMNS);
    int **CC = GenerateMatrix(ROWS, COLUMNS);

    FillMatrix(A, ROWS, COLUMNS);
    FillMatrix(B, ROWS, COLUMNS);
    SumSecuencial(A, B, CS, ROWS, COLUMNS);
    SumConcurrent(A, B, CC, ROWS, COLUMNS, THREADS);

    PrintMatrix(A, ROWS, COLUMNS, "A");
    PrintMatrix(B, ROWS, COLUMNS, "B");
    PrintMatrix(CS, ROWS, COLUMNS, "CS");
    PrintMatrix(CC, ROWS, COLUMNS, "CC");

    CompareMatrix(CC, CS, ROWS, COLUMNS);

    FreeMatrix(A, ROWS);
    FreeMatrix(B, ROWS);
    FreeMatrix(CC, ROWS);
    FreeMatrix(CS, ROWS);
}


int** GenerateMatrix(int rows, int columns)
{
   int **result = (int **) calloc(rows, sizeof (int *) * rows);
   for(int i = 0; i < rows; i++)
   {
     result[i] = (int *) calloc(columns, sizeof (int) * columns);
   }
   return result;
}


void FillMatrix(int** matrix, int rows, int columns)
{
  for(int i = 0; i < rows; i++)
     for(int j= 0; j < columns; j++)
       matrix[i][j] = GenerateRandomNumber(MIN, MAX);
}


int GenerateRandomNumber(int min, int max)
{
   int mod = (((max) + 1)-(min));
   int r = rand() % mod + min;
   return r;
}


void PrintMatrix(int **matrix, int rows, int columns, char *name)
{
     printf("\n\nMatrix %s:\n\n", name);
     for(int i = 0; i < rows; i++)
     {
       for(int j= 0; j < columns; j++)
         printf(" %d ", matrix[i][j]);
       printf("\n");
     }
}


void SumSecuencial(int** A, int** B, int** result, int rows, int columns)
{
  for(int i = 0; i < rows; i++)
    for(int j = 0; j < columns; j++)
      result[i][j] = A[i][j] + B[i][j];
}


void SumConcurrent(int** A, int** B, int** result, int rows, int columns, int threads)
{
  pthread_t t_id[threads];
  parameters params[threads];
  for(int i = 0; i < threads; i++)
  {
    params[i].A = A[i];
    params[i].B = B[i];
    params[i].result = result[i];
    params[i].columns = columns;
    pthread_create(&t_id[i], NULL, SumRow, &params[i]);
  }
  for(int i = 0; i < threads; i++)
  {
    pthread_join(t_id[i], NULL);
  }
}


void* SumRow(void *arguments)
{
  parameters *params  = (parameters*)arguments;
  for(int i = 0; i < params->columns; i++)
    params->result[i] = params->A[i] + params->B[i];
}


void FreeMatrix(int **matrix, int rows)
{
  for(int i = 0; i < rows; i++)
  {
    free(matrix[i]);
  }
  free(matrix);
}


void CompareMatrix(int **A, int **B, int rows, int columns)
{
  int iguales = 1;
  for(int i = 0; i < rows; i++)
    for(int j = 0; j < columns; j++)
      if(A[i][j] != B[i][j])
      {
         iguales = 0;
         i = rows;
         j = columns;
      }
  if(iguales)
  {
    printf("\n\nSON IGUALES");
  }
  else
  {
    printf("\n\nSON DISTINTOS");
  }
}