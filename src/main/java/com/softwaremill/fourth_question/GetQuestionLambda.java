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

        Option<Question> question = repository.getOldestUnaskedQuestion();
        question.forEach(q -> repository.markAsAsked(q));

        QuestionResponse response = new QuestionResponse(question);
        logger.log("Returning " + response);
        return response;
    }

}