package com.craftinginterpreters.lox;

/**
 * Represents the different types of tokens that can be identified by the scanner.
 * These tokens are the basic building blocks of the language's syntax and are used
 * by the parser to construct the Abstract Syntax Tree (AST).
 */
enum TokenType {
    // Single-character tokens
    LEFT_PAREN, RIGHT_PAREN, LEFT_BRACE, RIGHT_BRACE,
    COMMA, DOT, MINUS, PLUS, SEMICOLON, SLASH, STAR,

    // One or two character tokens.
    BANG, BANG_EQUAL,
    EQUAL, EQUAL_EQUAL,
    GREATER, GREATER_EQUAL,
    LESS, LESS_EQUAL,

    /**
     * Literals. 
     * Note: When the scanner processes a literal value (like a number or a string), it reads each character to identify 
     * the complete literal. During this process, it also transforms the textual representation of that literal into an 
     * actual runtime object that the interpreter will use during program execution. This way, the interpreter doesn't 
     * have to re-process the text; it can directly work with the ready-to-use values. 
     */
    IDENTIFIER, STRING, NUMBER,

    // Keywords.
    AND, CLASS, ELSE, FALSE, FUN, FOR, IF, NIL, OR,
    PRINT, RETURN, SUPER, THIS, TRUE, VAR, WHILE,

    EOF
}
