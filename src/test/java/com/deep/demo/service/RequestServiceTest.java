package com.deep.demo.service;

import com.deep.demo.dto.ApprovalDto;
import com.deep.demo.dto.tempJournalResponse;
import com.deep.demo.repository.JournalRepository;
import com.deep.demo.repository.RequestRepository;
import com.deep.demo.util.ValidationUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RequestServiceTest {

    @Mock
    private RequestRepository mockRequestRepository;
    @Mock
    private JournalRepository mockJournalRepository;
    @Mock
    private FileUploadService mockFileUploadService;
    @Mock
    private JournalEntryService mockJournalEntryService;
    @Mock
    private UploadJournalService mockUploadService;
    @Mock
    private ValidationUtil mockValidateUtil;
    @Mock
    private EmailService mockEmailService;
    @Mock
    private FetchAllUsers mockFetchAllUsers;

    private RequestService requestServiceUnderTest;

    @BeforeEach
    void setUp() {
        requestServiceUnderTest = new RequestService(mockRequestRepository, mockJournalRepository,
                mockFileUploadService, mockJournalEntryService, mockUploadService, mockValidateUtil, mockEmailService,
                mockFetchAllUsers);
    }

    @Test
    void testPostReviewApprove() {
        // Setup
        final ApprovalDto approvalDto = new ApprovalDto();
        approvalDto.setRequestNo(0);
        approvalDto.setPostDate("postDate");
        approvalDto.setStatus("status");
        approvalDto.setRejectedReason("rejectedReason");
        approvalDto.setReviewUser("reviewUser");
        final List<ApprovalDto> approvalUploadDto = List.of(approvalDto);
        when(mockRequestRepository.getApproverByRequestNo("requestNo")).thenReturn(List.of("value"));

        // Configure RequestRepository.postApproval(...).
        final ApprovalDto approvalDto2 = new ApprovalDto();
        approvalDto2.setRequestNo(0);
        approvalDto2.setPostDate("postDate");
        approvalDto2.setStatus("status");
        approvalDto2.setRejectedReason("rejectedReason");
        approvalDto2.setReviewUser("reviewUser");
        final List<ApprovalDto> approvalDto1 = List.of(approvalDto2);
        when(mockRequestRepository.postApproval(approvalDto1)).thenReturn("result");

        // Configure RequestRepository.getMjeByRequestNo(...).
        final tempJournalResponse tempJournalResponse = new tempJournalResponse();
        tempJournalResponse.setIdentifier(0);
        tempJournalResponse.setModification("modification");
        tempJournalResponse.setSegment(List.of("value"));
        tempJournalResponse.setDataSources(List.of("value"));
        tempJournalResponse.setUpstreamDependency(List.of("value"));
        tempJournalResponse.setDownstreamImpact(List.of("value"));
        tempJournalResponse.setExpectedPeriod(List.of("value"));
        tempJournalResponse.setGlAccount(List.of("value"));
        tempJournalResponse.setCompanyCode(List.of("value"));
        tempJournalResponse.setCenterDepartment(List.of("value"));
        tempJournalResponse.setFunctionalArea(List.of("value"));
        final List<com.deep.demo.dto.tempJournalResponse> tempJournalResponses = List.of(tempJournalResponse);
        when(mockRequestRepository.getMjeByRequestNo(Map.ofEntries(Map.entry("value", "value"))))
                .thenReturn(tempJournalResponses);

        // Run the test
        final String result = requestServiceUnderTest.postReviewApprove(approvalUploadDto);

        // Verify the results
        assertThat(result).isEqualTo("result");
        verify(mockRequestRepository).updateApprover1Flag("requestNo");

        // Confirm RequestRepository.postJournalEntry(...).
        final com.deep.demo.dto.tempJournalResponse tempJournalResponse1 = new tempJournalResponse();
        tempJournalResponse1.setIdentifier(0);
        tempJournalResponse1.setModification("modification");
        tempJournalResponse1.setSegment(List.of("value"));
        tempJournalResponse1.setDataSources(List.of("value"));
        tempJournalResponse1.setUpstreamDependency(List.of("value"));
        tempJournalResponse1.setDownstreamImpact(List.of("value"));
        tempJournalResponse1.setExpectedPeriod(List.of("value"));
        tempJournalResponse1.setGlAccount(List.of("value"));
        tempJournalResponse1.setCompanyCode(List.of("value"));
        tempJournalResponse1.setCenterDepartment(List.of("value"));
        tempJournalResponse1.setFunctionalArea(List.of("value"));
        verify(mockRequestRepository).postJournalEntry(tempJournalResponse1);

        // Confirm RequestRepository.updateJournalEntry(...).
        final com.deep.demo.dto.tempJournalResponse journalEntry = new tempJournalResponse();
        journalEntry.setIdentifier(0);
        journalEntry.setModification("modification");
        journalEntry.setSegment(List.of("value"));
        journalEntry.setDataSources(List.of("value"));
        journalEntry.setUpstreamDependency(List.of("value"));
        journalEntry.setDownstreamImpact(List.of("value"));
        journalEntry.setExpectedPeriod(List.of("value"));
        journalEntry.setGlAccount(List.of("value"));
        journalEntry.setCompanyCode(List.of("value"));
        journalEntry.setCenterDepartment(List.of("value"));
        journalEntry.setFunctionalArea(List.of("value"));
        verify(mockRequestRepository).updateJournalEntry(journalEntry);
        verify(mockJournalRepository).deleteDataSourceById(0);
        verify(mockJournalRepository).saveDataSourceById(0, List.of(0));
        verify(mockJournalRepository).deleteSegmentById(0);
        verify(mockJournalRepository).saveSegmentById(0, List.of(0));
        verify(mockJournalRepository).deleteDownStreamImpactById(0);
        verify(mockJournalRepository).saveDownstreamImpactById(0, List.of(0));
        verify(mockJournalRepository).deleteUpstreamDependencyById(0);
        verify(mockJournalRepository).saveUpstreamDependencyById(0, List.of(0));
        verify(mockJournalRepository).deleteExpectedPeriodById(0);
        verify(mockJournalRepository).saveExpectedPeriodById(0, List.of(0));
        verify(mockJournalRepository).deleteCompanyCodeById(0);
        verify(mockJournalRepository).saveCompanyCodeById(0, List.of("value"));
        verify(mockJournalRepository).deleteGlAccountById(0);
        verify(mockJournalRepository).saveGlAccountById(0, List.of("value"));
        verify(mockJournalRepository).deleteFunctionalAreaById(0);
        verify(mockJournalRepository).saveFunctionalAreaById(0, List.of("value"));
        verify(mockJournalRepository).deleteCenterDepartmentById(0);
        verify(mockJournalRepository).saveCenterDepartmentById(0, List.of("value"));
    }

    @Test
    void testPostReviewApprove_RequestRepositoryGetApproverByRequestNoReturnsNoItems() {
        // Setup
        final ApprovalDto approvalDto = new ApprovalDto();
        approvalDto.setRequestNo(0);
        approvalDto.setPostDate("postDate");
        approvalDto.setStatus("status");
        approvalDto.setRejectedReason("rejectedReason");
        approvalDto.setReviewUser("reviewUser");
        final List<ApprovalDto> approvalUploadDto = List.of(approvalDto);
        when(mockRequestRepository.getApproverByRequestNo("requestNo")).thenReturn(Collections.emptyList());

        // Configure RequestRepository.postApproval(...).
        final ApprovalDto approvalDto2 = new ApprovalDto();
        approvalDto2.setRequestNo(0);
        approvalDto2.setPostDate("postDate");
        approvalDto2.setStatus("status");
        approvalDto2.setRejectedReason("rejectedReason");
        approvalDto2.setReviewUser("reviewUser");
        final List<ApprovalDto> approvalDto1 = List.of(approvalDto2);
        when(mockRequestRepository.postApproval(approvalDto1)).thenReturn("result");

        // Configure RequestRepository.getMjeByRequestNo(...).
        final tempJournalResponse tempJournalResponse = new tempJournalResponse();
        tempJournalResponse.setIdentifier(0);
        tempJournalResponse.setModification("modification");
        tempJournalResponse.setSegment(List.of("value"));
        tempJournalResponse.setDataSources(List.of("value"));
        tempJournalResponse.setUpstreamDependency(List.of("value"));
        tempJournalResponse.setDownstreamImpact(List.of("value"));
        tempJournalResponse.setExpectedPeriod(List.of("value"));
        tempJournalResponse.setGlAccount(List.of("value"));
        tempJournalResponse.setCompanyCode(List.of("value"));
        tempJournalResponse.setCenterDepartment(List.of("value"));
        tempJournalResponse.setFunctionalArea(List.of("value"));
        final List<com.deep.demo.dto.tempJournalResponse> tempJournalResponses = List.of(tempJournalResponse);
        when(mockRequestRepository.getMjeByRequestNo(Map.ofEntries(Map.entry("value", "value"))))
                .thenReturn(tempJournalResponses);

        // Run the test
        final String result = requestServiceUnderTest.postReviewApprove(approvalUploadDto);

        // Verify the results
        assertThat(result).isEqualTo("result");
        verify(mockRequestRepository).updateApprover1Flag("requestNo");

        // Confirm RequestRepository.postJournalEntry(...).
        final com.deep.demo.dto.tempJournalResponse tempJournalResponse1 = new tempJournalResponse();
        tempJournalResponse1.setIdentifier(0);
        tempJournalResponse1.setModification("modification");
        tempJournalResponse1.setSegment(List.of("value"));
        tempJournalResponse1.setDataSources(List.of("value"));
        tempJournalResponse1.setUpstreamDependency(List.of("value"));
        tempJournalResponse1.setDownstreamImpact(List.of("value"));
        tempJournalResponse1.setExpectedPeriod(List.of("value"));
        tempJournalResponse1.setGlAccount(List.of("value"));
        tempJournalResponse1.setCompanyCode(List.of("value"));
        tempJournalResponse1.setCenterDepartment(List.of("value"));
        tempJournalResponse1.setFunctionalArea(List.of("value"));
        verify(mockRequestRepository).postJournalEntry(tempJournalResponse1);

        // Confirm RequestRepository.updateJournalEntry(...).
        final com.deep.demo.dto.tempJournalResponse journalEntry = new tempJournalResponse();
        journalEntry.setIdentifier(0);
        journalEntry.setModification("modification");
        journalEntry.setSegment(List.of("value"));
        journalEntry.setDataSources(List.of("value"));
        journalEntry.setUpstreamDependency(List.of("value"));
        journalEntry.setDownstreamImpact(List.of("value"));
        journalEntry.setExpectedPeriod(List.of("value"));
        journalEntry.setGlAccount(List.of("value"));
        journalEntry.setCompanyCode(List.of("value"));
        journalEntry.setCenterDepartment(List.of("value"));
        journalEntry.setFunctionalArea(List.of("value"));
        verify(mockRequestRepository).updateJournalEntry(journalEntry);
        verify(mockJournalRepository).deleteDataSourceById(0);
        verify(mockJournalRepository).saveDataSourceById(0, List.of(0));
        verify(mockJournalRepository).deleteSegmentById(0);
        verify(mockJournalRepository).saveSegmentById(0, List.of(0));
        verify(mockJournalRepository).deleteDownStreamImpactById(0);
        verify(mockJournalRepository).saveDownstreamImpactById(0, List.of(0));
        verify(mockJournalRepository).deleteUpstreamDependencyById(0);
        verify(mockJournalRepository).saveUpstreamDependencyById(0, List.of(0));
        verify(mockJournalRepository).deleteExpectedPeriodById(0);
        verify(mockJournalRepository).saveExpectedPeriodById(0, List.of(0));
        verify(mockJournalRepository).deleteCompanyCodeById(0);
        verify(mockJournalRepository).saveCompanyCodeById(0, List.of("value"));
        verify(mockJournalRepository).deleteGlAccountById(0);
        verify(mockJournalRepository).saveGlAccountById(0, List.of("value"));
        verify(mockJournalRepository).deleteFunctionalAreaById(0);
        verify(mockJournalRepository).saveFunctionalAreaById(0, List.of("value"));
        verify(mockJournalRepository).deleteCenterDepartmentById(0);
        verify(mockJournalRepository).saveCenterDepartmentById(0, List.of("value"));
    }

    @Test
    void testPostReviewApprove_RequestRepositoryGetMjeByRequestNoReturnsNoItems() {
        // Setup
        final ApprovalDto approvalDto = new ApprovalDto();
        approvalDto.setRequestNo(0);
        approvalDto.setPostDate("postDate");
        approvalDto.setStatus("status");
        approvalDto.setRejectedReason("rejectedReason");
        approvalDto.setReviewUser("reviewUser");
        final List<ApprovalDto> approvalUploadDto = List.of(approvalDto);
        when(mockRequestRepository.getApproverByRequestNo("requestNo")).thenReturn(List.of("value"));

        // Configure RequestRepository.postApproval(...).
        final ApprovalDto approvalDto2 = new ApprovalDto();
        approvalDto2.setRequestNo(0);
        approvalDto2.setPostDate("postDate");
        approvalDto2.setStatus("status");
        approvalDto2.setRejectedReason("rejectedReason");
        approvalDto2.setReviewUser("reviewUser");
        final List<ApprovalDto> approvalDto1 = List.of(approvalDto2);
        when(mockRequestRepository.postApproval(approvalDto1)).thenReturn("result");

        when(mockRequestRepository.getMjeByRequestNo(Map.ofEntries(Map.entry("value", "value"))))
                .thenReturn(Collections.emptyList());

        // Run the test
        assertThatThrownBy(() -> requestServiceUnderTest.postReviewApprove(approvalUploadDto))
                .isInstanceOf(RuntimeException.class);
        verify(mockRequestRepository).updateApprover1Flag("requestNo");
    }
}
