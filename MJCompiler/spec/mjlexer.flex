package rs.ac.bg.etf.pp1;

import java_cup.runtime.Symbol;

%%

%{
	// ukljucivanje informacije o poziciji tokena
	private Symbol new_symbol(int type) {
		return new Symbol(type, yyline+1, yycolumn);
	}
	
	// ukljucivanje informacije o poziciji tokena
	private Symbol new_symbol(int type, Object value) {
		return new Symbol(type, yyline+1, yycolumn, value);
	}
%}

%cup
%line
%column

%xstate COMMENT

%eofval{
	return new_symbol(sym.EOF);
%eofval}

%%


" " 	{ }
"\b" 	{ }
"\t" 	{ }
"\r\n" 	{ }
"\f" 	{ }

"program" { return new_symbol(sym.PROG, yytext());}
"print" 	{ return new_symbol(sym.PRINT, yytext()); }
"return" 	{ return new_symbol(sym.RETURN, yytext()); }
"void" 		{ return new_symbol(sym.VOID, yytext()); }
"if"		{ return new_symbol(sym.IF, yytext());	}
"else"		{ return new_symbol(sym.ELSE, yytext());	}
"extends"	{ return new_symbol(sym.EXTENDS, yytext());	}
"super"		{ return new_symbol(sym.SUPER, yytext());	}
"do"		{ return new_symbol(sym.DO, yytext());	}
"while"		{ return new_symbol(sym.WHILE, yytext());	}
"const"		{ return new_symbol(sym.CONST, yytext());	}
"class"		{ return new_symbol(sym.CLASS, yytext());	}
"break"		{ return new_symbol(sym.BREAK, yytext());	}
"continue"	{ return new_symbol(sym.CONTINUE, yytext());	}
"read"		{ return new_symbol(sym.READ, yytext());	}
"enum"		{ return new_symbol(sym.ENUM, yytext());	}
"new"		{ return new_symbol(sym.NEW, yytext());	}
"this"		{ return new_symbol(sym.THIS, yytext());	}
"+" 		{ return new_symbol(sym.PLUS, yytext()); }
"++"		{ return new_symbol(sym.DOUBLEPLUS, yytext());	}
"-"			{ return new_symbol(sym.MINUS, yytext());	}
"--"		{ return new_symbol(sym.DOUBLEMINUS, yytext());	}
"=" 		{ return new_symbol(sym.EQUAL, yytext()); }
"=="		{ return new_symbol(sym.DOUBLEEQUAL, yytext());	}
"!="		{ return new_symbol(sym.NOTEQUAL, yytext());	}
">"			{ return new_symbol(sym.GREATERTHAN, yytext());	}
">="		{ return new_symbol(sym.GREATEREQUAL, yytext());	}
"<"			{ return new_symbol(sym.LESSTHAN, yytext());	}
"<="		{ return new_symbol(sym.LESSEQUAL, yytext());	}
"*"			{ return new_symbol(sym.MUL, yytext());	}
"/"			{ return new_symbol(sym.DIVIDE, yytext());	}
"%"			{ return new_symbol(sym.MOD, yytext());	}
"&&"		{ return new_symbol(sym.LOGICALAND, yytext());	}
"||"		{ return new_symbol(sym.LOGICALOR, yytext());	}
"."			{ return new_symbol(sym.DOT, yytext());	}
":"			{ return new_symbol(sym.COLON, yytext());	}
"["			{ return new_symbol(sym.LSQUAREBRACE, yytext());	}
"]"			{ return new_symbol(sym.RSQUAREBRACE, yytext());	}
";" 		{ return new_symbol(sym.SEMI, yytext()); }
"," 		{ return new_symbol(sym.COMMA, yytext()); }
"(" 		{ return new_symbol(sym.LPAREN, yytext()); }
")" 		{ return new_symbol(sym.RPAREN, yytext()); }
"{" 		{ return new_symbol(sym.LBRACE, yytext()); }
"}"			{ return new_symbol(sym.RBRACE, yytext()); }

"//" {yybegin(COMMENT);}
<COMMENT> . {yybegin(COMMENT);}
<COMMENT> "\r\n" { yybegin(YYINITIAL); }

[0-9]+  { return new_symbol(sym.NUMCONST, new Integer (yytext())); }
"true" | "false" 	{ return new_symbol(sym.BOOLCONST, yytext());	}
([a-z]|[A-Z])[a-z|A-Z|0-9|_]* 	{return new_symbol (sym.IDENT, yytext()); }
"'" [ -~] "'" 	{return new_symbol (sym.CHARCONST, yytext()); }

. { System.err.println("Leksicka greska ("+yytext()+") u liniji "+(yyline+1)); }
