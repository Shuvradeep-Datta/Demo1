package com.deep.demo.scheduler;


import com.deep.demo.service.JournalEntryService;
import com.deep.demo.constants.JournalEntryConstants;
import lombok.RequiredArgsConstructor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.Duration;
import java.time.Instant;
import java.util.Arrays;
import java.util.Objects;

import static com.deep.demo.constants.JournalEntryConstants.*;

@Service
@Slf4j
@RequiredArgsConstructor
@ConditionalOnProperty(prefix = "purgeData",name = "enabled", havingValue = "true")
public class FilePurgeScheduler {



    @Value("${file.tmp.path}")
    private String tmpPath;


    private  final JournalEntryService journalEntryService;


    @Scheduled(cron = "${purgeData.cron}")
    public void syncData(){

        log.info("===================================================");
        Arrays.stream(Objects.requireNonNull(Paths.get(tmpPath).toFile().listFiles())).filter(files ->files.getName().startsWith(SUCCESS_REPORT)|| files.getName().startsWith(ERROR_REPORT)
                ||(files.getName().startsWith(DOWNLOAD_CATALOG_REPORT))).filter(f->{
                    try {
                        return Duration.between(Files.getLastModifiedTime(f.toPath()).toInstant(), Instant.now()).toMinutes()>=30;
                    }catch (IOException e){
                        log.error("IO Exception Occured for the file" +f.getName());
                    }
                    return false;
        }).forEach(file -> journalEntryService.deleteFile(file.getName()));
    }




}
