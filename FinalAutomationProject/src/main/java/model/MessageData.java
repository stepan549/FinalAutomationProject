package model;

public class MessageData {
    private String sendTo;
    private String subject;
    private String message;

    public String getSendTo() {
        return sendTo;
    }

    public String getSubject() {
        return subject;
    }

    public String getMessage() {
        return message;
    }

    public MessageData(String sendTo, String subject, String message) {
        this.sendTo = sendTo;
        this.subject = subject;
        this.message = message;
    }
}
