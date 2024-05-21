package com.craftinginterpreters.lox;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map; 

// Import all token types from the TokenType enum for easier access
import static com.craftinginterpreters.lox.TokenType.*;

public class Scanner {
    // Source code as simple string
    private final String source;
    // List ready to fill with tokens
    private final List<Token> tokens = new ArrayList<>();
    // Points to the first character in the lexeme being scanned
    private int start = 0;
    // Points to the character currently being considered
    private int current = 0;
    // Tracks what source line `current` is on so we can produce tokens that know their location
    private int line = 1;

    Scanner(String source) {
        this.source = source;
    }

    List<Token> scanTokens() {
        while (!isAtEnd()) {
            // We are at the beginning of the next lexeme.
            start = current;
            scanToken();
        }

        tokens.add(new Token(EOF, "", null, line));
        return tokens;
    }

    private void scanToken() {
        char c = advance();
        switch (c) {
            // One token lexemes
            case '(': addToken(LEFT_PAREN); break;
            case ')': addToken(RIGHT_PAREN); break;
            case '{': addToken(LEFT_BRACE); break;
            case '}': addToken(RIGHT_BRACE); break;
            case ',': addToken(COMMA); break;
            case '.': addToken(DOT); break;
            case '-': addToken(MINUS); break;
            case '+': addToken(PLUS); break;
            case ';': addToken(SEMICOLON); break;
            case '*': addToken(STAR); break;

            // Two token lexemes
            case '!': 
                addToken(match('=') ? BANG_EQUAL : BANG);
                break;
            case '=': 
                addToken(match('=') ? EQUAL_EQUAL : BANG);
                break;
            case '<': 
                addToken(match('=') ? LESS_EQUAL : LESS);
                break;
            case '>': 
                addToken(match('=') ? GREATER_EQUAL : GREATER);
                break;

            // Single-line comments
            case '/':
                if (match('/')) {
                    // Consume characters until newline is detected
                    while (peek() != '\n' && !isAtEnd()) advance();
                } else {
                    addToken(SLASH);
                }
                break;

            default:
                // Invalid characters are still consumed by `advance()` to prevent infinite loop
                Lox.error(line, "Unexpected character.");
                break;
        }
    }

    /**
     * ...
     * @param expected
     * @return
     */
    private boolean match (char expected) {
        if (isAtEnd()) return false;
        if (source.charAt(current) != expected) return false;

        current++;
        return true;
    }

    /**
     * "Lookahead" - Similar to `advance()` except it doesn't consume the character.
     * The rules of the lexical grammar dictate how much lookahead we need. Fortunately, 
     * most languages in wide use peek only one or two characters ahead.
     * @return
     */
    private char peek() {
        // If we've consumed all the characters, return null character
        if (isAtEnd()) return '\0';
        // Otherwise, return 
        return source.charAt(current);
    }

    /**
     * "EOF" - Checks to see if all characters in source string have been consumed.
     * @return
     */
    private boolean isAtEnd() {
        return current >= source.length();
    }

    /**
     * Consumes the next character in the source file and returns it.
     * @return
     */
    private char advance() {
        return source.charAt(current++);
    }

    /**
     * Overload for tokens without Literal values
     * @param type
     */
    private void addToken(TokenType type) {
        addToken(type, null);
    }

    /**
     * Grabs the text of the current lexeme and creates a new token for it
     * @param type
     * @param literal
     */
    private void addToken(TokenType type, Object literal) {
        String text = source.substring(start, current);
        tokens.add(new Token(type, text, literal, line));   
    }
}
