package com.vae.account.rest;

import com.vae.account.enums.TypeEnums;
import com.vae.account.service.ExcelService;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;

/**
 * @author vae
 */
@RestController
@RequestMapping("/excel")
public class ExcelController {

    @Autowired
    private ExcelService excelService;

    @PostMapping("/merge")
    public ResponseEntity<byte[]> merge(@RequestParam("sourceFile") MultipartFile sourceFile
            , @RequestParam("targetFile") MultipartFile targetFile
            , @RequestParam("type") TypeEnums excelConvertType
            , @RequestParam("sourceSheetName") String sourceSheetName
            , @RequestParam("targetSheetName") String targetSheetName) throws UnsupportedEncodingException {
        Workbook targetWorkbook = excelService.merge(excelConvertType, sourceFile, targetFile, sourceSheetName, targetSheetName);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentDispositionFormData("fileName", targetFile.getOriginalFilename());
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        headers.set(HttpHeaders.ACCEPT_CHARSET, StandardCharsets.UTF_8.name());
        try {
            ByteArrayOutputStream byteOutPutStream = new ByteArrayOutputStream();
            targetWorkbook.write(byteOutPutStream);
            byte[] bytes;
            bytes = byteOutPutStream.toByteArray();
            return new ResponseEntity<>(bytes, headers, HttpStatus.OK);
        } catch (IOException e) {
            e.printStackTrace();
            return new ResponseEntity<>(null, headers, HttpStatus.BAD_REQUEST);
        }
    }

}
