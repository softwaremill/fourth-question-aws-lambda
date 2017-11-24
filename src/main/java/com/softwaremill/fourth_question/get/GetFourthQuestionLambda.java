package com.softwaremill.fourth_question.get;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.softwaremill.fourth_question.add.FourthQuestionJson;
import com.softwaremill.fourth_question.add.FourthQuestionResponse;

public class GetFourthQuestionLambda implements RequestHandler<FourthQuestionJson, FourthQuestionResponse> {

    public FourthQuestionResponse handleRequest(FourthQuestionJson request, Context context) {
        return new FourthQuestionResponse(request.getQuestion());
    }

}