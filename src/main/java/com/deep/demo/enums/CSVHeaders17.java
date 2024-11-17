package com.deep.demo.enums;

import lombok.Getter;

public enum CSVHeaders17 {

    UPSTREAM_DEPENDENCY(21,30,true);




    @Getter
    private final Integer position;
    @Getter
    private final Integer maxLength;

    @Getter
    private final Boolean required;


    CSVHeaders17(Integer position, Integer maxLength, Boolean required) {
        this.position = position;
        this.maxLength=maxLength;
        this.required = required;
    }



    public  static final CSVHeaders17[] values =values();

    public static CSVHeaders17 getCSVHeadersEnum(Integer position){
        for (CSVHeaders17 csvHeaders:values){
            if(csvHeaders.getPosition() == position){
                return csvHeaders;
            }
        }
        return null;
    }

}
