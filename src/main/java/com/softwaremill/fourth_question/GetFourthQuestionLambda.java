package com.softwaremill.fourth_question;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import io.vavr.control.Option;

public class GetFourthQuestionLambda extends BaseLambdaHandler
    implements RequestHandler<FourthQuestionRequestData, FourthQuestionResponse> {

    private LambdaLogger logger;

    public FourthQuestionResponse handleRequest(FourthQuestionRequestData request, Context context) {
        initializeDatabaseRepository();
        logger = context.getLogger();

        Option<Question> question = repository.getOldestUnaskedQuestion();
        question.forEach(q -> repository.markAsAsked(q));

        FourthQuestionResponse response = new FourthQuestionResponse(question);
        logger.log("Returning " + response);
        return response;
    }

}