package com.example.myapplication.db;


import java.util.ArrayList;

public class Query {

    private final String command;
    private final String[] params;

    public Query(String command, String... params) {
        this.command = command;
        this.params = params;
    }

    public Query(String command, ArrayList<String> params) {
        this.command = command;
        this.params = params.toArray(new String[0]);
    }

    public String getCommand() {
        return command;
    }

    public String[] getParams() {
        return params;
    }
}
