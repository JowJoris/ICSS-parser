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
CAPITAL_IDENT: [A-Z] [A-Za-z0-9_]*;

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

variableassignment: variablereference ASSIGNMENT_OPERATOR (value | operation)+ SEMICOLON;
variablereference: CAPITAL_IDENT;

selector: LOWER_IDENT | ID_IDENT | CLASS_IDENT;
body: (condition | property)+;

condition: conditionstatement conditionmet;

conditionstatement: IF BOX_BRACKET_OPEN variablereference BOX_BRACKET_CLOSE ;

conditionmet: OPEN_BRACE (condition | property)+ CLOSE_BRACE;

property: propertyname COLON (value | operation)+ SEMICOLON;
propertyname: LOWER_IDENT;

value: COLOR | PIXELSIZE | PERCENTAGE | TRUE | FALSE | CAPITAL_IDENT | SCALAR | variablereference;
operation: operator value;
operator:PLUS | MIN | MUL;
