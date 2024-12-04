package com.deep.demo.repository.Impl;


import com.deep.demo.dto.*;
import com.deep.demo.repository.RequestRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.*;
import org.springframework.stereotype.Repository;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;

import static com.deep.demo.constants.JournalEntryConstants.OFFSET;
import static com.deep.demo.constants.JournalEntryConstants.PAGESIZE;
import static com.deep.demo.constants.JournalQueriesConstants.*;


@RequiredArgsConstructor
@Repository
public class RequestRepositoryImpl implements RequestRepository {
    private final JdbcTemplate jdbcTemplate;
    private final NamedParameterJdbcTemplate namedJdbcTemplate;
    private static final BeanPropertyRowMapper<RequestDto> beanPropertyRowMapper = BeanPropertyRowMapper.newInstance(RequestDto.class);
    private static final BeanPropertyRowMapper<RecordResponse> BEAN_PROPERTY_ROW_MAPPER = BeanPropertyRowMapper.newInstance(RecordResponse.class);
    private static final BeanPropertyRowMapper<String> BEAN_PROPERTY_ROW_MAPPER_5 = BeanPropertyRowMapper.newInstance(String.class);
    private static final BeanPropertyRowMapper<RecordResponseAdmin> BEAN_PROPERTY_ROW_MAPPER_1 = BeanPropertyRowMapper.newInstance(RecordResponseAdmin.class);
    private static final BeanPropertyRowMapper<tempJournalResponse> BEAN_PROPERTY_ROW_MAPPER_2 = BeanPropertyRowMapper.newInstance(tempJournalResponse.class);
    private static final BeanPropertyRowMapper<CommentDto> BEAN_PROPERTY_ROW_MAPPER_3 = BeanPropertyRowMapper.newInstance(CommentDto.class);
    private static final BeanPropertyRowMapper<RecordInfoResponse> BEAN_PROPERTY_ROW_MAPPER_4 = BeanPropertyRowMapper.newInstance(RecordInfoResponse.class);
    private static final BeanPropertyRowMapper<JournalEntryResponse> BEAN_PROPERTY_ROW_MAPPER_6 = BeanPropertyRowMapper.newInstance(JournalEntryResponse.class);


    public List<RequestDto> getRequests() {
        return jdbcTemplate.query(RECORD_STATUS_QUERY, beanPropertyRowMapper);
    }

    public List<RecordInfoResponse> getRequestsByRequestNumber(String requestNo) {
        return jdbcTemplate.query(GET_REQUEST_BY_REQUEST_NO, BEAN_PROPERTY_ROW_MAPPER_4, requestNo);
    }

    public int getRequestNo() {
        return jdbcTemplate.queryForObject(GET_LATEST_REQUEST_NO, Integer.class);
    }





    public void modifyRequest(Map<String, String> parameters){
        namedJdbcTemplate.update(MODIFY_RECORD, parameters);
    }

    public int getTempJournalEntryCount() {
        return jdbcTemplate.queryForObject(TEMP_JOURNAL_ENTRY_COUNT, Integer.class);
    }

    public List<RecordResponse> getRequestByStatus(Map<String, String> parameters) {
        return namedJdbcTemplate.query(GET_BY_REQUEST_STATUS, parameters, BEAN_PROPERTY_ROW_MAPPER);
    }

    public String getFileName(String requestNo,String fileType) {
        String fileName = null;
        String sql_MJE= "select  MJEFile FROM mje_dbo.Record_Status where mje_dbo.Record_Status.requestNo ='"+requestNo+"'";
        String sql_AJE= "select  AJEFile FROM mje_dbo.Record_Status where mje_dbo.Record_Status.requestNo ='"+requestNo+"'";
        if (fileType.equals("MJE"))
        {
            fileName =   jdbcTemplate.queryForObject(sql_MJE, String.class);
        }
        if (fileType.equals("AJE"))
        {
            fileName =  jdbcTemplate.queryForObject(sql_AJE, String.class);

        }
        return fileName;
    }
    public List<RecordResponseAdmin> getApproveRequestByStatus(Map<String, String> parameters) {
        return namedJdbcTemplate.query(GET_BY_REQUEST_STATUS_APPROVER, parameters, BEAN_PROPERTY_ROW_MAPPER_1);
    }

