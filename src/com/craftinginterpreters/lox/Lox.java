package com.craftinginterpreters.lox;

/** 
 * Java.io is a Java package that provides for system input and output through 
 * data streams, serialization, and the file system
 */

// Importing BufferedReader for reading text from a character-input stream
import java.io.BufferedReader;          
// Importing IOException class for handling I/O errors
import java.io.IOException;             
// Importing InputStreamReader to bridge byte streams to character streams.
// It reads bytes and decodes them into characters using a specified charset.
import java.io.InputStreamReader;        


/**
 * Java NIO (New Input/Output) is a high-performance networking and file handling API 
 * and structure which works as an alternative IO API
 */

// Importing Charset class to handle character encoding
import java.nio.charset.Charset;
// Importing Files class for file operations
import java.nio.file.Files;
// Importing Paths utility class to operate on file paths
import java.nio.file.Paths;             
// Importing List collection for storing lists of objects
import java.util.List;

/**
 * The java.util package is a built-in package in Java that contains various utility classes and interfaces.
 */

// A simple text scanner which can parse primitive types and strings using regular expressions.
// We won't actually be using this as I assumed we were as we're writing our own!
//import java.util.Scanner;


/**
 * Main class for the Lox programming language interpreter
 */
public class Lox {
    // So that we don’t try to execute code that has a known error
    static boolean hadError = false;

    /**
     * Main method
     * @param args
     * @throws IOException
     */
    public static void main(String[] args) throws IOException {
        if (args.length > 1) {
            // Informs the user about correct usage if the argument count is incorrect
            System.out.println("Usage: jlox [script]");
            // Exits the program with status code 64 (UNIX standard for command line usage error)
            System.exit(64); 
        } else if (args.length == 1) {
            runFile(args[0]); // Runs the file specified by the user
        } else {
            runPrompt(); // Starting an interactive prompt if no arguments were provided
        }
    }

    /**
     * File reading and execution logic
     * @param path
     * @throws IOException
     */
    private static void runFile(String path) throws IOException {
        byte[] bytes = Files.readAllBytes(Paths.get(path)); // Reads all the bytes from a file
        // Constructs a new String object by decoding the specified array of bytes using the specified charset and then
        // passes it to the run() method.
        run(new String(bytes, Charset.defaultCharset()));

        // Indicate an error in the exit code.
        // Code 65 is UNIX standard for a data format error
        if (hadError) System.exit(65); 
    }

    /**
     * Interactive prompt logic
     * @throws IOException
     */
    private static void runPrompt() throws IOException {
        // An InputStreamReader is a bridge from byte streams to character streams: It reads bytes and decodes them 
        // into characters using a specified charset.
        InputStreamReader input = new InputStreamReader(System.in);
        // Reads text from a character-input stream, buffering characters so as to provide for the efficient reading 
        // of characters, arrays, and lines.
        BufferedReader reader = new BufferedReader(input);

        // REPL loop
        // [R]ead a line of input, [E]valuate it, [P]rint the result, then [L]oop and do it all over again
        for(;;) {
            System.out.print("lox> "); // Prompt
            String line = reader.readLine(); // Read buffered user input stream
            if (line == null) break; // End interactive prompt on Ctrl + D (end-of-file signaled)
            run(line); // Pass expression to run() method
            hadError = false; // Resets flag so user can make mistakes without killing the session
        }
    }

    /**
     * Both the prompt and the file runner are thin wrappers around this core function
     * Not super useful yet since we haven't written the interpreter
     * @param source
     */
    private static void run(String source) {
        // Instantiates a new scanner to parse the source stream. To-do
        Scanner scanner = new Scanner(source);
        // Creates a list of <Token> objects. To-do
        List<Token> tokens = scanner.scanTokens();

        // For now, just print the tokens.
        for (Token token : tokens) {
            System.out.println(token);
        }
    }

    /**
     * Error Handling
     * Note: It’s good engineering practice to separate the code that generates 
     *       the errors from the code that reports them
     * @param line
     * @param message
     */
    static void error(int line, String message) {
        report(line, "", message);
    }

    /**
     * Prints the Line number where the error() occurred, where it occurred at, and a specific error message
     * @param line
     * @param where
     * @param message
     */
    private static void report(int line, String where, String message) {
        System.err.println("[line " + line + "] Error" + where + ": " + message);
        hadError = true;
    }
}
