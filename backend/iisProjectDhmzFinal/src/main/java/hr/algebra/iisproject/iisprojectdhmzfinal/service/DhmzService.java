package hr.algebra.iisproject.iisprojectdhmzfinal.service;

import org.springframework.stereotype.Service;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class DhmzService {

    private static final String DHMZ_URL = "https://vrijeme.hr/hrvatska_n.xml";


    public String getTemperature(String cityName) {
        System.out.println("Received request for temperature: " + cityName);
        try {
            Document document = fetchXml();
            NodeList cities = document.getElementsByTagName("Grad");

            for (int i = 0; i < cities.getLength(); i++) {
                Element city = (Element) cities.item(i);


                Node nameNode = city.getElementsByTagName("GradIme").item(0);
                Node tempNode = city.getElementsByTagName("Temp").item(0);

                if (nameNode != null && tempNode != null) {
                    String name = nameNode.getTextContent();
                    String temp = tempNode.getTextContent();

                    if (name.equalsIgnoreCase(cityName)) {
                        System.out.println("Returning temperature for " + cityName + ": " + temp);
                        return temp + "°C";
                    }
                }
            }
            System.out.println("City not found: " + cityName);
            return "Grad nije pronađen.";
        } catch (Exception e) {
            System.err.println("Error while fetching temperature: " + e.getMessage());
            return "Greška pri dohvaćanju podataka.";
        }
    }

    public List<String> getMatchingCities(String cityName) {
        System.out.println("Received request for matching cities: " + cityName);
        List<String> matchingCities = new ArrayList<>();
        try {
            Document document = fetchXml();
            NodeList cities = document.getElementsByTagName("Grad");

            for (int i = 0; i < cities.getLength(); i++) {
                Element city = (Element) cities.item(i);

                Node nameNode = city.getElementsByTagName("GradIme").item(0);

                if (nameNode != null) {
                    String name = nameNode.getTextContent();

                    if (name.toLowerCase().contains(cityName.toLowerCase())) {
                        matchingCities.add(name);
                    }
                }
            }

            System.out.println("Returning matching cities: " + matchingCities);
            return matchingCities;
        } catch (Exception e) {
            System.err.println("Error while fetching matching cities: " + e.getMessage());
            matchingCities.add("Greška pri dohvaćanju podataka.");
            return matchingCities;
        }
    }

    // Metoda za dohvaćanje i parsiranje XML-a
    private Document fetchXml() throws Exception {
        System.out.println("Fetching XML from DHMZ...");
        URL url = new URL("https://vrijeme.hr/hrvatska_n.xml"); // Zamijeni s točnim URL-om
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        try (InputStream stream = url.openStream()) {
            // Ispis dohvaćenog XML-a
            String xmlContent = new BufferedReader(new InputStreamReader(stream))
                    .lines()
                    .collect(Collectors.joining("\n"));
            System.out.println("Fetched XML content:\n" + xmlContent);

            // Parsiraj XML
            return builder.parse(new ByteArrayInputStream(xmlContent.getBytes(StandardCharsets.UTF_8)));
        } catch (Exception e) {
            System.err.println("Error fetching XML: " + e.getMessage());
            throw e;
        }
    }
}
