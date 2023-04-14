grammar LangX;

prog: ( stat? NEWLINE )* 
    ;

stat:	WRITE ID		#write
	| READ ID   		#read
 	| ID '=' expr		#assign
  ;

expr: value ADD value		#add
	| value		 	#single
   ;

value: ID
       | INT
       | STRING
   ;	

WRITE:	'write' 
   ;

READ:	'read' 
   ;
   
ID:   ('a'..'z'|'A'..'Z')+
   ;

INT:   '0'..'9'+
    ;

ADD: '+'
    ;

STRING :  '"' ( ~('\\'|'"') )* '"'
    ;

NEWLINE:	'\r'? '\n'
    ;

WS:   (' '|'\t')+ { skip(); }
    ;
