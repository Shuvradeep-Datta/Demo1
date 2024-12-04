package com.deep.demo.exception;

public class JournalEntryExceptions extends RuntimeException {
    public JournalEntryExceptions(String message) {super(message);}

    public JournalEntryExceptions (String message,Throwable cause) {super(message,cause);}
}
