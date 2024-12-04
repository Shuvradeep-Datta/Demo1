package com.deep.demo.repository.Impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.atLeast;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.deep.demo.dto.AdminRevDto;
import com.deep.demo.dto.ApprovalDto;
import com.deep.demo.dto.CommentDto;
import com.deep.demo.dto.RecordInfoResponse;
import com.deep.demo.dto.RecordResponse;
import com.deep.demo.dto.RecordResponseAdmin;
import com.deep.demo.dto.RequestDto;
import com.deep.demo.dto.tempJournalResponse;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Disabled;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Pageable;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.multipart.MultipartFile;

@ContextConfiguration(classes = {RequestRepositoryImpl.class})
@ExtendWith(SpringExtension.class)
class RequestRepositoryImplDiffblueTest {
    @MockBean
    private JdbcTemplate jdbcTemplate;

    @MockBean
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Autowired
    private RequestRepositoryImpl requestRepositoryImpl;

    /**
     * Method under test: {@link RequestRepositoryImpl#getRequests()}
     */
    @Test
    void testGetRequests() throws DataAccessException {
        ArrayList<Object> objectList = new ArrayList<>();
        when(jdbcTemplate.query(Mockito.<String>any(), Mockito.<RowMapper<Object>>any())).thenReturn(objectList);
        List<RequestDto> actualRequests = requestRepositoryImpl.getRequests();
        verify(jdbcTemplate).query(Mockito.<String>any(), Mockito.<RowMapper<Object>>any());
        assertTrue(actualRequests.isEmpty());
        assertSame(objectList, actualRequests);
    }

    /**
     * Method under test:
     * {@link RequestRepositoryImpl#getRequestsByRequestNumber(String)}
     */
    @Test
    void testGetRequestsByRequestNumber() throws DataAccessException {
        ArrayList<Object> objectList = new ArrayList<>();
        when(jdbcTemplate.query(Mockito.<String>any(), Mockito.<RowMapper<Object>>any(), isA(Object[].class)))
                .thenReturn(objectList);
        List<RecordInfoResponse> actualRequestsByRequestNumber = requestRepositoryImpl
                .getRequestsByRequestNumber("Request No");
        verify(jdbcTemplate).query(Mockito.<String>any(), Mockito.<RowMapper<Object>>any(), isA(Object[].class));
        assertTrue(actualRequestsByRequestNumber.isEmpty());
        assertSame(objectList, actualRequestsByRequestNumber);
    }

    /**
     * Method under test: {@link RequestRepositoryImpl#getRequestNo()}
     */
    @Test
    void testGetRequestNo() throws DataAccessException {
        when(jdbcTemplate.queryForObject(Mockito.<String>any(), Mockito.<Class<Integer>>any())).thenReturn(1);
        int actualRequestNo = requestRepositoryImpl.getRequestNo();
        verify(jdbcTemplate).queryForObject(Mockito.<String>any(), Mockito.<Class<Integer>>any());
        assertEquals(1, actualRequestNo);
    }

    /**
     * Method under test: {@link RequestRepositoryImpl#modifyRequest(Map)}
     */
    @Test
    void testModifyRequest() throws DataAccessException {
        when(namedParameterJdbcTemplate.update(Mockito.<String>any(), Mockito.<Map<String, Object>>any())).thenReturn(1);
        requestRepositoryImpl.modifyRequest(new HashMap<>());
        verify(namedParameterJdbcTemplate).update(Mockito.<String>any(), Mockito.<Map<String, Object>>any());
    }

    /**
     * Method under test: {@link RequestRepositoryImpl#getTempJournalEntryCount()}
     */
    @Test
    void testGetTempJournalEntryCount() throws DataAccessException {
        when(jdbcTemplate.queryForObject(Mockito.<String>any(), Mockito.<Class<Integer>>any())).thenReturn(1);
        int actualTempJournalEntryCount = requestRepositoryImpl.getTempJournalEntryCount();
        verify(jdbcTemplate).queryForObject(Mockito.<String>any(), Mockito.<Class<Integer>>any());
        assertEquals(1, actualTempJournalEntryCount);
    }

    /**
     * Method under test: {@link RequestRepositoryImpl#getRequestByStatus(Map)}
     */
    @Test
    void testGetRequestByStatus() throws DataAccessException {
        ArrayList<Object> objectList = new ArrayList<>();
        when(namedParameterJdbcTemplate.query(Mockito.<String>any(), Mockito.<Map<String, Object>>any(),
                Mockito.<RowMapper<Object>>any())).thenReturn(objectList);
        List<RecordResponse> actualRequestByStatus = requestRepositoryImpl.getRequestByStatus(new HashMap<>());
        verify(namedParameterJdbcTemplate).query(Mockito.<String>any(), Mockito.<Map<String, Object>>any(),
                Mockito.<RowMapper<Object>>any());
        assertTrue(actualRequestByStatus.isEmpty());
        assertSame(objectList, actualRequestByStatus);
    }

    /**
     * Method under test: {@link RequestRepositoryImpl#getFileName(String, String)}
     */
    @Test
    void testGetFileName() {
        assertNull(requestRepositoryImpl.getFileName("Request No", "File Type"));
    }

    /**
     * Method under test: {@link RequestRepositoryImpl#getFileName(String, String)}
     */
    @Test
    void testGetFileName2() throws DataAccessException {
        when(jdbcTemplate.queryForObject(Mockito.<String>any(), Mockito.<Class<String>>any()))
                .thenReturn("Query For Object");
        String actualFileName = requestRepositoryImpl.getFileName("Request No", "MJE");
        verify(jdbcTemplate).queryForObject(Mockito.<String>any(), Mockito.<Class<String>>any());
        assertEquals("Query For Object", actualFileName);
    }

    /**
     * Method under test: {@link RequestRepositoryImpl#getFileName(String, String)}
     */
    @Test
    void testGetFileName3() throws DataAccessException {
        when(jdbcTemplate.queryForObject(Mockito.<String>any(), Mockito.<Class<String>>any()))
                .thenReturn("Query For Object");
        String actualFileName = requestRepositoryImpl.getFileName("Request No", "AJE");
        verify(jdbcTemplate).queryForObject(Mockito.<String>any(), Mockito.<Class<String>>any());
        assertEquals("Query For Object", actualFileName);
    }

    /**
     * Method under test:
     * {@link RequestRepositoryImpl#getApproveRequestByStatus(Map)}
     */
    @Test
    void testGetApproveRequestByStatus() throws DataAccessException {
        ArrayList<Object> objectList = new ArrayList<>();
        when(namedParameterJdbcTemplate.query(Mockito.<String>any(), Mockito.<Map<String, Object>>any(),
                Mockito.<RowMapper<Object>>any())).thenReturn(objectList);
        List<RecordResponseAdmin> actualApproveRequestByStatus = requestRepositoryImpl
                .getApproveRequestByStatus(new HashMap<>());
        verify(namedParameterJdbcTemplate).query(Mockito.<String>any(), Mockito.<Map<String, Object>>any(),
                Mockito.<RowMapper<Object>>any());
        assertTrue(actualApproveRequestByStatus.isEmpty());
        assertSame(objectList, actualApproveRequestByStatus);
    }

    /**
     * Method under test:
     * {@link RequestRepositoryImpl#getApproveRequestByUserPending(Map)}
     */
    @Test
    void testGetApproveRequestByUserPending() throws DataAccessException {
        ArrayList<Object> objectList = new ArrayList<>();
        when(namedParameterJdbcTemplate.query(Mockito.<String>any(), Mockito.<Map<String, Object>>any(),
                Mockito.<RowMapper<Object>>any())).thenReturn(objectList);
        List<RecordResponseAdmin> actualApproveRequestByUserPending = requestRepositoryImpl
                .getApproveRequestByUserPending(new HashMap<>());
        verify(namedParameterJdbcTemplate).query(Mockito.<String>any(), Mockito.<Map<String, Object>>any(),
                Mockito.<RowMapper<Object>>any());
        assertTrue(actualApproveRequestByUserPending.isEmpty());
        assertSame(objectList, actualApproveRequestByUserPending);
    }

    /**
     * Method under test:
     * {@link RequestRepositoryImpl#getApproveRequestByUserApproved(Map)}
     */
    @Test
    void testGetApproveRequestByUserApproved() throws DataAccessException {
        ArrayList<Object> objectList = new ArrayList<>();
        when(namedParameterJdbcTemplate.query(Mockito.<String>any(), Mockito.<Map<String, Object>>any(),
                Mockito.<RowMapper<Object>>any())).thenReturn(objectList);
        List<RecordResponseAdmin> actualApproveRequestByUserApproved = requestRepositoryImpl
                .getApproveRequestByUserApproved(new HashMap<>());
        verify(namedParameterJdbcTemplate).query(Mockito.<String>any(), Mockito.<Map<String, Object>>any(),
                Mockito.<RowMapper<Object>>any());
        assertTrue(actualApproveRequestByUserApproved.isEmpty());
        assertSame(objectList, actualApproveRequestByUserApproved);
    }

    /**
     * Method under test: {@link RequestRepositoryImpl#getAdminRequestByStatus(Map)}
     */
    @Test
    void testGetAdminRequestByStatus() throws DataAccessException {
        ArrayList<Object> objectList = new ArrayList<>();
        when(namedParameterJdbcTemplate.query(Mockito.<String>any(), Mockito.<Map<String, Object>>any(),
                Mockito.<RowMapper<Object>>any())).thenReturn(objectList);
        List<RecordResponseAdmin> actualAdminRequestByStatus = requestRepositoryImpl
                .getAdminRequestByStatus(new HashMap<>());
        verify(namedParameterJdbcTemplate).query(Mockito.<String>any(), Mockito.<Map<String, Object>>any(),
                Mockito.<RowMapper<Object>>any());
        assertTrue(actualAdminRequestByStatus.isEmpty());
        assertSame(objectList, actualAdminRequestByStatus);
    }

