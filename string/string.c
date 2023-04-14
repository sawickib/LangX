#include <stdio.h>
#include <string.h>
#include <stdlib.h>


int main () {
   char str[16];
//   char ab[] = "abc";
//   char b[] = "def";
//   strcpy(str, a);
//   char *z = ab;
//   strcat(str, b);
//   char* abc;
//   abc = str+3;
   int aaa = 2323;
   sprintf(str, "%d", aaa);
   printf("Enter x: ");
   scanf("%10s", str);

   printf("%s\n", str);

   int x;
  x = atoi(str);
   printf("Entered x: %d\n", x);
   
//   double f;
//   f = atof(str1);
//  printf("Entered x: %f\n", f);
   
   return(0);
}
