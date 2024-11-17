package com.deep.demo.enums;

import lombok.Getter;

public enum CSVHeaders11 {

    FREQUENCY(11,true);




    @Getter
    private final Integer position;
//    @Getter
//    private final Integer maxLength;

    @Getter
    private final Boolean required;


    CSVHeaders11(Integer position, Boolean required) {
        this.position = position;

        this.required = required;
    }



    public  static final CSVHeaders11[] values =values();

    public static CSVHeaders11 getCSVHeadersEnum(Integer position){
        for (CSVHeaders11 csvHeaders:values){
            if(csvHeaders.getPosition() == position){
                return csvHeaders;
            }
        }
        return null;
    }


}
