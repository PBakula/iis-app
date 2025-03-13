import React from "react";
import { Container, Row, Col, Card } from "react-bootstrap";
import { XmlValidation } from "../components/validation/XmlUpload";
import { StudentSearch } from "../components/student/StudentSearch";
import { WeatherSearch } from "../components/weather/WeatherSearch";
import { XPathSearch } from "../components/student/XPathSearch";
import { WeatherWidget } from "../components/weather/WeatherWidget";

const ValidationPage = () => {
  return (
    <div className="min-vh-100 bg-light">
      <Container className="py-4">
        <Row>
          <Col md={12}>
            <Card className="mb-4">
              <Card.Body>
                <XmlValidation />
              </Card.Body>
            </Card>

            <Card className="mb-4">
              <Card.Body>
                <div className="d-flex flex-column gap-4">
                  <StudentSearch />
                  <hr />
                  <XPathSearch />
                </div>
              </Card.Body>
            </Card>

            <Card className="mb-4">
              <Card.Body>
                <WeatherSearch />
              </Card.Body>
            </Card>
          </Col>
        </Row>
      </Container>
    </div>
  );
};

export default ValidationPage;