    /**
     * Method under test:
     * {@link RequestRepositoryImpl#getApproveRequestByUserByStatus(Map)}
     */
    @Test
    void testGetApproveRequestByUserByStatus() {
        assertNull(requestRepositoryImpl.getApproveRequestByUserByStatus(new HashMap<>()));
    }

    /**
     * Method under test: {@link RequestRepositoryImpl#getMjeByRequestNo(Map)}
     */
    @Test
    void testGetMjeByRequestNo() throws DataAccessException {
        ArrayList<Object> objectList = new ArrayList<>();
        when(namedParameterJdbcTemplate.query(Mockito.<String>any(), Mockito.<Map<String, Object>>any(),
                Mockito.<RowMapper<Object>>any())).thenReturn(objectList);
        List<tempJournalResponse> actualMjeByRequestNo = requestRepositoryImpl.getMjeByRequestNo(new HashMap<>());
        verify(namedParameterJdbcTemplate).query(Mockito.<String>any(), Mockito.<Map<String, Object>>any(),
                Mockito.<RowMapper<Object>>any());
        assertTrue(actualMjeByRequestNo.isEmpty());
        assertSame(objectList, actualMjeByRequestNo);
    }

    /**
     * Method under test: {@link RequestRepositoryImpl#getCommentByRequestNo(Map)}
     */
    @Test
    void testGetCommentByRequestNo() throws DataAccessException {
        ArrayList<Object> objectList = new ArrayList<>();
        when(namedParameterJdbcTemplate.query(Mockito.<String>any(), Mockito.<Map<String, Object>>any(),
                Mockito.<RowMapper<Object>>any())).thenReturn(objectList);
        List<CommentDto> actualCommentByRequestNo = requestRepositoryImpl.getCommentByRequestNo(new HashMap<>());
        verify(namedParameterJdbcTemplate).query(Mockito.<String>any(), Mockito.<Map<String, Object>>any(),
                Mockito.<RowMapper<Object>>any());
        assertTrue(actualCommentByRequestNo.isEmpty());
        assertSame(objectList, actualCommentByRequestNo);
    }

    /**
     * Method under test:
     * {@link RequestRepositoryImpl#getJournalInfoByRequestNo(Map)}
     */
    @Test
    void testGetJournalInfoByRequestNo() throws DataAccessException {
        ArrayList<Object> objectList = new ArrayList<>();
        when(namedParameterJdbcTemplate.query(Mockito.<String>any(), Mockito.<Map<String, Object>>any(),
                Mockito.<RowMapper<Object>>any())).thenReturn(objectList);
        List<RecordInfoResponse> actualJournalInfoByRequestNo = requestRepositoryImpl
                .getJournalInfoByRequestNo(new HashMap<>());
        verify(namedParameterJdbcTemplate).query(Mockito.<String>any(), Mockito.<Map<String, Object>>any(),
                Mockito.<RowMapper<Object>>any());
        assertTrue(actualJournalInfoByRequestNo.isEmpty());
        assertSame(objectList, actualJournalInfoByRequestNo);
    }

    /**
     * Method under test:
     * {@link RequestRepositoryImpl#deleteRecordByRequest(String)}
     */
    @Test
    void testDeleteRecordByRequest() throws DataAccessException {
        when(namedParameterJdbcTemplate.update(Mockito.<String>any(), Mockito.<SqlParameterSource>any())).thenReturn(1);
        requestRepositoryImpl.deleteRecordByRequest("Request No");
        verify(namedParameterJdbcTemplate).update(Mockito.<String>any(), Mockito.<SqlParameterSource>any());
    }

    /**
     * Method under test: {@link RequestRepositoryImpl#deleteTempByRequest(String)}
     */
    @Test
    void testDeleteTempByRequest() throws DataAccessException {
        when(namedParameterJdbcTemplate.update(Mockito.<String>any(), Mockito.<SqlParameterSource>any())).thenReturn(1);
        requestRepositoryImpl.deleteTempByRequest("Request No");
        verify(namedParameterJdbcTemplate).update(Mockito.<String>any(), Mockito.<SqlParameterSource>any());
    }

    /**
     * Method under test:
     * {@link RequestRepositoryImpl#addRequest(List, MultipartFile, MultipartFile)}
     */
    @Test
    void testAddRequest() throws IOException {
        when(namedParameterJdbcTemplate.batchUpdate(Mockito.<String>any(), Mockito.<SqlParameterSource[]>any()))
                .thenReturn(new int[]{1, -1, 1, -1});
        ArrayList<RequestDto> requestUploadDto = new ArrayList<>();
        MockMultipartFile mjeFile = new MockMultipartFile("Name", new ByteArrayInputStream("AXAXAXAX".getBytes("UTF-8")));

        requestRepositoryImpl.addRequest(requestUploadDto, mjeFile,
                new MockMultipartFile("Name", new ByteArrayInputStream("AXAXAXAX".getBytes("UTF-8"))));
        verify(namedParameterJdbcTemplate).batchUpdate(Mockito.<String>any(), Mockito.<SqlParameterSource[]>any());
    }

    /**
     * Method under test:
     * {@link RequestRepositoryImpl#addRequest(List, MultipartFile, MultipartFile)}
     */
    @Test
    void testAddRequest2() throws IOException {
        when(namedParameterJdbcTemplate.batchUpdate(Mockito.<String>any(), Mockito.<SqlParameterSource[]>any()))
                .thenReturn(new int[]{1, -1, 1, -1});

        RequestDto requestDto = new RequestDto();
        requestDto.setAdminDate("2020-03-01");
        requestDto.setAdminUser(
                "Insert into  mje_dbo.Record_Status values (:postDate, :description, :requestType, :status, :country,"
                        + " :approverGroup1, :requestorName, :approverGroup2, :approverUser1, :approverUser2, :userId"
                        + " ,:reason,:mjeFile ,:ajeFile, :rejectedReason, :approvedUser1Status, :approvedUser2Status, :adminUser"
                        + " , :adminDate )");
        requestDto.setApprovedUser1Status(
                "Insert into  mje_dbo.Record_Status values (:postDate, :description, :requestType, :status, :country,"
                        + " :approverGroup1, :requestorName, :approverGroup2, :approverUser1, :approverUser2, :userId"
                        + " ,:reason,:mjeFile ,:ajeFile, :rejectedReason, :approvedUser1Status, :approvedUser2Status, :adminUser"
                        + " , :adminDate )");
        requestDto.setApprovedUser2Status(
                "Insert into  mje_dbo.Record_Status values (:postDate, :description, :requestType, :status, :country,"
                        + " :approverGroup1, :requestorName, :approverGroup2, :approverUser1, :approverUser2, :userId"
                        + " ,:reason,:mjeFile ,:ajeFile, :rejectedReason, :approvedUser1Status, :approvedUser2Status, :adminUser"
                        + " , :adminDate )");
        requestDto.setApproverGroup1(
                "Insert into  mje_dbo.Record_Status values (:postDate, :description, :requestType, :status, :country,"
                        + " :approverGroup1, :requestorName, :approverGroup2, :approverUser1, :approverUser2, :userId"
                        + " ,:reason,:mjeFile ,:ajeFile, :rejectedReason, :approvedUser1Status, :approvedUser2Status, :adminUser"
                        + " , :adminDate )");
        requestDto.setApproverGroup2(
                "Insert into  mje_dbo.Record_Status values (:postDate, :description, :requestType, :status, :country,"
                        + " :approverGroup1, :requestorName, :approverGroup2, :approverUser1, :approverUser2, :userId"
                        + " ,:reason,:mjeFile ,:ajeFile, :rejectedReason, :approvedUser1Status, :approvedUser2Status, :adminUser"
                        + " , :adminDate )");
        requestDto.setApproverUser1(
                "Insert into  mje_dbo.Record_Status values (:postDate, :description, :requestType, :status, :country,"
                        + " :approverGroup1, :requestorName, :approverGroup2, :approverUser1, :approverUser2, :userId"
                        + " ,:reason,:mjeFile ,:ajeFile, :rejectedReason, :approvedUser1Status, :approvedUser2Status, :adminUser"
                        + " , :adminDate )");
        requestDto.setApproverUser2(
                "Insert into  mje_dbo.Record_Status values (:postDate, :description, :requestType, :status, :country,"
                        + " :approverGroup1, :requestorName, :approverGroup2, :approverUser1, :approverUser2, :userId"
                        + " ,:reason,:mjeFile ,:ajeFile, :rejectedReason, :approvedUser1Status, :approvedUser2Status, :adminUser"
                        + " , :adminDate )");
        requestDto.setCountry("GB");
        requestDto.setDescription("The characteristics of someone or something");
        requestDto.setPostDate(LocalDate.of(1970, 1, 1).atStartOfDay());
        requestDto.setReason("Just cause");
        requestDto.setRejectedReason("Just cause");
        requestDto.setRequestNo(1);
        requestDto.setRequestType(
                "Insert into  mje_dbo.Record_Status values (:postDate, :description, :requestType, :status, :country,"
                        + " :approverGroup1, :requestorName, :approverGroup2, :approverUser1, :approverUser2, :userId"
                        + " ,:reason,:mjeFile ,:ajeFile, :rejectedReason, :approvedUser1Status, :approvedUser2Status, :adminUser"
                        + " , :adminDate )");
        requestDto.setRequestorName(
                "Insert into  mje_dbo.Record_Status values (:postDate, :description, :requestType, :status, :country,"
                        + " :approverGroup1, :requestorName, :approverGroup2, :approverUser1, :approverUser2, :userId"
                        + " ,:reason,:mjeFile ,:ajeFile, :rejectedReason, :approvedUser1Status, :approvedUser2Status, :adminUser"
                        + " , :adminDate )");
        requestDto.setStatus(
                "Insert into  mje_dbo.Record_Status values (:postDate, :description, :requestType, :status, :country,"
                        + " :approverGroup1, :requestorName, :approverGroup2, :approverUser1, :approverUser2, :userId"
                        + " ,:reason,:mjeFile ,:ajeFile, :rejectedReason, :approvedUser1Status, :approvedUser2Status, :adminUser"
                        + " , :adminDate )");
        requestDto.setUserId("42");

        ArrayList<RequestDto> requestUploadDto = new ArrayList<>();
        requestUploadDto.add(requestDto);
        MockMultipartFile mjeFile = new MockMultipartFile("Name", new ByteArrayInputStream("AXAXAXAX".getBytes("UTF-8")));

        requestRepositoryImpl.addRequest(requestUploadDto, mjeFile,
                new MockMultipartFile("Name", new ByteArrayInputStream("AXAXAXAX".getBytes("UTF-8"))));
        verify(namedParameterJdbcTemplate).batchUpdate(Mockito.<String>any(), Mockito.<SqlParameterSource[]>any());
    }

