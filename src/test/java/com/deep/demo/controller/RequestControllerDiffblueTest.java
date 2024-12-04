package com.deep.demo.controller;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.deep.demo.dto.AdminRevUploadDto;
import com.deep.demo.dto.ApprovalDto;
import com.deep.demo.dto.ApprovalUploadDto;
import com.deep.demo.dto.CommentDto;
import com.deep.demo.dto.CommentUploadDto;
import com.deep.demo.dto.RecordInfoResponse;
import com.deep.demo.dto.RequestUploadDto;
import com.deep.demo.service.FetchAllUsers;
import com.deep.demo.service.FileUploadService;
import com.deep.demo.service.RequestService;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.DataInputStream;
import java.io.InputStream;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.multipart.MultipartFile;

@ContextConfiguration(classes = {RequestController.class})
@ExtendWith(SpringExtension.class)
class RequestControllerDiffblueTest {
    @MockBean
    private FetchAllUsers fetchAllUsers;

    @MockBean
    private FileUploadService fileUploadService;

    @Autowired
    private RequestController requestController;

    @MockBean
    private RequestService requestService;

    /**
     * Method under test: {@link RequestController#adminReviewer(AdminRevUploadDto)}
     */

    /**
     * Method under test:
     * {@link RequestController#addRequest(RequestUploadDto, MultipartFile, MultipartFile)}
     */
    @Test
    void testAddRequest() throws Exception {
        RequestUploadDto requestUploadDto = new RequestUploadDto();
        requestUploadDto.setRequestData(new ArrayList<>());
        MockHttpServletRequestBuilder postResult = MockMvcRequestBuilders.post("/v1/createRecord");
        MockHttpServletRequestBuilder paramResult = postResult.param("ajeFile",
                String.valueOf(new MockMultipartFile("Name", (InputStream) null)));
        MockHttpServletRequestBuilder requestBuilder = paramResult
                .param("mjeFile", String.valueOf(new MockMultipartFile("Name", (InputStream) null)))
                .param("requestDtoList", String.valueOf(requestUploadDto));
        ResultActions actualPerformResult = MockMvcBuilders.standaloneSetup(requestController)
                .build()
                .perform(requestBuilder);
        actualPerformResult.andExpect(MockMvcResultMatchers.status().is(415));
    }

    /**
     * Method under test:
     * {@link RequestController#addRequest(RequestUploadDto, MultipartFile, MultipartFile)}
     */
    @Test
    void testAddRequest2() throws Exception {
        DataInputStream contentStream = mock(DataInputStream.class);
        when(contentStream.readAllBytes()).thenReturn("AXAXAXAX".getBytes("UTF-8"));
        doNothing().when(contentStream).close();
        MockHttpServletRequestBuilder paramResult = MockMvcRequestBuilders.post("/v1/createRecord")
                .param("ajeFile", String.valueOf(new MockMultipartFile("Name", contentStream)));

        RequestUploadDto requestUploadDto = new RequestUploadDto();
        requestUploadDto.setRequestData(new ArrayList<>());
        MockHttpServletRequestBuilder requestBuilder = paramResult
                .param("mjeFile", String.valueOf(new MockMultipartFile("Name", (InputStream) null)))
                .param("requestDtoList", String.valueOf(requestUploadDto));
        ResultActions actualPerformResult = MockMvcBuilders.standaloneSetup(requestController)
                .build()
                .perform(requestBuilder);
        actualPerformResult.andExpect(MockMvcResultMatchers.status().is(415));
    }

    /**
     * Method under test: {@link RequestController#deleteRecordByRequest(String)}
     */
    @Test
    void testDeleteRecordByRequest() throws Exception {
        when(requestService.getRequestsByRequestNumber(Mockito.<String>any())).thenReturn(new ArrayList<>());
        doNothing().when(requestService).deleteRecordByRequest(Mockito.<String>any());
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/v1/deleteRecordByRequest")
                .param("requestNo", "foo");
        MockMvcBuilders.standaloneSetup(requestController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("text/plain;charset=ISO-8859-1"))
                .andExpect(MockMvcResultMatchers.content()
                        .string("java.lang.IndexOutOfBoundsException: Index 0 out of bounds for length 0"));
    }

