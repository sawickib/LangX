grammar LangX;

prog: block 
; 

block: ( stat? NEWLINE )* 
; 

stat:	IF equal THEN blockif ENDIF 	#if
	| WRITE ID			#write
	| ID '=' INT			#assign
	| READ ID   			#read
;

blockif: block
;

equal: ID '==' INT
;

value: ID
       | INT
;	

WRITE:	'write' 

;
READ:	'read' 
;

IF:	'if'
;

THEN:	'then'
;

ENDIF:	'endif'
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
