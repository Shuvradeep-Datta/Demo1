package com.deep.demo.enums;

import lombok.Getter;

public enum CSVHeaders2 {

    TITLE(2,200,true);


    ;


    @Getter
    private final Integer position;
    @Getter
    private final Integer maxLength;

    @Getter
    private final Boolean required;


    CSVHeaders2(Integer position, Integer maxLength, Boolean required) {
        this.position = position;
        this.maxLength = maxLength;
        this.required = required;
    }



    public  static final CSVHeaders2[] values =values();

    public static CSVHeaders2 getCSVHeadersEnum(Integer position){
        for (CSVHeaders2 csvHeaders:values){
            if(csvHeaders.getPosition() == position){
                return csvHeaders;
            }
        }
        return null;
    }


}