import React, { useState } from "react";
import { Form, Button, Alert, Accordion, Card } from "react-bootstrap";
import { filterByXPath } from "../../services/studentService";

// Primjeri XPath upita koje ćemo prikazati
const XPATH_EXAMPLES = [
  {
    name: "Svi studenti",
    expression: "//student",
    description: "Pronalazi sve student elemente",
  },
  {
    name: "Studenti s visokim GPA",
    expression: "//student[gpa > 4.0]",
    description: "Pronalazi studente s GPA većim od 4.0",
  },
  {
    name: "Studenti s visokim stresom",
    expression: "//student[stressLevel='high']",
    description: "Pronalazi studente s visokom razinom stresa",
  },
  {
    name: "Malo sna, dobar GPA",
    expression: "//student[sleepHoursPerDay < 6 and gpa > 3.5]",
    description: "Studenti koji malo spavaju ali imaju dobar GPA",
  },
  {
    name: "Društveni studenti",
    expression: "//student[socialHoursPerDay > 3]",
    description:
      "Studenti koji provode više od 3 sata dnevno na društvene aktivnosti",
  },
];

export const XPathSearch = () => {
  const [xpathExpression, setXpathExpression] = useState("");
  const [searchResults, setSearchResults] = useState("");
  const [error, setError] = useState("");
  const [loading, setLoading] = useState(false);

  const handleSearch = async (e) => {
    e.preventDefault();

    if (!xpathExpression.trim()) {
      setError("Unesite XPath izraz za pretraživanje");
      return;
    }

    setLoading(true);
    setError("");
    setSearchResults("");

    try {
      const result = await filterByXPath(xpathExpression);
      setSearchResults(result);
      setLoading(false);
    } catch (err) {
      setError(err.message);
      setLoading(false);
    }
  };

  const applyXPathExample = (expression) => {
    setXpathExpression(expression);
  };

  return (
    <section className="mb-4">
      <h2 className="h4 mb-3">XPath Pretraživanje Studenata</h2>

      <Form onSubmit={handleSearch} className="mb-3">
        <Form.Group className="mb-3">
          <Form.Label>XPath izraz:</Form.Label>
          <Form.Control
            type="text"
            value={xpathExpression}
            onChange={(e) => setXpathExpression(e.target.value)}
            placeholder="Unesite XPath izraz (npr. //student[gpa > 4.0])"
            required
          />
        </Form.Group>
        <Button type="submit" variant="primary" disabled={loading}>
          {loading ? "Pretraživanje..." : "Pretraži pomoću XPath"}
        </Button>
      </Form>

      <Accordion className="mb-3">
        <Accordion.Item eventKey="0">
          <Accordion.Header>Pomoć za XPath upite</Accordion.Header>
          <Accordion.Body>
            <p>
              XPath je jezik za navigaciju kroz XML dokumente. Evo nekoliko
              primjera upita:
            </p>
            <div className="row">
              {XPATH_EXAMPLES.slice(0, 4).map((example, index) => (
                <div key={index} className="col-lg-3 col-md-6 col-sm-12 mb-3">
                  <Card className="h-100">
                    <Card.Header>{example.name}</Card.Header>
                    <Card.Body>
                      <Card.Text>{example.description}</Card.Text>
                      <code className="d-block mb-2">{example.expression}</code>
                      <Button
                        variant="outline-secondary"
                        size="sm"
                        onClick={() => applyXPathExample(example.expression)}
                      >
                        Primijeni
                      </Button>
                    </Card.Body>
                  </Card>
                </div>
              ))}
            </div>
          </Accordion.Body>
        </Accordion.Item>
      </Accordion>

      {error && (
        <Alert variant="danger" className="mb-3">
          {error}
        </Alert>
      )}

      {searchResults && (
        <div className="mb-3">
          <h3 className="h5 mb-2">Rezultati pretraživanja</h3>
          <Form.Control
            as="textarea"
            value={searchResults}
            readOnly
            style={{ height: "300px", fontFamily: "monospace" }}
          />
        </div>
      )}
    </section>
  );
};

export default XPathSearch;
