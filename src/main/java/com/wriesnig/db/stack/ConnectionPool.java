package com.wriesnig.db.stack;

import java.util.ArrayList;
import java.util.List;

public class ConnectionPool {
    private List<StackDbConnection> connectionPool;
    private List<StackDbConnection> usedConnections;

    public ConnectionPool(int size, String url, String password, String user){
        usedConnections = new ArrayList<>();
        connectionPool = new ArrayList<>();
        for(int i=1; i<=size; i++){
            connectionPool.add(new StackDbConnection(url, user, password));
        }
    }

    public synchronized StackDbConnection getDBConnection(){
        StackDbConnection stackDbConnection = connectionPool.remove(connectionPool.size()-1);
        usedConnections.add(stackDbConnection);
        return stackDbConnection;
    }

    public synchronized boolean releaseDBConnection(StackDbConnection stackDbConnection) {
        connectionPool.add(stackDbConnection);
        return usedConnections.remove(stackDbConnection);
    }

    public void closeConnections(){
        for(StackDbConnection connection: connectionPool) {
            connection.closeConnection();
        }
    }

}