    /**
     * Method under test:
     * {@link RequestRepositoryImpl#addRequest(List, MultipartFile, MultipartFile)}
     */
    @Test
    void testAddRequest3() throws IOException {
        when(namedParameterJdbcTemplate.batchUpdate(Mockito.<String>any(), Mockito.<SqlParameterSource[]>any()))
                .thenReturn(new int[]{1, -1, 1, -1});

        RequestDto requestDto = new RequestDto();
        requestDto.setAdminDate("2020-03-01");
        requestDto.setAdminUser(
                "Insert into  mje_dbo.Record_Status values (:postDate, :description, :requestType, :status, :country,"
                        + " :approverGroup1, :requestorName, :approverGroup2, :approverUser1, :approverUser2, :userId"
                        + " ,:reason,:mjeFile ,:ajeFile, :rejectedReason, :approvedUser1Status, :approvedUser2Status, :adminUser"
                        + " , :adminDate )");
        requestDto.setApprovedUser1Status(
                "Insert into  mje_dbo.Record_Status values (:postDate, :description, :requestType, :status, :country,"
                        + " :approverGroup1, :requestorName, :approverGroup2, :approverUser1, :approverUser2, :userId"
                        + " ,:reason,:mjeFile ,:ajeFile, :rejectedReason, :approvedUser1Status, :approvedUser2Status, :adminUser"
                        + " , :adminDate )");
        requestDto.setApprovedUser2Status(
                "Insert into  mje_dbo.Record_Status values (:postDate, :description, :requestType, :status, :country,"
                        + " :approverGroup1, :requestorName, :approverGroup2, :approverUser1, :approverUser2, :userId"
                        + " ,:reason,:mjeFile ,:ajeFile, :rejectedReason, :approvedUser1Status, :approvedUser2Status, :adminUser"
                        + " , :adminDate )");
        requestDto.setApproverGroup1(
                "Insert into  mje_dbo.Record_Status values (:postDate, :description, :requestType, :status, :country,"
                        + " :approverGroup1, :requestorName, :approverGroup2, :approverUser1, :approverUser2, :userId"
                        + " ,:reason,:mjeFile ,:ajeFile, :rejectedReason, :approvedUser1Status, :approvedUser2Status, :adminUser"
                        + " , :adminDate )");
        requestDto.setApproverGroup2(
                "Insert into  mje_dbo.Record_Status values (:postDate, :description, :requestType, :status, :country,"
                        + " :approverGroup1, :requestorName, :approverGroup2, :approverUser1, :approverUser2, :userId"
                        + " ,:reason,:mjeFile ,:ajeFile, :rejectedReason, :approvedUser1Status, :approvedUser2Status, :adminUser"
                        + " , :adminDate )");
        requestDto.setApproverUser1(
                "Insert into  mje_dbo.Record_Status values (:postDate, :description, :requestType, :status, :country,"
                        + " :approverGroup1, :requestorName, :approverGroup2, :approverUser1, :approverUser2, :userId"
                        + " ,:reason,:mjeFile ,:ajeFile, :rejectedReason, :approvedUser1Status, :approvedUser2Status, :adminUser"
                        + " , :adminDate )");
        requestDto.setApproverUser2(
                "Insert into  mje_dbo.Record_Status values (:postDate, :description, :requestType, :status, :country,"
                        + " :approverGroup1, :requestorName, :approverGroup2, :approverUser1, :approverUser2, :userId"
                        + " ,:reason,:mjeFile ,:ajeFile, :rejectedReason, :approvedUser1Status, :approvedUser2Status, :adminUser"
                        + " , :adminDate )");
        requestDto.setCountry("GB");
        requestDto.setDescription("The characteristics of someone or something");
        requestDto.setPostDate(LocalDate.of(1970, 1, 1).atStartOfDay());
        requestDto.setReason("Just cause");
        requestDto.setRejectedReason("Just cause");
        requestDto.setRequestNo(1);
        requestDto.setRequestType(
                "Insert into  mje_dbo.Record_Status values (:postDate, :description, :requestType, :status, :country,"
                        + " :approverGroup1, :requestorName, :approverGroup2, :approverUser1, :approverUser2, :userId"
                        + " ,:reason,:mjeFile ,:ajeFile, :rejectedReason, :approvedUser1Status, :approvedUser2Status, :adminUser"
                        + " , :adminDate )");
        requestDto.setRequestorName(
                "Insert into  mje_dbo.Record_Status values (:postDate, :description, :requestType, :status, :country,"
                        + " :approverGroup1, :requestorName, :approverGroup2, :approverUser1, :approverUser2, :userId"
                        + " ,:reason,:mjeFile ,:ajeFile, :rejectedReason, :approvedUser1Status, :approvedUser2Status, :adminUser"
                        + " , :adminDate )");
        requestDto.setStatus(
                "Insert into  mje_dbo.Record_Status values (:postDate, :description, :requestType, :status, :country,"
                        + " :approverGroup1, :requestorName, :approverGroup2, :approverUser1, :approverUser2, :userId"
                        + " ,:reason,:mjeFile ,:ajeFile, :rejectedReason, :approvedUser1Status, :approvedUser2Status, :adminUser"
                        + " , :adminDate )");
        requestDto.setUserId("42");

        RequestDto requestDto2 = new RequestDto();
        requestDto2.setAdminDate("2020/03/01");
        requestDto2.setAdminUser("New");
        requestDto2.setApprovedUser1Status("New");
        requestDto2.setApprovedUser2Status("New");
        requestDto2.setApproverGroup1("New");
        requestDto2.setApproverGroup2("New");
        requestDto2.setApproverUser1("New");
        requestDto2.setApproverUser2("New");
        requestDto2.setCountry("GBR");
        requestDto2.setDescription(
                "Insert into  mje_dbo.Record_Status values (:postDate, :description, :requestType, :status, :country,"
                        + " :approverGroup1, :requestorName, :approverGroup2, :approverUser1, :approverUser2, :userId"
                        + " ,:reason,:mjeFile ,:ajeFile, :rejectedReason, :approvedUser1Status, :approvedUser2Status, :adminUser"
                        + " , :adminDate )");
        requestDto2.setPostDate(LocalDate.of(1970, 1, 1).atStartOfDay());
        requestDto2.setReason(
                "Insert into  mje_dbo.Record_Status values (:postDate, :description, :requestType, :status, :country,"
                        + " :approverGroup1, :requestorName, :approverGroup2, :approverUser1, :approverUser2, :userId"
                        + " ,:reason,:mjeFile ,:ajeFile, :rejectedReason, :approvedUser1Status, :approvedUser2Status, :adminUser"
                        + " , :adminDate )");
        requestDto2.setRejectedReason(
                "Insert into  mje_dbo.Record_Status values (:postDate, :description, :requestType, :status, :country,"
                        + " :approverGroup1, :requestorName, :approverGroup2, :approverUser1, :approverUser2, :userId"
                        + " ,:reason,:mjeFile ,:ajeFile, :rejectedReason, :approvedUser1Status, :approvedUser2Status, :adminUser"
                        + " , :adminDate )");
        requestDto2.setRequestNo(0);
        requestDto2.setRequestType("New");
        requestDto2.setRequestorName("New");
        requestDto2.setStatus("New");
        requestDto2.setUserId(
                "Insert into  mje_dbo.Record_Status values (:postDate, :description, :requestType, :status, :country,"
                        + " :approverGroup1, :requestorName, :approverGroup2, :approverUser1, :approverUser2, :userId"
                        + " ,:reason,:mjeFile ,:ajeFile, :rejectedReason, :approvedUser1Status, :approvedUser2Status, :adminUser"
                        + " , :adminDate )");

        ArrayList<RequestDto> requestUploadDto = new ArrayList<>();
        requestUploadDto.add(requestDto2);
        requestUploadDto.add(requestDto);
        MockMultipartFile mjeFile = new MockMultipartFile("Name", new ByteArrayInputStream("AXAXAXAX".getBytes("UTF-8")));

        requestRepositoryImpl.addRequest(requestUploadDto, mjeFile,
                new MockMultipartFile("Name", new ByteArrayInputStream("AXAXAXAX".getBytes("UTF-8"))));
        verify(namedParameterJdbcTemplate).batchUpdate(Mockito.<String>any(), Mockito.<SqlParameterSource[]>any());
    }

