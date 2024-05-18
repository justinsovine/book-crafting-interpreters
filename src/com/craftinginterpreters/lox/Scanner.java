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
        char c = advance(); // ?
        switch (c) {
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
        }
    }

    /**
     * Helper function to tell us if we've consumed all of the characters
     * @return
     */
    private boolean isAtEnd() {
        return current >= source.length();
    }

    /**
     * Consumes the next character in the source file and returns it
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
