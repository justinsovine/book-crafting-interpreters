package com.craftinginterpreters.lox;

/**
 * Represents a single token with type, lexeme, literal value, and line number.
 */
public class Token {
    final TokenType type; // The type of the token, represented by the TokenType enum
    final String lexeme; // The string representation of the token
    final Object literal; // An optional field that holds the literal value of the token (e.g. number or string)
    final int line; // The line number in the source code where the token appears

    // Constructor
    Token(TokenType type, String lexeme, Object literal, int line) {
        this.type = type;
        this.lexeme = lexeme;
        this.literal = literal;
        this.line = line;
    }

    // This method overrides the toString() method from the Object class
    // Useful for debugging/logging token contents
    
    public String toString() {
        return type + " " + lexeme + " " + literal;
    }
}
