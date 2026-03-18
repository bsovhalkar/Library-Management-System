package com.app.Library_Management.exception;

public class PlanCodeAlreadyExist extends Exception {
    public PlanCodeAlreadyExist(String plancode) {
        super("Plan code already exist: " + plancode);
    }
}
