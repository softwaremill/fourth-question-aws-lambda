package com.softwaremill.fourth_question.logic;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
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

import static com.amazonaws.services.dynamodbv2.model.AttributeAction.PUT;
import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;

@Value
public class QuestionRepository {

    private static final String TABLE_NAME = "FourthQuestionsTable";

    private final AmazonDynamoDB dynamoDb;
    private final NowProvider nowProvider;

    public void save(Question question) throws ConditionalCheckFailedException {
        this.dynamoDb
            .putItem(new PutItemRequest(TABLE_NAME,
                HashMap.of(
                    "id", new AttributeValue(UUID.randomUUID().toString()),
                    "question", new AttributeValue(question.getQuestion()),
                    "author", new AttributeValue(question.getAuthor()),
                    "added_timestamp", new AttributeValue(String.valueOf(System.currentTimeMillis())),
                    "asked", new AttributeValue(FALSE.toString())
                ).toJavaMap()
            ));
    }

    public Option<Question> getQuestionForToday() {

        ScanResult result = dynamoDb.scan(
            new ScanRequest(TABLE_NAME)
                .withFilterExpression("asked = :asked and asked_date = :asked_date")
                .withExpressionAttributeValues(
                    HashMap.of(
                        ":asked", new AttributeValue(TRUE.toString()),
                        ":asked_date", new AttributeValue(nowProvider.todayAsString())
                    ).toJavaMap()
                )
        );

        if (result.getCount() == 0) {
            return Option.none();
        } else {
            Map<String, AttributeValue> resultRow = result.getItems().get(0);

            return Option.of(
                new Question(
                    resultRow.get("id").getS(),
                    resultRow.get("question").getS(),
                    resultRow.get("author").getS()
                )
            );
        }
    }

    public Option<Question> getOldestUnaskedQuestion() {
        ScanResult result = dynamoDb.scan(
            new ScanRequest(TABLE_NAME)
                .withFilterExpression("asked = :asked")
                .withExpressionAttributeValues(
                    HashMap.of(":asked", new AttributeValue(FALSE.toString())).toJavaMap()
                )
        );

        if (result.getCount() == 0) {
            return Option.none();
        } else {
            Map<String, AttributeValue> resultRow = List.ofAll(result.getItems()).sortBy(i ->
                Long.valueOf(i.get("added_timestamp").getS())
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

    public void markAsAsked(Question question) {
        dynamoDb.updateItem(new UpdateItemRequest(
                TABLE_NAME,
                HashMap.of("id", new AttributeValue(question.getId())).toJavaMap(),
                HashMap.of(
                    "asked", new AttributeValueUpdate(new AttributeValue(TRUE.toString()), PUT),
                    "asked_date", new AttributeValueUpdate(new AttributeValue(nowProvider.todayAsString()), PUT)
                ).toJavaMap()
            )
        );
    }

    public long countNumberOfUnaskedQuestions() {
        ScanResult result = dynamoDb.scan(
            new ScanRequest(TABLE_NAME)
                .withFilterExpression("asked = :asked")
                .withExpressionAttributeValues(
                    HashMap.of(":asked", new AttributeValue(FALSE.toString())).toJavaMap()
                )
                .withProjectionExpression("id")
        );
        return result.getItems().size();
    }

}
