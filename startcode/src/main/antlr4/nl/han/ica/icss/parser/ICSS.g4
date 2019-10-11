grammar ICSS;

//--- LEXER: ---
// IF support:
IF: 'if';
BOX_BRACKET_OPEN: '[';
BOX_BRACKET_CLOSE: ']';


//Literals
TRUE: 'TRUE';
FALSE: 'FALSE';
PIXELSIZE: [0-9]+ 'px';
PERCENTAGE: [0-9]+ '%';
SCALAR: [0-9]+;

//Color value takes precedence over id idents
COLOR: '#' [0-9a-f] [0-9a-f] [0-9a-f] [0-9a-f] [0-9a-f] [0-9a-f];

//Specific identifiers for id's and css classes
ID_IDENT: '#' [a-z0-9\-]+;
CLASS_IDENT: '.' [a-z0-9\-]+;

//General identifiers
LOWER_IDENT: [a-z] [a-z0-9\-]*;
CAPITAL_IDENT: [A-Z]+[A-Za-z0-9_]*;

//All whitespace is skipped
WS: [ \t\r\n]+ -> skip;

//
OPEN_BRACE: '{';
CLOSE_BRACE: '}';
SEMICOLON: ';';
COLON: ':';
PLUS: '+';
MIN: '-';
MUL: '*';
ASSIGNMENT_OPERATOR: ':=';

//--- PARSER: ---

stylesheet: (variableassignment | stylerule)+ EOF;
stylerule: selector OPEN_BRACE body CLOSE_BRACE;

variableassignment: variablereference ASSIGNMENT_OPERATOR (literal | expression)+ SEMICOLON;
variablereference: CAPITAL_IDENT;

selector:   classselector |
            idselector |
            tagselector;

classselector:CLASS_IDENT;
idselector:ID_IDENT;
tagselector:LOWER_IDENT;
body: (ifclause | declaration)+;

ifclause: IF BOX_BRACKET_OPEN variablereference BOX_BRACKET_CLOSE conditionmet;

conditionmet: OPEN_BRACE (ifclause | declaration)+ CLOSE_BRACE;

declaration: property COLON value SEMICOLON;
property: LOWER_IDENT;

literal:    colorliteral |
            pixelliteral |
            percentageliteral |
            boolliteral |
            scalarliteral;

value:expression | operation;

colorliteral: COLOR;
pixelliteral: PIXELSIZE;
percentageliteral: PERCENTAGE;
boolliteral: TRUE | FALSE;
scalarliteral: SCALAR;

expression: variablereference | literal;
operation: addoperation | multiplyoperation | substractoperation;
addoperation: expression PLUS (expression | operation) ;
multiplyoperation: expression MUL (expression | operation);
substractoperation: expression MIN (expression | operation);
