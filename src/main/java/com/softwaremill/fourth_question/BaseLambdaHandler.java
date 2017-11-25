package com.softwaremill.fourth_question;

import com.amazonaws.auth.EnvironmentVariableCredentialsProvider;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;

abstract class BaseLambdaHandler {

    FourthQuestionRepository repository;

    FourthQuestionRepository initializeDatabaseRepository() {
        repository = new FourthQuestionRepository(
            AmazonDynamoDBClientBuilder
                .standard()
                .withRegion(Regions.EU_CENTRAL_1)
                .withCredentials(new EnvironmentVariableCredentialsProvider())
                .build()
        );

        return repository;
    }

}
