grammar Babble;

program : clazz* ;

clazz : classname=ID (EXTENDS ':' superclass=ID)? '[' (decls '.')? mthd* '].' ;

mthd : method=ID '[' sequence '].'                    # UnaryMethod
     | method=OPERATOR ':' arg=decl '[' sequence '].' # InfixMethod
     | (ID ':' decl)+ '[' sequence '].'               # KeywordMethod
     ;

sequence : (expr ('.'+ expr)*)? '.'? ;

expr : rcv=expr method=ID                  # UnarySend
     | rcv=expr method=OPERATOR arg=expr   # InfixSend
     | rcv=expr (ID ':' subexpr)+          # KeywordSend
     | (ID ':' subexpr)+                   # GlobalKeywordSend //TODO put in IRtree
     | ID ':=' expr                        # Assignment
     | subexpr                             # LoneExpr
     ;

subexpr : value=INTEGER                 # IntLit
        | string=STRING                 # StrLit
        | TRUE                          # TrueLit
        | FALSE                         # FalseLit
        | NIL                           # NilLit
        | ID                            # VarRef
        | '#' ID                        # SymbolLit
        | '[' (decl* '|')? sequence ']' # Block
        | '{' (expr ',')* expr? '}'     # ArrayLit
        | '(' expr ')'                  # ParenExpr
        | decls                         # DeclExpr
        ;
        
decls : '|' decl+ '|';
decl : ID;

//MAYBE: Add return statement (Currently last expression)

TRUE : 'true';
FALSE : 'false';
NIL : 'nil';

EXTENDS : 'extends';

ID: [A-Za-z][a-zA-Z0-9_\\]*;
INTEGER   : '-'? [0-9]+;
STRING    : '"' (.*?) '"';

OPERATOR  : ('+' | '-' | '*' | '/' | '=' | '!' | '<' | '>' )+;

COMMENT   : '/*' (.)*? '*/' -> skip;
SEPARATOR : [ \t\r\n] -> skip;
