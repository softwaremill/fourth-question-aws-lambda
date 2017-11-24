package com.softwaremill.fourth_question.add;

import com.amazonaws.auth.EnvironmentVariableCredentialsProvider;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.ConditionalCheckFailedException;
import com.amazonaws.services.dynamodbv2.model.PutItemRequest;
import com.amazonaws.services.dynamodbv2.model.QueryRequest;
import com.amazonaws.services.dynamodbv2.model.QueryResult;
import com.amazonaws.services.dynamodbv2.model.ScanRequest;
import com.amazonaws.services.dynamodbv2.model.ScanResult;
import com.amazonaws.services.dynamodbv2.model.Select;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import io.vavr.collection.HashMap;
import io.vavr.collection.List;

import java.util.UUID;


public class AddFourthQuestionLambda implements RequestHandler<FourthQuestionJson, FourthQuestionResponse> {

    private AmazonDynamoDB dynamoDb;
    private LambdaLogger logger;

    public FourthQuestionResponse handleRequest(FourthQuestionJson request, Context context) {
        logger = context.getLogger();
        logger.log("Trying to persist " + request);
        initDynamoDbClient();
        logger.log("After DB init");
        persistData(request);

        return new FourthQuestionResponse(String.valueOf(calculateQueueSize()));
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
        logger.log("result = " + result);

        return result.getItems().size();
    }

    private void initDynamoDbClient() {
        dynamoDb = AmazonDynamoDBClientBuilder.standard()
            .withRegion(Regions.EU_CENTRAL_1)
            .withCredentials(new EnvironmentVariableCredentialsProvider())
            .build();
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