package com.softwaremill.fourth_question;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.softwaremill.fourth_question.logic.Question;

public class AddQuestionLambda extends BaseLambdaHandler
    implements RequestHandler<QuestionRequestData, QuestionResponse> {

    private LambdaLogger logger;

    public QuestionResponse handleRequest(QuestionRequestData request, Context context) {
        logger = context.getLogger();
        logger.log("Trying to persist " + request);
        initializeDatabaseRepository();

        repository.save(Question.fromRequest(request));

        QuestionResponse response = new QuestionResponse(repository.countNumberOfUnaskedQuestions());
        logger.log("Going to return " + response);
        return response;
    }

}