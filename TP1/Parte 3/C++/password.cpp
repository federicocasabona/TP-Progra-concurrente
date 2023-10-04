%%writefile password.cpp
#include <iostream>
#include <string.h>
#include <thread>
#include <mutex>

std::mutex mtx;
int pos = 0;
char *args = NULL;
int result[50] = {};

int getNumberFromChar(char letter){
  if(letter >= 'a' && letter <= 'z'){
    return int(letter)-96;
  }
  if(letter >= 'A' && letter <= 'Z'){
    return int(letter)-64;
  }
  return -1;
}

void getPassword(int cant) {
  std::cout << "TID: " << std::this_thread::get_id() << std::endl;
  std::cout << "Cantidad de letras a procesar: " << cant << std::endl << std::endl;

  for(int i=0; i<cant; i++){
    mtx.lock();
    int number = getNumberFromChar(args[pos]);
    result[pos] = number;
    pos++;
    mtx.unlock();
  }
}

int main(int argc, char **argv) {

  args = argv[1];
  int cant1 = strlen(argv[1]) / 2;
  int cant2 = strlen(argv[1]) - cant1;

  std::thread thread_1(getPassword, cant1);
  std::thread thread_2(getPassword, cant2);

  thread_1.join();
  thread_2.join();

  std::cout << "password numbers: " << std::endl;
  for(int i=0; i<strlen(argv[1]); i++)
  {
    std::cout << result[i] << std::endl;
  }

  return EXIT_SUCCESS;
}
