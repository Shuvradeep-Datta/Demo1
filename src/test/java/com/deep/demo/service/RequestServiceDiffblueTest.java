package com.deep.demo.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.anyInt;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.deep.demo.dto.PagingResponseModifiedJournal;
import com.deep.demo.dto.RecordInfoResponse;
import com.deep.demo.dto.RecordResponse;
import com.deep.demo.dto.RecordResponseAdmin;
import com.deep.demo.dto.RequestDto;
import com.deep.demo.dto.tempJournalResponse;
import com.deep.demo.repository.JournalRepository;
import com.deep.demo.repository.RequestRepository;
import com.deep.demo.util.ValidationUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ContextConfiguration(classes = {RequestService.class})
@ExtendWith(SpringExtension.class)
class RequestServiceDiffblueTest {
    @MockBean
    private EmailService emailService;

    @MockBean
    private FetchAllUsers fetchAllUsers;

    @MockBean
    private FileUploadService fileUploadService;

    @MockBean
    private JournalEntryService journalEntryService;

    @MockBean
    private JournalRepository journalRepository;

    @MockBean
    private RequestRepository requestRepository;

    @Autowired
    private RequestService requestService;

    @MockBean
    private UploadJournalService uploadJournalService;

    @MockBean
    private ValidationUtil validationUtil;

    /**
     * Method under test: {@link RequestService#getRequestList()}
     */
    @Test
    void testGetRequestList() {
        ArrayList<RequestDto> requestDtoList = new ArrayList<>();
        when(requestRepository.getRequests()).thenReturn(requestDtoList);
        List<RequestDto> actualRequestList = requestService.getRequestList();
        verify(requestRepository).getRequests();
        assertTrue(actualRequestList.isEmpty());
        assertSame(requestDtoList, actualRequestList);
    }

    /**
     * Method under test: {@link RequestService#getRequestList()}
     */
    @Test
    void testGetRequestList2() {
        when(requestRepository.getRequests()).thenThrow(new RuntimeException("foo"));
        assertThrows(RuntimeException.class, () -> requestService.getRequestList());
        verify(requestRepository).getRequests();
    }

    /**
     * Method under test: {@link RequestService#getRequestsByRequestNumber(String)}
     */
    @Test
    void testGetRequestsByRequestNumber() {
        ArrayList<RecordInfoResponse> recordInfoResponseList = new ArrayList<>();
        when(requestRepository.getRequestsByRequestNumber(Mockito.<String>any())).thenReturn(recordInfoResponseList);
        List<RecordInfoResponse> actualRequestsByRequestNumber = requestService.getRequestsByRequestNumber("Request No");
        verify(requestRepository).getRequestsByRequestNumber(Mockito.<String>any());
        assertTrue(actualRequestsByRequestNumber.isEmpty());
        assertSame(recordInfoResponseList, actualRequestsByRequestNumber);
    }

    /**
     * Method under test: {@link RequestService#getRequestsByRequestNumber(String)}
     */
    @Test
    void testGetRequestsByRequestNumber2() {
        when(requestRepository.getRequestsByRequestNumber(Mockito.<String>any())).thenThrow(new RuntimeException("foo"));
        assertThrows(RuntimeException.class, () -> requestService.getRequestsByRequestNumber("Request No"));
        verify(requestRepository).getRequestsByRequestNumber(Mockito.<String>any());
    }

    /**
     * Method under test: {@link RequestService#getLatestRequest()}
     */
    @Test
    void testGetLatestRequest() {
        when(requestRepository.getRequestNo()).thenReturn(1);
        int actualLatestRequest = requestService.getLatestRequest();
        verify(requestRepository).getRequestNo();
        assertEquals(1, actualLatestRequest);
    }

    /**
     * Method under test: {@link RequestService#getLatestRequest()}
     */
    @Test
    void testGetLatestRequest2() {
        when(requestRepository.getRequestNo()).thenThrow(new RuntimeException("foo"));
        assertThrows(RuntimeException.class, () -> requestService.getLatestRequest());
        verify(requestRepository).getRequestNo();
    }

    /**
     * Method under test: {@link RequestService#getRequestByStatus(String, String)}
     */
    @Test
    void testGetRequestByStatus() throws JournalEntryNotFoundException {
        when(requestRepository.getRequestByStatus(Mockito.<Map<String, String>>any())).thenReturn(new ArrayList<>());
        assertThrows(JournalEntryNotFoundException.class, () -> requestService.getRequestByStatus("42", "Status"));
        verify(requestRepository).getRequestByStatus(Mockito.<Map<String, String>>any());
    }