    /**
     * Method under test:
     * {@link RequestRepositoryImpl#addRequest(List, MultipartFile, MultipartFile)}
     */
    @Test
    void testAddRequest4() throws IOException {
        when(namedParameterJdbcTemplate.batchUpdate(Mockito.<String>any(), Mockito.<SqlParameterSource[]>any()))
                .thenReturn(new int[]{1, -1, 1, -1});
        ArrayList<RequestDto> requestUploadDto = new ArrayList<>();
        requestRepositoryImpl.addRequest(requestUploadDto,
                new MockMultipartFile("Name", new ByteArrayInputStream("AXAXAXAX".getBytes("UTF-8"))), null);
        verify(namedParameterJdbcTemplate).batchUpdate(Mockito.<String>any(), Mockito.<SqlParameterSource[]>any());
    }

    /**
     * Method under test: {@link RequestRepositoryImpl#postComment(List)}
     */
    @Test
    void testPostComment() {
        when(namedParameterJdbcTemplate.batchUpdate(Mockito.<String>any(), Mockito.<SqlParameterSource[]>any()))
                .thenReturn(new int[]{1, -1, 1, -1});
        requestRepositoryImpl.postComment(new ArrayList<>());
        verify(namedParameterJdbcTemplate).batchUpdate(Mockito.<String>any(), Mockito.<SqlParameterSource[]>any());
    }

    /**
     * Method under test: {@link RequestRepositoryImpl#postComment(List)}
     */
    @Test
    void testPostComment2() {
        when(namedParameterJdbcTemplate.batchUpdate(Mockito.<String>any(), Mockito.<SqlParameterSource[]>any()))
                .thenReturn(new int[]{1, -1, 1, -1});

        CommentDto commentDto = new CommentDto();
        commentDto.setComment("Insert into  mje_dbo.config_comment values (:requestNo, :comment, :commentDate, :userId)");
        commentDto.setCommentDate(LocalDate.of(1970, 1, 1).atStartOfDay());
        commentDto.setRequestNo("Insert into  mje_dbo.config_comment values (:requestNo, :comment, :commentDate, :userId)");
        commentDto.setUserId("42");

        ArrayList<CommentDto> commentDto2 = new ArrayList<>();
        commentDto2.add(commentDto);
        requestRepositoryImpl.postComment(commentDto2);
        verify(namedParameterJdbcTemplate).batchUpdate(Mockito.<String>any(), Mockito.<SqlParameterSource[]>any());
    }

    /**
     * Method under test: {@link RequestRepositoryImpl#postComment(List)}
     */
    @Test
    void testPostComment3() {
        when(namedParameterJdbcTemplate.batchUpdate(Mockito.<String>any(), Mockito.<SqlParameterSource[]>any()))
                .thenReturn(new int[]{1, -1, 1, -1});

        CommentDto commentDto = new CommentDto();
        commentDto.setComment("Insert into  mje_dbo.config_comment values (:requestNo, :comment, :commentDate, :userId)");
        commentDto.setCommentDate(LocalDate.of(1970, 1, 1).atStartOfDay());
        commentDto.setRequestNo("Insert into  mje_dbo.config_comment values (:requestNo, :comment, :commentDate, :userId)");
        commentDto.setUserId("42");

        CommentDto commentDto2 = new CommentDto();
        commentDto2.setComment("America/Chicago");
        commentDto2.setCommentDate(LocalDate.of(1970, 1, 1).atStartOfDay());
        commentDto2.setRequestNo("America/Chicago");
        commentDto2.setUserId("Insert into  mje_dbo.config_comment values (:requestNo, :comment, :commentDate, :userId)");

        ArrayList<CommentDto> commentDto3 = new ArrayList<>();
        commentDto3.add(commentDto2);
        commentDto3.add(commentDto);
        requestRepositoryImpl.postComment(commentDto3);
        verify(namedParameterJdbcTemplate).batchUpdate(Mockito.<String>any(), Mockito.<SqlParameterSource[]>any());
    }

    /**
     * Method under test: {@link RequestRepositoryImpl#postApproval(List)}
     */
    @Test
    void testPostApproval() throws DataAccessException {
        ArrayList<Object> objectList = new ArrayList<>();
        objectList.add("42");
        when(namedParameterJdbcTemplate.query(Mockito.<String>any(), Mockito.<Map<String, Object>>any(),
                Mockito.<RowMapper<Object>>any())).thenReturn(objectList);
        when(namedParameterJdbcTemplate.batchUpdate(Mockito.<String>any(), Mockito.<SqlParameterSource[]>any()))
                .thenReturn(new int[]{1, -1, 1, -1});

        ApprovalDto approvalDto = new ApprovalDto();
        approvalDto.setApprovedUser1Status(Boolean.TRUE.toString());
        approvalDto.setApprovedUser2Status(Boolean.TRUE.toString());
        approvalDto.setFlag(Boolean.TRUE.toString());
        approvalDto.setPostDate("2020-03-01");
        approvalDto.setRejectedReason("Just cause");
        approvalDto.setRequestNo(1);
        approvalDto.setReviewUser(Boolean.TRUE.toString());
        approvalDto.setStatus(Boolean.TRUE.toString());

        ArrayList<ApprovalDto> approvalDto2 = new ArrayList<>();
        approvalDto2.add(approvalDto);
        String actualPostApprovalResult = requestRepositoryImpl.postApproval(approvalDto2);
        verify(namedParameterJdbcTemplate, atLeast(1)).batchUpdate(Mockito.<String>any(),
                Mockito.<SqlParameterSource[]>any());
        verify(namedParameterJdbcTemplate).query(Mockito.<String>any(), Mockito.<Map<String, Object>>any(),
                Mockito.<RowMapper<Object>>any());
        assertEquals("42", actualPostApprovalResult);
    }

    /**
     * Method under test: {@link RequestRepositoryImpl#postRejection(List)}
     */
    @Test
    void testPostRejection() {
        when(namedParameterJdbcTemplate.batchUpdate(Mockito.<String>any(), Mockito.<SqlParameterSource[]>any()))
                .thenReturn(new int[]{1, -1, 1, -1});
        requestRepositoryImpl.postRejection(new ArrayList<>());
        verify(namedParameterJdbcTemplate).batchUpdate(Mockito.<String>any(), Mockito.<SqlParameterSource[]>any());
    }

    /**
     * Method under test: {@link RequestRepositoryImpl#postRejection(List)}
     */
    @Test
    void testPostRejection2() {
        when(namedParameterJdbcTemplate.batchUpdate(Mockito.<String>any(), Mockito.<SqlParameterSource[]>any()))
                .thenReturn(new int[]{1, -1, 1, -1});

        ApprovalDto approvalDto = new ApprovalDto();
        approvalDto.setApprovedUser1Status("Reject");
        approvalDto.setApprovedUser2Status("Reject");
        approvalDto.setFlag("Reject");
        approvalDto.setPostDate("2020-03-01");
        approvalDto.setRejectedReason("Just cause");
        approvalDto.setRequestNo(1);
        approvalDto.setReviewUser("Reject");
        approvalDto.setStatus("Reject");

        ArrayList<ApprovalDto> approvalDto2 = new ArrayList<>();
        approvalDto2.add(approvalDto);
        requestRepositoryImpl.postRejection(approvalDto2);
        verify(namedParameterJdbcTemplate).batchUpdate(Mockito.<String>any(), Mockito.<SqlParameterSource[]>any());
    }

    /**
     * Method under test: {@link RequestRepositoryImpl#postRejection(List)}
     */
    @Test
    void testPostRejection3() {
        when(namedParameterJdbcTemplate.batchUpdate(Mockito.<String>any(), Mockito.<SqlParameterSource[]>any()))
                .thenReturn(new int[]{1, -1, 1, -1});

        ApprovalDto approvalDto = new ApprovalDto();
        approvalDto.setApprovedUser1Status("Reject");
        approvalDto.setApprovedUser2Status("Reject");
        approvalDto.setFlag("Reject");
        approvalDto.setPostDate("2020-03-01");
        approvalDto.setRejectedReason("Just cause");
        approvalDto.setRequestNo(1);
        approvalDto.setReviewUser("Reject");
        approvalDto.setStatus("Reject");

        ApprovalDto approvalDto2 = new ApprovalDto();
        approvalDto2.setApprovedUser1Status("Rejected");
        approvalDto2.setApprovedUser2Status("Rejected");
        approvalDto2.setFlag("Rejected");
        approvalDto2.setPostDate("2020/03/01");
        approvalDto2.setRejectedReason("Reject");
        approvalDto2.setRequestNo(0);
        approvalDto2.setReviewUser("Rejected");
        approvalDto2.setStatus("Rejected");

        ArrayList<ApprovalDto> approvalDto3 = new ArrayList<>();
        approvalDto3.add(approvalDto2);
        approvalDto3.add(approvalDto);
        requestRepositoryImpl.postRejection(approvalDto3);
        verify(namedParameterJdbcTemplate).batchUpdate(Mockito.<String>any(), Mockito.<SqlParameterSource[]>any());
    }

