import java.util.Stack;

class LLVMGenerator{
   
   static String header_text = "";
   static String main_text = "";
   static int tmp = 1;
   static int br = 0;

   static Stack<Integer> brstack = new Stack<Integer>();

   static void icmp(String id, String value){
     main_text += "%"+tmp+" = load i32, i32* %"+id+"\n";
     tmp++;
     main_text += "%"+tmp+" = icmp eq i32 %"+(tmp-1)+", "+value+"\n";
     tmp++;
   }

   static void ifstart(){
     br++;
     main_text += "br i1 %"+(tmp-1)+", label %true"+br+", label %false"+br+"\n";
     main_text += "true"+br+":\n";
     brstack.push(br);
   }

   static void ifend(){
     int b = brstack.pop();
     main_text += "br label %false"+b+"\n";
     main_text += "false"+b+":\n";
   }

   static void printf(String id){
      main_text += "%"+tmp+" = load i32, i32* %"+id+"\n";
      tmp++;
      main_text += "%"+tmp+" = call i32 (i8*, ...) @printf(i8* getelementptr inbounds ([4 x i8], [4 x i8]* @strp, i32 0, i32 0), i32 %"+(tmp-1)+")\n";
      tmp++;
   }

   static void scanf(String id){
      main_text += "%"+tmp+" = call i32 (i8*, ...) @__isoc99_scanf(i8* getelementptr inbounds ([3 x i8], [3 x i8]* @strs, i32 0, i32 0), i32* %"+id+")\n";
      tmp++;      
   }


   static void declare(String id){
      main_text += "%"+id+" = alloca i32\n";
   }

   static void assign(String id, String value){
      main_text += "store i32 "+value+", i32* %"+id+"\n";
   }

   static String generate(){
      String text = "";
      text += "declare i32 @printf(i8*, ...)\n";
      text += "declare i32 @__isoc99_scanf(i8*, ...)\n";
      text += "@strp = constant [4 x i8] c\"%d\\0A\\00\"\n";
      text += "@strs = constant [3 x i8] c\"%d\\00\"\n";
      text += header_text;
      text += "define i32 @main() nounwind{\n";
      text += main_text;
      text += "ret i32 0 }\n";
      return text;
   }

}
