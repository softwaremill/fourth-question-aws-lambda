package com.softwaremill.fourth_question;

public class FourthQuestionJson {

    private String question;
    private String author;

    public FourthQuestionJson() {
    }

    public FourthQuestionJson(String question, String author) {
        this.question = question;
        this.author = author;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("FourthQuestionJson{");
        sb.append("question='").append(question).append('\'');
        sb.append(", author='").append(author).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
