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
 * Main class for the Lox programming language interpreter
 */
public class Lox {

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
        for(;;) {
            System.out.print("lox> "); // Prompt
            String line = reader.readLine(); // Read buffered user input stream
            if (line == null) break; // End interactive prompt on Ctrl + D (end-of-file signaled)
            run(line); // Pass expression to run() method
        }
    }
}
