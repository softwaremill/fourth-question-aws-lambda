package com.softwaremill.fourth_question;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class QuestionRequestData {

    private String question;
    private String author;

}