    /**
     * Method under test: {@link RequestService#getRequestByStatus(String, String)}
     */
    @Test
    void testGetRequestByStatus2() throws JournalEntryNotFoundException {
        RecordResponse recordResponse = new RecordResponse();
        recordResponse.setApproverGroup1("status");
        recordResponse.setApproverGroup2("status");
        recordResponse.setApproverUser1("status");
        recordResponse.setApproverUser2("status");
        recordResponse.setCountry("GB");
        recordResponse.setDescription("The characteristics of someone or something");
        recordResponse.setPostDate("2020-03-01");
        recordResponse.setRequestNo(1);
        recordResponse.setRequestType("status");
        recordResponse.setStatus("status");

        ArrayList<RecordResponse> recordResponseList = new ArrayList<>();
        recordResponseList.add(recordResponse);
        when(requestRepository.getRequestByStatus(Mockito.<Map<String, String>>any())).thenReturn(recordResponseList);
        List<RecordResponse> actualRequestByStatus = requestService.getRequestByStatus("42", "Status");
        verify(requestRepository).getRequestByStatus(Mockito.<Map<String, String>>any());
        assertEquals(1, actualRequestByStatus.size());
        assertSame(recordResponseList, actualRequestByStatus);
    }

    /**
     * Method under test: {@link RequestService#getRequestByStatus(String, String)}
     */
    @Test
    void testGetRequestByStatus3() throws JournalEntryNotFoundException {
        when(requestRepository.getRequestByStatus(Mockito.<Map<String, String>>any()))
                .thenThrow(new RuntimeException("status"));
        assertThrows(RuntimeException.class, () -> requestService.getRequestByStatus("42", "Status"));
        verify(requestRepository).getRequestByStatus(Mockito.<Map<String, String>>any());
    }

    /**
     * Method under test: {@link RequestService#getFileName(String, String)}
     */
    @Test
    void testGetFileName() {
        when(requestRepository.getFileName(Mockito.<String>any(), Mockito.<String>any())).thenReturn("foo.txt");
        String actualFileName = requestService.getFileName("Request No", "File Type");
        verify(requestRepository).getFileName(Mockito.<String>any(), Mockito.<String>any());
        assertEquals("foo.txt", actualFileName);
    }

    /**
     * Method under test: {@link RequestService#getFileName(String, String)}
     */
    @Test
    void testGetFileName2() {
        when(requestRepository.getFileName(Mockito.<String>any(), Mockito.<String>any()))
                .thenThrow(new RuntimeException("requestNo"));
        assertThrows(RuntimeException.class, () -> requestService.getFileName("Request No", "File Type"));
        verify(requestRepository).getFileName(Mockito.<String>any(), Mockito.<String>any());
    }

    /**
     * Method under test:
     * {@link RequestService#getApproveRequestByStatus(String, String)}
     */
    @Test
    void testGetApproveRequestByStatus() {
        ArrayList<RecordResponseAdmin> recordResponseAdminList = new ArrayList<>();
        when(requestRepository.getApproveRequestByStatus(Mockito.<Map<String, String>>any()))
                .thenReturn(recordResponseAdminList);
        List<RecordResponseAdmin> actualApproveRequestByStatus = requestService.getApproveRequestByStatus("Approver User",
                "Status");
        verify(requestRepository).getApproveRequestByStatus(Mockito.<Map<String, String>>any());
        assertTrue(actualApproveRequestByStatus.isEmpty());
        assertSame(recordResponseAdminList, actualApproveRequestByStatus);
    }

    /**
     * Method under test:
     * {@link RequestService#getApproveRequestByStatus(String, String)}
     */
    @Test
    void testGetApproveRequestByStatus2() {
        when(requestRepository.getApproveRequestByStatus(Mockito.<Map<String, String>>any()))
                .thenThrow(new RuntimeException("status"));
        assertThrows(RuntimeException.class, () -> requestService.getApproveRequestByStatus("Approver User", "Status"));
        verify(requestRepository).getApproveRequestByStatus(Mockito.<Map<String, String>>any());
    }

    /**
     * Method under test:
     * {@link RequestService#getApproveRequestByUserByStatus(String)}
     */
    @Test
    void testGetApproveRequestByUserByStatus() {
        when(requestRepository.getApproveRequestByUserByStatus(Mockito.<Map<String, String>>any()))
                .thenReturn(new ArrayList<>());
        assertThrows(RuntimeException.class, () -> requestService.getApproveRequestByUserByStatus("Approver User"));
        verify(requestRepository).getApproveRequestByUserByStatus(Mockito.<Map<String, String>>any());
    }

