package com.softwaremill.fourth_question.logic;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

public class NowProvider {

    private static final ZoneId WARSAW_ZONE_ID = ZoneId.of("Europe/Warsaw");
    private static final DateTimeFormatter FORMATTING_PATTERN = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    public LocalDateTime now() {
        return LocalDateTime.now(WARSAW_ZONE_ID);
    }

    public String todayAsString() {
        return now().toLocalDate().format(FORMATTING_PATTERN);
    }

}
