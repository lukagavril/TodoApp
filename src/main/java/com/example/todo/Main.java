package com.example.todo;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
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



    private static JPanel createTodoColumn(String title, boolean activeColumn) {
        JPanel columnPanel = new JPanel();
        columnPanel.setLayout(new BorderLayout());

        // Label at the top
        JLabel label = new JLabel(title, SwingConstants.CENTER);
        label.setFont(label.getFont().deriveFont(25f));
        columnPanel.add(label, BorderLayout.NORTH);

        // Panel for todos
        JPanel todoListPanel = new JPanel();
        todoListPanel.setLayout(new BoxLayout(todoListPanel, BoxLayout.Y_AXIS));
        todoListPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        for (Todo todo : todos) {
            if (activeColumn && !todo.isCompleted()) {
                JCheckBox checkBox = new JCheckBox(todo.getText());
                checkBox.setFont(checkBox.getFont().deriveFont(Font.PLAIN, 18f));
                todoListPanel.add(checkBox);
            } else if (!activeColumn && todo.isCompleted()) {
                JLabel todoLabel = new JLabel(todo.getText());
                todoLabel.setFont(todoLabel.getFont().deriveFont(Font.PLAIN, 18f));
                todoListPanel.add(todoLabel);
            }
        }

        // Make it scrollable (important!)
        JScrollPane scrollPane = new JScrollPane(todoListPanel);
        columnPanel.add(scrollPane, BorderLayout.CENTER);

        return columnPanel;
    }

    public static JPanel createBottomPanel() {
        JPanel bottomPanel = new JPanel();
        bottomPanel.setLayout(new BorderLayout());
        bottomPanel.setBorder(BorderFactory.createEmptyBorder(10, 5, 10, 5));

        JButton deleteButton = new JButton("Delete all completed todos");
        deleteButton.setPreferredSize(new Dimension(385, 30));
        bottomPanel.add(deleteButton, BorderLayout.EAST);

        JPanel addTodoPanel = new JPanel();
        addTodoPanel.setLayout(new BorderLayout());
        addTodoPanel.setPreferredSize(new Dimension(385, 30));
        JTextField todoTextField = new JTextField("Enter a new todo");
        todoTextField.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                if (todoTextField.getText().equals("Enter a new todo")) {
                    todoTextField.setText("");
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (todoTextField.getText().equals("")) {
                    todoTextField.setText("Enter a new todo");
                }
            }
        });
        JButton addButton = new JButton("Add todo");
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                onAddTodo(todoTextField);
            }
        });

        addTodoPanel.add(todoTextField, BorderLayout.CENTER);
        addTodoPanel.add(addButton, BorderLayout.EAST);

        bottomPanel.add(addTodoPanel, BorderLayout.WEST);
        return bottomPanel;
    }

    public static void refreshBothTodoPanels(JPanel activeColumn, JPanel completedColumn) {


        for (Todo todo : todos) {
            if (!todo.isCompleted()) {
                JCheckBox checkBox = new JCheckBox(todo.getText());
                checkBox.setFont(checkBox.getFont().deriveFont(Font.PLAIN, 18f));
                activeColumn.add(checkBox);
            } else {
                JLabel todoLabel = new JLabel(todo.getText());
                todoLabel.setFont(todoLabel.getFont().deriveFont(Font.PLAIN, 18f));
                completedColumn.add(todoLabel);
            }
        }
    }

    private static void onAddTodo(JTextField textField) {
        String todoText = textField.getText();
        textField.setText("");
        todos.add(new Todo(false, todoText));
        //refreshBothTodoPanels();
    }

    public static void main(String[] args) {

        File file = createDataDirFile(); // og
        //File file = createDataWithPath(); // todo doesn't work when file already exists
        if (file == null) {
            System.out.println("file is null");
            return; // todo exit codes in java
        }

        todos = loadOldTodosFromFile(file);

        buildUI();
    }

    private static void buildUI() {
        JFrame frame = new JFrame("Todo list");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 500);

        frame.setLayout(new BorderLayout()); // NORTH, SOUTH, EAST, WEST, CENTER

        // panel with 2 columns
        JPanel todosPanel = new JPanel();
        todosPanel.setLayout(new GridLayout(1, 2));
        frame.add(todosPanel, BorderLayout.CENTER);
        // the actual columns
        JPanel activeColumn = createTodoColumn("Active Todos", true);
        JPanel completedColumn = createTodoColumn("Completed Todos", false);

        todosPanel.add(activeColumn);
        todosPanel.add(completedColumn);

        JPanel bottomPanel = createBottomPanel();
        frame.add(bottomPanel, BorderLayout.SOUTH);

        frame.setLocationRelativeTo(null);
        frame.setResizable(false);
        frame.setVisible(true);
    }
}
