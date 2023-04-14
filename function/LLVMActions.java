
import java.util.HashSet;
import java.util.Stack;

public class LLVMActions extends LangXBaseListener {

    HashSet<String> globalnames = new HashSet<String>();
    HashSet<String> functions = new HashSet<String>();
    HashSet<String> localnames = new HashSet<String>(); 
    String value, function;
    Boolean global;

    @Override 
    public void enterProg(LangXParser.ProgContext ctx) { 
       global = true;
    }

    @Override 
    public void exitProg(LangXParser.ProgContext ctx) { 
       LLVMGenerator.close_main();
       System.out.println( LLVMGenerator.generate() );
    }

    @Override 
    public void exitFparam(LangXParser.FparamContext ctx) {
       String ID = ctx.ID().getText();
       functions.add(ID); 
       function = ID;
       LLVMGenerator.functionstart(ID);
    }

    @Override
    public void enterFblock(LangXParser.FblockContext ctx) {
       global = false;
    }

    @Override
    public void exitFblock(LangXParser.FblockContext ctx) {
       if( ! localnames.contains(function) ){
          LLVMGenerator.assign(set_variable(function), "0");
       }
       LLVMGenerator.load( "%"+function );
       LLVMGenerator.functionend();
       localnames = new HashSet<String>();
       global = true;
    }

    @Override
    public void exitAssign(LangXParser.AssignContext ctx) { 
       String ID = ctx.ID().getText();
       LLVMGenerator.assign(set_variable(ID), value);
    }

    @Override 
    public void exitValue(LangXParser.ValueContext ctx) { 
       if( ctx.ID() != null ){
         String ID = ctx.ID().getText();     

         if( localnames.contains(ID) ) {
            LLVMGenerator.load( "%"+ID );
         } else if( globalnames.contains(ID) ) {
            LLVMGenerator.load( "@"+ID );
         } else if( functions.contains(ID) ) {
            LLVMGenerator.call(ID);
         } else {
            error(ctx.getStart().getLine(), "Unknown "+ID+ ": local > global > function");
         }
         value = "%"+(LLVMGenerator.tmp-1); 
       } 

       if( ctx.INT() != null ){
         value = ctx.INT().getText();       
       } 
    }



    @Override
    public void exitWrite(LangXParser.WriteContext ctx) {
       LLVMGenerator.printf(value);
    } 

    @Override
    public void exitCall(LangXParser.CallContext ctx) {
       LLVMGenerator.call(ctx.ID().getText());
    } 


    @Override
    public void exitRead(LangXParser.ReadContext ctx) {
       String ID = ctx.ID().getText();
       LLVMGenerator.scanf( set_variable(ID) );
    } 
  
    public String set_variable(String ID){
       String id;
       if( global ){
          if( ! globalnames.contains(ID) ) {
             globalnames.add(ID);
             LLVMGenerator.declare(ID, true);          
          }
          id = "@"+ID; 
       } else {
          if( ! localnames.contains(ID) ) {
             localnames.add(ID);
             LLVMGenerator.declare(ID, false);
          }
          id = "%"+ID; 
       }
       return id;
    }

    void error(int line, String msg){
       System.err.println("Error! Line "+line+", "+msg);
       System.exit(1);
   } 

}
