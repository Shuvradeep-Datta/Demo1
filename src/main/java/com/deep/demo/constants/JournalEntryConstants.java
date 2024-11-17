package com.deep.demo.constants;

public class JournalEntryConstants {

    public static  final Integer ERROR_INDEX = 41;

    public static final String SUCCESS_REPORT ="SuccessReport_";

    public static final String ERROR_REPORT ="ErrorReport_";

    public static final String DOWNLOAD_CATALOG_REPORT = "journal_entry_report_";

    public static final String CSV =".csv";
    public  static final String OFFSET ="offset";
    public  static  final  String PAGESIZE ="pageSize";
    public static final String SUCCESS_REPORT_HEADER = "Action, Journal Identifier, Journal Title, Target Ledger System Id, Segment, Country, Processing Team, Journal Entry Category, Journal Entry Nature, Journal Entry Method, Description, Frequency, Expected Period(s),Journal Preparer User ID, Journal approver user ID, Journal reviewer user ID, Accountable business line, Accountable business line user id, EBS Middle office contact user id, Data source(s), Data Sources Description, Upstream dependency, Upstream Dependency Description, Downstream impact, Downstream Impact Description, F/S impact, Expected entry work day, Expected entry time, Intercompany indicator, COGS Impacting, SOX control number, Automation tool, Notes on elimination, Notes on automation, GL Account, Company Code, Center Department, Functional Area, Active Status, Reason why it is inactive, Date it became inactive";

    public static final String ERROR_REPORT_HEADER = "Action, JournalIdentifier, Journal Title, Target Ledger System Id, Segment, Country, Processing Team, Journal Entry Category, Journal Entry Nature, Journal Entry Method, Description, Frequency, Expected Period(s), Journal Preparer User ID, Journal approver user ID, Journal reviewer user ID, Accountable business line, Accountable business line user id, EBS Middle office contact user id, Data source(s), Data Sources Description, Upstream dependency, Upstream Dependency Description, Downstream impact, Downstream Impact Description, F/S impact, Expected entry work day, Expected entry time, Intercompany indicator, COGS Impacting, SOX control number, Automation tool, Notes on elimination, Notes on automation, GL Account, Company Code, Center Department, Functional Area, Active Status, Reason why it is inactive, Date it became inactive, Error Message";
    public static final String JOURNAL_ENTRY="select Distinctnk; shsjsusn";
    public static final String JOURNAL_ENTRY_SINGLE=JOURNAL_ENTRY+"where mje_dbo.journal_entry_identifier =:journalId";
    public static final String JOURNAL_ENTRY_FILTERS="";

    public static final String JOURNAL_ENTRY_FILTERS_PAGED="";
    public static final String LINE = ",1:1,1:1,1:1,1:n, 1:1,1:1,1:1,1:1,1:1,1:1,1:1,1:n, 1:1, 1:1,1:1,1:1,1:1,1:1,1:n, 1:1,1:n, 1:1 1:n, 1:1,1:1,1:1,1:1,1:1,1:1,1:1,1:1,1:1,1:1,1:n, 1:n, 1:n, 1:n, 1:1,1:1,1:1";
}
