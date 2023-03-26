# Bartosz.Sawicki@pw.edu.pl
# 2014-2023

ANTLR=../antlr-4.7.1-complete.jar

all: generate compile test

generate:
	java -jar $(ANTLR) -o output LangX.g4

compile:
	javac -cp $(ANTLR):output:. Main.java

test:
	java -cp $(ANTLR):output:. Main test.x > test.ll
	lli-10 test.ll

clean:
	rm test.ll
	rm *.class
	rm -rf output

