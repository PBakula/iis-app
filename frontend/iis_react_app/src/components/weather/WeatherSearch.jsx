import React, { useState } from "react";
import { Form, Button, Alert, ListGroup, Spinner } from "react-bootstrap";
import {
  getTemperature,
  getMatchingCities,
} from "../../services/weatherService";

export const WeatherSearch = () => {
  const [cityName, setCityName] = useState("");
  const [temperature, setTemperature] = useState("");
  const [matchingCities, setMatchingCities] = useState([]);
  const [error, setError] = useState("");
  const [loading, setLoading] = useState(false);
  const [selectedCity, setSelectedCity] = useState("");

  const handleSubmit = async (e) => {
    e.preventDefault();
    setLoading(true);
    setError("");

    try {
      // Dohvaćanje gradova koji odgovaraju pretrazi
      const cities = await getMatchingCities(cityName);
      setMatchingCities(cities);

      // Ako je pronađen točno jedan grad, odmah dohvati temperaturu
      if (cities.length === 1) {
        const temp = await getTemperature(cities[0]);
        setTemperature(temp);
        setSelectedCity(cities[0]);
      } else if (cities.length > 1) {
        // Resetiramo temperaturu ako ima više gradova
        setTemperature("");
        setSelectedCity("");
      } else {
        // Ako nema rezultata
        setError("No matching cities found");
        setTemperature("");
        setSelectedCity("");
      }
    } catch (err) {
      setError(err.message);
      setTemperature("");
      setSelectedCity("");
    } finally {
      setLoading(false);
    }
  };

  const handleCitySelect = async (city) => {
    setSelectedCity(city);
    setLoading(true);
    try {
      const temp = await getTemperature(city);
      setTemperature(temp);
      setError("");
    } catch (err) {
      setError(`Error fetching temperature for ${city}: ${err.message}`);
      setTemperature("");
    } finally {
      setLoading(false);
    }
  };

  return (
    <section className="mb-4">
      <h2 className="h4 mb-3">Podaci o vremenu</h2>

      <Form onSubmit={handleSubmit} className="mb-3">
        <Form.Group className="mb-3">
          <Form.Label>Unesite ime grada:</Form.Label>
          <Form.Control
            type="text"
            id="cityName"
            value={cityName}
            onChange={(e) => setCityName(e.target.value)}
            placeholder="npr. Zagreb, Split"
            required
          />
          <Form.Text className="text-muted">
            Unesite djelomično ili puno ime grada za pretraživanje
          </Form.Text>
        </Form.Group>
        <Button type="submit" variant="primary" disabled={loading}>
          {loading ? (
            <>
              <Spinner
                as="span"
                animation="border"
                size="sm"
                role="status"
                aria-hidden="true"
                className="me-2"
              />
              Pretraživanje...
            </>
          ) : (
            "Pretraži"
          )}
        </Button>
      </Form>

      {error && <Alert variant="danger">{error}</Alert>}

      {matchingCities.length > 0 && (
        <div className="mb-3">
          <h3 className="h5 mb-2">Pronađeni gradovi</h3>
          <ListGroup>
            {matchingCities.map((city, index) => (
              <ListGroup.Item
                key={index}
                action
                onClick={() => handleCitySelect(city)}
                active={city === selectedCity}
              >
                {city}
              </ListGroup.Item>
            ))}
          </ListGroup>
        </div>
      )}

      {temperature && (
        <div className="mb-3">
          <h3 className="h5 mb-2">Temperatura</h3>
          <Alert variant="info">
            Temperatura u gradu {selectedCity}: {temperature}
          </Alert>
        </div>
      )}
    </section>
  );
};

export default WeatherSearch;
