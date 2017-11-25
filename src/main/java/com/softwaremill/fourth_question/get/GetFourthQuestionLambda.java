package com.softwaremill.fourth_question.get;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.model.AttributeAction;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.AttributeValueUpdate;
import com.amazonaws.services.dynamodbv2.model.ScanRequest;
import com.amazonaws.services.dynamodbv2.model.ScanResult;
import com.amazonaws.services.dynamodbv2.model.UpdateItemRequest;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.softwaremill.fourth_question.BaseLambdaHandler;
import com.softwaremill.fourth_question.add.FourthQuestionJson;
import com.softwaremill.fourth_question.add.FourthQuestionResponse;
import io.vavr.collection.HashMap;
import io.vavr.collection.List;

import java.util.Map;

public class GetFourthQuestionLambda extends BaseLambdaHandler
    implements RequestHandler<FourthQuestionJson, FourthQuestionResponse> {

    private AmazonDynamoDB dynamoDb;
    private LambdaLogger logger;

    public FourthQuestionResponse handleRequest(FourthQuestionJson request, Context context) {
        dynamoDb = getDatabaseConnection();
        logger = context.getLogger();
        return getOldestUnaskedQuestion();
    }

    private FourthQuestionResponse getOldestUnaskedQuestion() {
        ScanResult result = dynamoDb.scan(
            new ScanRequest("FourthQuestionsTable")
                .withFilterExpression("asked = :asked")
                .withExpressionAttributeValues(
                    HashMap.of(":asked", new AttributeValue(Boolean.FALSE.toString())).toJavaMap()
                )
        );

        Map<String, AttributeValue> resultRow = List.ofAll(result.getItems()).sortBy(i ->
            Long.valueOf(i.get("timestamp").getS())
        ).head();

        dynamoDb.updateItem(new UpdateItemRequest(
            "FourthQuestionsTable",
            HashMap.of("id", resultRow.get("id")).toJavaMap(),
            HashMap.of("asked", new AttributeValueUpdate(
                new AttributeValue(Boolean.TRUE.toString()), AttributeAction.PUT)).toJavaMap()
            )
        );

        return new FourthQuestionResponse(
            "*" + resultRow.get("question").getS() + "* (zadane przez " + resultRow.get("author").getS() +")"
        );
    }

}