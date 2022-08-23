package org.saeta.controller;

//import org.apache.poi.ss.usermodel.Workbook;
import com.aspose.cells.Workbook;
import org.saeta.service.file.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import com.aspose.words.*;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import com.aspose.*;

@RestController
@CrossOrigin
@RequestMapping("files")

public class FileController {
    @Autowired
    FileService fileService;


    @ResponseBody
    @PostMapping("/upload")
    public ResponseEntity uploadFile() throws Exception {
        HashMap<String, Object> responseMap = new HashMap<>();

        License license=new License();
        license.setLicense("C:\\archivos\\SSignerProd.lic");


        Document doc = new Document("C:\\archivos\\cotizacion.docx");
        doc.save("Output.pdf");


        Workbook book = new Workbook("C:\\archivos\\Plan de Trabajo OEFA 2022.xlsx");// guardar XLS como PDF
        book.save("output.pdf");


        responseMap.put("status", "ok");
        responseMap.put("fileName", "Output.pdf");
        return new ResponseEntity(responseMap, HttpStatus.OK);

    }

    @ResponseBody
    @PostMapping("/informatization")
    public ResponseEntity informatizationFile() throws Exception {
        HashMap<String, Object> responseMap = new HashMap<>();

        ValidarPdf val = new ValidarPdf();
        DocumentoPdf doc = val.validar("D:\\Disco D\\Dump0\\firmados\\102.pdf");




        return new ResponseEntity(doc, HttpStatus.OK);

    }
    @ResponseBody
    @PostMapping("/informatization2")

    public ResponseEntity informatization2File(@RequestParam("files")MultipartFile file) throws Exception {
        HashMap<String, Object> responseMap = new HashMap<>();
        ValidarPdf validarPdf = new ValidarPdf();
        DocumentoPdf doc = validarPdf.validar(file.getBytes(),file.getOriginalFilename());






        return new ResponseEntity(doc, HttpStatus.OK);

    }

    @ResponseBody
    @PostMapping("/upload2")

    public ResponseEntity upload2File(@RequestParam("files")MultipartFile file) throws Exception {
        HashMap<String, Object> responseMap = new HashMap<>();
        License license=new License();
        license.setLicense("C:\\archivos\\SSignerProd.lic");

        Document doc = new ValidarPdf();
        doc.save("Output.pdf");












        return new ResponseEntity(doc, HttpStatus.OK);

    }

}
