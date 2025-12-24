package com.example.todo;

import javax.swing.plaf.basic.BasicInternalFrameTitlePane;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class TodoRepository {

    private final File dataDirectory = new File("data");
    private File file = new File(dataDirectory, "todos.txt");

    public List<Todo> initTodos() {
        File file = createDataDirFile(); // og
        //File file = createDataWithPath(); // todo doesn't work when file already exists
        if (file == null) {
            System.out.println("file is null");
            System.exit(3);
        }

        return loadOldTodosFromFile(file);
    }

    public void refreshFile(List<Todo> todos) {
        if (!file.delete()) {
            System.exit(7);
            // todo throw exception or smth
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
                System.exit(9);
            }
        }
    }

    private File createDataDirFile() {
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





    // TODO Path???
//    private static File createDataWithPath() {
//
//        Path dir = Path.of("data");
//        Path filepath = dir.resolve("todos.txt");
//
//        try {
//            Files.createDirectories(dir);
//            File file = Files.createFile(filepath).toFile();
//            return file;
//        } catch (IOException e) {
//            e.printStackTrace();
//            return null;
//        }
//    }
}
