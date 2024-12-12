package com.example.myapplication.db;

import org.json.JSONException;
import org.neo4j.driver.AuthToken;
import org.neo4j.driver.AuthTokens;
import org.neo4j.driver.Driver;
import org.neo4j.driver.GraphDatabase;
import org.neo4j.driver.Result;
import org.neo4j.driver.Session;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class Neo4jConnection {
    static private Neo4jConnection instance;

    static public Neo4jConnection getInstance() {
        if (instance == null) {
            instance = new Neo4jConnection();
        }
        return instance;
    }

    private Neo4jConnection(){
    }

    protected Driver driver = null;
    protected Session session = null;
    public Session getSession() {
        return session;
    }

    public void startConnection(String uri, String username, String password) {
        System.out.println("AVVIO CONNESSIONE NEO4J");
        AuthToken token = AuthTokens.basic(username, password);
        this.driver = GraphDatabase.driver(uri,  token);
        this.session = driver.session();
    }

    public Result runQuery(String query) {
        return this.session.run(query);
    }

    public void closeConnection() {
        this.session.close();
        this.driver.close();

    }
}
