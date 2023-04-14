
class LLVMGenerator{
   
   static String header_text = "";
   static String main_text = "";
   static int reg = 1;
   static int str = 1;

   static void printf_string(String id){
      main_text += "%"+reg+" = load i8*, i8** %"+id+"\n";
      reg++;      
      main_text += "%"+reg+" = call i32 (i8*, ...) @printf(i8* getelementptr inbounds ([4 x i8], [4 x i8]* @strps, i32 0, i32 0), i8* %"+(reg-1)+")\n";
      reg++;
   }

   static void printf_int(String id){
      main_text += "%"+reg+" = load i32, i32* %"+id+"\n";
      reg++;
      main_text += "%"+reg+" = call i32 (i8*, ...) @printf(i8* getelementptr inbounds ([4 x i8], [4 x i8]* @strpi, i32 0, i32 0), i32 %"+(reg-1)+")\n";
      reg++;
   }

   static void scanf(String id, int l){
      allocate_string("str"+str, l);
      main_text += "%"+id+" = alloca i8*\n";
      main_text += "%"+reg+" = getelementptr inbounds ["+(l+1)+" x i8], ["+(l+1)+" x i8]* %str"+str+", i64 0, i64 0\n";
      reg++;
      main_text += "store i8* %"+(reg-1)+", i8** %"+id+"\n"; 
      str++;
      main_text += "%"+reg+" = call i32 (i8*, ...) @__isoc99_scanf(i8* getelementptr inbounds ([5 x i8], [5 x i8]* @strs, i32 0, i32 0), i8* %"+(reg-1)+")\n";
      reg++;
   }


   static void declare_int(String id){
      main_text += "%"+id+" = alloca i32\n";
   }

   static void declare_string(String id){
      main_text += "%"+id+" = alloca i8*\n";
   }

   static void allocate_string(String id, int l){
      main_text += "%"+id+" = alloca ["+(l+1)+" x i8]\n";
   }

   static void assign_int(String id, String value){
      main_text += "store i32 "+value+", i32* %"+id+"\n";
   }

   static void assign_string(String id){  
      main_text += "store i8* %"+(reg-1)+", i8** %"+id+"\n";
   }

   static void constant_string(String content){
      int l = content.length()+1;     
      header_text += "@str"+str+" = constant ["+l+" x i8] c\""+content+"\\00\"\n";
      String n = "str"+str;
      LLVMGenerator.allocate_string(n, (l-1));
      main_text += "%"+reg+" = bitcast ["+l+" x i8]* %"+n+" to i8*\n";
      main_text += "call void @llvm.memcpy.p0i8.p0i8.i64(i8* align 1 %"+reg+", i8* align 1 getelementptr inbounds (["+l+" x i8], ["+l+" x i8]* @"+n+", i32 0, i32 0), i64 "+l+", i1 false)\n";
      reg++;
      main_text += "%ptr"+n+" = alloca i8*\n";
      main_text += "%"+reg+" = getelementptr inbounds ["+l+" x i8], ["+l+" x i8]* %"+n+", i64 0, i64 0\n";
      reg++;
      main_text += "store i8* %"+(reg-1)+", i8** %ptr"+n+"\n";    
      str++;
   }
   
   static void cons_string(String id, String constant, int length){
      int l = length+1;

    }


   static void load_int(String id){
      main_text += "%"+reg+" = load i32, i32* %"+id+"\n";
      reg++;
   }

   static void load_string(String id){
      main_text += "%"+reg+" = load i8*, i8** %"+id+"\n";
      reg++;
   }

   static void string_pointer(String id, int l){
      main_text += "%"+reg+" = getelementptr inbounds ["+(l+1)+" x i8], ["+(l+1)+" x i8]* %"+id+", i64 0, i64 0\n";
      reg++;
   }

   static void add_int(String id1, String id2){
      main_text += "%"+reg+" = add i32 "+id1+", "+id2+"\n";
      reg++;
   }
   
   static void add_string(String id1, int l1, String id2, int l2){
      allocate_string("str"+str, l1+l2);
      main_text += "%ptrstr"+str+" = alloca i8*\n";
      main_text += "%"+reg+" = getelementptr inbounds ["+(l1+l2+1)+" x i8], ["+(l1+l2+1)+" x i8]* %str"+str+", i64 0, i64 0\n";
      reg++;
      main_text += "store i8* %"+(reg-1)+", i8** %ptrstr"+str+"\n"; 
      main_text += "%"+reg+" = load i8*, i8** %ptrstr"+str+"\n";
      reg++;  
      main_text += "%"+reg+" = call i8* @strcpy(i8* %"+(reg-1)+", i8* "+id1+")\n";
      reg++;
      main_text += "%"+reg+" = call i8* @strcat(i8* %"+(reg-2)+", i8* "+id2+")\n";
      reg++;
      str++;      
   }
   
   static void int_to_string(String in, int lout){
      allocate_string("str"+str, lout);
      main_text += "%ptrstr"+str+" = alloca i8*\n";
      main_text += "%"+reg+" = getelementptr inbounds ["+(lout+1)+" x i8], ["+(lout+1)+" x i8]* %str"+str+", i64 0, i64 0\n";
      reg++;
      main_text += "store i8* %"+(reg-1)+", i8** %ptrstr"+str+"\n"; 
      main_text += "%"+reg+" = load i8*, i8** %ptrstr"+str+"\n";
      reg++;
      str++;  
      main_text += "%"+reg+" = call i32 (i8*, i8*, ...) @sprintf(i8* %"+(reg-1)+", i8* getelementptr inbounds ([3 x i8], [3 x i8]* @strspi, i32 0, i32 0), i32 "+in+")\n";
      reg++;
   }

   
   static void string_to_int(String in){
      main_text += "%"+reg+" = call i32 @atoi(i8* "+in+")\n";
      reg++;      
   }
   
   static String generate(){
      String text = "";
      text += "declare i32 @printf(i8*, ...)\n";
      text += "declare i32 @sprintf(i8*, i8*, ...)\n";
      text += "declare i8* @strcpy(i8*, i8*)\n";
      text += "declare i8* @strcat(i8*, i8*)\n";
      text += "declare i32 @atoi(i8*)\n";
      text += "declare i32 @__isoc99_scanf(i8*, ...)\n";
      text += "declare void @llvm.memcpy.p0i8.p0i8.i64(i8* noalias nocapture writeonly, i8* noalias nocapture readonly, i64, i1 immarg)\n";
      text += "@strps = constant [4 x i8] c\"%s\\0A\\00\"\n";
      text += "@strpi = constant [4 x i8] c\"%d\\0A\\00\"\n";
      text += "@strs = constant [5 x i8] c\"%10s\\00\"\n";
      text += "@strspi = constant [3 x i8] c\"%d\\00\"\n";
      text += header_text;
      text += "define i32 @main() nounwind{\n";
      text += main_text;
      text += "ret i32 0 }\n";
      return text;
   }

}
