package hr.algebra.iisProject.controller.soap.response;


import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "searchByGpaResponse", namespace = "http://interoperabilnost.hr/students")
@XmlAccessorType(XmlAccessType.FIELD)
public class SearchByGpaResponse {

    @XmlElement(namespace = "http://interoperabilnost.hr/students")
    private String xmlContent;

    public String getXmlContent() {
        return xmlContent;
    }

    public void setXmlContent(String xmlContent) {
        this.xmlContent = xmlContent;
    }
}
