package com.example.todo;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.List;

public class TodoAppFrame extends JFrame {

    private final List<Todo> todos;
    private final TodoRepository repo;

    private final JPanel todosPanel = new JPanel();

    private JPanel activeTodoListPanel = new JPanel();
    private JPanel completedTodoListPanel = new JPanel();
    private final JTextField todoTextField = new JTextField();


    public TodoAppFrame(TodoRepository repo) {
        this.todos = repo.initTodos();
        this.repo = repo;
        JFrame frame = new JFrame();
        frame.setTitle("Todo List");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 500);

        frame.setLayout(new BorderLayout()); // NORTH, SOUTH, EAST, WEST, CENTER

        // panel with 2 columns
        todosPanel.setLayout(new GridLayout(1, 2));
        frame.add(todosPanel, BorderLayout.CENTER);
        // the actual columns
        activeTodoListPanel = createTodoColumn("Active Todos", true);
        completedTodoListPanel = createTodoColumn("Completed Todos", false);

        todosPanel.add(activeTodoListPanel);
        todosPanel.add(completedTodoListPanel);

        JPanel bottomPanel = createBottomPanel();
        frame.add(bottomPanel, BorderLayout.SOUTH);

        frame.setLocationRelativeTo(null);
        frame.setResizable(false);
        frame.setVisible(true);
    }

    private void refreshTodos() {
        todosPanel.removeAll();

        activeTodoListPanel = createTodoColumn("Active Todos", true);
        completedTodoListPanel = createTodoColumn("Completed Todos", false);

        todosPanel.add(activeTodoListPanel);
        todosPanel.add(completedTodoListPanel);

        todosPanel.revalidate();
        todosPanel.repaint();

        repo.refreshFile(todos);
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
                checkBox.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        checkBoxAction(e);
                    }
                });
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
                if (todoTextField.getText().isEmpty()) {
                    todoTextField.setText("Enter a new todo");
                }
            }
        });
        todoTextField.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {
            }

            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER && !todoTextField.getText().isEmpty()) {
                    onAddTodo();
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {
            }
        });
        JButton addButton = new JButton("Add todo");
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!todoTextField.getText().isEmpty() && !todoTextField.getText().equals("Enter a new todo")) {
                    onAddTodo();
                    todoTextField.setText("Enter a new todo");
                }
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
        todoTextField.setText("");
        todos.add(new Todo(false, todoText));
        refreshTodos();
    }

    private void checkBoxAction(ActionEvent e) {
        int startIndexOfText = e.getSource().toString().indexOf("text") + 5; // 5 = text=
        int lastIndexOfText = e.getSource().toString().length() - 1;
        String todoText = e.getSource().toString().substring(startIndexOfText, lastIndexOfText);

        // mark as done
        for (Todo todo : todos) {
            if (todo.getText().equals(todoText) && !todo.isCompleted()) {
                todo.setCompleteness(true);
                break;
            }
        }

        refreshTodos();
    }
}