    @Override
    public List<RecordResponseAdmin> getApproveRequestByUserPending(Map<String, String> parameters) {
        return namedJdbcTemplate.query(GET_BY_REQUEST_STATUS_APPROVER_PENDING, parameters, BEAN_PROPERTY_ROW_MAPPER_1);
    }

    @Override
    public List<RecordResponseAdmin> getApproveRequestByUserApproved(Map<String, String> parameters) {
        return namedJdbcTemplate.query(GET_BY_REQUEST_STATUS_APPROVER_APPROVED, parameters, BEAN_PROPERTY_ROW_MAPPER_1);
    }

    public List<RecordResponseAdmin> getAdminRequestByStatus(Map<String, String> parameters) {
        return namedJdbcTemplate.query(GET_BY_REQUEST_STATUS_ADMIN, parameters, BEAN_PROPERTY_ROW_MAPPER_1);
    }

    @Override
    public List<RecordResponseAdmin> getApproveRequestByUserByStatus(Map<String, String> parameters) {
        return null;
    }

    public List<tempJournalResponse> getMjeByRequestNo(Map<String, String> parameters) {
        return namedJdbcTemplate.query(GET_BY_REQUEST_NO_MJE, parameters, BEAN_PROPERTY_ROW_MAPPER_2);
    }

    public List<CommentDto> getCommentByRequestNo(Map<String, String> parameters) {
        return namedJdbcTemplate.query(GET_BY_REQUEST_NO_COMMENT, parameters, BEAN_PROPERTY_ROW_MAPPER_3);
    }

    public List<RecordInfoResponse> getJournalInfoByRequestNo(Map<String, String> parameters) {
        return namedJdbcTemplate.query(GET_BY_REQUEST_NO_JOURNAL_INFO, parameters, BEAN_PROPERTY_ROW_MAPPER_4);
    }

    public void deleteRecordByRequest(String requestNo) {
        namedJdbcTemplate.update(DELETE_RECORD, getParams(requestNo));
    }

    public void deleteTempByRequest(String requestNo) {
        namedJdbcTemplate.update(DELETE_TEMP_JOURNAL, getParams(requestNo));
    }


    public void addRequest(List<RequestDto> requestUploadDto, MultipartFile mjeFile, MultipartFile ajeFile) {
        if (ajeFile!= null) {
            namedJdbcTemplate.batchUpdate(INSERT_RECORD_STATUS_QUERY, getBatchSetter(requestUploadDto, mjeFile.getOriginalFilename(), ajeFile.getOriginalFilename()));
        }
        else
            namedJdbcTemplate.batchUpdate(INSERT_RECORD_STATUS_QUERY, getBatchSetter(requestUploadDto, mjeFile.getOriginalFilename(), null));
    }



    public void postComment(List<CommentDto> commentDto) {
        namedJdbcTemplate.batchUpdate(INSERT_CONFIG_COMMENT, getBatchSetterComment(commentDto));
    }

    public String postApproval(List<ApprovalDto> approvalDto) {
        String flag = "true";
        String status= "In Progress";
        String final_status = "Approved";
        namedJdbcTemplate.batchUpdate(UPDATE_RECORD_AFTER_APPROVAL, getBatchSetterApproval(approvalDto,flag,status));
        namedJdbcTemplate.batchUpdate(UPDATE_RECORD_AFTER_FINAL_APPROVAL, getBatchSetterApprovalFinal(approvalDto,final_status));
        return getStatus(String.valueOf(approvalDto.get(0).getRequestNo())).get(0);
    }

    public void postRejection(List<ApprovalDto> approvalDto) {
        String flag = "Reject";
        String status = "Rejected";
        namedJdbcTemplate.batchUpdate(UPDATE_RECORD_AFTER_REJECT, getBatchSetterApproval(approvalDto,flag,status));
    }



    @Override
    public void postJournalEntry(tempJournalResponse tempJournalResponses) {
        SqlParameterSource[] batch = SqlParameterSourceUtils.createBatch(tempJournalResponses);
        namedJdbcTemplate.batchUpdate(INSERT_JOURNAL_QUERY, batch);
    }

