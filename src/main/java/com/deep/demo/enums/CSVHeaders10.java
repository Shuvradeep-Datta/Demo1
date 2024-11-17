package com.deep.demo.enums;

import lombok.Getter;

public enum CSVHeaders10 {

    DESCRIPTION(10,2000, true);




    @Getter
    private final Integer position;
    @Getter
    private final Integer maxLength;

    @Getter
    private final Boolean required;


    CSVHeaders10(Integer position, Integer maxLength, Boolean required) {
        this.position = position;
        this.maxLength = maxLength;
        this.required = required;
    }



    public  static final CSVHeaders10[] values =values();

    public static CSVHeaders10 getCSVHeadersEnum(Integer position){
        for (CSVHeaders10 csvHeaders:values){
            if(csvHeaders.getPosition() == position){
                return csvHeaders;
            }
        }
        return null;
    }


}
