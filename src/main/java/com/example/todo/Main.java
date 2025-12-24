package com.example.todo;

public class Main {

    public static void main(String[] args) {
        TodoRepository repo = new TodoRepository();
        new TodoAppFrame(repo.initTodos());
    }

}