    /**
     * Method under test: {@link RequestController#deleteRecordByRequest(String)}
     */
    @Test
    void testDeleteRecordByRequest2() throws Exception {
        RecordInfoResponse recordInfoResponse = new RecordInfoResponse();
        recordInfoResponse.setAdminUser("Admin User");
        recordInfoResponse.setAjeFile("Aje File");
        recordInfoResponse.setApprovedUser1Status("Approved User1 Status");
        recordInfoResponse.setApprovedUser2Status("Approved User2 Status");
        recordInfoResponse.setApproverGroup1("Approver Group1");
        recordInfoResponse.setApproverGroup2("Approver Group2");
        recordInfoResponse.setApproverUser1("Approver User1");
        recordInfoResponse.setApproverUser2("Approver User2");
        recordInfoResponse.setCountry("GB");
        recordInfoResponse.setDescription("The characteristics of someone or something");
        recordInfoResponse.setMjeFile("Mje File");
        recordInfoResponse.setPostDate("2020-03-01");
        recordInfoResponse.setReason("Just cause");
        recordInfoResponse.setRejectedReason("Just cause");
        recordInfoResponse.setRequestNo(1);
        recordInfoResponse.setRequestType("Request Type");
        recordInfoResponse.setRequestorName("Requestor Name");
        recordInfoResponse.setStatus("Status");
        recordInfoResponse.setUserld("Userld");

        ArrayList<RecordInfoResponse> recordInfoResponseList = new ArrayList<>();
        recordInfoResponseList.add(recordInfoResponse);
        when(requestService.sendEmail(Mockito.<String>any(), Mockito.<String>any(), Mockito.<String>any(),
                Mockito.<List<RecordInfoResponse>>any())).thenReturn("jane.doe@example.org");
        when(requestService.getRequestsByRequestNumber(Mockito.<String>any())).thenReturn(recordInfoResponseList);
        doNothing().when(requestService).deleteRecordByRequest(Mockito.<String>any());
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/v1/deleteRecordByRequest")
                .param("requestNo", "foo");
        MockMvcBuilders.standaloneSetup(requestController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("text/plain;charset=ISO-8859-1"))
                .andExpect(MockMvcResultMatchers.content().string("Deleted Succesfully"));
    }

    /**
     * Method under test:
     * {@link RequestController#modifyJournals(RequestUploadDto, MultipartFile, MultipartFile)}
     */
    @Test
    void testModifyJournals() throws Exception {
        RequestUploadDto requestUploadDto = new RequestUploadDto();
        requestUploadDto.setRequestData(new ArrayList<>());
        MockHttpServletRequestBuilder postResult = MockMvcRequestBuilders.post("/v1/modifyJournals");
        MockHttpServletRequestBuilder paramResult = postResult.param("ajeFile",
                String.valueOf(new MockMultipartFile("Name", (InputStream) null)));
        MockHttpServletRequestBuilder requestBuilder = paramResult
                .param("mjeFile", String.valueOf(new MockMultipartFile("Name", (InputStream) null)))
                .param("requestDtoList", String.valueOf(requestUploadDto));
        ResultActions actualPerformResult = MockMvcBuilders.standaloneSetup(requestController)
                .build()
                .perform(requestBuilder);
        actualPerformResult.andExpect(MockMvcResultMatchers.status().is(415));
    }

