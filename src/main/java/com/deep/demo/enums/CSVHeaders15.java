package com.deep.demo.enums;

import lombok.Getter;

public enum CSVHeaders15 {

    ACC_BUSINESS_LINE(16,true);




    @Getter
    private final Integer position;
//    @Getter
//    private final Integer maxLength;

    @Getter
    private final Boolean required;


    CSVHeaders15(Integer position, Boolean required) {
        this.position = position;

        this.required = required;
    }



    public  static final CSVHeaders15[] values =values();

    public static CSVHeaders15 getCSVHeadersEnum(Integer position){
        for (CSVHeaders15 csvHeaders:values){
            if(csvHeaders.getPosition() == position){
                return csvHeaders;
            }
        }
        return null;
    }


}
