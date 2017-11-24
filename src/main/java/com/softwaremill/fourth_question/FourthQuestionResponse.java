package com.softwaremill.fourth_question;

public class FourthQuestionResponse {

    private String message;

    public FourthQuestionResponse() {
    }

    public FourthQuestionResponse(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("FourthQuestionResponse{");
        sb.append("message='").append(message).append('\'');
        sb.append('}');
        return sb.toString();
    }

}
