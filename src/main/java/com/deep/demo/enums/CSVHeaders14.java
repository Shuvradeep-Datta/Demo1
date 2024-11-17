package com.deep.demo.enums;

import lombok.Getter;

public enum CSVHeaders14 {

    JOURNAL_REVIEWER_USERID(15,8,true);




    @Getter
    private final Integer position;
    @Getter
    private final Integer maxLength;

    @Getter
    private final Boolean required;


    CSVHeaders14(Integer position,Integer maxLength, Boolean required) {
        this.position = position;
        this.maxLength=maxLength;
        this.required = required;
    }



    public  static final CSVHeaders14[] values =values();

    public static CSVHeaders14 getCSVHeadersEnum(Integer position){
        for (CSVHeaders14 csvHeaders:values){
            if(csvHeaders.getPosition() == position){
                return csvHeaders;
            }
        }
        return null;
    }

}
