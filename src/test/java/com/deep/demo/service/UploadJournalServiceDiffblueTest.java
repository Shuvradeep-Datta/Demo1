package com.deep.demo.service;

import static org.junit.jupiter.api.Assertions.assertTrue;

import com.deep.demo.repository.AdminRepository;
import com.deep.demo.repository.JournalRepository;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ContextConfiguration(classes = {UploadJournalService.class})
@ExtendWith(SpringExtension.class)
class UploadJournalServiceDiffblueTest {
    @MockBean
    private AdminRepository adminRepository;

    @MockBean
    private JournalRepository journalRepository;

    @Autowired
    private UploadJournalService uploadJournalService;

    /**
     * Method under test: {@link UploadJournalService#getParameters(String[])}
     */
//    @Test
//    void testGetParameters2() {
//        assertTrue(
//                ((MapSqlParameterSource) uploadJournalService.getParameters(new String[]{"identifier", "42"})).hasValues());
//    }
}
