package com.deep.demo.repository;

import com.deep.demo.dto.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

public interface RequestRepository {
    List<RequestDto> getRequests();
    List<RecordInfoResponse> getRequestsByRequestNumber(String requestNo);
    List<RecordResponse> getRequestByStatus(Map<String, String> parameters) ;
    List<RecordResponseAdmin> getApproveRequestByStatus(Map<String, String> parameters) ;
    List<tempJournalResponse> getMjeByRequestNo(Map<String, String> parameters);
    List<CommentDto> getCommentByRequestNo(Map<String, String> parameters);
    List<RecordInfoResponse> getJournalInfoByRequestNo(Map<String, String> parameters);
    List<RecordResponseAdmin> getApproveRequestByUserPending( Map<String, String> parameters) ;
    List<RecordResponseAdmin> getApproveRequestByUserApproved( Map<String, String> parameters) ;
    void deleteRecordByRequest(String requestNo) ;
    void deleteTempByRequest(String requestNo);
    String getFileName(String requestNo,String fileType);
    List<RecordResponseAdmin> getAdminRequestByStatus(Map<String, String> parameters) ;
    List<RecordResponseAdmin> getApproveRequestByUserByStatus(Map<String, String> parameters) ;
    int getTempJournalEntryCount();
    Page<tempJournalResponse> findAll(String requestNo, Pageable page, int count) ;
    List<tempJournalResponse> getJournalEntryList(Map<String, Number> parameters);
    void addRequest(List<RequestDto> requestUploadDto, MultipartFile mjeFile, MultipartFile ajeFile);
    void adminReviewUpdate(List<AdminRevDto> adminUploadDto);
    void modifyRequest(Map<String, String> parameters);
    int getRequestNo();
    void postComment(List<CommentDto> commentDto) ;
    String postApproval(List<ApprovalDto> approvalDto) ;
    void postRejection(List<ApprovalDto> approvalDto) ;
    void postJournalEntry(tempJournalResponse tempJournalResponse);
    void saveExpectedPeriodToTemp(Integer journalId, String mappedValue);
    void saveSegmentToTemp(Integer journalId, String mappedValue);
    void saveDataSourcesToTemp(Integer journalId, String mappedValue);
    void saveUpstreamDependencyToTemp(Integer journalId, String mappedValue);
    void saveDownStreamImpactToTemp(Integer journalId, String mappedValue);
    void saveGlAccountToTemp(Integer journalId, String mappedValue);
    void saveFunctionalAreaToTemp(Integer journalId, String mappedValue);
    void saveDepartmentToTemp(Integer journalId, String mappedValue);
    void saveCompanyCodeToTemp(Integer journalId, String mappedValue);
    List<String> getApproverByRequestNo(String requestNo);
    void updateApprover1Flag(String requestNo);
    void updateJournalEntry(tempJournalResponse journalEntry);
}


