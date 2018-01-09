package com.softwaremill.fourth_question;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.softwaremill.fourth_question.logic.Question;
import io.vavr.control.Option;

public class GetQuestionLambda extends BaseLambdaHandler
    implements RequestHandler<QuestionRequestData, QuestionResponse> {

    private LambdaLogger logger;

    public QuestionResponse handleRequest(QuestionRequestData request, Context context) {
        initializeDatabaseRepository();
        logger = context.getLogger();
        QuestionResponse response = new QuestionResponse(getQuestion(), repository.countNumberOfUnaskedQuestions());
        logger.log("Returning " + response);
        return response;
    }

    private Option<Question> getQuestion() {
        Option<Question> question =  repository.getQuestionForToday();

        question.forEach(q -> logger.log("Found question for today"));
        return question.orElse(() -> {
            Option<Question> newQuestion = repository.getOldestUnaskedQuestion();
            newQuestion.forEach(q -> {
                logger.log("Taking the oldest unasked question as question for today");
                repository.markAsAsked(q);
            });
            return newQuestion;
        });
    }

}