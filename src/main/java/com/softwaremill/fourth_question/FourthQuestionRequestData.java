package com.softwaremill.fourth_question;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FourthQuestionRequestData {

    private String question;
    private String author;

}
