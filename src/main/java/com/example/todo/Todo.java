package com.example.todo;

public class Todo {
    // todo make them private?
    //final int id;
    boolean completed;
    final String title;

    public Todo (boolean c, String t) {
        completed = c;
        title = t;
    }

    public void setCompleteness(boolean completed) {
        this.completed = completed;
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
