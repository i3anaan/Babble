grammar Babble;

program : mthd* ;

sequence : (expr ('.'+ expr)*)? '.'? ;

mthd : object=ID (ID ':' ID)+ '[' sequence ']'  # ClassMethodDefinition
     | (ID ':' ID)+ '[' sequence ']'            # GlobalMethodDefinition
     ;
     //TODO no-argument methods?

expr : ID ':=' expr                     # Assignment
     | expr method=ID                      # UnarySend
     | expr method=OPERATOR subexpr        # InfixSend
     | expr (ID ':' subexpr)+              # KeywordSend
     | (ID ':' subexpr)+                   # GlobalKeywordSend //TODO put in IRtree
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
     ;
//MAYBE: Add return statement (Currently last expression)
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

