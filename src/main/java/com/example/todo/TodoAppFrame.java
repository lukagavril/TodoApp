package com.example.todo;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.lang.ref.Cleaner;
import java.util.ArrayList;
import java.util.List;

public class TodoAppFrame extends JFrame {

    private List<Todo> todos;

    private JPanel activeTodoListPanel = new JPanel();
    private JPanel completedTodoListPanel = new JPanel();
    private JTextField todoTextField = new JTextField();


    public TodoAppFrame(List<Todo> todos) {
        this.todos = todos;
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





    private JPanel createTodoColumn(String title, boolean activeColumn) {
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



    private JPanel createTodoPanel() {
        JPanel todoPanel = new JPanel();
        todoPanel.setLayout(new GridLayout(1, 2));
        activeTodoListPanel.setLayout(new BorderLayout());
        completedTodoListPanel.setLayout(new BorderLayout());

        // Labels at the top
        JLabel labelActive = new JLabel("Active Todos", SwingConstants.CENTER);
        labelActive.setFont(labelActive.getFont().deriveFont(25f));
        activeTodoListPanel.add(labelActive, BorderLayout.NORTH);

        JLabel labelCompleted = new JLabel("Completed Todos", SwingConstants.CENTER);
        labelCompleted.setFont(labelCompleted.getFont().deriveFont(25f));
        completedTodoListPanel.add(labelCompleted, BorderLayout.NORTH);

        for (Todo todo : todos) {
            if (!todo.isCompleted()) {
                JCheckBox checkBox = new JCheckBox(todo.getText());
                checkBox.setFont(checkBox.getFont().deriveFont(Font.PLAIN, 18f));
                activeTodoListPanel.add(checkBox);
            } else {
                JLabel todoLabel = new JLabel(todo.getText());
                todoLabel.setFont(todoLabel.getFont().deriveFont(Font.PLAIN, 18f));
                completedTodoListPanel.add(todoLabel);
            }
        }


//        JScrollPane scrollPaneActive = new JScrollPane(activeTodoListPanel);
//        JScrollPane scrollPaneCompleted = new JScrollPane(completedTodoListPanel);
        todoPanel.add(activeTodoListPanel);
        todoPanel.add(completedTodoListPanel);
//        todoPanel.add(scrollPaneActive, BorderLayout.CENTER);
//        todoPanel.add(scrollPaneCompleted, BorderLayout.CENTER);

        return todoPanel;
    }

    private JPanel createBottomPanel() {
        JPanel bottomPanel = new JPanel();
        bottomPanel.setLayout(new BorderLayout());
        bottomPanel.setBorder(BorderFactory.createEmptyBorder(10, 5, 10, 5));

        JButton deleteButton = new JButton("Delete all completed todos");
        deleteButton.setPreferredSize(new Dimension(385, 30));
        bottomPanel.add(deleteButton, BorderLayout.EAST);

        JPanel addTodoPanel = new JPanel();
        addTodoPanel.setLayout(new BorderLayout());
        addTodoPanel.setPreferredSize(new Dimension(385, 30));
        todoTextField.setText("Enter a new todo");
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
                onAddTodo();
            }
        });

        addTodoPanel.add(todoTextField, BorderLayout.CENTER);
        addTodoPanel.add(addButton, BorderLayout.EAST);

        bottomPanel.add(addTodoPanel, BorderLayout.WEST);
        return bottomPanel;
    }


    // ---------------------------------------------- logic -----------------------------------------------------


    private void onAddTodo() {
        String todoText = todoTextField.getText();
        if (!todoText.isEmpty() && !todoText.equals("Enter a new todo")) {
            todoTextField.setText("");
            todos.add(new Todo(false, todoText));
            // refresh
        }
    }
}