    /**
     * Method under test:
     * {@link RequestRepositoryImpl#postJournalEntry(tempJournalResponse)}
     */
    @Test
    void testPostJournalEntry() {
        when(namedParameterJdbcTemplate.batchUpdate(Mockito.<String>any(), Mockito.<SqlParameterSource[]>any()))
                .thenReturn(new int[]{1, -1, 1, -1});

        tempJournalResponse tempJournalResponses = new tempJournalResponse();
        tempJournalResponses.setAccountableBusinessLineId("42");
        tempJournalResponses.setAccountableBusinessLineUserId("42");
        tempJournalResponses.setApproverUserId("42");
        tempJournalResponses.setAutomationNotes("Automation Notes");
        tempJournalResponses.setAutomationToolId("42");
        tempJournalResponses.setCategoryId("42");
        tempJournalResponses.setCenterDepartment(new ArrayList<>());
        tempJournalResponses.setCertificationDate("2020-03-01");
        tempJournalResponses.setCertificationStatus("Certification Status");
        tempJournalResponses.setCogsImpacting("Cogs Impacting");
        tempJournalResponses.setCompanyCode(new ArrayList<>());
        tempJournalResponses.setCountryId("GB");
        tempJournalResponses.setCreationDate("2020-03-01");
        tempJournalResponses.setDataSources(new ArrayList<>());
        tempJournalResponses.setDataSourcesDescription("Data Sources Description");
        tempJournalResponses.setDelayedBy(1);
        tempJournalResponses.setDescription("The characteristics of someone or something");
        tempJournalResponses.setDownstreamImpact(new ArrayList<>());
        tempJournalResponses.setDownstreamImpactDescription("Downstream Impact Description");
        tempJournalResponses.setEbsMiddleOfficeUserId("42");
        tempJournalResponses.setEliminationNotes("Elimination Notes");
        tempJournalResponses.setEntryWorkdayTime("Entry Workday Time");
        tempJournalResponses.setExpectedEntryWorkdayId(1);
        tempJournalResponses.setExpectedPeriod(new ArrayList<>());
        tempJournalResponses.setFrequencyId("42");
        tempJournalResponses.setFsImpactId("42");
        tempJournalResponses.setFunctionalArea(new ArrayList<>());
        tempJournalResponses.setGlAccount(new ArrayList<>());
        tempJournalResponses.setIdentifier(1);
        tempJournalResponses.setInactivatedDate("2020-03-01");
        tempJournalResponses.setIntercompanyIndicatorId("42");
        tempJournalResponses.setMethodId("42");
        tempJournalResponses.setModification("Modification");
        tempJournalResponses.setNatureId("42");
        tempJournalResponses.setPreparerUserId("42");
        tempJournalResponses.setProcessingTeamId("42");
        tempJournalResponses.setReasonForInactive("Just cause");
        tempJournalResponses.setRequestNo("Request No");
        tempJournalResponses.setReviewerUserId("42");
        tempJournalResponses.setRn("Rn");
        tempJournalResponses.setSegment(new ArrayList<>());
        tempJournalResponses.setSoxControlNumber("42");
        tempJournalResponses.setStatus("Status");
        tempJournalResponses.setTargetLedgerSystemId("42");
        tempJournalResponses.setTitle("Dr");
        tempJournalResponses.setUpstreamDependency(new ArrayList<>());
        tempJournalResponses.setUpstreamDependencyDescription("Upstream Dependency Description");
        requestRepositoryImpl.postJournalEntry(tempJournalResponses);
        verify(namedParameterJdbcTemplate).batchUpdate(Mockito.<String>any(), Mockito.<SqlParameterSource[]>any());
    }

    /**
     * Method under test: {@link RequestRepositoryImpl#getStatus(String)}
     */
    @Test
    void testGetStatus() throws DataAccessException {
        ArrayList<Object> objectList = new ArrayList<>();
        when(namedParameterJdbcTemplate.query(Mockito.<String>any(), Mockito.<Map<String, Object>>any(),
                Mockito.<RowMapper<Object>>any())).thenReturn(objectList);
        List<String> actualStatus = requestRepositoryImpl.getStatus("Request No");
        verify(namedParameterJdbcTemplate).query(Mockito.<String>any(), Mockito.<Map<String, Object>>any(),
                Mockito.<RowMapper<Object>>any());
        assertTrue(actualStatus.isEmpty());
        assertSame(objectList, actualStatus);
    }

    /**
     * Method under test:
     * {@link RequestRepositoryImpl#getApproverByRequestNo(String)}
     */
    @Test
    void testGetApproverByRequestNo() throws DataAccessException {
        ArrayList<Object> objectList = new ArrayList<>();
        when(namedParameterJdbcTemplate.query(Mockito.<String>any(), Mockito.<Map<String, Object>>any(),
                Mockito.<RowMapper<Object>>any())).thenReturn(objectList);
        List<String> actualApproverByRequestNo = requestRepositoryImpl.getApproverByRequestNo("Request No");
        verify(namedParameterJdbcTemplate).query(Mockito.<String>any(), Mockito.<Map<String, Object>>any(),
                Mockito.<RowMapper<Object>>any());
        assertTrue(actualApproverByRequestNo.isEmpty());
        assertSame(objectList, actualApproverByRequestNo);
    }

    /**
     * Method under test: {@link RequestRepositoryImpl#updateApprover1Flag(String)}
     */
    @Test
    void testUpdateApprover1Flag() throws DataAccessException {
        when(namedParameterJdbcTemplate.update(Mockito.<String>any(), Mockito.<Map<String, Object>>any())).thenReturn(1);
        requestRepositoryImpl.updateApprover1Flag("Request No");
        verify(namedParameterJdbcTemplate).update(Mockito.<String>any(), Mockito.<Map<String, Object>>any());
    }

    /**
     * Method under test:
     * {@link RequestRepositoryImpl#updateJournalEntry(tempJournalResponse)}
     */
    @Test
    void testUpdateJournalEntry() throws DataAccessException {
        when(namedParameterJdbcTemplate.update(Mockito.<String>any(), Mockito.<SqlParameterSource>any())).thenReturn(1);

        tempJournalResponse journalEntry = new tempJournalResponse();
        journalEntry.setAccountableBusinessLineId("42");
        journalEntry.setAccountableBusinessLineUserId("42");
        journalEntry.setApproverUserId("42");
        journalEntry.setAutomationNotes("Automation Notes");
        journalEntry.setAutomationToolId("42");
        journalEntry.setCategoryId("42");
        journalEntry.setCenterDepartment(new ArrayList<>());
        journalEntry.setCertificationDate("2020-03-01");
        journalEntry.setCertificationStatus("Certification Status");
        journalEntry.setCogsImpacting("Cogs Impacting");
        journalEntry.setCompanyCode(new ArrayList<>());
        journalEntry.setCountryId("GB");
        journalEntry.setCreationDate("2020-03-01");
        journalEntry.setDataSources(new ArrayList<>());
        journalEntry.setDataSourcesDescription("Data Sources Description");
        journalEntry.setDelayedBy(1);
        journalEntry.setDescription("The characteristics of someone or something");
        journalEntry.setDownstreamImpact(new ArrayList<>());
        journalEntry.setDownstreamImpactDescription("Downstream Impact Description");
        journalEntry.setEbsMiddleOfficeUserId("42");
        journalEntry.setEliminationNotes("Elimination Notes");
        journalEntry.setEntryWorkdayTime("Entry Workday Time");
        journalEntry.setExpectedEntryWorkdayId(1);
        journalEntry.setExpectedPeriod(new ArrayList<>());
        journalEntry.setFrequencyId("42");
        journalEntry.setFsImpactId("42");
        journalEntry.setFunctionalArea(new ArrayList<>());
        journalEntry.setGlAccount(new ArrayList<>());
        journalEntry.setIdentifier(1);
        journalEntry.setInactivatedDate("2020-03-01");
        journalEntry.setIntercompanyIndicatorId("42");
        journalEntry.setMethodId("42");
        journalEntry.setModification("Modification");
        journalEntry.setNatureId("42");
        journalEntry.setPreparerUserId("42");
        journalEntry.setProcessingTeamId("42");
        journalEntry.setReasonForInactive("Just cause");
        journalEntry.setRequestNo("Request No");
        journalEntry.setReviewerUserId("42");
        journalEntry.setRn("Rn");
        journalEntry.setSegment(new ArrayList<>());
        journalEntry.setSoxControlNumber("42");
        journalEntry.setStatus("Status");
        journalEntry.setTargetLedgerSystemId("42");
        journalEntry.setTitle("Dr");
        journalEntry.setUpstreamDependency(new ArrayList<>());
        journalEntry.setUpstreamDependencyDescription("Upstream Dependency Description");
        requestRepositoryImpl.updateJournalEntry(journalEntry);
        verify(namedParameterJdbcTemplate).update(Mockito.<String>any(), Mockito.<SqlParameterSource>any());
    }

    /**
     * Method under test: {@link RequestRepositoryImpl#adminReviewUpdate(List)}
     */
    @Test
    void testAdminReviewUpdate() {
        when(namedParameterJdbcTemplate.batchUpdate(Mockito.<String>any(), Mockito.<SqlParameterSource[]>any()))
                .thenReturn(new int[]{1, -1, 1, -1});
        requestRepositoryImpl.adminReviewUpdate(new ArrayList<>());
        verify(namedParameterJdbcTemplate).batchUpdate(Mockito.<String>any(), Mockito.<SqlParameterSource[]>any());
    }

