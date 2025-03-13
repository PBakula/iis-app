import React from "react";
import { Alert, Form } from "react-bootstrap";

export const ValidationResult = ({ results, type }) => {
  if (type === "xml" && !results.xsd && !results.rng) return null;

  return (
    <div className="mt-3">
      <h3 className="h5 mb-3">Rezultati validacije:</h3>

      {type === "xml" && (
        <>
          {results.xsd && (
            <div className="mb-4">
              <Alert
                variant={
                  results.xsd.message.includes("Greška") ? "danger" : "success"
                }
                className="mb-2"
              >
                <strong>XSD Validacija:</strong> {results.xsd.message}
              </Alert>

              {results.xsd.xmlContent && (
                <Form.Group className="mb-3">
                  <Form.Label>
                    <strong>Sadržaj XML-a:</strong>
                  </Form.Label>
                  <Form.Control
                    as="textarea"
                    rows={10}
                    value={results.xsd.xmlContent}
                    readOnly
                  />
                </Form.Group>
              )}
            </div>
          )}

          {results.rng && (
            <div className="mb-4">
              <Alert
                variant={
                  results.rng.message.includes("Greška") ? "danger" : "success"
                }
                className="mb-2"
              >
                <strong>RNG Validacija:</strong> {results.rng.message}
              </Alert>

              {results.rng.xmlContent && (
                <Form.Group className="mb-3">
                  <Form.Label>
                    <strong>Sadržaj XML-a:</strong>
                  </Form.Label>
                  <Form.Control
                    as="textarea"
                    rows={10}
                    value={results.rng.xmlContent}
                    readOnly
                  />
                </Form.Group>
              )}
            </div>
          )}
        </>
      )}
    </div>
  );
};

export default ValidationResult;
