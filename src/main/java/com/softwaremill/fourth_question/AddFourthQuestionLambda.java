package com.softwaremill.fourth_question;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.lambda.runtime.RequestHandler;

public class AddFourthQuestionLambda extends BaseLambdaHandler
    implements RequestHandler<FourthQuestionRequestData, FourthQuestionResponse> {

    private LambdaLogger logger;

    public FourthQuestionResponse handleRequest(FourthQuestionRequestData request, Context context) {
        logger = context.getLogger();
        logger.log("Trying to persist " + request);
        initializeDatabaseRepository();

        repository.persist(Question.fromRequest(request));

        FourthQuestionResponse response = new FourthQuestionResponse(repository.countNumberOfUnaskedQuestions());
        logger.log("Going to return " + response);
        return response;
    }

}