package com.deep.demo.enums;

import lombok.Getter;

public enum CSVHeaders13 {

    JOURNAL_APPROVER_USERID(14,8,true);




    @Getter
    private final Integer position;
    @Getter
    private final Integer maxLength;

    @Getter
    private final Boolean required;


    CSVHeaders13(Integer position,Integer maxLength, Boolean required) {
        this.position = position;
        this.maxLength=maxLength;
        this.required = required;
    }



    public  static final CSVHeaders13[] values =values();

    public static CSVHeaders13 getCSVHeadersEnum(Integer position){
        for (CSVHeaders13 csvHeaders:values){
            if(csvHeaders.getPosition() == position){
                return csvHeaders;
            }
        }
        return null;
    }

}
