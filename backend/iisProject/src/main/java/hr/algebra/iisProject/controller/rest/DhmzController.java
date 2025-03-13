package hr.algebra.iisProject.controller.rest;

import hr.algebra.iisProject.service.XmlRpcService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/weather")
public class DhmzController {

    private final XmlRpcService xmlRpcService;

    public DhmzController(XmlRpcService xmlRpcService) {
        this.xmlRpcService = xmlRpcService;
    }


    @GetMapping("/temperature")
    public ResponseEntity<String> getTemperature(@RequestParam String cityName) {
        try {
            System.out.println("Received request for temperature: " + cityName);
            String temperature = xmlRpcService.getTemperature(cityName);
            System.out.println("Temperature response: " + temperature);
            return ResponseEntity.ok(temperature);
        } catch (Exception e) {
            System.err.println("Error while fetching temperature: " + e.getMessage());
            return ResponseEntity.status(500).body("Error while fetching temperature.");
        }
    }

    @GetMapping("/cities")
    public ResponseEntity<List<String>> getMatchingCities(@RequestParam String cityName) {
        try {
            System.out.println("Received request for matching cities: " + cityName);
            List<String> cities = xmlRpcService.getMatchingCities(cityName);
            System.out.println("Matching cities response: " + cities);
            return ResponseEntity.ok(cities);
        } catch (Exception e) {
            System.err.println("Error while fetching cities: " + e.getMessage());
            return ResponseEntity.status(500).body(null);
        }
    }
}
