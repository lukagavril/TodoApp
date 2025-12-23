package com.example.todo;

public class Todo {
    // todo make them private?
    final int id;
    final boolean completed;
    final String title;
    // todo add priority, due date, assigned day

    public Todo (boolean c, String t) {
        this.id = 2 + t.length();
        completed = c;
        title = t;
    }

    public Todo (int id, boolean c, String t) {
        this.id = id;
        completed = c;
        title = t;
    }

    public String getText() {
        return this.title;
    }

    public boolean isCompleted() {
        return this.completed;
    }

    @Override
    public String toString() {
        String completed = this.completed ? "1" : "0";
        return completed + ": " + this.title;
    }
}