    /**
     * Method under test: {@link RequestRepositoryImpl#adminReviewUpdate(List)}
     */
    @Test
    void testAdminReviewUpdate2() {
        when(namedParameterJdbcTemplate.batchUpdate(Mockito.<String>any(), Mockito.<SqlParameterSource[]>any()))
                .thenReturn(new int[]{1, -1, 1, -1});

        AdminRevDto adminRevDto = new AdminRevDto();
        adminRevDto.setAdminDate(LocalDate.of(1970, 1, 1).atStartOfDay());
        adminRevDto.setAdminUser(
                "Update mje_dbo.Record_Status set approverGroup1 = :approverGroup1, approverUser1 = :approverUser1,"
                        + " approverGroup2 = :approverGroup2, approverUser2 = :approverUser2 , adminUser= :adminUser , adminDate"
                        + " = :adminDate where requestNo=:requestNo");
        adminRevDto.setApprover1Status(
                "Update mje_dbo.Record_Status set approverGroup1 = :approverGroup1, approverUser1 = :approverUser1,"
                        + " approverGroup2 = :approverGroup2, approverUser2 = :approverUser2 , adminUser= :adminUser , adminDate"
                        + " = :adminDate where requestNo=:requestNo");
        adminRevDto.setApprover2Status(
                "Update mje_dbo.Record_Status set approverGroup1 = :approverGroup1, approverUser1 = :approverUser1,"
                        + " approverGroup2 = :approverGroup2, approverUser2 = :approverUser2 , adminUser= :adminUser , adminDate"
                        + " = :adminDate where requestNo=:requestNo");
        adminRevDto.setApproverGroup1(
                "Update mje_dbo.Record_Status set approverGroup1 = :approverGroup1, approverUser1 = :approverUser1,"
                        + " approverGroup2 = :approverGroup2, approverUser2 = :approverUser2 , adminUser= :adminUser , adminDate"
                        + " = :adminDate where requestNo=:requestNo");
        adminRevDto.setApproverGroup2(
                "Update mje_dbo.Record_Status set approverGroup1 = :approverGroup1, approverUser1 = :approverUser1,"
                        + " approverGroup2 = :approverGroup2, approverUser2 = :approverUser2 , adminUser= :adminUser , adminDate"
                        + " = :adminDate where requestNo=:requestNo");
        adminRevDto.setApproverUser1(
                "Update mje_dbo.Record_Status set approverGroup1 = :approverGroup1, approverUser1 = :approverUser1,"
                        + " approverGroup2 = :approverGroup2, approverUser2 = :approverUser2 , adminUser= :adminUser , adminDate"
                        + " = :adminDate where requestNo=:requestNo");
        adminRevDto.setApproverUser2(
                "Update mje_dbo.Record_Status set approverGroup1 = :approverGroup1, approverUser1 = :approverUser1,"
                        + " approverGroup2 = :approverGroup2, approverUser2 = :approverUser2 , adminUser= :adminUser , adminDate"
                        + " = :adminDate where requestNo=:requestNo");
        adminRevDto.setRejectedReason("Just cause");
        adminRevDto.setRequestNo(
                "Update mje_dbo.Record_Status set approverGroup1 = :approverGroup1, approverUser1 = :approverUser1,"
                        + " approverGroup2 = :approverGroup2, approverUser2 = :approverUser2 , adminUser= :adminUser , adminDate"
                        + " = :adminDate where requestNo=:requestNo");

        ArrayList<AdminRevDto> adminUploadDto = new ArrayList<>();
        adminUploadDto.add(adminRevDto);
        requestRepositoryImpl.adminReviewUpdate(adminUploadDto);
        verify(namedParameterJdbcTemplate).batchUpdate(Mockito.<String>any(), Mockito.<SqlParameterSource[]>any());
    }

    /**
     * Method under test: {@link RequestRepositoryImpl#adminReviewUpdate(List)}
     */
    @Test
    void testAdminReviewUpdate3() {
        when(namedParameterJdbcTemplate.batchUpdate(Mockito.<String>any(), Mockito.<SqlParameterSource[]>any()))
                .thenReturn(new int[]{1, -1, 1, -1});

        AdminRevDto adminRevDto = new AdminRevDto();
        adminRevDto.setAdminDate(LocalDate.of(1970, 1, 1).atStartOfDay());
        adminRevDto.setAdminUser(
                "Update mje_dbo.Record_Status set approverGroup1 = :approverGroup1, approverUser1 = :approverUser1,"
                        + " approverGroup2 = :approverGroup2, approverUser2 = :approverUser2 , adminUser= :adminUser , adminDate"
                        + " = :adminDate where requestNo=:requestNo");
        adminRevDto.setApprover1Status(
                "Update mje_dbo.Record_Status set approverGroup1 = :approverGroup1, approverUser1 = :approverUser1,"
                        + " approverGroup2 = :approverGroup2, approverUser2 = :approverUser2 , adminUser= :adminUser , adminDate"
                        + " = :adminDate where requestNo=:requestNo");
        adminRevDto.setApprover2Status(
                "Update mje_dbo.Record_Status set approverGroup1 = :approverGroup1, approverUser1 = :approverUser1,"
                        + " approverGroup2 = :approverGroup2, approverUser2 = :approverUser2 , adminUser= :adminUser , adminDate"
                        + " = :adminDate where requestNo=:requestNo");
        adminRevDto.setApproverGroup1(
                "Update mje_dbo.Record_Status set approverGroup1 = :approverGroup1, approverUser1 = :approverUser1,"
                        + " approverGroup2 = :approverGroup2, approverUser2 = :approverUser2 , adminUser= :adminUser , adminDate"
                        + " = :adminDate where requestNo=:requestNo");
        adminRevDto.setApproverGroup2(
                "Update mje_dbo.Record_Status set approverGroup1 = :approverGroup1, approverUser1 = :approverUser1,"
                        + " approverGroup2 = :approverGroup2, approverUser2 = :approverUser2 , adminUser= :adminUser , adminDate"
                        + " = :adminDate where requestNo=:requestNo");
        adminRevDto.setApproverUser1(
                "Update mje_dbo.Record_Status set approverGroup1 = :approverGroup1, approverUser1 = :approverUser1,"
                        + " approverGroup2 = :approverGroup2, approverUser2 = :approverUser2 , adminUser= :adminUser , adminDate"
                        + " = :adminDate where requestNo=:requestNo");
        adminRevDto.setApproverUser2(
                "Update mje_dbo.Record_Status set approverGroup1 = :approverGroup1, approverUser1 = :approverUser1,"
                        + " approverGroup2 = :approverGroup2, approverUser2 = :approverUser2 , adminUser= :adminUser , adminDate"
                        + " = :adminDate where requestNo=:requestNo");
        adminRevDto.setRejectedReason("Just cause");
        adminRevDto.setRequestNo(
                "Update mje_dbo.Record_Status set approverGroup1 = :approverGroup1, approverUser1 = :approverUser1,"
                        + " approverGroup2 = :approverGroup2, approverUser2 = :approverUser2 , adminUser= :adminUser , adminDate"
                        + " = :adminDate where requestNo=:requestNo");

        AdminRevDto adminRevDto2 = new AdminRevDto();
        adminRevDto2.setAdminDate(LocalDate.of(1970, 1, 1).atStartOfDay());
        adminRevDto2.setAdminUser("America/Chicago");
        adminRevDto2.setApprover1Status("America/Chicago");
        adminRevDto2.setApprover2Status("America/Chicago");
        adminRevDto2.setApproverGroup1("America/Chicago");
        adminRevDto2.setApproverGroup2("America/Chicago");
        adminRevDto2.setApproverUser1("America/Chicago");
        adminRevDto2.setApproverUser2("America/Chicago");
        adminRevDto2.setRejectedReason(
                "Update mje_dbo.Record_Status set approverGroup1 = :approverGroup1, approverUser1 = :approverUser1,"
                        + " approverGroup2 = :approverGroup2, approverUser2 = :approverUser2 , adminUser= :adminUser , adminDate"
                        + " = :adminDate where requestNo=:requestNo");
        adminRevDto2.setRequestNo("America/Chicago");

        ArrayList<AdminRevDto> adminUploadDto = new ArrayList<>();
        adminUploadDto.add(adminRevDto2);
        adminUploadDto.add(adminRevDto);
        requestRepositoryImpl.adminReviewUpdate(adminUploadDto);
        verify(namedParameterJdbcTemplate).batchUpdate(Mockito.<String>any(), Mockito.<SqlParameterSource[]>any());
    }

    /**
     * Method under test:
     * {@link RequestRepositoryImpl#deleteTempByRequestIdentifier(String, String)}
     */
    @Test
    void testDeleteTempByRequestIdentifier() throws DataAccessException {
        when(namedParameterJdbcTemplate.update(Mockito.<String>any(), Mockito.<Map<String, Object>>any())).thenReturn(1);
        requestRepositoryImpl.deleteTempByRequestIdentifier("Request No", "42");
        verify(namedParameterJdbcTemplate).update(Mockito.<String>any(), Mockito.<Map<String, Object>>any());
    }

    /**
     * Method under test: {@link RequestRepositoryImpl#getJournalEntryList(Map)}
     */
    @Test
    void testGetJournalEntryList() throws DataAccessException {
        ArrayList<Object> objectList = new ArrayList<>();
        when(namedParameterJdbcTemplate.query(Mockito.<String>any(), Mockito.<Map<String, Object>>any(),
                Mockito.<RowMapper<Object>>any())).thenReturn(objectList);
        List<tempJournalResponse> actualJournalEntryList = requestRepositoryImpl.getJournalEntryList(new HashMap<>());
        verify(namedParameterJdbcTemplate).query(Mockito.<String>any(), Mockito.<Map<String, Object>>any(),
                Mockito.<RowMapper<Object>>any());
        assertTrue(actualJournalEntryList.isEmpty());
        assertSame(objectList, actualJournalEntryList);
    }

    /**
     * Method under test:
     * {@link RequestRepositoryImpl#getBatchSetterAdminApproval(List)}
     */
    @Test
    void testGetBatchSetterAdminApproval() {
        assertEquals(0, requestRepositoryImpl.getBatchSetterAdminApproval(new ArrayList<>()).length);
    }

