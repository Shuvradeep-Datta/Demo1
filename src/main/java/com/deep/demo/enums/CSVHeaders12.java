package com.deep.demo.enums;

import lombok.Getter;

public enum CSVHeaders12 {

    JOURNAL_PREPARER_USERID(13,8,true);




    @Getter
    private final Integer position;
    @Getter
    private final Integer maxLength;

    @Getter
    private final Boolean required;


    CSVHeaders12(Integer position,Integer maxLength, Boolean required) {
        this.position = position;
        this.maxLength=maxLength;
        this.required = required;
    }



    public  static final CSVHeaders12[] values =values();

    public static CSVHeaders12 getCSVHeadersEnum(Integer position){
        for (CSVHeaders12 csvHeaders:values){
            if(csvHeaders.getPosition() == position){
                return csvHeaders;
            }
        }
        return null;
    }


}
