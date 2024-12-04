package com.deep.demo.service;

import com.microsoft.azure.storage.CloudStorageAccount;
import com.microsoft.azure.storage.OperationContext;
import com.microsoft.azure.storage.StorageException;
import com.microsoft.azure.storage.blob.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.InvalidKeyException;
@Service
@Slf4j
@RequiredArgsConstructor

public class FileUploadService {


    @Value("${mje.blobstorage.connectionstring}")
    private String storageConnectionString;

    @Value("${envName}")
    private String envName;
    public String fileUpload(MultipartFile multipartFile,int requestNo) throws IOException {
        log.info("FileStorageService.fileUpload() " + multipartFile.getSize());
        String fileName = multipartFile.getOriginalFilename();
        byte[] bytes = multipartFile.getBytes();
        //String newfilename = "MJE_"+requestNo+".csv";
        log.info("FileStorage before write " + fileName);
        //Files.write(Paths.get(newfilename), bytes);

        //log.info("FileStorage after write " + newfilename);

        try {


            log.info("FileStorageService.fileUpload() fileSize {}", multipartFile.getSize());
            uploadFile(multipartFile,requestNo,fileName);


        } catch (Exception e) {
            return "not ok ";
        } finally {
            log.info("FileStorageService.fileUpload()  Finally block");
            multipartFile = null;
        }


        return "ok" ;

    }

    private URI uploadFile(MultipartFile multipartFile, int requestNo ,String fileNameNew)
            throws InvalidKeyException, URISyntaxException, StorageException, IOException {

        String containerName = envName + "-" +requestNo;
        log.info("ContainerName",containerName);
        log.info("FileStorageService.uploadFile() Container Name {} ", containerName);

        log.debug("FileStorageService.uploadFile() storageConnectionString {}", storageConnectionString);
        createContainer(containerName, storageConnectionString);
        String filename = StringUtils.cleanPath(multipartFile.getOriginalFilename());
        filename = filename.toLowerCase().replaceAll(" ", "-");

        CloudBlockBlob blob = this.cloudBlobContainer(containerName, storageConnectionString).getBlockBlobReference(fileNameNew);
        blob.upload(multipartFile.getInputStream(), -1);

        log.info("FileStorageService.uploadFile() blob.getUri() " + blob.getUri());

        return blob.getUri();
    }
    private boolean createContainer(String containerName, String connectionString)
            throws InvalidKeyException, URISyntaxException, StorageException {
        log.info("FileStorageService.createContainer() start {} {}", containerName, connectionString);
        CloudBlobContainer container = this.cloudBlobClient(storageConnectionString)
                .getContainerReference(containerName.trim());
        log.info("FileStorageService.createContainer() Created Container {}", containerName.trim());
        return container.createIfNotExists(new BlobRequestOptions(), new OperationContext());
    }
    private CloudBlobClient cloudBlobClient(String connectionString)
            throws URISyntaxException, StorageException, InvalidKeyException {

        CloudStorageAccount storageAccount = CloudStorageAccount.parse(connectionString);

        return storageAccount.createCloudBlobClient();
    }

    private CloudBlobContainer cloudBlobContainer(String containerName, String connectionString)
            throws URISyntaxException, StorageException, InvalidKeyException {
        //LOGGER.info("FileStorageService.cloudBlobContainer() start {}", containerName);
        CloudBlobContainer blobContainer = cloudBlobClient(connectionString).getContainerReference(containerName);
        blobContainer.createIfNotExists(BlobContainerPublicAccessType.CONTAINER, new BlobRequestOptions(),
                new OperationContext());
        //LOGGER.info("FileStorageService.cloudBlobContainer(end start {}", containerName);
        return blobContainer;
    }


    public byte[] loadfilefromURI(String requestNo , String fileName)
            throws InvalidKeyException, URISyntaxException, StorageException {
        log.info(" FileStorageService.loadfilefromURI() start ");
        String containerName = envName + "-" +requestNo;
        CloudBlobContainer blobContainerClient = this.cloudBlobClient(storageConnectionString)
                .getContainerReference(containerName);
        log.info(" FileStorageService.loadfilefromURI() containername {}", containerName);
        CloudBlob cloudBlob = blobContainerClient.getBlobReferenceFromServer(fileName);
        int dataSize = (int) cloudBlob.getProperties().getLength();

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream(dataSize);
        cloudBlob.download(outputStream);
        return outputStream.toByteArray();

    }
}

