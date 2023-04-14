
import java.util.HashMap;
import java.util.Stack;

enum VarType{ INT, STRING, UNKNOWN }

class Value{ 
   public String name;
   public VarType type;
   public int length;
   public Value( String name, VarType type, int length ){
      this.name = name;
      this.type = type;
      this.length = length;
   }
}

public class LLVMActions extends LangXBaseListener {

    HashMap<String, Value> variables = new HashMap<String, Value>();
    Stack<Value> stack = new Stack<Value>();
    
    static int BUFFER_SIZE = 16;

    @Override
    public void exitAssign(LangXParser.AssignContext ctx) { 
       String ID = ctx.ID().getText();
       Value v = stack.pop();
       if( !variables.containsKey(ID) ) {
          variables.put(ID, v);
          if( v.type == VarType.INT ){
             LLVMGenerator.declare_int(ID);
          } 
          if( v.type == VarType.STRING ){
             LLVMGenerator.declare_string(ID);
          } 
       }   
       if( v.type == VarType.INT ){
         LLVMGenerator.assign_int(ID, v.name);
       } 
       if( v.type == VarType.STRING ){
         LLVMGenerator.assign_string(ID);
       } 
    }

    @Override 
    public void exitProg(LangXParser.ProgContext ctx) { 
       System.out.println( LLVMGenerator.generate() );
    }

    @Override 
    public void exitValue(LangXParser.ValueContext ctx) { 
       if( ctx.ID() != null ){
         String ID = ctx.ID().getText();     
         if( variables.containsKey(ID) ) {
            Value v = variables.get( ID );
            if( v.type == VarType.INT ){
               LLVMGenerator.load_int( ID );
            }
            if( v.type == VarType.STRING ){
               LLVMGenerator.load_string( ID );
            }
            stack.push( new Value("%"+(LLVMGenerator.reg-1), v.type, v.length)); 
         } else {
            error(ctx.getStart().getLine(), "unknown variable "+ID);         
         }
       } 
       if( ctx.INT() != null ){
         stack.push( new Value(ctx.INT().getText(), VarType.INT, 0) );
       } 
       if( ctx.STRING() != null ){
         String tmp = ctx.STRING().getText(); 
         String content = tmp.substring(1, tmp.length()-1);
         LLVMGenerator.constant_string(content);
         String n = "ptrstr"+(LLVMGenerator.str-1);
         stack.push( new Value(n, VarType.STRING, content.length()) );
       } 
    }
    
    @Override 
    public void exitAdd(LangXParser.AddContext ctx) { 
       Value v2 = stack.pop();
       Value v1 = stack.pop();
       if( v1.type==VarType.INT && v2.type==VarType.INT ){
          LLVMGenerator.add_int(v1.name, v2.name );
          Value v = new Value("%"+(LLVMGenerator.reg-1), VarType.INT, 0);
          stack.push(v); 
       } 
       if( v1.type==VarType.STRING && v2.type==VarType.STRING ){
          LLVMGenerator.add_string(v1.name, v1.length, v2.name, v2.length);
          Value v = new Value("%"+(LLVMGenerator.reg-3), VarType.STRING, v1.length);
          stack.push(v); 
       }    
       if( v1.type==VarType.STRING && v2.type==VarType.INT ){
          LLVMGenerator.int_to_string(v2.name, BUFFER_SIZE);
          v2.name = "%"+(LLVMGenerator.reg-2);
          LLVMGenerator.add_string(v1.name, v1.length, v2.name, BUFFER_SIZE);
          Value v = new Value("%"+(LLVMGenerator.reg-3), VarType.STRING, v1.length);
          stack.push(v); 
       }
       if( v1.type==VarType.INT && v2.type==VarType.STRING ){
          LLVMGenerator.string_to_int(v2.name);
          LLVMGenerator.add_int(v1.name, "%"+(LLVMGenerator.reg-1));       
          Value v = new Value("%"+(LLVMGenerator.reg-1), VarType.INT, 0);
          stack.push(v); 
       }
    }

    @Override
    public void exitWrite(LangXParser.WriteContext ctx) {
       String ID = ctx.ID().getText();
       if( variables.containsKey(ID) ) {
          Value v = variables.get( ID );
          if( v.type != null ) {
             if( v.type == VarType.INT ){
                LLVMGenerator.printf_int( ID );
             }
             if( v.type == VarType.STRING ){
                LLVMGenerator.printf_string( ID );
             }
          }  
       } else {
          error(ctx.getStart().getLine(), "unknown variable");
       }       
    } 

    @Override
    public void exitRead(LangXParser.ReadContext ctx) {
       String ID = ctx.ID().getText();
       Value v = new Value(ID, VarType.STRING, BUFFER_SIZE-1); 
       variables.put(ID, v);
       LLVMGenerator.scanf(ID, BUFFER_SIZE);
    } 

   void error(int line, String msg){
       System.err.println("Error, line "+line+", "+msg);
       System.exit(1);
   } 
       
}