    /**
     * Method under test:
     * {@link RequestService#getApproveRequestByUserByStatus(String)}
     */
    @Test
    void testGetApproveRequestByUserByStatus2() {
        RecordResponseAdmin recordResponseAdmin = new RecordResponseAdmin();
        recordResponseAdmin.setApprovedUser1Status("approverUser");
        recordResponseAdmin.setApprovedUser2Status("approverUser");
        recordResponseAdmin.setApproverGroup1("approverUser");
        recordResponseAdmin.setApproverUser1("approverUser");
        recordResponseAdmin.setApproverUser2("approverUser");
        recordResponseAdmin.setCountry("GB");
        recordResponseAdmin.setDescription("The characteristics of someone or something");
        recordResponseAdmin.setPostDate("2020-03-01");
        recordResponseAdmin.setRequestNo(1);
        recordResponseAdmin.setRequestType("approverUser");
        recordResponseAdmin.setRequestorName("approverUser");
        recordResponseAdmin.setStatus("approverUser");

        ArrayList<RecordResponseAdmin> recordResponseAdminList = new ArrayList<>();
        recordResponseAdminList.add(recordResponseAdmin);
        when(requestRepository.getApproveRequestByUserByStatus(Mockito.<Map<String, String>>any()))
                .thenReturn(recordResponseAdminList);
        List<RecordResponseAdmin> actualApproveRequestByUserByStatus = requestService
                .getApproveRequestByUserByStatus("Approver User");
        verify(requestRepository).getApproveRequestByUserByStatus(Mockito.<Map<String, String>>any());
        assertEquals(1, actualApproveRequestByUserByStatus.size());
        assertSame(recordResponseAdminList, actualApproveRequestByUserByStatus);
    }

    /**
     * Method under test:
     * {@link RequestService#getApproveRequestByUserByStatus(String)}
     */
    @Test
    void testGetApproveRequestByUserByStatus3() {
        when(requestRepository.getApproveRequestByUserByStatus(Mockito.<Map<String, String>>any()))
                .thenThrow(new RuntimeException("approverUser"));
        assertThrows(RuntimeException.class, () -> requestService.getApproveRequestByUserByStatus("Approver User"));
        verify(requestRepository).getApproveRequestByUserByStatus(Mockito.<Map<String, String>>any());
    }

    /**
     * Method under test: {@link RequestService#getAdminRequestByStatus(String)}
     */
    @Test
    void testGetAdminRequestByStatus() {
        when(requestRepository.getAdminRequestByStatus(Mockito.<Map<String, String>>any())).thenReturn(new ArrayList<>());
        assertThrows(RuntimeException.class, () -> requestService.getAdminRequestByStatus("Status"));
        verify(requestRepository).getAdminRequestByStatus(Mockito.<Map<String, String>>any());
    }

    /**
     * Method under test: {@link RequestService#getAdminRequestByStatus(String)}
     */
    @Test
    void testGetAdminRequestByStatus2() {
        RecordResponseAdmin recordResponseAdmin = new RecordResponseAdmin();
        recordResponseAdmin.setApprovedUser1Status("status");
        recordResponseAdmin.setApprovedUser2Status("status");
        recordResponseAdmin.setApproverGroup1("status");
        recordResponseAdmin.setApproverUser1("status");
        recordResponseAdmin.setApproverUser2("status");
        recordResponseAdmin.setCountry("GB");
        recordResponseAdmin.setDescription("The characteristics of someone or something");
        recordResponseAdmin.setPostDate("2020-03-01");
        recordResponseAdmin.setRequestNo(1);
        recordResponseAdmin.setRequestType("status");
        recordResponseAdmin.setRequestorName("status");
        recordResponseAdmin.setStatus("status");

        ArrayList<RecordResponseAdmin> recordResponseAdminList = new ArrayList<>();
        recordResponseAdminList.add(recordResponseAdmin);
        when(requestRepository.getAdminRequestByStatus(Mockito.<Map<String, String>>any()))
                .thenReturn(recordResponseAdminList);
        List<RecordResponseAdmin> actualAdminRequestByStatus = requestService.getAdminRequestByStatus("Status");
        verify(requestRepository).getAdminRequestByStatus(Mockito.<Map<String, String>>any());
        assertEquals(1, actualAdminRequestByStatus.size());
        assertSame(recordResponseAdminList, actualAdminRequestByStatus);
    }

    /**
     * Method under test: {@link RequestService#getAdminRequestByStatus(String)}
     */
    @Test
    void testGetAdminRequestByStatus3() {
        when(requestRepository.getAdminRequestByStatus(Mockito.<Map<String, String>>any()))
                .thenThrow(new RuntimeException("status"));
        assertThrows(RuntimeException.class, () -> requestService.getAdminRequestByStatus("Status"));
        verify(requestRepository).getAdminRequestByStatus(Mockito.<Map<String, String>>any());
    }

