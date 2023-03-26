grammar LangX;

prog: ( stat? NEWLINE )* 
    ;

stat:	WRITE ID		#write
	| ID '=' INT		#assign
	| READ ID   		#read
   ;

WRITE:	'write' 
   ;

READ:	'read' 
   ;
   
STRING :  '"' ( ~('\\'|'"') )* '"'
    ;
ID:   ('a'..'z'|'A'..'Z')+
   ;

INT:   '0'..'9'+
    ;

NEWLINE:	'\r'? '\n'
    ;

WS:   (' '|'\t')+ { skip(); }
    ;
