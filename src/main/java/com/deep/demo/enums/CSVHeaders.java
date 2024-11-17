package com.deep.demo.enums;

import lombok.Getter;

public enum CSVHeaders {

    SEGMENT(4,true);


    ;


    @Getter
    private final Integer position;
//    @Getter
//    private final Integer maxLength;

    @Getter
    private final Boolean required;


    CSVHeaders(Integer position, Boolean required) {
        this.position = position;
        this.required = required;
    }



    public  static final CSVHeaders[] values =values();
    
    public static CSVHeaders getCSVHeadersEnum(Integer position){
        for (CSVHeaders csvHeaders:values){
            if(csvHeaders.getPosition() == position){
                return csvHeaders;
            }
        }
        return null;
    }


}
