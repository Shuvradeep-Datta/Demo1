package com.deep.demo.enums;

import lombok.Getter;

public enum CSVHeaders6 {

    PROCESSING_TEAM(6,true);




    @Getter
    private final Integer position;
//    @Getter
//    private final Integer maxLength;

    @Getter
    private final Boolean required;


    CSVHeaders6(Integer position,  Boolean required) {
        this.position = position;

        this.required = required;
    }



    public  static final CSVHeaders6[] values =values();

    public static CSVHeaders6 getCSVHeadersEnum(Integer position){
        for (CSVHeaders6 csvHeaders:values){
            if(csvHeaders.getPosition() == position){
                return csvHeaders;
            }
        }
        return null;
    }




}
