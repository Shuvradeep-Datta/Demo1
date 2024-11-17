package com.deep.demo.enums;

import lombok.Getter;

public enum CSVHeaders4 {
    TARGET_LEDGER_SYSTEM_ID(3,true);




    @Getter
    private final Integer position;
//    @Getter
//    private final Integer maxLength;

    @Getter
    private final Boolean required;


    CSVHeaders4(Integer position,  Boolean required) {
        this.position = position;

        this.required = required;
    }



    public  static final CSVHeaders4[] values =values();

    public static CSVHeaders4 getCSVHeadersEnum(Integer position){
        for (CSVHeaders4 csvHeaders:values){
            if(csvHeaders.getPosition() == position){
                return csvHeaders;
            }
        }
        return null;
    }

}
