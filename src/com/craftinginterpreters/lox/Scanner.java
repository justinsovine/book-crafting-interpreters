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

    /**
     * Constructor method
     * @param source
     */
    Scanner(String source) {
        this.source = source;
    }

    /**
     * Scans all tokens from the source code and returns a list of tokens.
     * 
     * This method repeatedly calls {@link #scanToken()} until the end of the source is reached.
     * At the end, it adds an EOF token to signify the end of the input.
     * 
     * @return a list of tokens extracted from the source code.
     */
    List<Token> scanTokens() {
        while (!isAtEnd()) {
            // We are at the beginning of the next lexeme.
            start = current;
            scanToken();
        }

        tokens.add(new Token(EOF, "", null, line));
        return tokens;
    }

    /** 
     * Scans the next token from the source code and adds it to the list of tokens.
     * 
     * This method handles single-character tokens, two-character tokens, 
     * string literals, numeric literals, and skips whitespace and comments.
     * It is called by {@link #scanTokens()} to process each lexeme in the source code.
     */
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
            
            // "Whitespace" - Skip spaces, carriage returns, and tabs
            case ' ':
            // Skip carriage return
            case '\r':
            // Skip tab
            case '\t':
                break;
            
            // "Newline" - Increment the line number and skip
            case '\n':
                line++;
                break;
            
            // String literals
            case '"': string(); break;

            default:
                if (isDigit(c)) {
                    // It’s kind of tedious to add cases for every decimal digit.
                    // Stuffing it in the default case instead
                    number();
                } else {
                    // Invalid characters are still consumed by `advance()` to prevent infinite loop
                    Lox.error(line, "Unexpected character.");
                }
                break;
        }
    }

    /**
     * Scans a numeric literal from the source text. Handles both integers and floating-point numbers.
     */
    private void number() {
        // Look ahead until character is not a number
        while (isDigit(peek())) advance();

        // Look for a fractional part. This requires a second character of lookahead since we 
        // don't want to consume the . until we're sure there is a digit after it.
        if (peek() == '.' && isDigit(peekNext())) {
            // Consume the "."
            advance();

            // Look ahead until end of fractional
            while (isDigit(peek())) advance();
        }

        // Convert lexeme to its numeric value.
        // We’re using Java’s own parsing method to convert the lexeme to a real Java double. We can implement this  
        // ourselves, but, honestly, unless you’re trying to cram for an upcoming programming interview, 
        // it’s not worth your time.
        addToken(NUMBER, Double.parseDouble(source.substring(start, current)));
    }
    
    /**
     * Multi-line strings
     * 
     * Consumes characters until string is terminated
     */
    private void string() {
        // Look ahead until end of string (or file)
        while (peek() != '"' && !isAtEnd()) {
            // Increment line counter if newline detected
            if (peek() == '\n') line++;
            // Consume next character
            advance();
        }

        // String was not terminated. Throw error.
        if (isAtEnd()) {
            Lox.error(line, "Unterminated string.");
        }

        // Consume the closing "
        advance();

        // Trim the surrounding quotes.
        // Produces the actual string value that will be used later by the interpreter
        String value = source.substring(start + 1, current -1);
        addToken(STRING, value);
    }

    /**
     * Checks if the next character matches the expected character and advances the position if it does.
     * 
     * @param expected the character to match.
     * @return true if the next character matches and the position is advanced, false otherwise.
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
        // Otherwise, return character
        return source.charAt(current);
    }

    /**
     * Similar to `peek()` except it looks two characters ahead.
     * @return
     */
    private char peekNext() {
        // If less than two characters are left to consume, return null character
        if (current + 1 >= source.length()) return '\0';
        // Otherwise, return two characters ahead
        return source.charAt(current + 1);
    }

    /**
     * Checks to see if character is a number
     * @param c
     * @return
     */
    private boolean isDigit(char c) {
        return c >= '0' && c <= '9';
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