    /**
     * Method under test: {@link RequestService#getMjeByRequestNo(String)}
     */
    @Test
    void testGetMjeByRequestNo() {
        when(requestRepository.getMjeByRequestNo(Mockito.<Map<String, String>>any())).thenReturn(new ArrayList<>());
        assertThrows(RuntimeException.class, () -> requestService.getMjeByRequestNo("Request No"));
        verify(requestRepository).getMjeByRequestNo(Mockito.<Map<String, String>>any());
    }

    /**
     * Method under test: {@link RequestService#getMjeByRequestNo(String)}
     */
    @Test
    void testGetMjeByRequestNo2() {
        tempJournalResponse tempJournalResponse = new tempJournalResponse();
        tempJournalResponse.setAccountableBusinessLineId("42");
        tempJournalResponse.setAccountableBusinessLineUserId("42");
        tempJournalResponse.setApproverUserId("42");
        tempJournalResponse.setAutomationNotes("requestNo");
        tempJournalResponse.setAutomationToolId("42");
        tempJournalResponse.setCategoryId("42");
        tempJournalResponse.setCenterDepartment(new ArrayList<>());
        tempJournalResponse.setCertificationDate("2020-03-01");
        tempJournalResponse.setCertificationStatus("requestNo");
        tempJournalResponse.setCogsImpacting("requestNo");
        tempJournalResponse.setCompanyCode(new ArrayList<>());
        tempJournalResponse.setCountryId("GB");
        tempJournalResponse.setCreationDate("2020-03-01");
        tempJournalResponse.setDataSources(new ArrayList<>());
        tempJournalResponse.setDataSourcesDescription("requestNo");
        tempJournalResponse.setDelayedBy(1);
        tempJournalResponse.setDescription("The characteristics of someone or something");
        tempJournalResponse.setDownstreamImpact(new ArrayList<>());
        tempJournalResponse.setDownstreamImpactDescription("requestNo");
        tempJournalResponse.setEbsMiddleOfficeUserId("42");
        tempJournalResponse.setEliminationNotes("requestNo");
        tempJournalResponse.setEntryWorkdayTime("requestNo");
        tempJournalResponse.setExpectedEntryWorkdayId(1);
        tempJournalResponse.setExpectedPeriod(new ArrayList<>());
        tempJournalResponse.setFrequencyId("42");
        tempJournalResponse.setFsImpactId("42");
        tempJournalResponse.setFunctionalArea(new ArrayList<>());
        tempJournalResponse.setGlAccount(new ArrayList<>());
        tempJournalResponse.setIdentifier(1);
        tempJournalResponse.setInactivatedDate("2020-03-01");
        tempJournalResponse.setIntercompanyIndicatorId("42");
        tempJournalResponse.setMethodId("42");
        tempJournalResponse.setModification("requestNo");
        tempJournalResponse.setNatureId("42");
        tempJournalResponse.setPreparerUserId("42");
        tempJournalResponse.setProcessingTeamId("42");
        tempJournalResponse.setReasonForInactive("Just cause");
        tempJournalResponse.setRequestNo("requestNo");
        tempJournalResponse.setReviewerUserId("42");
        tempJournalResponse.setRn("requestNo");
        tempJournalResponse.setSegment(new ArrayList<>());
        tempJournalResponse.setSoxControlNumber("42");
        tempJournalResponse.setStatus("requestNo");
        tempJournalResponse.setTargetLedgerSystemId("42");
        tempJournalResponse.setTitle("Dr");
        tempJournalResponse.setUpstreamDependency(new ArrayList<>());
        tempJournalResponse.setUpstreamDependencyDescription("requestNo");

        ArrayList<tempJournalResponse> tempJournalResponseList = new ArrayList<>();
        tempJournalResponseList.add(tempJournalResponse);
        when(requestRepository.getMjeByRequestNo(Mockito.<Map<String, String>>any())).thenReturn(tempJournalResponseList);
        List<tempJournalResponse> actualMjeByRequestNo = requestService.getMjeByRequestNo("Request No");
        verify(requestRepository).getMjeByRequestNo(Mockito.<Map<String, String>>any());
        assertEquals(1, actualMjeByRequestNo.size());
        assertSame(tempJournalResponseList, actualMjeByRequestNo);
    }

