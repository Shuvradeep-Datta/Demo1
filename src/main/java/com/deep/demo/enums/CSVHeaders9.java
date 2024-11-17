package com.deep.demo.enums;

import lombok.Getter;

public enum CSVHeaders9 {

    JOURNAL_ENTRY_METHOD(9,true);




    @Getter
    private final Integer position;
//    @GetterCSVHeaders7
//    private final Integer maxLength;

    @Getter
    private final Boolean required;


    CSVHeaders9(Integer position, Boolean required) {
        this.position = position;

        this.required = required;
    }



    public  static final CSVHeaders9[] values =values();

    public static CSVHeaders9 getCSVHeadersEnum(Integer position){
        for (CSVHeaders9 csvHeaders:values){
            if(csvHeaders.getPosition() == position){
                return csvHeaders;
            }
        }
        return null;
    }


}
