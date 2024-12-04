package com.deep.demo.controller;

import com.deep.demo.dto.*;
import com.deep.demo.service.FetchAllUsers;
import com.deep.demo.service.FileUploadService;
import com.deep.demo.service.JournalEntryNotFoundException;
import com.deep.demo.service.RequestService;
import com.microsoft.azure.storage.StorageException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.multipart.MultipartFile;

import java.net.URISyntaxException;
import java.security.InvalidKeyException;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(RequestController.class)
class RequestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private RequestService mockRequestService;
    @MockBean
    private FetchAllUsers mockFetchAllUsers;
    @MockBean
    private FileUploadService mockFileUploadService;

    @Test
    void testGetRequests() throws Exception {
        // Setup
        // Configure RequestService.getRequestList(...).
        final RequestDto requestDto = new RequestDto();
        requestDto.setRequestNo(0);
        requestDto.setPostDate(LocalDateTime.of(2020, 1, 1, 0, 0, 0));
        requestDto.setDescription("description");
        requestDto.setRequestType("requestType");
        requestDto.setStatus("status");
        final List<RequestDto> requestDtos = List.of(requestDto);
        when(mockRequestService.getRequestList()).thenReturn(requestDtos);

        // Run the test and verify the results
        mockMvc.perform(get("/v1/getRequest")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json("{}", true));
    }

    @Test
    void testGetRequests_RequestServiceReturnsNoItems() throws Exception {
        // Setup
        when(mockRequestService.getRequestList()).thenReturn(Collections.emptyList());

        // Run the test and verify the results
        mockMvc.perform(get("/v1/getRequest")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json("[]", true));
    }

    @Test
    void testGetRequestNo() throws Exception {
        // Setup
        when(mockRequestService.getLatestRequest()).thenReturn(0);

        // Run the test and verify the results
        mockMvc.perform(get("/v1/getlatestRequestNo")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json("{}", true));
    }

    @Test
    void testGetRequestByStatus() throws Exception, JournalEntryNotFoundException {
        // Setup
        // Configure RequestService.getRequestByStatusAndUpdate(...).
        final RecordResponse recordResponse = new RecordResponse();
        recordResponse.setRequestNo(0);
        recordResponse.setPostDate("postDate");
        recordResponse.setDescription("description");
        recordResponse.setRequestType("requestType");
        recordResponse.setStatus("status");
        final List<RecordResponse> recordResponses = List.of(recordResponse);
        when(mockRequestService.getRequestByStatusAndUpdate("userId", "status")).thenReturn(recordResponses);

        // Run the test and verify the results
        mockMvc.perform(get("/v1/getMyRequestByStatus")
                        .param("userId", "userId")
                        .param("status", "status")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json("{}", true));
    }

    @Test
    void testGetRequestByStatus_RequestServiceReturnsNoItems() throws Exception, JournalEntryNotFoundException {
        // Setup
        when(mockRequestService.getRequestByStatusAndUpdate("userId", "status")).thenReturn(Collections.emptyList());

        // Run the test and verify the results
        mockMvc.perform(get("/v1/getMyRequestByStatus")
                        .param("userId", "userId")
                        .param("status", "status")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json("[]", true));
    }

    @Test
    void testGetRequestByStatus_RequestServiceThrowsJournalEntryNotFoundException() throws Exception, JournalEntryNotFoundException {
        // Setup
        when(mockRequestService.getRequestByStatusAndUpdate("userId", "status"))
                .thenThrow(JournalEntryNotFoundException.class);

        // Run the test and verify the results
        mockMvc.perform(get("/v1/getMyRequestByStatus")
                        .param("userId", "userId")
                        .param("status", "status")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is5xxServerError())
                .andExpect(content().json("{}", true));
    }

    @Test
    void testGetJournalInfoByRequestNo() throws Exception {
        // Setup
        // Configure RequestService.getJournalInfoAndUpdate(...).
        final RecordInfoResponse recordInfoResponse = new RecordInfoResponse();
        recordInfoResponse.setRequestNo(0);
        recordInfoResponse.setPostDate("postDate");
        recordInfoResponse.setDescription("description");
        recordInfoResponse.setStatus("Deleted");
        recordInfoResponse.setCountry("country");
        final List<RecordInfoResponse> recordInfoResponses = List.of(recordInfoResponse);
        when(mockRequestService.getJournalInfoAndUpdate("requestNo")).thenReturn(recordInfoResponses);

        // Run the test and verify the results
        mockMvc.perform(get("/v1/getJournalInfoByRequestNo")
                        .param("requestNo", "requestNo")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json("{}", true));
    }

    @Test
    void testGetJournalInfoByRequestNo_RequestServiceReturnsNoItems() throws Exception {
        // Setup
        when(mockRequestService.getJournalInfoAndUpdate("requestNo")).thenReturn(Collections.emptyList());

        // Run the test and verify the results
        mockMvc.perform(get("/v1/getJournalInfoByRequestNo")
                        .param("requestNo", "requestNo")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json("[]", true));
    }

    @Test
    void testEmail() throws Exception {
        // Setup
        // Run the test and verify the results
        mockMvc.perform(get("/v1/email")
                        .param("requestNo", "requestNo")
                        .param("requestType", "requestType")
                        .param("status", "status")
                        .param("recordInfoResponse", "recordInfoResponse")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json("{}", true));

        // Confirm RequestService.sendEmail(...).
        final RecordInfoResponse recordInfoResponse1 = new RecordInfoResponse();
        recordInfoResponse1.setRequestNo(0);
        recordInfoResponse1.setPostDate("postDate");
        recordInfoResponse1.setDescription("description");
        recordInfoResponse1.setStatus("Deleted");
        recordInfoResponse1.setCountry("country");
        final List<RecordInfoResponse> recordInfoResponse = List.of(recordInfoResponse1);
        verify(mockRequestService).sendEmail("requestNo", "requestType", "status", recordInfoResponse);
    }

    @Test
    void testGetApproveRequestByStatus() throws Exception {
        // Setup
        // Configure RequestService.getApproveRequestByStatus(...).
        final RecordResponseAdmin recordResponseAdmin = new RecordResponseAdmin();
        recordResponseAdmin.setRequestNo(0);
        recordResponseAdmin.setPostDate("postDate");
        recordResponseAdmin.setDescription("description");
        recordResponseAdmin.setRequestType("requestType");
        recordResponseAdmin.setStatus("status");
        final List<RecordResponseAdmin> recordResponseAdmins = List.of(recordResponseAdmin);
        when(mockRequestService.getApproveRequestByStatus("approverUser", "status")).thenReturn(recordResponseAdmins);

        // Run the test and verify the results
        mockMvc.perform(get("/v1/getApproveRequestByStatus")
                        .param("approverUser", "approverUser")
                        .param("status", "status")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json("{}", true));
    }

    @Test
    void testGetApproveRequestByStatus_RequestServiceReturnsNoItems() throws Exception {
        // Setup
        when(mockRequestService.getApproveRequestByStatus("approverUser", "status"))
                .thenReturn(Collections.emptyList());

        // Run the test and verify the results
        mockMvc.perform(get("/v1/getApproveRequestByStatus")
                        .param("approverUser", "approverUser")
                        .param("status", "status")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json("[]", true));
    }

    @Test
    void testGetApproveRequestByUserByStatus() throws Exception {
        // Setup
        // Configure RequestService.getApproveRequestByUserByStatus(...).
        final RecordResponseAdmin recordResponseAdmin = new RecordResponseAdmin();
        recordResponseAdmin.setRequestNo(0);
        recordResponseAdmin.setPostDate("postDate");
        recordResponseAdmin.setDescription("description");
        recordResponseAdmin.setRequestType("requestType");
        recordResponseAdmin.setStatus("status");
        final List<RecordResponseAdmin> recordResponseAdmins = List.of(recordResponseAdmin);
        when(mockRequestService.getApproveRequestByUserByStatus("approverUser")).thenReturn(recordResponseAdmins);

        // Run the test and verify the results
        mockMvc.perform(get("/v1/getApproveRequestByUserByStatus")
                        .param("approverUser", "approverUser")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json("{}", true));
    }

    @Test
    void testGetApproveRequestByUserByStatus_RequestServiceReturnsNoItems() throws Exception {
        // Setup
        when(mockRequestService.getApproveRequestByUserByStatus("approverUser")).thenReturn(Collections.emptyList());

        // Run the test and verify the results
        mockMvc.perform(get("/v1/getApproveRequestByUserByStatus")
                        .param("approverUser", "approverUser")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json("[]", true));
    }

    @Test
    void testGetAdminRequestByStatus() throws Exception {
        // Setup
        // Configure RequestService.getAdminRequestByStatus(...).
        final RecordResponseAdmin recordResponseAdmin = new RecordResponseAdmin();
        recordResponseAdmin.setRequestNo(0);
        recordResponseAdmin.setPostDate("postDate");
        recordResponseAdmin.setDescription("description");
        recordResponseAdmin.setRequestType("requestType");
        recordResponseAdmin.setStatus("status");
        final List<RecordResponseAdmin> recordResponseAdmins = List.of(recordResponseAdmin);
        when(mockRequestService.getAdminRequestByStatus("status")).thenReturn(recordResponseAdmins);

        // Run the test and verify the results
        mockMvc.perform(get("/v1/getAdminRequestByStatus")
                        .param("status", "status")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json("{}", true));
    }

    @Test
    void testGetAdminRequestByStatus_RequestServiceReturnsNoItems() throws Exception {
        // Setup
        when(mockRequestService.getAdminRequestByStatus("status")).thenReturn(Collections.emptyList());

        // Run the test and verify the results
        mockMvc.perform(get("/v1/getAdminRequestByStatus")
                        .param("status", "status")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json("[]", true));
    }

    @Test
    void testGetMjeByRequestNo() throws Exception {
        // Setup
        // Configure RequestService.getMjeByRequestNo(...).
        final tempJournalResponse tempJournalResponse = new tempJournalResponse();
        tempJournalResponse.setIdentifier(0);
        tempJournalResponse.setStatus("status");
        tempJournalResponse.setTitle("title");
        tempJournalResponse.setCategoryId("categoryId");
        tempJournalResponse.setNatureId("natureId");
        final List<com.deep.demo.dto.tempJournalResponse> tempJournalResponses = List.of(tempJournalResponse);
        when(mockRequestService.getMjeByRequestNo("requestNo")).thenReturn(tempJournalResponses);

        // Run the test and verify the results
        mockMvc.perform(get("/v1/getMjeByRequestNo")
                        .param("requestNo", "requestNo")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json("{}", true));
    }

    @Test
    void testGetMjeByRequestNo_RequestServiceReturnsNoItems() throws Exception {
        // Setup
        when(mockRequestService.getMjeByRequestNo("requestNo")).thenReturn(Collections.emptyList());

        // Run the test and verify the results
        mockMvc.perform(get("/v1/getMjeByRequestNo")
                        .param("requestNo", "requestNo")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json("[]", true));
    }

    @Test
    void testGetJournalEntries() throws Exception {
        // Setup
        // Configure RequestService.getJournals(...).
        final PagingResponseModifiedJournal pagingResponseModifiedJournal = PagingResponseModifiedJournal.builder().build();
        when(mockRequestService.getJournals("requestNo", 0, 0)).thenReturn(pagingResponseModifiedJournal);

        // Run the test and verify the results
        mockMvc.perform(get("/v1/tempJournals")
                        .param("pageNumber", "0")
                        .param("pageSize", "0")
                        .param("requestNo", "requestNo")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json("{}", true));
    }

    @Test
    void testDeleteRecordByRequest() throws Exception {
        // Setup
        // Configure RequestService.getRequestsByRequestNumber(...).
        final RecordInfoResponse recordInfoResponse = new RecordInfoResponse();
        recordInfoResponse.setRequestNo(0);
        recordInfoResponse.setPostDate("postDate");
        recordInfoResponse.setDescription("description");
        recordInfoResponse.setStatus("Deleted");
        recordInfoResponse.setCountry("country");
        final List<RecordInfoResponse> recordInfoResponses = List.of(recordInfoResponse);
        when(mockRequestService.getRequestsByRequestNumber("requestNo")).thenReturn(recordInfoResponses);

        // Run the test and verify the results
        mockMvc.perform(get("/v1/deleteRecordByRequest")
                        .param("requestNo", "requestNo")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json("{}", true));
        verify(mockRequestService).deleteRecordByRequest("requestNo");

        // Confirm RequestService.sendEmail(...).
        final RecordInfoResponse recordInfoResponse2 = new RecordInfoResponse();
        recordInfoResponse2.setRequestNo(0);
        recordInfoResponse2.setPostDate("postDate");
        recordInfoResponse2.setDescription("description");
        recordInfoResponse2.setStatus("Deleted");
        recordInfoResponse2.setCountry("country");
        final List<RecordInfoResponse> recordInfoResponse1 = List.of(recordInfoResponse2);
        verify(mockRequestService).sendEmail("requestNo", "requestType", "status", recordInfoResponse1);
    }

    @Test
    void testDeleteRecordByRequest_RequestServiceGetRequestsByRequestNumberReturnsNoItems() throws Exception {
        // Setup
        when(mockRequestService.getRequestsByRequestNumber("requestNo")).thenReturn(Collections.emptyList());

        // Run the test and verify the results
        mockMvc.perform(get("/v1/deleteRecordByRequest")
                        .param("requestNo", "requestNo")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json("{}", true));
        verify(mockRequestService).deleteRecordByRequest("requestNo");

        // Confirm RequestService.sendEmail(...).
        final RecordInfoResponse recordInfoResponse1 = new RecordInfoResponse();
        recordInfoResponse1.setRequestNo(0);
        recordInfoResponse1.setPostDate("postDate");
        recordInfoResponse1.setDescription("description");
        recordInfoResponse1.setStatus("Deleted");
        recordInfoResponse1.setCountry("country");
        final List<RecordInfoResponse> recordInfoResponse = List.of(recordInfoResponse1);
        verify(mockRequestService).sendEmail("requestNo", "requestType", "status", recordInfoResponse);
    }

    @Test
    void testModifyRecordByRequest() throws Exception {
        // Setup
        // Configure RequestService.modifyRequest(...).
        final JournalUploadResponse response = new JournalUploadResponse();
        response.setSuccessReport("successReport");
        response.setErrorReport("errorReport");
        response.setSuccessCount(0);
        response.setErrorCount(0);
        response.setRequestNo(0);
        final ModifyRequestDto modifyRequestDto = new ModifyRequestDto();
        modifyRequestDto.setRequestNo("requestNo");
        modifyRequestDto.setPostDate("postDate");
        modifyRequestDto.setReason("reason");
        modifyRequestDto.setRequestType("requestType");
        modifyRequestDto.setStatus("status");
        final List<ModifyRequestDto> requestModifyDto = List.of(modifyRequestDto);
        when(mockRequestService.modifyRequest(eq(requestModifyDto), any(MultipartFile.class),
                any(MultipartFile.class))).thenReturn(response);

        // Run the test and verify the results
        mockMvc.perform(multipart("/v1/modifyRecordByRequest")
                        .file(new MockMultipartFile("modifyRequestDto", "originalFilename", MediaType.APPLICATION_JSON_VALUE,
                                "content".getBytes()))
                        .file(new MockMultipartFile("MJEfile", "originalFilename", MediaType.APPLICATION_JSON_VALUE,
                                "content".getBytes()))
                        .file(new MockMultipartFile("AJEfile", "originalFilename", MediaType.APPLICATION_JSON_VALUE,
                                "content".getBytes()))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json("{}", true));

        // Confirm RequestService.sendEmail(...).
        final RecordInfoResponse recordInfoResponse1 = new RecordInfoResponse();
        recordInfoResponse1.setRequestNo(0);
        recordInfoResponse1.setPostDate("postDate");
        recordInfoResponse1.setDescription("description");
        recordInfoResponse1.setStatus("Deleted");
        recordInfoResponse1.setCountry("country");
        final List<RecordInfoResponse> recordInfoResponse = List.of(recordInfoResponse1);
        verify(mockRequestService).sendEmail("requestNo", "requestType", "status", recordInfoResponse);
    }

    @Test
    void testModifyRecordByRequest_RequestServiceModifyRequestThrowsException() throws Exception {
        // Setup
        // Configure RequestService.modifyRequest(...).
        final ModifyRequestDto modifyRequestDto = new ModifyRequestDto();
        modifyRequestDto.setRequestNo("requestNo");
        modifyRequestDto.setPostDate("postDate");
        modifyRequestDto.setReason("reason");
        modifyRequestDto.setRequestType("requestType");
        modifyRequestDto.setStatus("status");
        final List<ModifyRequestDto> requestModifyDto = List.of(modifyRequestDto);
        when(mockRequestService.modifyRequest(eq(requestModifyDto), any(MultipartFile.class),
                any(MultipartFile.class))).thenThrow(Exception.class);

        // Run the test and verify the results
        mockMvc.perform(multipart("/v1/modifyRecordByRequest")
                        .file(new MockMultipartFile("modifyRequestDto", "originalFilename", MediaType.APPLICATION_JSON_VALUE,
                                "content".getBytes()))
                        .file(new MockMultipartFile("MJEfile", "originalFilename", MediaType.APPLICATION_JSON_VALUE,
                                "content".getBytes()))
                        .file(new MockMultipartFile("AJEfile", "originalFilename", MediaType.APPLICATION_JSON_VALUE,
                                "content".getBytes()))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is5xxServerError())
                .andExpect(content().json("{}", true));
    }

    @Test
    void testModifyJournals_RequestServiceModifyJournalsThrowsException() throws Exception {
        // Setup
        // Configure RequestService.modifyJournals(...).
        final RequestDto requestDto = new RequestDto();
        requestDto.setRequestNo(0);
        requestDto.setPostDate(LocalDateTime.of(2020, 1, 1, 0, 0, 0));
        requestDto.setDescription("description");
        requestDto.setRequestType("requestType");
        requestDto.setStatus("status");
        final List<RequestDto> requestUploadDto = List.of(requestDto);
        when(mockRequestService.modifyJournals(eq(requestUploadDto), any(MultipartFile.class),
                any(MultipartFile.class))).thenThrow(Exception.class);

        // Run the test and verify the results
        mockMvc.perform(multipart("/v1/modifyJournals")
                        .file(new MockMultipartFile("requestDtoList", "originalFilename", MediaType.APPLICATION_JSON_VALUE,
                                "content".getBytes()))
                        .file(new MockMultipartFile("MJEfile", "originalFilename", MediaType.APPLICATION_JSON_VALUE,
                                "content".getBytes()))
                        .file(new MockMultipartFile("AJEfile", "originalFilename", MediaType.APPLICATION_JSON_VALUE,
                                "content".getBytes()))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is5xxServerError())
                .andExpect(content().json("{}", true));
    }

    @Test
    void testPostComment() throws Exception {
        // Setup
        // Run the test and verify the results
        mockMvc.perform(post("/v1/postComment")
                        .content("content").contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json("{}", true));

        // Confirm RequestService.postComment(...).
        final CommentDto commentDto = new CommentDto();
        commentDto.setRequestNo("requestNo");
        commentDto.setComment("comment");
        commentDto.setCommentDate(LocalDateTime.of(2020, 1, 1, 0, 0, 0));
        commentDto.setUserId("userId");
        final List<CommentDto> commentUploadDto = List.of(commentDto);
        verify(mockRequestService).postComment(commentUploadDto);
    }

    @Test
    void testPostRejectRequest() throws Exception {
        // Setup
        // Run the test and verify the results
        mockMvc.perform(post("/v1/reviewRequestReject")
                        .content("content").contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json("{}", true));

        // Confirm RequestService.postReviewReject(...).
        final ApprovalDto approvalDto = new ApprovalDto();
        approvalDto.setRequestNo(0);
        approvalDto.setPostDate("postDate");
        approvalDto.setStatus("status");
        approvalDto.setRejectedReason("rejectedReason");
        approvalDto.setReviewUser("reviewUser");
        final List<ApprovalDto> approvalUploadDto = List.of(approvalDto);
        verify(mockRequestService).postReviewReject(approvalUploadDto);

        // Confirm RequestService.sendEmail(...).
        final RecordInfoResponse recordInfoResponse1 = new RecordInfoResponse();
        recordInfoResponse1.setRequestNo(0);
        recordInfoResponse1.setPostDate("postDate");
        recordInfoResponse1.setDescription("description");
        recordInfoResponse1.setStatus("Deleted");
        recordInfoResponse1.setCountry("country");
        final List<RecordInfoResponse> recordInfoResponse = List.of(recordInfoResponse1);
        verify(mockRequestService).sendEmail("requestNo", "requestType", "status", recordInfoResponse);
    }

    @Test
    void testPostApproveRequest() throws Exception {
        // Setup
        // Configure RequestService.postReviewApprove(...).
        final ApprovalDto approvalDto = new ApprovalDto();
        approvalDto.setRequestNo(0);
        approvalDto.setPostDate("postDate");
        approvalDto.setStatus("status");
        approvalDto.setRejectedReason("rejectedReason");
        approvalDto.setReviewUser("reviewUser");
        final List<ApprovalDto> approvalUploadDto = List.of(approvalDto);
        when(mockRequestService.postReviewApprove(approvalUploadDto)).thenReturn("status");

        // Run the test and verify the results
        mockMvc.perform(post("/v1/reviewRequestApprove")
                        .content("content").contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json("{}", true));

        // Confirm RequestService.sendEmail(...).
        final RecordInfoResponse recordInfoResponse1 = new RecordInfoResponse();
        recordInfoResponse1.setRequestNo(0);
        recordInfoResponse1.setPostDate("postDate");
        recordInfoResponse1.setDescription("description");
        recordInfoResponse1.setStatus("Deleted");
        recordInfoResponse1.setCountry("country");
        final List<RecordInfoResponse> recordInfoResponse = List.of(recordInfoResponse1);
        verify(mockRequestService).sendEmail("requestNo", "requestType", "status", recordInfoResponse);
    }

    @Test
    void testGetCommentsByRequestNo() throws Exception {
        // Setup
        // Configure RequestService.getCommentByRequestNo(...).
        final CommentDto commentDto = new CommentDto();
        commentDto.setRequestNo("requestNo");
        commentDto.setComment("comment");
        commentDto.setCommentDate(LocalDateTime.of(2020, 1, 1, 0, 0, 0));
        commentDto.setUserId("userId");
        final List<CommentDto> commentDtos = List.of(commentDto);
        when(mockRequestService.getCommentByRequestNo("requestNo")).thenReturn(commentDtos);

        // Run the test and verify the results
        mockMvc.perform(get("/v1/getCommentByRequestNo")
                        .param("requestNo", "requestNo")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json("{}", true));
    }

    @Test
    void testGetCommentsByRequestNo_RequestServiceReturnsNoItems() throws Exception {
        // Setup
        when(mockRequestService.getCommentByRequestNo("requestNo")).thenReturn(Collections.emptyList());

        // Run the test and verify the results
        mockMvc.perform(get("/v1/getCommentByRequestNo")
                        .param("requestNo", "requestNo")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json("[]", true));
    }

    @Test
    void testFetchUserList() throws Exception {
        // Setup
        when(mockFetchAllUsers.getAttributesForGroup("adGroup")).thenReturn("result");

        // Run the test and verify the results
        mockMvc.perform(get("/v1/fetchUserList")
                        .param("adGroup", "adGroup")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json("{}", true));
    }

    @Test
    void testDownloadfile() throws Exception {
        // Setup
        when(mockRequestService.getFileName("requestNo", "jeType")).thenReturn("fileName");
        when(mockFileUploadService.loadfilefromURI("requestNo", "fileName")).thenReturn("content".getBytes());

        // Run the test and verify the results
        mockMvc.perform(get("/v1/downloadfile")
                        .param("requestNo", "requestNo")
                        .param("JEType", "jeType")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void testDownloadfile_FileUploadServiceThrowsInvalidKeyException() throws Exception {
        // Setup
        when(mockRequestService.getFileName("requestNo", "jeType")).thenReturn("fileName");
        when(mockFileUploadService.loadfilefromURI("requestNo", "fileName")).thenThrow(InvalidKeyException.class);

        // Run the test and verify the results
        mockMvc.perform(get("/v1/downloadfile")
                        .param("requestNo", "requestNo")
                        .param("JEType", "jeType")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json("{}", true));
    }

    @Test
    void testDownloadfile_FileUploadServiceThrowsURISyntaxException() throws Exception {
        // Setup
        when(mockRequestService.getFileName("requestNo", "jeType")).thenReturn("fileName");
        when(mockFileUploadService.loadfilefromURI("requestNo", "fileName")).thenThrow(URISyntaxException.class);

        // Run the test and verify the results
        mockMvc.perform(get("/v1/downloadfile")
                        .param("requestNo", "requestNo")
                        .param("JEType", "jeType")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json("{}", true));
    }

    @Test
    void testDownloadfile_FileUploadServiceThrowsStorageException() throws Exception {
        // Setup
        when(mockRequestService.getFileName("requestNo", "jeType")).thenReturn("fileName");
        when(mockFileUploadService.loadfilefromURI("requestNo", "fileName")).thenThrow(StorageException.class);

        // Run the test and verify the results
        mockMvc.perform(get("/v1/downloadfile")
                        .param("requestNo", "requestNo")
                        .param("JEType", "jeType")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json("{}", true));
    }

    @Test
    void testAdminReviewer() throws Exception {
        // Setup
        // Run the test and verify the results
        mockMvc.perform(multipart("/v1/adminReviewer")
                        .file(new MockMultipartFile("adminDtoList", "originalFilename", MediaType.APPLICATION_JSON_VALUE,
                                "content".getBytes()))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json("{}", true));

        // Confirm RequestService.adminReviewUpdate(...).
        final AdminRevDto adminRevDto = new AdminRevDto();
        adminRevDto.setApproverGroup1("approverGroup1");
        adminRevDto.setApproverGroup2("approverGroup2");
        adminRevDto.setApproverUser1("approverUser1");
        adminRevDto.setApproverUser2("approverUser2");
        adminRevDto.setRequestNo("requestNo");
        final List<AdminRevDto> adminUploadDto = List.of(adminRevDto);
        verify(mockRequestService).adminReviewUpdate(adminUploadDto);

        // Confirm RequestService.sendEmail(...).
        final RecordInfoResponse recordInfoResponse1 = new RecordInfoResponse();
        recordInfoResponse1.setRequestNo(0);
        recordInfoResponse1.setPostDate("postDate");
        recordInfoResponse1.setDescription("description");
        recordInfoResponse1.setStatus("Deleted");
        recordInfoResponse1.setCountry("country");
        final List<RecordInfoResponse> recordInfoResponse = List.of(recordInfoResponse1);
        verify(mockRequestService).sendEmail("requestNo", "requestType", "status", recordInfoResponse);
    }
}
