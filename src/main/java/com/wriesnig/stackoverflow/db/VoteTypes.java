package com.wriesnig.stackoverflow.db;

public enum VoteTypes {
    IS_ACCEPTED(1), UP_VOTE(2), DOWN_VOTE(3);

    private final int value;

    VoteTypes(int value) {
        this.value = value;
    }

    public int getValue(){return this.value;}
}
