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

stylesheet: (variables | elements)+ EOF;
variables: variable+;
elements: element+;

variable: variablename assignment (value | change)+ variableend;
variablename: CAPITAL_IDENT;
variableend: SEMICOLON;

element: elementname elementopen (condition | property)+ elementend;
elementname: (LOWER_IDENT | ID_IDENT | CLASS_IDENT);
elementopen: OPEN_BRACE;
elementend: CLOSE_BRACE;

condition:conditionstatement conditionmet;

conditionstatement: conditionopen variablename conditionend ;
conditionopen: IF BOX_BRACKET_OPEN;
conditionend: BOX_BRACKET_CLOSE;

conditionmet: elementopen (condition | property)+ elementend;

property: propertyname assignment (value | change)+ propertyend;
propertyname: LOWER_IDENT;
propertyend: SEMICOLON;

assignment: (COLON | ASSIGNMENT_OPERATOR);
value: (COLOR | PIXELSIZE | PERCENTAGE | TRUE | FALSE | CAPITAL_IDENT);
change: operator by;
operator:PLUS | MIN | MUL;
by: TRUE | FALSE | SCALAR | PIXELSIZE | PERCENTAGE;
