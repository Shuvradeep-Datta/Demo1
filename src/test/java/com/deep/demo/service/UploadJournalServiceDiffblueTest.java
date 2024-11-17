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
    @Test
    @Disabled("TODO: Complete this test")
    void testGetParameters() {
        // TODO: Complete this test.
        //   Reason: R013 No inputs found that don't throw a trivial exception.
        //   Diffblue Cover tried to run the arrange/act section, but the method under
        //   test threw
        //   java.lang.ArrayIndexOutOfBoundsException: Index 1 out of bounds for length 1
        //       at com.deep.demo.service.UploadJournalService.getParameters(UploadJournalService.java:122)
        //   See https://diff.blue/R013 to resolve this issue.

        uploadJournalService.getParameters(new String[]{"Line"});
    }

    /**
     * Method under test: {@link UploadJournalService#getParameters(String[])}
     */
    @Test
    void testGetParameters2() {
        assertTrue(
                ((MapSqlParameterSource) uploadJournalService.getParameters(new String[]{"identifier", "42"})).hasValues());
    }
}
