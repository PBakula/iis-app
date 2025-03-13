package hr.algebra.iisProject.controller.soap.response;


import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "validateXmlResponse", namespace = "http://interoperabilnost.hr/students")
@XmlAccessorType(XmlAccessType.FIELD)
public class ValidateXmlResponse {

    @XmlElement(namespace = "http://interoperabilnost.hr/students")
    private boolean valid;

    @XmlElement(namespace = "http://interoperabilnost.hr/students")
    private String message;

    public boolean isValid() {
        return valid;
    }

    public void setValid(boolean valid) {
        this.valid = valid;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
