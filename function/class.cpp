#include<stdio.h>

class Klasa
{
	private:
		int x;
	public:
		int get_x() {
			return x;
		}
};

int main(){
  Klasa k;
  printf("%d\n",k.get_x());
}

