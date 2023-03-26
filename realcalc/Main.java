// Intro to ANTLR+LLVM
// sawickib, 2014-2023

import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.*;

public class Main {
    public static void main(String[] args) throws Exception {
        CharStream input = CharStreams.fromFileName(args[0]);

        LangXLexer lexer = new LangXLexer(input);

        CommonTokenStream tokens = new CommonTokenStream(lexer);
        LangXParser parser = new LangXParser(tokens);

        ParseTree tree = parser.prog(); 

        //System.out.println(tree.toStringTree(parser));

        ParseTreeWalker walker = new ParseTreeWalker();
        walker.walk(new LLVMActions(), tree);

    }
}
