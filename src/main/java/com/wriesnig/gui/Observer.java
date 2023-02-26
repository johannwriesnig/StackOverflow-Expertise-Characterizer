package com.wriesnig.gui;

import com.wriesnig.expertise.User;

import java.util.ArrayList;

public interface Observer {
    public void notifyUpdate(ArrayList<User> users);
}
