import React, { useState } from "react";
import { Form, Button, Alert } from "react-bootstrap";
import { searchStudentsByGpa } from "../../services/studentService";

export const StudentSearch = () => {
  const [gpa, setGpa] = useState("");
  const [generatedXml, setGeneratedXml] = useState("");
  const [searchError, setSearchError] = useState("");
  const [loading, setLoading] = useState(false);

  const handleSearch = async (e) => {
    e.preventDefault();
    setLoading(true);
    setSearchError("");

    try {
      const result = await searchStudentsByGpa(gpa);
      setGeneratedXml(result);
      setLoading(false);
    } catch (err) {
      setSearchError(err.message);
      setLoading(false);
    }
  };

  return (
    <section className="mb-4">
      <h2 className="h4 mb-3">Pretraga studenata po GPA</h2>

      <Form onSubmit={handleSearch} className="mb-3">
        <Form.Group className="mb-3">
          <Form.Label>Unesite GPA:</Form.Label>
          <Form.Control
            type="number"
            step="0.1"
            id="gpa"
            value={gpa}
            onChange={(e) => setGpa(e.target.value)}
            required
          />
        </Form.Group>
        <Button type="submit" variant="primary" disabled={loading}>
          {loading ? "Pretraživanje..." : "Pretraži"}
        </Button>
      </Form>

      {searchError && (
        <Alert variant="danger" className="mb-3">
          {searchError}
        </Alert>
      )}

      {generatedXml && (
        <div className="mb-3">
          <h3 className="h5 mb-2">Generirani XML</h3>
          <Form.Control
            as="textarea"
            value={generatedXml}
            readOnly
            style={{ height: "300px", fontFamily: "monospace" }}
          />
        </div>
      )}
    </section>
  );
};

export default StudentSearch;
