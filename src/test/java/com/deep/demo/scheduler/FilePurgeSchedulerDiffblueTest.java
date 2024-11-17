package com.deep.demo.scheduler;

import com.deep.demo.service.JournalEntryService;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ContextConfiguration(classes = {FilePurgeScheduler.class})
@ExtendWith(SpringExtension.class)
class FilePurgeSchedulerDiffblueTest {
    @Autowired
    private FilePurgeScheduler filePurgeScheduler;

    @MockBean
    private JournalEntryService journalEntryService;

    /**
     * Method under test: {@link FilePurgeScheduler#syncData()}
     */
    @Test
    @Disabled("TODO: Complete this test")
    void testSyncData() {
        // TODO: Complete this test.
        //   Reason: R027 Missing beans when creating Spring context.
        //   Failed to create Spring context due to missing beans
        //   in the current Spring profile:
        //       com.deep.demo.scheduler.FilePurgeScheduler
        //   See https://diff.blue/R027 to resolve this issue.

        filePurgeScheduler.syncData();
    }
}
