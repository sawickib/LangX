
import java.util.HashSet;
import java.util.Stack;

public class LLVMActions extends LangXBaseListener {

    HashSet<String> variables = new HashSet<String>();

    @Override 
    public void exitProg(LangXParser.ProgContext ctx) { 
       System.out.println( LLVMGenerator.generate() );
    }

    @Override
    public void exitAssign(LangXParser.AssignContext ctx) { 
       String ID = ctx.ID().getText();
       String INT = ctx.INT().getText();
       if( ! variables.contains(ID) ) {
          variables.add(ID);
          LLVMGenerator.declare(ID);          
       } 
       LLVMGenerator.assign(ID, INT);
    }

    @Override
    public void exitIf(LangXParser.IfContext ctx) { 
    }

    @Override
    public void enterBlockif(LangXParser.BlockifContext ctx) {
       LLVMGenerator.ifstart();
    }

    @Override
    public void exitBlockif(LangXParser.BlockifContext ctx) {
       LLVMGenerator.ifend();
    }
   
    @Override
    public void exitEqual(LangXParser.EqualContext ctx) { 
       String ID = ctx.ID().getText();
       String INT = ctx.INT().getText();
       if( variables.contains(ID) ) {
          LLVMGenerator.icmp( ID, INT );
       } else {
          ctx.getStart().getLine();
          System.err.println("Line "+ ctx.getStart().getLine()+", unknown variable: "+ID);
       }
    }

    @Override
    public void exitWrite(LangXParser.WriteContext ctx) {
       String ID = ctx.ID().getText();
       if( variables.contains(ID) ) {
          LLVMGenerator.printf( ID );
       } else {
          ctx.getStart().getLine();
          System.err.println("Line "+ ctx.getStart().getLine()+", unknown variable: "+ID);
       }
    } 

    @Override
    public void exitRead(LangXParser.ReadContext ctx) {
       String ID = ctx.ID().getText();
       if( ! variables.contains(ID) ) {
          variables.add(ID);
          LLVMGenerator.declare(ID);          
       } 
       LLVMGenerator.scanf(ID);
    } 


}
