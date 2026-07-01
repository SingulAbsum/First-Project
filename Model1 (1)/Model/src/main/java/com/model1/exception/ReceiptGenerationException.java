package main.java.com.model1.exception;

public class ReceiptGenerationException extends RuntimeException {
    public ReceiptGenerationException(String message, Throwable cause) {
        super(message, cause);
    }
}
