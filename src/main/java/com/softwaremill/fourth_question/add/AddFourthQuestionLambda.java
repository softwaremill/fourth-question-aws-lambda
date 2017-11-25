package com.softwaremill.fourth_question.add;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.ConditionalCheckFailedException;
import com.amazonaws.services.dynamodbv2.model.PutItemRequest;
import com.amazonaws.services.dynamodbv2.model.ScanRequest;
import com.amazonaws.services.dynamodbv2.model.ScanResult;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.softwaremill.fourth_question.BaseLambdaHandler;
import io.vavr.collection.HashMap;

import java.util.UUID;

public class AddFourthQuestionLambda extends BaseLambdaHandler
    implements RequestHandler<FourthQuestionJson, FourthQuestionResponse> {

    private AmazonDynamoDB dynamoDb;
    private LambdaLogger logger;

    public FourthQuestionResponse handleRequest(FourthQuestionJson request, Context context) {
        logger = context.getLogger();
        logger.log("Trying to persist " + request);
        dynamoDb = getDatabaseConnection();

        persistData(request);

        FourthQuestionResponse response = new FourthQuestionResponse(String.valueOf(calculateQueueSize()));
        logger.log("Going to return " + response);
        return response;
    }

    private long calculateQueueSize() {
        ScanResult result = dynamoDb.scan(
            new ScanRequest("FourthQuestionsTable")
                .withFilterExpression("asked = :asked")
                .withExpressionAttributeValues(
                    HashMap.of(":asked", new AttributeValue(Boolean.FALSE.toString())).toJavaMap()
                )
                .withProjectionExpression("id")
        );
        return result.getItems().size();
    }

    private void persistData(FourthQuestionJson question) throws ConditionalCheckFailedException {
        this.dynamoDb
            .putItem(new PutItemRequest("FourthQuestionsTable",
                HashMap.of(
                    "id", new AttributeValue(UUID.randomUUID().toString()),
                    "question", new AttributeValue(question.getQuestion()),
                    "author", new AttributeValue(question.getAuthor()),
                    "timestamp", new AttributeValue(String.valueOf(System.currentTimeMillis())),
                    "asked", new AttributeValue(Boolean.FALSE.toString())
                ).toJavaMap()
            ));
    }

}