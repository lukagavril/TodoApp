package com.example.todo;

import javax.swing.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {

    private static List<Todo> todos = new ArrayList<>();

    private static File createDataDirFile() {
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
            return file;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    private static Todo makeTodoFromFileLine(String line) {
        // completed;title
        // todo add checks for invalid lines
        String[] parts = line.split(";");
        boolean completed = parts[0].equals("1");
        return new Todo(completed, parts[1]);
    }

    private static List<Todo> loadOldTodosFromFile(File file) {
        try(Scanner scanner = new Scanner(file)) {
            List<Todo> todos = new ArrayList<>();
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                todos.add(makeTodoFromFileLine(line));
            }
            return todos;

        }catch (FileNotFoundException e) {
            System.out.println("file not found");
            e.printStackTrace();
        }
        return null;
    }

    // TODO Path???
    private static File createDataWithPath() {

        Path dir = Path.of("data");
        Path filepath = dir.resolve("todos.txt");

        try {
            Files.createDirectories(dir);
            File file = Files.createFile(filepath).toFile();
            return file;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void main(String[] args) {

        File file = createDataDirFile(); // og
        //File file = createDataWithPath(); // todo doesn't work when file already exists
        if (file == null) {
            System.out.println("file is null");
            return; // todo exit codes in java
        }

        todos = loadOldTodosFromFile(file);

        // Always start Swing apps on the Event Dispatch Thread
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Todo List");

            frame.setSize(400, 300);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setLocationRelativeTo(null); // center on screen

            frame.setVisible(true);
        });

    }
}
