package com.softwaremill.fourth_question.add;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

public class AddFourthQuestionLambda implements RequestHandler<FourthQuestionJson, FourthQuestionResponse> {

    public FourthQuestionResponse handleRequest(FourthQuestionJson request, Context context) {
        return new FourthQuestionResponse(request.getQuestion());
    }

}