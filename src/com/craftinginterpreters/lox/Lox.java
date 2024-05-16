package com.craftinginterpreters.lox;   // This program

import java.io.BufferedReader;          // Importing BufferedReader for reading text from a character-input stream
import java.io.IOException;             // Importing IOException class for handling I/O errors
import java.io.InputStreamReader;       // Importing InputStreamReader to bridge byte streams to character streams

import java.nio.charset.Charset;        // Importing Charset class to handle character encoding
import java.nio.file.Files;             // Importing Files class for file operations
import java.nio.file.Paths;             // Importing Paths utility class to operate on file paths

import java.util.List;                  // Importing List collection for storing lists of objects

/**
 * Main class for the Lox programming language interpreter
 */
public class Lox {

    public static void main(String[] args) throws IOException {
        if (args.length > 1) {
            System.out.println("Usage: jlox [script]"); // Informs the user about correct usage if the argument count is incorrect
            System.exit(64); // Exits the program with status code 64 (UNIX standard for command line usage error)
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
        run(new String(bytes, Charset.defaultCharset())); // Constructs a new String by decoding the specified array of bytes using the specified charset
    }

    // interactive prompt logic
}
