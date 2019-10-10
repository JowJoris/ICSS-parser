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

variableassignment: variablereference ASSIGNMENT_OPERATOR (literal | operation)+ SEMICOLON;
variablereference: CAPITAL_IDENT;

selector: LOWER_IDENT | ID_IDENT | CLASS_IDENT;
body: (ifclause | declaration)+;

ifclause: IF BOX_BRACKET_OPEN variablereference BOX_BRACKET_CLOSE conditionmet;

conditionmet: OPEN_BRACE (ifclause | declaration)+ CLOSE_BRACE;

declaration: property COLON (literal | operation)+ SEMICOLON;
property: LOWER_IDENT;

literal: COLOR | PIXELSIZE | PERCENTAGE | TRUE | FALSE | CAPITAL_IDENT | SCALAR | variablereference;
operation: operator literal;
operator:PLUS | MIN | MUL;
