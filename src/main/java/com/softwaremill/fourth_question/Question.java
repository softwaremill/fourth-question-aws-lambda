package com.softwaremill.fourth_question;


import lombok.Value;

@Value
public class Question {

    private final String id;
    private final String question;
    private final String author;

    public static Question fromRequest(FourthQuestionRequestData requestData) {
        return new Question(null, requestData.getQuestion(), requestData.getAuthor());
    }

}
