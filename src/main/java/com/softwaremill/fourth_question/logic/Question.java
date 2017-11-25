package com.softwaremill.fourth_question.logic;


import com.softwaremill.fourth_question.QuestionRequestData;
import lombok.Value;

@Value
public class Question {

    private final String id;
    private final String question;
    private final String author;

    public static Question fromRequest(QuestionRequestData requestData) {
        return new Question(null, requestData.getQuestion(), requestData.getAuthor());
    }

}
