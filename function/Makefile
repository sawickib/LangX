# sawickib, 2014-2023

ANTLR=../antlr-4.7.1-complete.jar

all: generate compile test

generate:
	java -jar $(ANTLR) -o output LangX.g4

compile:
	javac -cp $(ANTLR):output:. Main.java

test:
	java -cp $(ANTLR):output:. Main test.x > test.ll
	lli test.ll

parsetree: generate
	java -cp $(ANTLR):output:. org.antlr.v4.runtime.misc.TestRig LangX prog -gui test.x

clean:
	rm test.ll
	rm *.class
	rm -rf output

