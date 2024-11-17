package com.deep.demo.enums;

import lombok.Getter;

public enum CSVHeaders3 {


    JOURNAL_IDENTIFIER(1,5,true);




    ;


    @Getter
    private final Integer position;
    @Getter
    private final Integer maxLength;

    @Getter
    private final Boolean required;


    CSVHeaders3(Integer position, Integer maxLength, Boolean required) {
        this.position = position;
        this.maxLength = maxLength;
        this.required = required;
    }


    public static final CSVHeaders3[] values = values();

    public static CSVHeaders3 getCSVHeadersEnum(Integer position) {
        for (CSVHeaders3 csvHeaders : values) {
            if (csvHeaders.getPosition() == position) {
                return csvHeaders;
            }
        }
        return null;
    }
}
