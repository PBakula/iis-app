package hr.algebra.iisProject.controller.soap.request;


import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "filterByXPathRequest", namespace = "http://interoperabilnost.hr/students")
@XmlAccessorType(XmlAccessType.FIELD)
public class FilterByXPathRequest {

    @XmlElement(namespace = "http://interoperabilnost.hr/students")
    private String xpathExpression;

    public String getXPathExpression() {
        return xpathExpression;
    }

    public void setXPathExpression(String xpathExpression) {
        this.xpathExpression = xpathExpression;
    }
}