    public List<String> getStatus(String requestNo) {
        Map<String, String> paramMap = new HashMap<>();
        paramMap.put("requestNo", requestNo);
        return namedJdbcTemplate.query(GET_STATUS_BY_REQUEST_NO, paramMap, (rs, rowNum) -> rs.getString("status"));
    }

    public List<String> getApproverByRequestNo(String requestNo) {
        Map<String, String> paramMap = new HashMap<>();
        paramMap.put("requestNo", requestNo);
        return namedJdbcTemplate.query(GET_APPROVER_BY_REQUEST_NO, paramMap, (rs, rowNum) -> rs.getString("ApproverUser1"));
    }

    public void updateApprover1Flag(String requestNo) {
        Map<String, String> paramMap = new HashMap<>();
        paramMap.put("requestNo", requestNo);
        namedJdbcTemplate.update(SET_STATUS_IN_PROGRESS, paramMap);
    }

    @Override
    public void updateJournalEntry(tempJournalResponse journalEntry) {
        SqlParameterSource paramSource = new BeanPropertySqlParameterSource(journalEntry);
        namedJdbcTemplate.update(UPDATE_JOURNAL_MODIFICATION_ENTRY, paramSource);
    }


    public void adminReviewUpdate(List<AdminRevDto> adminUploadDto) {
        namedJdbcTemplate.batchUpdate(UPDATE_RECORD_ADMIN_REVIEW, getBatchSetterAdminReview(adminUploadDto));
    }

    private SqlParameterSource getParams(String requestNo) {
        return new MapSqlParameterSource().addValue("requestNo", requestNo);
    }

    public void deleteTempByRequestIdentifier(String requestNo, String identifier) {
        Map<String, String> parameterSource = Map.of("requestNo",requestNo,"identifier", identifier);
        namedJdbcTemplate.update(DELETE_TEMP_JOURNAL_IDENTIFIER, parameterSource);
    }

    public List<tempJournalResponse> getJournalEntryList(Map<String, Number> parameters) {
        return namedJdbcTemplate.query(TEMP_JOURNAL_ENTRY_MODIFICATION, parameters, BEAN_PROPERTY_ROW_MAPPER_2);
    }

    private MapSqlParameterSource[] getBatchSetter(List<RequestDto> values, String mjeFileName, String ajeFileName) {
        String requestType = "New";
        String status = "Pending";
        ZoneId zoneId = ZoneId.of("America/Chicago");
        LocalDateTime dateFlag= LocalDateTime.now(zoneId);
        return values.stream().map(value -> new MapSqlParameterSource().addValue("postDate", dateFlag).addValue("description", value.getDescription()).addValue("requestType", requestType).addValue("status", status).addValue("country", value.getCountry()).addValue("approverGroup1", value.getApproverGroup1()).addValue("requestorName", value.getRequestorName()).addValue("approverGroup2", value.getApproverGroup2()).addValue("approverUser1", value.getApproverUser1()).addValue("approverUser2", value.getApproverUser2()).addValue("userId", value.getUserId()).addValue("reason",null).addValue("mjeFile",mjeFileName).addValue("ajeFile",ajeFileName).addValue("rejectedReason",null).addValue("approvedUser1Status",null).addValue("approvedUser2Status",null).addValue("adminUser",null).addValue("adminDate",null)).toArray(MapSqlParameterSource[]::new);
    }

    private MapSqlParameterSource[] getBatchSetterComment(List<CommentDto> commentDto) {
        ZoneId zoneId = ZoneId.of("America/Chicago");
        LocalDateTime dateTimeFlag= LocalDateTime.now(zoneId);
        return commentDto.stream().map(value -> new MapSqlParameterSource().addValue("requestNo", value.getRequestNo()).addValue("commentDate", dateTimeFlag).addValue("comment", value.getComment()).addValue("userId",value.getUserId() )).toArray(MapSqlParameterSource[]::new);
    }

