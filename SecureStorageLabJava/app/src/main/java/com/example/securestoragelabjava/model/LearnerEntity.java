package com.example.securestoragelabjava.model;

public class LearnerEntity {

    public final int learnerId;
    public final String learnerName;
    public final int learnerAge;

    public LearnerEntity(int learnerId, String learnerName, int learnerAge) {
        this.learnerId = learnerId;
        this.learnerName = learnerName;
        this.learnerAge = learnerAge;
    }
}