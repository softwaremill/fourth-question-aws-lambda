package com.softwaremill.fourth_question;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.model.AttributeAction;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.AttributeValueUpdate;
import com.amazonaws.services.dynamodbv2.model.ConditionalCheckFailedException;
import com.amazonaws.services.dynamodbv2.model.PutItemRequest;
import com.amazonaws.services.dynamodbv2.model.ScanRequest;
import com.amazonaws.services.dynamodbv2.model.ScanResult;
import com.amazonaws.services.dynamodbv2.model.UpdateItemRequest;
import io.vavr.collection.HashMap;
import io.vavr.collection.List;
import io.vavr.control.Option;
import lombok.Value;

import java.util.Map;
import java.util.UUID;

import static io.vavr.API.$;
import static io.vavr.API.Case;
import static io.vavr.API.Match;

@Value
class FourthQuestionRepository {

    private static final String TABLE_NAME = "FourthQuestionsTable";

    private final AmazonDynamoDB dynamoDb;

    void persist(Question question) throws ConditionalCheckFailedException {
        this.dynamoDb
            .putItem(new PutItemRequest(TABLE_NAME,
                HashMap.of(
                    "id", new AttributeValue(UUID.randomUUID().toString()),
                    "question", new AttributeValue(question.getQuestion()),
                    "author", new AttributeValue(question.getAuthor()),
                    "timestamp", new AttributeValue(String.valueOf(System.currentTimeMillis())),
                    "asked", new AttributeValue(Boolean.FALSE.toString())
                ).toJavaMap()
            ));
    }

    Option<Question> getOldestUnaskedQuestion() {
        ScanResult result = dynamoDb.scan(
            new ScanRequest(TABLE_NAME)
                .withFilterExpression("asked = :asked")
                .withExpressionAttributeValues(
                    HashMap.of(":asked", new AttributeValue(Boolean.FALSE.toString())).toJavaMap()
                )
        );

        if (result.getCount() == 0) {
            return Option.none();
        } else {
            Map<String, AttributeValue> resultRow = List.ofAll(result.getItems()).sortBy(i ->
                Long.valueOf(i.get("timestamp").getS())
            ).head();

            return Option.of(
                new Question(
                    resultRow.get("id").getS(),
                    resultRow.get("question").getS(),
                    resultRow.get("author").getS()
                )
            );
        }
    }

    void markAsAsked(Question question) {
        dynamoDb.updateItem(new UpdateItemRequest(
                TABLE_NAME,
                HashMap.of("id", new AttributeValue(question.getId())).toJavaMap(),
                HashMap.of("asked", new AttributeValueUpdate(
                    new AttributeValue(Boolean.TRUE.toString()), AttributeAction.PUT)).toJavaMap()
            )
        );
    }

    long countNumberOfUnaskedQuestions() {
        ScanResult result = dynamoDb.scan(
            new ScanRequest(TABLE_NAME)
                .withFilterExpression("asked = :asked")
                .withExpressionAttributeValues(
                    HashMap.of(":asked", new AttributeValue(Boolean.FALSE.toString())).toJavaMap()
                )
                .withProjectionExpression("id")
        );
        return result.getItems().size();
    }
}
