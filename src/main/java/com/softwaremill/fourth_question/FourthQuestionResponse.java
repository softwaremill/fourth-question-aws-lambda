package com.softwaremill.fourth_question;

import io.vavr.control.Option;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class FourthQuestionResponse {

    private final String message;

    public FourthQuestionResponse(Option<Question> question) {

        if (question.isDefined()) {
            message = "*" + question.get().getQuestion() + "* (zadane przez " + question.get ().getAuthor() +")";
        } else {
            message = "brak, a może *Ty* masz jakiś pomyysł?";
        }
    }

    public FourthQuestionResponse(long queueSize) {
        message = String.format("Ok, czwarte pytanie dodane. Jesteś %d w kolejce :)", queueSize);
    }

}
