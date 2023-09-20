#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>
#include <sys/wait.h>

char fork_wait(char process, int child_count, char childs[]);

int main()
{
  char childs_a[] = {'B', 'C'};
  char childs_b[] = {'D', 'E'};
  char childs_c[] = {'F'};
  char childs_e[] = {'G', 'H'};

  printf("Proceso A PID: %d, PPID: %d\n", getpid(), getppid());

	char process = fork_wait('A', 2, childs_a);
  if(process == 'B')
  {
    printf("Proceso B PID: %d, PPID: %d\n", getpid(), getppid());
    char process = fork_wait('B', 2, childs_b);
    if(process == 'D')
    {
      printf("Proceso D PID: %d, PPID: %d\n", getpid(), getppid());
      sleep(10);
      return EXIT_SUCCESS;
    }
    if(process == 'E')
    {
      printf("Proceso E PID: %d, PPID: %d\n", getpid(), getppid());
      char process = fork_wait('E', 2, childs_e);
      if(process == 'G' || process == 'H')
      {
        printf("Proceso %c PID: %d, PPID: %d\n", process, getpid(), getppid());
        sleep(10);
        return EXIT_SUCCESS;
      }
    }
  }
  if(process == 'C')
  {
    printf("Proceso C PID: %d, PPID: %d\n", getpid(), getppid());
    char process = fork_wait('C', 1, childs_c);
    if(process == 'F')
    {
      printf("Proceso F PID: %d, PPID: %d\n", getpid(), getppid());
      sleep(10);
      return EXIT_SUCCESS;
    }
  }
	return EXIT_SUCCESS;
}

char fork_wait(char process, int child_count, char childs[])
{
  printf("Process %c PID: %d, PPID: %d\n",process, getpid(), getppid());

  for(int child=0; child<child_count; child++)
  {
    pid_t pid = fork();
    if(!pid)
    {
      return childs[child];
    }
  }
  for(int child=0; child<child_count; child++)
  {
    wait(NULL);
  }
  return process;
}
