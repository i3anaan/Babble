grammar Chitchat;

sequence : defs? (stmt ('.'+ stmt)*)? '.'? ;

stmt : ID ':=' expr                     # Assignment
     | stmt ID                          # UnarySend
     | stmt OPERATOR expr               # InfixSend
     | stmt (ID ':' expr)+              # KeywordSend
     | (ID ':' expr)+                   # ObjKeywordSend
     | ID (ID ':' ID)+ '[' sequence ']' # MethodDefinition
     | expr                             # LoneExpr
     ;

defs : '|' ID+ '|' ;

expr : ID                          # VarExpr
     | INTEGER                     # IntExpr
     | STRING                      # StrExpr
     | '#' ID                      # SymbolExpr
     | '[' (ID* '|')? sequence ']' # BlockExpr
     | '(' stmt ')'                # ParenExpr
     ;

ID: [A-Za-z][a-zA-Z0-9_]*;
INTEGER   : [0-9]+;
STRING    : '"' (.*?) '"';

OPERATOR  : ('+' | '-' | '*' | '/' | '=' | '!')+;

COMMENT   : '/*' (.)*? '*/' -> skip;
SEPARATOR : [ \t\r\n] -> skip;

