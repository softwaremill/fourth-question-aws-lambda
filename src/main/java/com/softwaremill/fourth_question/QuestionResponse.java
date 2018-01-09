package com.softwaremill.fourth_question;

import com.softwaremill.fourth_question.logic.Question;
import io.vavr.control.Option;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class QuestionResponse {

    private final String message;

    public QuestionResponse(Option<Question> question) {

        if (question.isDefined()) {
            message = "*" + question.get().getQuestion() + "* (zadane przez " + question.get ().getAuthor() +")";
        } else {
            message = "brak, a może *Ty* masz jakiś pomysł?";
        }
    }

    public QuestionResponse(long queueSize) {
        message = String.format("Ok, czwarte pytanie dodane. Jesteś %d w kolejce :)", queueSize);
    }

}
