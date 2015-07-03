grammar Babble;

program : clazz* ;

clazz : classname=ID '[' mthd* '].'
	  ;

mthd : (ID ':' ID)+ '[' sequence '].'   # KeywordMethod
     | ID '[' sequence '].'             # UnaryMethod
     ;

sequence : (expr ('.'+ expr)*)? '.'? ;

expr : ID ':=' expr                        # Assignment
     | rcv=expr method=ID                  # UnarySend
     | rcv=expr method=OPERATOR arg=expr   # InfixSend
     | rcv=expr (ID ':' subexpr)+          # KeywordSend
     | (ID ':' subexpr)+                   # GlobalKeywordSend
//MAYBE: Add types to method definition
     | subexpr                             # LoneExpr
     ;

subexpr : value=INTEGER            # IntLit
     | string=STRING               # StrLit
     | TRUE                        # TrueLit
     | FALSE                       # FalseLit
     | NIL                         # NilLit
     | ID                          # VarRef
     | '#' ID                      # SymbolLit
     | '[' (ID* '|')? sequence ']' # Block
     | '(' expr ')'                # ParenExpr
     | '|' decl+ '|'                 # declExpr
     ;
     
decl : ID;
//MAYBE: Add return statement (Currently last expression)
//TODO: Array syntax ('{}')

TRUE : 'true';
FALSE : 'false';
NIL : 'nil';

ID: [A-Za-z][a-zA-Z0-9_]*;
INTEGER   : '-'? [0-9]+;
STRING    : '"' (.*?) '"';

//MAYBE: think of something smart for associativity
OPERATOR  : ('+' | '-' | '*' | '/' | '=' | '!' | ',' | '<' | '>' )+;

COMMENT   : '/*' (.)*? '*/' -> skip;
SEPARATOR : [ \t\r\n] -> skip;

