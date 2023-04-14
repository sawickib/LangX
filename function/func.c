#include<stdio.h>

int z=4;

int func(){
  int y,z;
  z = 2;
  return z;
}

int main(){
  int a;
  z = 3;
  z=func();
  printf("%d\n",z);
}

