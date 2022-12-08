package com.wriesnig.stackoverflow.db;

import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ConnectionPool {
    private int size;
    private List<StackDbConnection> connectionPool;
    private List<StackDbConnection> usedConnections = new ArrayList<>();

    public ConnectionPool(int size, String url, String password, String user) throws SQLException {
        connectionPool = new ArrayList<>();
        for(int i=1; i<=size; i++){
            connectionPool.add(new StackDbConnection(DriverManager.getConnection(url, user, password)));
        }
        this.size = size;
    }

    public StackDbConnection getDBConnection(){
        StackDbConnection stackDbConnection = connectionPool.remove(connectionPool.size()-1);
        usedConnections.add(stackDbConnection);
        return stackDbConnection;
    }

    public boolean releaseDBConnection(StackDbConnection stackDbConnection) {
        connectionPool.add(stackDbConnection);
        return usedConnections.remove(stackDbConnection);
    }

}
