grammar Babble;

sequence : defs? (stmt ('.'+ stmt)*)? '.'? ;

stmt : ID ':=' expr                     # Assignment
     | stmt ID                          # UnarySend
     | stmt OPERATOR expr               # InfixSend
     | stmt (ID ':' expr)+              # KeywordSend
     | (ID ':' expr)+                   # ObjKeywordSend
     | ID (ID ':' ID)+ '[' sequence ']' # MethodDefinition
//MAYBE: Add types to method definition     
     | expr                             # LoneExpr
     ;
//MAYBE: Add return statement (Currently last expression)

defs : '|' ID+ '|' ;

expr : ID                          # VarExpr
     | INTEGER                     # IntExpr
     | STRING                      # StrExpr
     | '#' ID                      # SymbolExpr
     | '[' (ID* '|')? sequence ']' # BlockExpr
     | '(' stmt ')'                # ParenExpr
     ;
//TODO: Array syntax ('{}')


ID: [A-Za-z][a-zA-Z0-9_]*;
INTEGER   : [0-9]+;
STRING    : '"' (.*?) '"';

//MAYBE: think of something smart for associativity
OPERATOR  : ('+' | '-' | '*' | '/' | '=' | '!')+;

COMMENT   : '/*' (.)*? '*/' -> skip;
SEPARATOR : [ \t\r\n] -> skip;

