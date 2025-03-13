// components/validation/CsvValidation.jsx
import React, { useState } from "react";
import { Form, Button } from "react-bootstrap";
import { validateCSV } from "../../services/validationService";
import { FileInput } from "../shared/FileInput";
import { ValidationResult } from "./ValidationResult";

export const CsvValidation = () => {
  const [csvFile, setCsvFile] = useState(null);
  const [validationType, setValidationType] = useState("xsd");
  const [result, setResult] = useState("");

  const handleValidation = async () => {
    if (!csvFile) {
      alert("Molimo uploadajte CSV datoteku prije validacije.");
      return;
    }

    try {
      const validationResult = await validateCSV(csvFile, validationType);
      setResult(validationResult);
    } catch (error) {
      setResult(error.message);
    }
  };

  return (
    <section className="mb-4">
      <h2 className="h4 mb-3">CSV Validacija</h2>

      <FileInput
        id="csvFile"
        accept=".csv"
        onChange={(file) => setCsvFile(file)}
        label="Upload CSV datoteke:"
      />

      <Form.Group className="mb-3">
        <Form.Select
          value={validationType}
          onChange={(e) => setValidationType(e.target.value)}
        >
          <option value="xsd">XSD</option>
          <option value="rng">RNG</option>
        </Form.Select>
      </Form.Group>

      <Button variant="primary" onClick={handleValidation} className="mb-3">
        Validiraj CSV
      </Button>

      <ValidationResult results={{ csv: result }} type="csv" />
    </section>
  );
};

export default CsvValidation;