    private MapSqlParameterSource[] getBatchSetterApproval(List<ApprovalDto> approvalDto, String flag, String Status) {
        ZoneId zoneId = ZoneId.of("America/Chicago");
        LocalDateTime dateTimeFlag = LocalDateTime.now(zoneId);
        return approvalDto.stream().map(value ->
                new MapSqlParameterSource()
                        .addValue("requestNo", value.getRequestNo())
                        .addValue("postDate", dateTimeFlag)
                        .addValue("status", Status)
                        .addValue("rejectedReason", value.getRejectedReason())
                        .addValue("reviewerUser", value.getReviewUser())
                        .addValue("flag", flag)
        ).toArray(MapSqlParameterSource[]::new);
    }

    private MapSqlParameterSource[] getBatchSetterAdminReview(List<AdminRevDto> adminUploadDto) {
        ZoneId zoneId = ZoneId.of("America/Chicago");
        LocalDateTime dateTimeFlag= LocalDateTime.now(zoneId);
        return adminUploadDto.stream().map(value -> new MapSqlParameterSource().addValue("requestNo", value.getRequestNo()).addValue("approverGroup1", value.getApproverGroup1()).addValue("approverUser1", value.getApproverUser1()).addValue("approverGroup2", value.getApproverGroup2()).addValue("approverUser2", value.getApproverUser2()).addValue("adminUser", value.getAdminUser()).addValue("adminDate", dateTimeFlag)).toArray(MapSqlParameterSource[]::new);
    }

    public MapSqlParameterSource[] getBatchSetterAdminApproval(List<AdminRevDto> adminUploadDto) {
        ZoneId zoneId = ZoneId.of("America/Chicago");
        LocalDateTime dateTimeFlag= LocalDateTime.now(zoneId);
        return adminUploadDto.stream().map(value -> new MapSqlParameterSource().addValue("requestNo", value.getRequestNo())
                        .addValue("approver1Status", value.getApprover1Status())
                        .addValue("approver2Status", value.getApprover2Status())
                        .addValue("status", "Approved")
                        .addValue("adminUser", value.getAdminUser())
                        .addValue("postDate", dateTimeFlag))
                .toArray(MapSqlParameterSource[]::new);
    }

    public MapSqlParameterSource[] getBatchSetterAdminRejected(List<AdminRevDto> adminUploadDto) {
        ZoneId zoneId = ZoneId.of("America/Chicago");
        LocalDateTime dateTimeFlag= LocalDateTime.now(zoneId);
        return adminUploadDto.stream().map(value -> new MapSqlParameterSource().addValue("requestNo", value.getRequestNo()).addValue("status", "Rejected").addValue("adminUser", value.getAdminUser()).addValue("rejectedReason",value.getRejectedReason()).addValue("postDate", dateTimeFlag)).toArray(MapSqlParameterSource[]::new);
    }

    private MapSqlParameterSource[] getBatchSetterApprovalFinal(List<ApprovalDto> approvalDto, String Status) {
        ZoneId zoneId = ZoneId.of("America/Chicago");
        LocalDateTime dateTimeFlag = LocalDateTime.now(zoneId);

        return approvalDto.stream()
                .map(value -> new MapSqlParameterSource()
                        .addValue("requestNo", value.getRequestNo())
                        .addValue("postDate", dateTimeFlag)
                        .addValue("status", Status)
                        .addValue("rejectedReason", value.getRejectedReason())
                        .addValue("reviewerUser", value.getReviewUser()))
                .toArray(MapSqlParameterSource[]::new);
    }

    public Page<tempJournalResponse> findAll(String requestNo, Pageable page, int count) {
        Map<String, Number> parameters = Map.of(OFFSET,page.getOffset(),PAGESIZE, page.getPageSize(),"requestNo",Integer.parseInt(requestNo) );
        List<tempJournalResponse> tempJournalResponseList = getJournalEntryList(parameters);
        return new PageImpl<>(tempJournalResponseList, page, count);
    }

    public void saveExpectedPeriodToTemp(Integer journalId, String mappedValue) {
        MapSqlParameterSource parameters = new MapSqlParameterSource();
        parameters.addValue("expectedPeriod", mappedValue);
        parameters.addValue("identifier", journalId);
        namedJdbcTemplate.update(UPDATE_EXPECTED_PERIOD_QUERY, parameters);
        namedJdbcTemplate.update(UPDATE_EXPECTED_PERIOD_MODIFICATION_QUERY, parameters);
    }

