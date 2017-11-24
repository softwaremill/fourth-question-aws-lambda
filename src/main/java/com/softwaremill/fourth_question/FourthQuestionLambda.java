package com.softwaremill.fourth_question;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

public class FourthQuestionLambda
    implements RequestHandler<FourthQuestionJson, FourthQuestionResponse> {

    public FourthQuestionResponse handleRequest(FourthQuestionJson request, Context context) {
        return new FourthQuestionResponse(request.getQuestion());
    }

}