    /**
     * Method under test:
     * {@link RequestRepositoryImpl#getBatchSetterAdminApproval(List)}
     */
    @Test
    void testGetBatchSetterAdminApproval2() {
        AdminRevDto adminRevDto = new AdminRevDto();
        adminRevDto.setAdminDate(LocalDate.of(1970, 1, 1).atStartOfDay());
        adminRevDto.setAdminUser("America/Chicago");
        adminRevDto.setApprover1Status("America/Chicago");
        adminRevDto.setApprover2Status("America/Chicago");
        adminRevDto.setApproverGroup1("America/Chicago");
        adminRevDto.setApproverGroup2("America/Chicago");
        adminRevDto.setApproverUser1("America/Chicago");
        adminRevDto.setApproverUser2("America/Chicago");
        adminRevDto.setRejectedReason("Just cause");
        adminRevDto.setRequestNo("America/Chicago");

        ArrayList<AdminRevDto> adminUploadDto = new ArrayList<>();
        adminUploadDto.add(adminRevDto);
        MapSqlParameterSource[] actualBatchSetterAdminApproval = requestRepositoryImpl
                .getBatchSetterAdminApproval(adminUploadDto);
        assertEquals(1, actualBatchSetterAdminApproval.length);
        assertTrue((actualBatchSetterAdminApproval[0]).hasValues());
    }

    /**
     * Method under test:
     * {@link RequestRepositoryImpl#getBatchSetterAdminApproval(List)}
     */
    @Test
    void testGetBatchSetterAdminApproval3() {
        AdminRevDto adminRevDto = new AdminRevDto();
        adminRevDto.setAdminDate(LocalDate.of(1970, 1, 1).atStartOfDay());
        adminRevDto.setAdminUser("America/Chicago");
        adminRevDto.setApprover1Status("America/Chicago");
        adminRevDto.setApprover2Status("America/Chicago");
        adminRevDto.setApproverGroup1("America/Chicago");
        adminRevDto.setApproverGroup2("America/Chicago");
        adminRevDto.setApproverUser1("America/Chicago");
        adminRevDto.setApproverUser2("America/Chicago");
        adminRevDto.setRejectedReason("Just cause");
        adminRevDto.setRequestNo("America/Chicago");

        AdminRevDto adminRevDto2 = new AdminRevDto();
        adminRevDto2.setAdminDate(LocalDate.of(1970, 1, 1).atStartOfDay());
        adminRevDto2.setAdminUser("requestNo");
        adminRevDto2.setApprover1Status("requestNo");
        adminRevDto2.setApprover2Status("requestNo");
        adminRevDto2.setApproverGroup1("requestNo");
        adminRevDto2.setApproverGroup2("requestNo");
        adminRevDto2.setApproverUser1("requestNo");
        adminRevDto2.setApproverUser2("requestNo");
        adminRevDto2.setRejectedReason("America/Chicago");
        adminRevDto2.setRequestNo("requestNo");

        ArrayList<AdminRevDto> adminUploadDto = new ArrayList<>();
        adminUploadDto.add(adminRevDto2);
        adminUploadDto.add(adminRevDto);
        MapSqlParameterSource[] actualBatchSetterAdminApproval = requestRepositoryImpl
                .getBatchSetterAdminApproval(adminUploadDto);
        assertEquals(2, actualBatchSetterAdminApproval.length);
        assertTrue((actualBatchSetterAdminApproval[0]).hasValues());
        assertTrue((actualBatchSetterAdminApproval[1]).hasValues());
    }

    /**
     * Method under test:
     * {@link RequestRepositoryImpl#getBatchSetterAdminApproval(List)}
     */
    @Test
    void testGetBatchSetterAdminApproval4() {
        AdminRevDto adminRevDto = mock(AdminRevDto.class);
        when(adminRevDto.getAdminUser()).thenReturn("Admin User");
        when(adminRevDto.getApprover1Status()).thenReturn("Approver1 Status");
        when(adminRevDto.getApprover2Status()).thenReturn("Approver2 Status");
        when(adminRevDto.getRequestNo()).thenReturn("Request No");
        doNothing().when(adminRevDto).setAdminDate(Mockito.<LocalDateTime>any());
        doNothing().when(adminRevDto).setAdminUser(Mockito.<String>any());
        doNothing().when(adminRevDto).setApprover1Status(Mockito.<String>any());
        doNothing().when(adminRevDto).setApprover2Status(Mockito.<String>any());
        doNothing().when(adminRevDto).setApproverGroup1(Mockito.<String>any());
        doNothing().when(adminRevDto).setApproverGroup2(Mockito.<String>any());
        doNothing().when(adminRevDto).setApproverUser1(Mockito.<String>any());
        doNothing().when(adminRevDto).setApproverUser2(Mockito.<String>any());
        doNothing().when(adminRevDto).setRejectedReason(Mockito.<String>any());
        doNothing().when(adminRevDto).setRequestNo(Mockito.<String>any());
        adminRevDto.setAdminDate(LocalDate.of(1970, 1, 1).atStartOfDay());
        adminRevDto.setAdminUser("America/Chicago");
        adminRevDto.setApprover1Status("America/Chicago");
        adminRevDto.setApprover2Status("America/Chicago");
        adminRevDto.setApproverGroup1("America/Chicago");
        adminRevDto.setApproverGroup2("America/Chicago");
        adminRevDto.setApproverUser1("America/Chicago");
        adminRevDto.setApproverUser2("America/Chicago");
        adminRevDto.setRejectedReason("Just cause");
        adminRevDto.setRequestNo("America/Chicago");

        ArrayList<AdminRevDto> adminUploadDto = new ArrayList<>();
        adminUploadDto.add(adminRevDto);
        MapSqlParameterSource[] actualBatchSetterAdminApproval = requestRepositoryImpl
                .getBatchSetterAdminApproval(adminUploadDto);
        verify(adminRevDto).getAdminUser();
        verify(adminRevDto).getApprover1Status();
        verify(adminRevDto).getApprover2Status();
        verify(adminRevDto).getRequestNo();
        verify(adminRevDto).setAdminDate(Mockito.<LocalDateTime>any());
        verify(adminRevDto).setAdminUser(Mockito.<String>any());
        verify(adminRevDto).setApprover1Status(Mockito.<String>any());
        verify(adminRevDto).setApprover2Status(Mockito.<String>any());
        verify(adminRevDto).setApproverGroup1(Mockito.<String>any());
        verify(adminRevDto).setApproverGroup2(Mockito.<String>any());
        verify(adminRevDto).setApproverUser1(Mockito.<String>any());
        verify(adminRevDto).setApproverUser2(Mockito.<String>any());
        verify(adminRevDto).setRejectedReason(Mockito.<String>any());
        verify(adminRevDto).setRequestNo(Mockito.<String>any());
        assertEquals(1, actualBatchSetterAdminApproval.length);
        assertTrue((actualBatchSetterAdminApproval[0]).hasValues());
    }

    /**
     * Method under test:
     * {@link RequestRepositoryImpl#getBatchSetterAdminRejected(List)}
     */
    @Test
    void testGetBatchSetterAdminRejected() {
        assertEquals(0, requestRepositoryImpl.getBatchSetterAdminRejected(new ArrayList<>()).length);
    }

    /**
     * Method under test:
     * {@link RequestRepositoryImpl#getBatchSetterAdminRejected(List)}
     */
    @Test
    void testGetBatchSetterAdminRejected2() {
        AdminRevDto adminRevDto = new AdminRevDto();
        adminRevDto.setAdminDate(LocalDate.of(1970, 1, 1).atStartOfDay());
        adminRevDto.setAdminUser("America/Chicago");
        adminRevDto.setApprover1Status("America/Chicago");
        adminRevDto.setApprover2Status("America/Chicago");
        adminRevDto.setApproverGroup1("America/Chicago");
        adminRevDto.setApproverGroup2("America/Chicago");
        adminRevDto.setApproverUser1("America/Chicago");
        adminRevDto.setApproverUser2("America/Chicago");
        adminRevDto.setRejectedReason("Just cause");
        adminRevDto.setRequestNo("America/Chicago");

        ArrayList<AdminRevDto> adminUploadDto = new ArrayList<>();
        adminUploadDto.add(adminRevDto);
        MapSqlParameterSource[] actualBatchSetterAdminRejected = requestRepositoryImpl
                .getBatchSetterAdminRejected(adminUploadDto);
        assertEquals(1, actualBatchSetterAdminRejected.length);
        assertTrue((actualBatchSetterAdminRejected[0]).hasValues());
    }

    /**
     * Method under test:
     * {@link RequestRepositoryImpl#getBatchSetterAdminRejected(List)}
     */
    @Test
    void testGetBatchSetterAdminRejected3() {
        AdminRevDto adminRevDto = new AdminRevDto();
        adminRevDto.setAdminDate(LocalDate.of(1970, 1, 1).atStartOfDay());
        adminRevDto.setAdminUser("America/Chicago");
        adminRevDto.setApprover1Status("America/Chicago");
        adminRevDto.setApprover2Status("America/Chicago");
        adminRevDto.setApproverGroup1("America/Chicago");
        adminRevDto.setApproverGroup2("America/Chicago");
        adminRevDto.setApproverUser1("America/Chicago");
        adminRevDto.setApproverUser2("America/Chicago");
        adminRevDto.setRejectedReason("Just cause");
        adminRevDto.setRequestNo("America/Chicago");

        AdminRevDto adminRevDto2 = new AdminRevDto();
        adminRevDto2.setAdminDate(LocalDate.of(1970, 1, 1).atStartOfDay());
        adminRevDto2.setAdminUser("requestNo");
        adminRevDto2.setApprover1Status("requestNo");
        adminRevDto2.setApprover2Status("requestNo");
        adminRevDto2.setApproverGroup1("requestNo");
        adminRevDto2.setApproverGroup2("requestNo");
        adminRevDto2.setApproverUser1("requestNo");
        adminRevDto2.setApproverUser2("requestNo");
        adminRevDto2.setRejectedReason("America/Chicago");
        adminRevDto2.setRequestNo("requestNo");

        ArrayList<AdminRevDto> adminUploadDto = new ArrayList<>();
        adminUploadDto.add(adminRevDto2);
        adminUploadDto.add(adminRevDto);
        MapSqlParameterSource[] actualBatchSetterAdminRejected = requestRepositoryImpl
                .getBatchSetterAdminRejected(adminUploadDto);
        assertEquals(2, actualBatchSetterAdminRejected.length);
        assertTrue((actualBatchSetterAdminRejected[0]).hasValues());
        assertTrue((actualBatchSetterAdminRejected[1]).hasValues());
    }

