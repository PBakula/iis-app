import { apiClient } from "./apiClient";

const baseURL = import.meta.env.PROD ? "/ws" : "http://localhost:8087/ws";

export const searchStudentsByGpa = async (gpa) => {
  const soapRequest = `
        <soapenv:Envelope xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/" xmlns:stud="http://interoperabilnost.hr/students">
            <soapenv:Header/>
            <soapenv:Body>
                <stud:searchByGpaRequest>
                    <stud:gpa>${gpa}</stud:gpa>
                </stud:searchByGpaRequest>
            </soapenv:Body>
        </soapenv:Envelope>`;

  try {
    const response = await apiClient(`${baseURL}`, {
      method: "POST",
      headers: { "Content-Type": "text/xml" },
      body: soapRequest,
    });

    if (!response.ok) throw new Error(await response.text());

    const soapResponse = await response.text();
    return extractXmlContentFromSoap(soapResponse);
  } catch (error) {
    throw new Error(`GPA Search Error: ${error.message}`);
  }
};

export const filterByXPath = async (xpathExpression) => {
  try {
    // Pripremi SOAP request envelope - šaljemo samo XPath izraz
    const soapRequest = `
      <soapenv:Envelope xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/"
                       xmlns:stud="http://interoperabilnost.hr/students">
        <soapenv:Header/>
        <soapenv:Body>
          <stud:filterByXPathRequest>
            <stud:xpathExpression>${xpathExpression}</stud:xpathExpression>
          </stud:filterByXPathRequest>
        </soapenv:Body>
      </soapenv:Envelope>
    `;

    const response = await apiClient(`${baseURL}`, {
      method: "POST",
      headers: {
        "Content-Type": "text/xml",
      },
      body: soapRequest,
    });

    if (!response.ok) throw new Error(await response.text());

    const soapResponse = await response.text();
    return extractXmlContentFromSoap(soapResponse);
  } catch (error) {
    throw new Error(`XPath Filter Error: ${error.message}`);
  }
};

export const validateStudentXml = async (xmlContent) => {
  const escapedXml = `<![CDATA[${xmlContent}]]>`;

  const soapRequest = `
        <soapenv:Envelope xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/" xmlns:stud="http://interoperabilnost.hr/students">
            <soapenv:Header/>
            <soapenv:Body>
                <stud:validateXmlRequest>
                    <stud:xml>${escapedXml}</stud:xml>
                </stud:validateXmlRequest>
            </soapenv:Body>
        </soapenv:Envelope>`;

  try {
    const response = await apiClient(`${baseURL}`, {
      method: "POST",
      headers: { "Content-Type": "text/xml" },
      body: soapRequest,
    });

    if (!response.ok) throw new Error(await response.text());

    const soapResponse = await response.text();
    const parser = new DOMParser();
    const xmlDoc = parser.parseFromString(soapResponse, "application/xml");

    return {
      message:
        xmlDoc.getElementsByTagName("ns2:message")[0]?.textContent ||
        "Nepoznata greška.",
      isValid:
        xmlDoc.getElementsByTagName("ns2:valid")[0]?.textContent === "true",
    };
  } catch (error) {
    throw new Error(`Validation Error: ${error.message}`);
  }
};

// Helper funkcije možeš staviti u utils/xmlParser.js, ali za sad ih ostavljam ovdje
const extractXmlContentFromSoap = (soapResponse) => {
  const parser = new DOMParser();
  const xmlDoc = parser.parseFromString(soapResponse, "application/xml");
  const xmlContentNode = xmlDoc.getElementsByTagName("ns2:xmlContent")[0];
  return xmlContentNode
    ? xmlContentNode.textContent
    : "XML content not found in SOAP response.";
};
