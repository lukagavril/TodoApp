package com.example.todo;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class TodoRepository {


    final int WRITER_FAILED = 2;
    final int FILE_FAILED_TO_DELETE = 3;
    final int CREATE_FILE_FAILED = 4;
    final int FILE_NOT_FOUND = 5;

    private final File dataDirectory = new File("data");
    private File file = new File(dataDirectory, "todos.txt");

    public List<Todo> initTodos() {
        File file = createDataDirFile();
        if (file == null) {
            System.out.println("file is null");
            System.exit(CREATE_FILE_FAILED);
        }
        return loadOldTodosFromFile(file);
    }

    public void refreshFile(List<Todo> todos) {
        if (!file.delete()) {
            System.exit(FILE_FAILED_TO_DELETE);
        }
        file = new File(dataDirectory, "todos.txt");

        for (Todo todo : todos) {
            String thisTodo = "";
            if (todo.isCompleted()) {
                thisTodo = "1";
            }
            else {
                thisTodo = "0";
            }
            thisTodo += ";" + todo.getText() + "\n";
            try (FileWriter writer = new FileWriter(file, true)) {
                writer.write(thisTodo);
            } catch (IOException e) {
                e.printStackTrace();
                System.exit(WRITER_FAILED);
            }
        }
    }

    private File createDataDirFile() {
        if (!dataDirectory.exists()) {
            dataDirectory.mkdir();
        }

        try {
            file.createNewFile();
            return file;
        } catch (IOException e) {
            return null;
        }
    }

    private List<Todo> loadOldTodosFromFile(File file) {
        try(Scanner scanner = new Scanner(file)) {
            List<Todo> todos = new ArrayList<>();
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                todos.add(makeTodoFromFileLine(line));
            }
            return todos;

        } catch (FileNotFoundException e) {
            System.out.println("file not found");
            e.printStackTrace();
            System.exit(FILE_NOT_FOUND);
        }
        return null;
    }

    private Todo makeTodoFromFileLine(String line) {
        // completed;title
        // todo add checks for invalid lines
        String[] parts = line.split(";");
        boolean completed = parts[0].equals("1");
        return new Todo(completed, parts[1]);
    }


    private File createDataDirFileWithDebugStatements() {
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

}