    /**
     * Method under test:
     * {@link RequestRepositoryImpl#getBatchSetterAdminRejected(List)}
     */
    @Test
    void testGetBatchSetterAdminRejected4() {
        AdminRevDto adminRevDto = mock(AdminRevDto.class);
        when(adminRevDto.getAdminUser()).thenReturn("Admin User");
        when(adminRevDto.getRejectedReason()).thenReturn("Just cause");
        when(adminRevDto.getRequestNo()).thenReturn("Request No");
        doNothing().when(adminRevDto).setAdminDate(Mockito.<LocalDateTime>any());
        doNothing().when(adminRevDto).setAdminUser(Mockito.<String>any());
        doNothing().when(adminRevDto).setApprover1Status(Mockito.<String>any());
        doNothing().when(adminRevDto).setApprover2Status(Mockito.<String>any());
        doNothing().when(adminRevDto).setApproverGroup1(Mockito.<String>any());
        doNothing().when(adminRevDto).setApproverGroup2(Mockito.<String>any());
        doNothing().when(adminRevDto).setApproverUser1(Mockito.<String>any());
        doNothing().when(adminRevDto).setApproverUser2(Mockito.<String>any());
        doNothing().when(adminRevDto).setRejectedReason(Mockito.<String>any());
        doNothing().when(adminRevDto).setRequestNo(Mockito.<String>any());
        adminRevDto.setAdminDate(LocalDate.of(1970, 1, 1).atStartOfDay());
        adminRevDto.setAdminUser("America/Chicago");
        adminRevDto.setApprover1Status("America/Chicago");
        adminRevDto.setApprover2Status("America/Chicago");
        adminRevDto.setApproverGroup1("America/Chicago");
        adminRevDto.setApproverGroup2("America/Chicago");
        adminRevDto.setApproverUser1("America/Chicago");
        adminRevDto.setApproverUser2("America/Chicago");
        adminRevDto.setRejectedReason("Just cause");
        adminRevDto.setRequestNo("America/Chicago");

        ArrayList<AdminRevDto> adminUploadDto = new ArrayList<>();
        adminUploadDto.add(adminRevDto);
        MapSqlParameterSource[] actualBatchSetterAdminRejected = requestRepositoryImpl
                .getBatchSetterAdminRejected(adminUploadDto);
        verify(adminRevDto).getAdminUser();
        verify(adminRevDto).getRejectedReason();
        verify(adminRevDto).getRequestNo();
        verify(adminRevDto).setAdminDate(Mockito.<LocalDateTime>any());
        verify(adminRevDto).setAdminUser(Mockito.<String>any());
        verify(adminRevDto).setApprover1Status(Mockito.<String>any());
        verify(adminRevDto).setApprover2Status(Mockito.<String>any());
        verify(adminRevDto).setApproverGroup1(Mockito.<String>any());
        verify(adminRevDto).setApproverGroup2(Mockito.<String>any());
        verify(adminRevDto).setApproverUser1(Mockito.<String>any());
        verify(adminRevDto).setApproverUser2(Mockito.<String>any());
        verify(adminRevDto).setRejectedReason(Mockito.<String>any());
        verify(adminRevDto).setRequestNo(Mockito.<String>any());
        assertEquals(1, actualBatchSetterAdminRejected.length);
        assertTrue((actualBatchSetterAdminRejected[0]).hasValues());
    }

    /**
     * Method under test:
     * {@link RequestRepositoryImpl#findAll(String, Pageable, int)}
     */
    @Test
    @Disabled("TODO: Complete this test")
    void testFindAll() {
        // TODO: Complete this test.
        //   Reason: R013 No inputs found that don't throw a trivial exception.
        //   Diffblue Cover tried to run the arrange/act section, but the method under
        //   test threw
        //   java.lang.NullPointerException: Cannot invoke "org.springframework.data.domain.Pageable.getOffset()" because "page" is null
        //       at com.deep.demo.repository.Impl.RequestRepositoryImpl.findAll(RequestRepositoryImpl.java:273)
        //   See https://diff.blue/R013 to resolve this issue.

        requestRepositoryImpl.findAll("Request No", null, 3);
    }

    /**
     * Method under test:
     * {@link RequestRepositoryImpl#saveExpectedPeriodToTemp(Integer, String)}
     */
    @Test
    void testSaveExpectedPeriodToTemp() throws DataAccessException {
        when(namedParameterJdbcTemplate.update(Mockito.<String>any(), Mockito.<SqlParameterSource>any())).thenReturn(1);
        requestRepositoryImpl.saveExpectedPeriodToTemp(1, "42");
        verify(namedParameterJdbcTemplate, atLeast(1)).update(Mockito.<String>any(), Mockito.<SqlParameterSource>any());
    }

    /**
     * Method under test:
     * {@link RequestRepositoryImpl#saveSegmentToTemp(Integer, String)}
     */
    @Test
    void testSaveSegmentToTemp() throws DataAccessException {
        when(namedParameterJdbcTemplate.update(Mockito.<String>any(), Mockito.<SqlParameterSource>any())).thenReturn(1);
        requestRepositoryImpl.saveSegmentToTemp(1, "42");
        verify(namedParameterJdbcTemplate, atLeast(1)).update(Mockito.<String>any(), Mockito.<SqlParameterSource>any());
    }

    /**
     * Method under test:
     * {@link RequestRepositoryImpl#saveDataSourcesToTemp(Integer, String)}
     */
    @Test
    void testSaveDataSourcesToTemp() throws DataAccessException {
        when(namedParameterJdbcTemplate.update(Mockito.<String>any(), Mockito.<SqlParameterSource>any())).thenReturn(1);
        requestRepositoryImpl.saveDataSourcesToTemp(1, "42");
        verify(namedParameterJdbcTemplate, atLeast(1)).update(Mockito.<String>any(), Mockito.<SqlParameterSource>any());
    }

    /**
     * Method under test:
     * {@link RequestRepositoryImpl#saveUpstreamDependencyToTemp(Integer, String)}
     */
    @Test
    void testSaveUpstreamDependencyToTemp() throws DataAccessException {
        when(namedParameterJdbcTemplate.update(Mockito.<String>any(), Mockito.<SqlParameterSource>any())).thenReturn(1);
        requestRepositoryImpl.saveUpstreamDependencyToTemp(1, "42");
        verify(namedParameterJdbcTemplate, atLeast(1)).update(Mockito.<String>any(), Mockito.<SqlParameterSource>any());
    }

    /**
     * Method under test:
     * {@link RequestRepositoryImpl#saveDownStreamImpactToTemp(Integer, String)}
     */
    @Test
    void testSaveDownStreamImpactToTemp() throws DataAccessException {
        when(namedParameterJdbcTemplate.update(Mockito.<String>any(), Mockito.<SqlParameterSource>any())).thenReturn(1);
        requestRepositoryImpl.saveDownStreamImpactToTemp(1, "42");
        verify(namedParameterJdbcTemplate, atLeast(1)).update(Mockito.<String>any(), Mockito.<SqlParameterSource>any());
    }

    /**
     * Method under test:
     * {@link RequestRepositoryImpl#saveGlAccountToTemp(Integer, String)}
     */
    @Test
    void testSaveGlAccountToTemp() throws DataAccessException {
        when(namedParameterJdbcTemplate.update(Mockito.<String>any(), Mockito.<SqlParameterSource>any())).thenReturn(1);
        requestRepositoryImpl.saveGlAccountToTemp(1, "42");
        verify(namedParameterJdbcTemplate, atLeast(1)).update(Mockito.<String>any(), Mockito.<SqlParameterSource>any());
    }

    /**
     * Method under test:
     * {@link RequestRepositoryImpl#saveFunctionalAreaToTemp(Integer, String)}
     */
    @Test
    void testSaveFunctionalAreaToTemp() throws DataAccessException {
        when(namedParameterJdbcTemplate.update(Mockito.<String>any(), Mockito.<SqlParameterSource>any())).thenReturn(1);
        requestRepositoryImpl.saveFunctionalAreaToTemp(1, "42");
        verify(namedParameterJdbcTemplate, atLeast(1)).update(Mockito.<String>any(), Mockito.<SqlParameterSource>any());
    }

    /**
     * Method under test:
     * {@link RequestRepositoryImpl#saveDepartmentToTemp(Integer, String)}
     */
    @Test
    void testSaveDepartmentToTemp() throws DataAccessException {
        when(namedParameterJdbcTemplate.update(Mockito.<String>any(), Mockito.<SqlParameterSource>any())).thenReturn(1);
        requestRepositoryImpl.saveDepartmentToTemp(1, "42");
        verify(namedParameterJdbcTemplate, atLeast(1)).update(Mockito.<String>any(), Mockito.<SqlParameterSource>any());
    }

    /**
     * Method under test:
     * {@link RequestRepositoryImpl#saveCompanyCodeToTemp(Integer, String)}
     */
    @Test
    void testSaveCompanyCodeToTemp() throws DataAccessException {
        when(namedParameterJdbcTemplate.update(Mockito.<String>any(), Mockito.<SqlParameterSource>any())).thenReturn(1);
        requestRepositoryImpl.saveCompanyCodeToTemp(1, "42");
        verify(namedParameterJdbcTemplate, atLeast(1)).update(Mockito.<String>any(), Mockito.<SqlParameterSource>any());
    }
}
