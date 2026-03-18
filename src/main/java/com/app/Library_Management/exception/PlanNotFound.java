package com.app.Library_Management.exception;

public class PlanNotFound extends Exception {
    public PlanNotFound(Long planCode) {
        super("Plan with code " + planCode + " not found");
    }
}
