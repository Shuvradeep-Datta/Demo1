package com.deep.demo.enums;

import lombok.Getter;

public enum CSVHeaders16 {

    ACC_BUSINESS_USERID(17,8,true);




    @Getter
    private final Integer position;
    @Getter
    private final Integer maxLength;

    @Getter
    private final Boolean required;


    CSVHeaders16(Integer position,Integer maxLength, Boolean required) {
        this.position = position;
        this.maxLength=maxLength;
        this.required = required;
    }



    public  static final CSVHeaders16[] values =values();

    public static CSVHeaders16 getCSVHeadersEnum(Integer position){
        for (CSVHeaders16 csvHeaders:values){
            if(csvHeaders.getPosition() == position){
                return csvHeaders;
            }
        }
        return null;
    }

}