    /**
     * Method under test: {@link RequestService#getMjeByRequestNo(String)}
     */
    @Test
    void testGetMjeByRequestNo3() {
        when(requestRepository.getMjeByRequestNo(Mockito.<Map<String, String>>any()))
                .thenThrow(new RuntimeException("requestNo"));
        assertThrows(RuntimeException.class, () -> requestService.getMjeByRequestNo("Request No"));
        verify(requestRepository).getMjeByRequestNo(Mockito.<Map<String, String>>any());
    }

    /**
     * Method under test:
     * {@link RequestService#getJournals(String, Integer, Integer)}
     */
    @Test
    void testGetJournals() {
        when(requestRepository.getTempJournalEntryCount()).thenReturn(3);
        when(requestRepository.findAll(Mockito.<String>any(), Mockito.<Pageable>any(), anyInt()))
                .thenReturn(new PageImpl<>(new ArrayList<>()));
        PagingResponseModifiedJournal actualJournals = requestService.getJournals("Request No", 10, 3);
        verify(requestRepository).findAll(Mockito.<String>any(), Mockito.<Pageable>any(), anyInt());
        verify(requestRepository).getTempJournalEntryCount();
        assertEquals(0, actualJournals.getCurrentPage().intValue());
        assertEquals(0, actualJournals.getPageSize().intValue());
        assertEquals(0L, actualJournals.getTotalItems().longValue());
        assertEquals(1, actualJournals.getTotalPages().intValue());
        assertTrue(actualJournals.getJournals().isEmpty());
    }

    /**
     * Method under test:
     * {@link RequestService#getJournals(String, Integer, Integer)}
     */
    @Test
    void testGetJournals2() {
        when(requestRepository.getTempJournalEntryCount()).thenReturn(3);
        when(requestRepository.findAll(Mockito.<String>any(), Mockito.<Pageable>any(), anyInt()))
                .thenReturn(new PageImpl<>(new ArrayList<>()));
        PagingResponseModifiedJournal actualJournals = requestService.getJournals("Request No", null, 3);
        verify(requestRepository).findAll(Mockito.<String>any(), Mockito.<Pageable>any(), anyInt());
        verify(requestRepository).getTempJournalEntryCount();
        assertEquals(0, actualJournals.getCurrentPage().intValue());
        assertEquals(0, actualJournals.getPageSize().intValue());
        assertEquals(0L, actualJournals.getTotalItems().longValue());
        assertEquals(1, actualJournals.getTotalPages().intValue());
        assertTrue(actualJournals.getJournals().isEmpty());
    }

    /**
     * Method under test:
     * {@link RequestService#getJournals(String, Integer, Integer)}
     */
    @Test
    void testGetJournals3() {
        when(requestRepository.getTempJournalEntryCount()).thenReturn(3);
        assertThrows(RuntimeException.class, () -> requestService.getJournals("Request No", -1, 3));
        verify(requestRepository).getTempJournalEntryCount();
    }

    /**
     * Method under test:
     * {@link RequestService#getJournals(String, Integer, Integer)}
     */
    @Test
    void testGetJournals4() {
        when(requestRepository.getTempJournalEntryCount()).thenReturn(3);
        when(requestRepository.findAll(Mockito.<String>any(), Mockito.<Pageable>any(), anyInt()))
                .thenReturn(new PageImpl<>(new ArrayList<>()));
        PagingResponseModifiedJournal actualJournals = requestService.getJournals("Request No", 10, null);
        verify(requestRepository).findAll(Mockito.<String>any(), Mockito.<Pageable>any(), anyInt());
        verify(requestRepository).getTempJournalEntryCount();
        assertEquals(0, actualJournals.getCurrentPage().intValue());
        assertEquals(0, actualJournals.getPageSize().intValue());
        assertEquals(0L, actualJournals.getTotalItems().longValue());
        assertEquals(1, actualJournals.getTotalPages().intValue());
        assertTrue(actualJournals.getJournals().isEmpty());
    }

    /**
     * Method under test:
     * {@link RequestService#getJournals(String, Integer, Integer)}
     */
    @Test
    void testGetJournals5() {
        when(requestRepository.getTempJournalEntryCount())
                .thenThrow(new IllegalArgumentException("Exception occurred while getting journals"));
        assertThrows(RuntimeException.class, () -> requestService.getJournals("Request No", 10, 3));
        verify(requestRepository).getTempJournalEntryCount();
    }

    /**
     * Method under test:
     * {@link RequestService#getJournals(String, Integer, Integer)}
     */
    @Test
    void testGetJournals6() {
        when(requestRepository.getTempJournalEntryCount()).thenReturn(0);
        assertThrows(RuntimeException.class, () -> requestService.getJournals("Request No", null, 3));
        verify(requestRepository).getTempJournalEntryCount();
    }
}
