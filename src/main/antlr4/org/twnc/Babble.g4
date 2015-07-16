grammar Babble;

// Top most rule of a Babble program.
program : clazz* ;

// Rule for class definitions inside a Babble program.
clazz : classname=ID (EXTENDS ':' superclass=ID)? '[' (decls '.')? mthd* ']' '.' ;

// Several types of method definition inside a Babble class.
mthd : method=ID '[' sequence ']' '.'                    # UnaryMethod
     | method=OPERATOR ':' arg=decl '[' sequence ']' '.' # InfixMethod
     | (ID ':' decl)+ '[' sequence ']' '.'               # KeywordMethod
     ;

// A sequence of expressions.
sequence : (expr ('.'+ expr)*)? '.'? ;

// Several rules for expressions.
// Where non-(sub)expressions offer functionality, 
// expressions access and combine these functionalities into a working program.
expr : rcv=expr method=ID                  # UnarySend
     | rcv=expr method=OPERATOR arg=expr   # InfixSend
     | rcv=expr (ID ':' subexpr)+          # KeywordSend
     | ID ':=' expr                        # Assignment
     | subexpr                             # LoneExpr
     ;

// Sub expressions, in the AST these are treated like normal expressions.
// These differ from the expr rule to disallow sending messages as arguments.
subexpr : value=INTEGER                 # IntLit
        | string=STRING                 # StrLit
        | TRUE                          # TrueLit
        | FALSE                         # FalseLit
        | NIL                           # NilLit
        | ID                            # VarRef
        | '#' ID                        # SymbolLit
        | '[' sequence ']'              # Block
        | '{' (expr ',')* expr? '}'     # ArrayLit
        | '(' expr ')'                  # ParenExpr
        | decls                         # DeclExpr
        ;

// Rules for variable declaration.
decls : '|' decl+ '|';
decl : ID;

// Rules for literals.
TRUE : 'true';
FALSE : 'false';
NIL : 'nil';

EXTENDS : 'extends';

ID: [A-Za-z][a-zA-Z0-9_\\]*;
INTEGER   : '-'? [0-9]+;
STRING    : '"' (.*?) '"';

OPERATOR  : ('+' | '-' | '*' | '/' | '=' | '!' | '<' | '>' )+;

// Ignore comments and whitespace.
COMMENT   : '/*' (.)*? '*/' -> skip;
SEPARATOR : [ \t\r\n] -> skip;
