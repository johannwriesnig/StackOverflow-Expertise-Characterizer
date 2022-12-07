package com.wriesnig.stackoverflow.db;

import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ConnectionPool {
    private int size;
    private List<DBConnection> connectionPool;
    private List<DBConnection> usedConnections = new ArrayList<>();

    public ConnectionPool(int size, String url, String password, String user) throws SQLException {
        connectionPool = new ArrayList<>();
        for(int i=1; i<=size; i++){
            connectionPool.add(new DBConnection(DriverManager.getConnection(url, user, password)));
        }
        this.size = size;
    }

    public DBConnection getDBConnection(){
        DBConnection dbConnection = connectionPool.remove(connectionPool.size()-1);
        usedConnections.add(dbConnection);
        return dbConnection;
    }

    public boolean releaseDBConnection(DBConnection dbConnection) {
        connectionPool.add(dbConnection);
        return usedConnections.remove(dbConnection);
    }

}
