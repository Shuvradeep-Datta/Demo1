package com.deep.demo.enums;

import lombok.Getter;

public enum CSVHeaders8 {

    JOURNAL_ENTRY_NATURE(8,true);




    @Getter
    private final Integer position;
//    @Getter
//    private final Integer maxLength;

    @Getter
    private final Boolean required;


    CSVHeaders8(Integer position, Boolean required) {
        this.position = position;

        this.required = required;
    }



    public  static final CSVHeaders8[] values =values();

    public static CSVHeaders8 getCSVHeadersEnum(Integer position){
        for (CSVHeaders8 csvHeaders:values){
            if(csvHeaders.getPosition() == position){
                return csvHeaders;
            }
        }
        return null;
    }


}
