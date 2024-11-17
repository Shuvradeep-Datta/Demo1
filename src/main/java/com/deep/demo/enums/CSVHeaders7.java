package com.deep.demo.enums;

import lombok.Getter;

public enum CSVHeaders7 {

    JOURNAL_ENTRY_CATEGORY(7,true);




    @Getter
    private final Integer position;
//    @Getter
//    private final Integer maxLength;

    @Getter
    private final Boolean required;


    CSVHeaders7(Integer position,  Boolean required) {
        this.position = position;

        this.required = required;
    }



    public  static final CSVHeaders7[] values =values();

    public static CSVHeaders7 getCSVHeadersEnum(Integer position){
        for (CSVHeaders7 csvHeaders:values){
            if(csvHeaders.getPosition() == position){
                return csvHeaders;
            }
        }
        return null;
    }


}