    public void saveSegmentToTemp(Integer journalId, String mappedValue) {
        MapSqlParameterSource parameters = new MapSqlParameterSource();
        parameters.addValue("segment", mappedValue);
        parameters.addValue("identifier", journalId);
        namedJdbcTemplate.update(UPDATE_SEGMENT_QUERY, parameters);
        namedJdbcTemplate.update(UPDATE_SEGMENT_MODIFICATION_QUERY, parameters);
    }

    public void saveDataSourcesToTemp(Integer journalId, String mappedValue) {
        MapSqlParameterSource parameters = new MapSqlParameterSource();
        parameters.addValue("dataSource", mappedValue);
        parameters.addValue("identifier", journalId);
        namedJdbcTemplate.update(UPDATE_DATASOURCE_QUERY, parameters);
        namedJdbcTemplate.update(UPDATE_DATASOURCE_MODIFICATION_QUERY, parameters);
    }

    public void saveUpstreamDependencyToTemp(Integer journalId, String mappedValue) {
        MapSqlParameterSource parameters = new MapSqlParameterSource();
        parameters.addValue("upstreamDependency", mappedValue);
        parameters.addValue("identifier", journalId);
        namedJdbcTemplate.update(UPDATE_UPSTREAM_DEPENDENCY_QUERY, parameters);
        namedJdbcTemplate.update(UPDATE_UPSTREAM_DEPENDENCY_MODIFICATION_QUERY, parameters);
    }

    public void saveDownStreamImpactToTemp(Integer journalId, String mappedValue) {
        MapSqlParameterSource parameters = new MapSqlParameterSource();
        parameters.addValue("downstreamImpact", mappedValue);
        parameters.addValue("identifier", journalId);
        namedJdbcTemplate.update(UPDATE_DOWNSTREAM_IMPACT_QUERY, parameters);
        namedJdbcTemplate.update(UPDATE_DOWNSTREAM_IMPACT_MODIFICATION_QUERY, parameters);
    }

    public void saveGlAccountToTemp(Integer journalId, String mappedValue) {
        MapSqlParameterSource parameters = new MapSqlParameterSource();
        parameters.addValue("glAccount", mappedValue);
        parameters.addValue("identifier", journalId);
        namedJdbcTemplate.update(UPDATE_GL_ACCOUNT_QUERY, parameters);
        namedJdbcTemplate.update(UPDATE_GL_ACCOUNT_MODIFICATION_QUERY, parameters);
    }

    public void saveFunctionalAreaToTemp(Integer journalId, String mappedValue) {
        MapSqlParameterSource parameters = new MapSqlParameterSource();
        parameters.addValue("functionalArea", mappedValue);
        parameters.addValue("identifier", journalId);
        namedJdbcTemplate.update(UPDATE_FUNCTIONAL_AREA_QUERY, parameters);
        namedJdbcTemplate.update(UPDATE_FUNCTIONAL_AREA_MODIFICATION_QUERY, parameters);
    }

    public void saveDepartmentToTemp(Integer journalId, String mappedValue) {
        MapSqlParameterSource parameters = new MapSqlParameterSource();
        parameters.addValue("centerDepartment", mappedValue);
        parameters.addValue("identifier", journalId);
        namedJdbcTemplate.update(UPDATE_DEPARTMENT_QUERY, parameters);
        namedJdbcTemplate.update(UPDATE_DEPARTMENT_MODIFICATION_QUERY, parameters);
    }

    public void saveCompanyCodeToTemp(Integer journalId, String mappedValue) {
        MapSqlParameterSource parameters = new MapSqlParameterSource();
        parameters.addValue("companyCode", mappedValue);
        parameters.addValue("identifier", journalId);
        namedJdbcTemplate.update(UPDATE_COMPANY_CODE_QUERY, parameters);
        namedJdbcTemplate.update(UPDATE_COMPANY_CODE_MODIFICATION_QUERY, parameters);
    }
}
