import React, { useState, useRef } from "react";
import { Button, Stack, Form } from "react-bootstrap";
import { validateXML } from "../../services/validationService";
import { FileInput } from "../shared/FileInput";
import { ValidationResult } from "./ValidationResult";

export const XmlValidation = () => {
  const [xmlFile, setXmlFile] = useState(null);
  const [validationType, setValidationType] = useState("xsd");
  const [results, setResults] = useState({ xsd: null, rng: null });
  const fileInputRef = useRef(null);

  const handleFileChange = (file) => {
    setResults({ xsd: null, rng: null });
    setXmlFile(file);
  };

  const handleValidation = async () => {
    if (!xmlFile) {
      alert("Molimo uploadajte XML datoteku prije validacije.");
      return;
    }

    try {
      // Resetirajmo rezultate za odabrani tip validacije
      setResults((prev) => ({
        ...prev,
        [validationType]: null,
      }));

      const result = await validateXML(xmlFile, validationType);

      setResults((prev) => ({
        ...prev,
        [validationType]: result,
      }));
    } catch (error) {
      setResults((prev) => ({
        ...prev,
        [validationType]: {
          message: error.message,
          xmlContent: null,
          isError: true,
        },
      }));
    } finally {
      // Očisti input polje nakon validacije, bez obzira na ishod
      setXmlFile(null);
      const fileInput = document.getElementById("xmlFile");
      if (fileInput) {
        fileInput.value = "";
      }
    }
  };

  return (
    <section className="mb-4">
      <h2 className="h4 mb-3">XML Validacija</h2>

      <FileInput
        id="xmlFile"
        accept=".xml"
        onChange={handleFileChange}
        label="Upload XML datoteke:"
        ref={fileInputRef}
      />

      <Stack direction="horizontal" gap={2} className="mb-3 mt-3">
        <Form.Group className="me-3" style={{ minWidth: "150px" }}>
          <Form.Select
            value={validationType}
            onChange={(e) => setValidationType(e.target.value)}
          >
            <option value="xsd">XSD validacija</option>
            <option value="rng">RNG validacija</option>
          </Form.Select>
        </Form.Group>

        <Button variant="primary" onClick={handleValidation}>
          Validiraj i spremi
        </Button>
      </Stack>

      <ValidationResult results={results} type="xml" />
    </section>
  );
};

export default XmlValidation;
