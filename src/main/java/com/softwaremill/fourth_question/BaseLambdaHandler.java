package com.softwaremill.fourth_question;

import com.amazonaws.auth.EnvironmentVariableCredentialsProvider;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;

public abstract class BaseLambdaHandler {

    protected AmazonDynamoDB getDatabaseConnection() {
        return AmazonDynamoDBClientBuilder
            .standard()
            .withRegion(Regions.EU_CENTRAL_1)
            .withCredentials(new EnvironmentVariableCredentialsProvider())
            .build();
    }

}
