grammar LangX;

prog: block 
; 

block: ( stat? NEWLINE )* 
; 

stat:	REPEAT repetitions block ENDREPEAT		#repeat
	| WRITE value					#write
	| ID '=' INT					#assign
	| READ ID   					#read
;

repetitions: value
;

value: ID
	| INT
;

WRITE:	'write' 

;
READ:	'read' 
;

REPEAT: 'repeat'
;

ENDREPEAT: 'endrepeat'
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
