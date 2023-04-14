
import java.util.HashSet;
import java.util.Stack;

public class LLVMActions extends LangXBaseListener {

    HashSet<String> variables = new HashSet<String>();
    String value;

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
    public void exitValue(LangXParser.ValueContext ctx) { 
       if( ctx.ID() != null ){
         String ID = ctx.ID().getText();     
         if( variables.contains(ID) ) {
            LLVMGenerator.load( ID );
            value = "%"+(LLVMGenerator.tmp-1); 
         } else {
            error(ctx.getStart().getLine(), "unknown variable "+ID);         
         }
       } 
       if( ctx.INT() != null ){
         value = ctx.INT().getText();       
       } 
    }

    @Override
    public void exitRepetitions(LangXParser.RepetitionsContext ctx) { 
       LLVMGenerator.repeatstart(value);
    }

    @Override
    public void exitBlock(LangXParser.BlockContext ctx) {
       if( ctx.getParent() instanceof LangXParser.RepeatContext ){
          LLVMGenerator.repeatend();
       }
    }
   
    @Override
    public void exitWrite(LangXParser.WriteContext ctx) {
       LLVMGenerator.printf(value);
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

   void error(int line, String msg){
       System.err.println("Error, line "+line+", "+msg);
       System.exit(1);
   } 

}
