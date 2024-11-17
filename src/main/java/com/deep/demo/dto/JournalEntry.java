package com.deep.demo.dto;


import lombok.Builder;
import lombok.Data;
import org.apache.logging.log4j.util.Strings;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.deep.demo.constants.JournalEntryConstants.ERROR_INDEX;
import static java.lang.Integer.max;


@Data
@Builder
public class JournalEntry {
    private boolean header;
    private boolean error;
    private String[] line;
    private String journalId;
    @Builder.Default
    private List<Integer> dataSource = new ArrayList<>();
    @Builder.Default
    private List<Integer> segment = new ArrayList<>();
    @Builder.Default
    private List<Integer> downStreamImpact = new ArrayList<>();
    @Builder.Default
    private List<Integer> upStreamDependency = new ArrayList<>();
    @Builder.Default
    private List<String> department = new ArrayList<>();
    @Builder.Default
    private List<String> companyCode = new ArrayList<>();
    @Builder.Default
    private List<Integer> expectedPeriod = new ArrayList<>();
    @Builder.Default
    private List<String> functionalArea = new ArrayList<>();
    @Builder.Default
    private List<String> glAccount = new ArrayList<>();
    @Builder.Default
    private List<String[]> journalIds = new ArrayList<>();


    public String[] addLine(String[] line,String errorMessage){
        String [] newLine = new String[max(ERROR_INDEX+1, line.length)];
        Arrays.fill(newLine, Strings.EMPTY);
        for(int i=0;i<line.length;i++){
            newLine[i] = line[i].trim();

        }
        newLine[ERROR_INDEX] = errorMessage;
        if(!errorMessage.isEmpty())error =true;
        return newLine;

    }


}
