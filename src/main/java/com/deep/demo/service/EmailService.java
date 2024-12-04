package com.deep.demo.service;


import com.deep.demo.dto.RecordInfoResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.*;
import java.sql.Timestamp;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class EmailService {

    @Value("${mail.smtp.host}")
    private String smtpHost;

    @Value("${mail.smtp.port}")
    private Integer smtpPort;

    @Value("${mail.smtp.auth}")
    private Boolean smtpAuth;

    @Value("${mail.smtp.starttls.enable}")
    private Boolean starttls;

    @Value("${mail.smtp.ssl.protocols}")
    private String smtpSSLProtocols;

    @Value("${mail.mailTo}")
    private String mailTo;

    @Value("${mail.mailFrom}")
    private String mailFrom;

    @Value("${mail.mailDL}")
    private String mailDL;

    @Value("${business.message}")
    private String message;

    private final FetchAllUsers fetchAllUsers;
    private static final Logger LOGGER = LoggerFactory.getLogger(EmailService.class);
    private static final String[] USER_LIST = {"GL-MJEMS-Approver-non-prod", "GL-MJEMS-DataSteward-non-prod", "GL-MJEMS-Admin-non-prod", "GL-MJEMS-Certifier-non-prod"};

    private static final String HTML_BODY_START = "<html><body><div><p>";
    private static final String HTML_BODY_END = "</p></div></body></html>";

    private static final String MAIL_SENT_AT = "Mail Sent at : {} ";
    private static final String ERROR_EMAIL = "Error while sending mail";
    private static final String APPLICATION = "<b>Application: MJE Governance Tool</b> ";
    private static final String ERROR_RESPONSE_STRING = "Error with response string -- ";

    private static final String BATCH_NO = "<b>Batch Number:</b> ";
    private static final String STATUS = "<b>Status: </b> ";
    private static final String DESCRIPTION = "<b>Description : </b>";
    private static final String REQUESTOR_NAME = "<b>Requestor Name: </b> ";
    private static final String REQUESTOR_COUNTRY = "<b>Requestor Country: </b> ";
    private static final String REQUESTED_DATE = "<b>Batch Created: </b> ";
    private static final String REQUEST_LINK = "<b>Batch link: </b>";
    private static final String REJECTION_REASON = "<b>Reason of rejection: </b> ";

    private static final String LEVEL_1_APPROVER_USER = "<b>Level 1 Approver User: </b> ";
    private static final String LEVEL_2_APPROVER_USER = "<b>Level 2 Approver User: </b> ";

    private static final String BUSINESS_MESSAGE = "<b>Message from Business: </b> ";
    private static final String DOMAIN = "@wal-mart.com";
    private static final String SEPARATOR = " , ";

    public void sendMail(List<RecordInfoResponse> records, String approvalStatus, File attachment) {
        try {
            JavaMailSenderImpl mailSender = initializeMailSender();
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true);
            mimeMessageHelper.setFrom(new InternetAddress(mailFrom));

            List<String> subjects = buildEmailSubject(records, approvalStatus);

            for (String subject : subjects) {
                List<InternetAddress> emailRecipients = getEmailRecipients(records, subject);
                String body = getEmailBody(records, subject);
                if(subject.contains("Sweep")){
                    if(attachment != null) {
//                        mimeMessageHelper.addAttachment(attachment.getName(), attachment);
                    }
                }
                if (emailRecipients != null) {
                    mimeMessageHelper.setSubject(subject);
                    mimeMessageHelper.setTo(emailRecipients.toArray(new InternetAddress[0]));
                    mimeMessageHelper.setText(body, true);
                    mailSender.send(mimeMessage);
                } else {
                    LOGGER.info("Not a valid scenario for sending email");
                }
            }

            LOGGER.info(MAIL_SENT_AT, getCurrentSQLTimeStamp());
        } catch (Exception e) {
            LOGGER.error(ERROR_EMAIL, e);
        }
    }

    private JavaMailSenderImpl initializeMailSender() {
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost(smtpHost);
        mailSender.setPort(smtpPort);

        Properties javaMailProperties = new Properties();
        javaMailProperties.put("mail.smtp.auth", smtpAuth);
        javaMailProperties.put("mail.smtp.starttls.enable", starttls);
        javaMailProperties.setProperty("mail.smtp.ssl.protocols", smtpSSLProtocols);

        mailSender.setJavaMailProperties(javaMailProperties);
        return mailSender;
    }

    private List<InternetAddress> getEmailRecipients(List<RecordInfoResponse> records, String subject) throws AddressException {
        if (subject.contains("Sweep/AJE")) {
            return List.of(InternetAddress.parse(mailDL));
        } else if (subject.contains("Created") || subject.contains("Modified") || subject.contains("Reminder")) {
            return List.of(InternetAddress.parse(buildEmailRecipientListForApprovers(records.get(0))));
        } else if (subject.contains("Approved") || subject.contains("Rejected")) {
            return List.of(InternetAddress.parse(buildEmailRecipientListForRequester(records.get(0))));
        } else if (subject.contains("Cancelled") || subject.contains("Deleted")) {
            return List.of(InternetAddress.parse(buildEmailRecipientListForAllPartiesInvolved(records.get(0))));
        }
        return null;
    }

    private String getEmailBody(List<RecordInfoResponse> records, String subject) {
        if (subject.contains("Sweep/AJE")) {
            return createBodyMessageMECTeamEmail(records.get(0));
        } else if (subject.contains("Created") || subject.contains("Modified") || subject.contains("Reminder")) {
            return createBodyMessageForRequestEmail(records.get(0));
        } else if (subject.contains("Approved") || subject.contains("Rejected")) {
            return createBodyMessageDecisionEmail(records.get(0));
        } else if (subject.contains("Cancelled") || subject.contains("Deleted")) {
            return createBodyMessageForCancelEmail(records.get(0));
        }
        return null;
    }

    public String createBodyMessage(RecordInfoResponse record, String batchLink, String rejectionReason) {
        StringBuilder bodyMessage = new StringBuilder();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        simpleDateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        Map<String, String> map = getUserMap();
        try {
            bodyMessage.append(HTML_BODY_START)
                    .append(APPLICATION).append("<br>")
                    .append(REQUESTOR_NAME)
                    .append(record.getRequestorName()).append("<br>")
                    .append(REQUESTOR_COUNTRY)
                    .append(record.getCountry()).append("<br>")
                    .append(LEVEL_1_APPROVER_USER)
                    .append(map.getOrDefault(record.getApproverUser1(), "NULL")).append("<br>")
                    .append(LEVEL_2_APPROVER_USER)
                    .append(map.getOrDefault(record.getApproverUser2(), "NULL")).append("<br>")
                    .append(REQUESTED_DATE)
                    .append(record.getPostDate()).append("<br>");
            if (record.getRequestNo() != 0) {
                bodyMessage.append(BATCH_NO).append(record.getRequestNo()).append("<br>");
            } else {
                bodyMessage.append(" Batch number not generated as batch submission failed to save data in Azure DB <br>");
            }

            if(rejectionReason != null && !rejectionReason.trim().isEmpty()) {
                bodyMessage.append(REJECTION_REASON).append(rejectionReason).append("<br>");
            }

            bodyMessage.append(STATUS)
                    .append(record.getStatus().equals("Pending") ? "Pending Approval" : record.getStatus()).append("<br>")
                    .append(DESCRIPTION)
                    .append(record.getDescription()).append("<br>");

            if (batchLink != null) {
                bodyMessage.append(REQUEST_LINK)
                        .append(batchLink)
                        .append(record.getRequestNo()).append("<br>");
            }
            bodyMessage.append(BUSINESS_MESSAGE).append(message);
            bodyMessage.append(HTML_BODY_END);
        } catch (Exception e) {
            LOGGER.error(ERROR_RESPONSE_STRING, e);
        }
        LOGGER.debug(bodyMessage.toString());
        return bodyMessage.toString();
    }

    public Map<String, String> getUserMap() {
        Map<String, String> userMap = new HashMap<>();
        for (String user : USER_LIST) {
            userMap.putAll(parseUserList(fetchAllUsers.getAttributesForGroup(user)));
        }
        userMap.put("LB-mje-approver1", "LB-mje-approver1");
        userMap.put("LB-mje-approver", "LB-mje-approver");
        userMap.put("LB-mje-approver2", "LB-mje-approver2");
        return userMap;
    }

    public String createBodyMessageForRequestEmail(RecordInfoResponse record) {
        String batchLink = "https://mje-governance.dev.walmart.net/approvalinfo/";
        return createBodyMessage(record, batchLink, null);
    }

    public String createBodyMessageMECTeamEmail(RecordInfoResponse record) {
        return createBodyMessage(record, null, null);
    }

    public String createBodyMessageForCancelEmail(RecordInfoResponse record) {
        return createBodyMessage(record, null, null);
    }

    public String createBodyMessageDecisionEmail(RecordInfoResponse record) {
        return createBodyMessage(record, null, record.getRejectedReason());
    }

    public Timestamp getCurrentSQLTimeStamp() {
        return new Timestamp(System.currentTimeMillis());
    }

    public List<String> buildEmailSubject(List<RecordInfoResponse> records, String approverStatus) {
        List<String> subjects = new ArrayList<>();
        RecordInfoResponse record = records.get(0);

        String status = record.getStatus();
        String requestType = record.getRequestType();
        Integer requestNumber = record.getRequestNo();
        String ajeFile = record.getAjeFile();
        return buildSubjectLine(status, approverStatus, requestType, requestNumber, subjects, ajeFile);
    }

    private List<String> buildSubjectLine(String status, String approverStatus, String requestType, Integer requestNumber, List<String> subjects, String ajeFile) {
        switch (status) {
            case "Approved":
                subjects.add(String.format("MJE Record Catalogue - Journal Identifier Request (#%d) Approved", requestNumber));
                if(ajeFile != null && !ajeFile.trim().isEmpty()) {
                    subjects.add(String.format(" MJE Record Catalogue - Sweep/AJE added for Journal Identifier Request (#%d)", requestNumber));
                }
                break;
            case "Rejected":
                subjects.add(String.format("MJE Record Catalogue - Journal Identifier Request (#%d) Rejected", requestNumber));
                break;
            case "Cancelled":
                subjects.add(String.format("MJE Record Catalogue - Journal Identifier Request (#%d) Cancelled", requestNumber));
                break;
            case "Deleted":
                subjects.add(String.format("MJE Record Catalogue - Journal Identifier Request (#%d) Deleted", requestNumber));
                break;
            case "Pending":
                if(approverStatus != null && approverStatus.equals("Pending")) {
                    subjects.add("Not a valid scenario for sending email");
                } else if(requestType.equals("New")) {
                    subjects.add(String.format("MJE Record Catalogue - New Journal Identifier Request (#%d) Created", requestNumber));
                } else if (requestType.equals("Modify")){
                    subjects.add(String.format("MJE Record Catalogue - Journal Identifier Request (#%d) Modified", requestNumber));
                } else {
                    subjects.add("Not a valid scenario for sending email");
                }
                break;
        }
        return subjects;
    }

    public String buildEmailRecipientListForApprovers(RecordInfoResponse request) {
        List<String> recipientList = buildRecipientList(request.getAdminUser(), request.getApproverUser1(), request.getApproverUser2());
        recipientList.add(mailTo);
        return StringUtils.join(recipientList, SEPARATOR);
    }

    public String buildEmailRecipientListForRequester(RecordInfoResponse record) {
        return record.getUserld() + DOMAIN;
    }

    public String buildEmailRecipientListForAllPartiesInvolved(RecordInfoResponse request) {
        return StringUtils.join(buildRecipientList(request.getAdminUser(), request.getApproverUser1(), request.getApproverUser2(), request.getUserld()), SEPARATOR);
    }

    private List<String> buildRecipientList(String... users) {
        return Arrays.stream(users)
                .filter(user -> user != null && !user.isEmpty())
                .map(user -> user + DOMAIN)
                .collect(Collectors.toList());
    }

    public Map<String, String> parseUserList(String userList) {
        if(userList == null || userList.isEmpty()) {
            throw new IllegalArgumentException("UserList must not be null or empty");
        }
        Map<String, String> map = new HashMap<>();
        userList = userList.substring(1, userList.length()-1);
        String[] users = userList.split(", ");

        for(String user : users) {
            String[] parts = user.split(" - ");
            if(parts.length == 2) {
                map.put(parts[1], parts[0]);
            } else {
                throw new IllegalArgumentException("User data is in incorrect format");
            }
        }
        return map;
    }
}

