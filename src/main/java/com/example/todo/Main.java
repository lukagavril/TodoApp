package com.example.todo;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class Main {

    private static void createDataDirFile() {
        File dataDirectory = new File("data");
        File file = new File(dataDirectory, "todos.txt");

        if (!dataDirectory.exists()) {
            if (dataDirectory.mkdir()) {
                System.out.println("Directory created");
            }
            else {
                System.out.println("Failed to create, even though it doesn't exist");
            }
        }

        try {
            if (file.createNewFile()) {
                System.out.println("File created");
            } else {
                System.out.println("File already exists");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /// should work on all OS
    private static void createDataWithPath() {

        Path dir = Path.of("data");
        Path file = dir.resolve("todos.txt");

        try {
            Files.createDirectories(dir);
            Files.createFile(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {

        //createDataDirFile();
        createDataWithPath();



    }
}