    /**
     * Method under test:
     * {@link RequestController#modifyJournals(RequestUploadDto, MultipartFile, MultipartFile)}
     */
    @Test
    void testModifyJournals2() throws Exception {
        DataInputStream contentStream = mock(DataInputStream.class);
        when(contentStream.readAllBytes()).thenReturn("AXAXAXAX".getBytes("UTF-8"));
        doNothing().when(contentStream).close();
        MockHttpServletRequestBuilder paramResult = MockMvcRequestBuilders.post("/v1/modifyJournals")
                .param("ajeFile", String.valueOf(new MockMultipartFile("Name", contentStream)));

        RequestUploadDto requestUploadDto = new RequestUploadDto();
        requestUploadDto.setRequestData(new ArrayList<>());
        MockHttpServletRequestBuilder requestBuilder = paramResult
                .param("mjeFile", String.valueOf(new MockMultipartFile("Name", (InputStream) null)))
                .param("requestDtoList", String.valueOf(requestUploadDto));
        ResultActions actualPerformResult = MockMvcBuilders.standaloneSetup(requestController)
                .build()
                .perform(requestBuilder);
        actualPerformResult.andExpect(MockMvcResultMatchers.status().is(415));
    }

    /**
     * Method under test:
     * {@link RequestController#postApproveRequest(ApprovalUploadDto)}
     */
    @Test
    void testPostApproveRequest() throws Exception {
        when(requestService.sendEmail(Mockito.<String>any(), Mockito.<String>any(), Mockito.<String>any(),
                Mockito.<List<RecordInfoResponse>>any())).thenReturn("jane.doe@example.org");
        when(requestService.postReviewApprove(Mockito.<List<ApprovalDto>>any())).thenReturn("Post Review Approve");

        ApprovalDto approvalDto = new ApprovalDto();
        approvalDto.setApprovedUser1Status("Approved User1 Status");
        approvalDto.setApprovedUser2Status("Approved User2 Status");
        approvalDto.setFlag("Flag");
        approvalDto.setPostDate("2020-03-01");
        approvalDto.setRejectedReason("Just cause");
        approvalDto.setRequestNo(1);
        approvalDto.setReviewUser("Review User");
        approvalDto.setStatus("Status");

        ArrayList<ApprovalDto> approvalData = new ArrayList<>();
        approvalData.add(approvalDto);

        ApprovalUploadDto approvalUploadDto = new ApprovalUploadDto();
        approvalUploadDto.setApprovalData(approvalData);
        String content = (new ObjectMapper()).writeValueAsString(approvalUploadDto);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.post("/v1/reviewRequestApprove")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content);
        MockMvcBuilders.standaloneSetup(requestController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    /**
     * Method under test: {@link RequestController#postComment(CommentUploadDto)}
     */
    @Test
    void testPostComment() throws Exception {
        doNothing().when(requestService).postComment(Mockito.<List<CommentDto>>any());

        CommentUploadDto commentUploadDto = new CommentUploadDto();
        commentUploadDto.setCommentData(new ArrayList<>());
        String content = (new ObjectMapper()).writeValueAsString(commentUploadDto);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.post("/v1/postComment")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content);
        MockMvcBuilders.standaloneSetup(requestController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    /**
     * Method under test:
     * {@link RequestController#postRejectRequest(ApprovalUploadDto)}
     */
    @Test
    void testPostRejectRequest() throws Exception {
        when(requestService.sendEmail(Mockito.<String>any(), Mockito.<String>any(), Mockito.<String>any(),
                Mockito.<List<RecordInfoResponse>>any())).thenReturn("jane.doe@example.org");
        doNothing().when(requestService).postReviewReject(Mockito.<List<ApprovalDto>>any());

        ApprovalDto approvalDto = new ApprovalDto();
        approvalDto.setApprovedUser1Status("Approved User1 Status");
        approvalDto.setApprovedUser2Status("Approved User2 Status");
        approvalDto.setFlag("Flag");
        approvalDto.setPostDate("2020-03-01");
        approvalDto.setRejectedReason("Just cause");
        approvalDto.setRequestNo(1);
        approvalDto.setReviewUser("Review User");
        approvalDto.setStatus("Status");

        ArrayList<ApprovalDto> approvalData = new ArrayList<>();
        approvalData.add(approvalDto);

        ApprovalUploadDto approvalUploadDto = new ApprovalUploadDto();
        approvalUploadDto.setApprovalData(approvalData);
        String content = (new ObjectMapper()).writeValueAsString(approvalUploadDto);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.post("/v1/reviewRequestReject")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content);
        MockMvcBuilders.standaloneSetup(requestController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk());
    }
}
