grammar Babble;

program : mthd* ;

sequence : (stmt ('.'+ stmt)*)? '.'? ;

mthd : object=ID (ID ':' ID)+ '[' sequence ']'  # ClassMethodDefinition
     | (ID ':' ID)+ '[' sequence ']'            # GlobalMethodDefinition
     ;

stmt : ID ':=' expr                     # Assignment
     | stmt method=ID                   # UnarySend
     | stmt method=OPERATOR expr        # InfixSend
     | stmt (ID ':' expr)+              # KeywordSend
     | (ID ':' expr)+                   # ObjKeywordSend //TODO put in IRtree
//MAYBE: Add types to method definition     
     | expr                             # LoneExpr
     ;
//MAYBE: Add return statement (Currently last expression)

defs : '|' ID+ '|' ;

expr : value=INTEGER               # IntExpr
     | string=STRING               # StrExpr
     | TRUE                        # TrueExpr
     | FALSE                       # FalseExpr
     | NIL                         # NilExpr
     | ID                          # VarExpr
     | '#' ID                      # SymbolExpr
     | '[' (ID* '|')? sequence ']' # BlockExpr
     | '(' stmt ')'                # ParenExpr
     ;
//TODO: Array syntax ('{}')

TRUE : 'true';
FALSE : 'false';
NIL : 'nil';

ID: [A-Za-z][a-zA-Z0-9_]*;
INTEGER   : '-'? [0-9]+;
STRING    : '"' (.*?) '"';

//MAYBE: think of something smart for associativity
OPERATOR  : ('+' | '-' | '*' | '/' | '=' | '!' | ',' )+;


COMMENT   : '/*' (.)*? '*/' -> skip;
SEPARATOR : [ \t\r\n] -> skip;

