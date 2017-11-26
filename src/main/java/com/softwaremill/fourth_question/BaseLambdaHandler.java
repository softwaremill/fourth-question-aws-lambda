package com.softwaremill.fourth_question;

import com.amazonaws.auth.EnvironmentVariableCredentialsProvider;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.softwaremill.fourth_question.logic.NowProvider;
import com.softwaremill.fourth_question.logic.QuestionRepository;

abstract class BaseLambdaHandler {

    QuestionRepository repository;

    QuestionRepository initializeDatabaseRepository() {
        repository = new QuestionRepository(
            AmazonDynamoDBClientBuilder
                .standard()
                .withRegion(Regions.EU_CENTRAL_1)
                .withCredentials(new EnvironmentVariableCredentialsProvider())
                .build(),
            new NowProvider()
        );

        return repository;
    }

}
