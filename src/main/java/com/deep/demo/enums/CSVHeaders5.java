package com.deep.demo.enums;

import lombok.Getter;

public enum CSVHeaders5 {

   COUNTRY(5,true);




    @Getter
    private final Integer position;
//    @Getter
//    private final Integer maxLength;

    @Getter
    private final Boolean required;


    CSVHeaders5(Integer position,  Boolean required) {
        this.position = position;

        this.required = required;
    }



    public  static final CSVHeaders5[] values =values();

    public static CSVHeaders5 getCSVHeadersEnum(Integer position){
        for (CSVHeaders5 csvHeaders:values){
            if(csvHeaders.getPosition() == position){
                return csvHeaders;
            }
        }
        return null;
    }
}
