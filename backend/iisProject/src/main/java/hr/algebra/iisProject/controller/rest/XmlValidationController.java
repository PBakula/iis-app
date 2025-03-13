package hr.algebra.iisProject.controller.rest;

import hr.algebra.iisProject.dto.StudentsDataDTO;
import hr.algebra.iisProject.model.ValidationType;
import hr.algebra.iisProject.service.StudentDataService;
import hr.algebra.iisProject.utils.ValidationXML;
import jakarta.xml.bind.JAXBException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/xml")
public class XmlValidationController {

    public static final Logger logger = LoggerFactory.getLogger(XmlValidationController.class);


    private final StudentDataService studentDataService;

    public XmlValidationController(StudentDataService studentDataService) {
        this.studentDataService = studentDataService;
    }


    @PostMapping("/validateAndSave")
    public ResponseEntity<Map<String, String>> validateAndSaveXml(
            @RequestBody String xmlContent,
            @RequestParam(value = "validationType", defaultValue = "xsd") String validationType) {
        try {
            ValidationType type = ValidationType.valueOf(validationType.toUpperCase());

            studentDataService.validateAndSave(xmlContent, type);

            Map<String, String> response = new HashMap<>();
            response.put("message", "XML je validan.");
            response.put("xmlContent", xmlContent);

            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            Map<String, String> response = new HashMap<>();
            response.put("message", e.getMessage());
            response.put("xmlContent", xmlContent);

            return ResponseEntity.badRequest().body(response);
        } catch (RuntimeException e) {
            Map<String, String> response = new HashMap<>();
            response.put("message", "Greška: " + e.getMessage());
            response.put("xmlContent", xmlContent);

            return ResponseEntity.status(500).body(response);
        }

    }
}