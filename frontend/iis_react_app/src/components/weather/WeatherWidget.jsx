import React, { useState, useEffect } from "react";
import { Form, Button, Spinner, Overlay, Popover } from "react-bootstrap";
import {
  getTemperature,
  getMatchingCities,
} from "../../services/weatherService";

export const WeatherWidget = () => {
  const [cityName, setCityName] = useState("");
  const [temperature, setTemperature] = useState("");
  const [matchingCities, setMatchingCities] = useState([]);
  const [error, setError] = useState("");
  const [loading, setLoading] = useState(false);
  const [selectedCity, setSelectedCity] = useState("");
  const [show, setShow] = useState(false);
  const [target, setTarget] = useState(null);

  // Referenca za gumb
  const ref = React.useRef(null);

  // Učitajte inicijalne podatke (npr. Zagreb)
  useEffect(() => {
    const loadInitialData = async () => {
      try {
        const temp = await getTemperature("Split");
        setTemperature(temp);
        setSelectedCity("Split");
      } catch (error) {
        console.error("Error loading initial data:", error);
      }
    };

    loadInitialData();
  }, []);

  const handleClick = (event) => {
    setShow(!show);
    setTarget(event.target);
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    setLoading(true);
    setError("");

    try {
      const cities = await getMatchingCities(cityName);
      setMatchingCities(cities);

      if (cities.length === 1) {
        const temp = await getTemperature(cities[0]);
        setTemperature(temp);
        setSelectedCity(cities[0]);
        setShow(false);
      } else if (cities.length > 1) {
        setTemperature("");
        setSelectedCity("");
      } else {
        setError("Nema pronađenih gradova");
      }
    } catch (err) {
      setError(err.message);
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
      setShow(false);
    } catch (err) {
      setError(`Greška: ${err.message}`);
    } finally {
      setLoading(false);
    }
  };

  return (
    <>
      <div
        ref={ref}
        className="weather-widget-mini"
        onClick={handleClick}
        style={{
          position: "fixed",
          right: "20px",
          top: "80px",
          background: "white",
          borderRadius: "50%",
          width: "60px",
          height: "60px",
          display: "flex",
          alignItems: "center",
          justifyContent: "center",
          boxShadow: "0 2px 10px rgba(0,0,0,0.1)",
          cursor: "pointer",
          zIndex: 1000,
          flexDirection: "column",
          padding: "5px",
        }}
      >
        <div style={{ fontSize: "0.8rem", marginBottom: "-2px" }}>
          {selectedCity}
        </div>
        {temperature ? (
          <div style={{ fontWeight: "bold", fontSize: "1.2rem" }}>
            {temperature}
          </div>
        ) : (
          <div>
            <i className="bi bi-cloud" style={{ fontSize: "1.2rem" }}></i>
          </div>
        )}
      </div>

      <Overlay
        show={show}
        target={target}
        placement="bottom"
        container={document.body}
        rootClose={true}
        onHide={() => setShow(false)}
      >
        <Popover style={{ maxWidth: "250px" }}>
          <Popover.Header as="h3">Vremenska prognoza</Popover.Header>
          <Popover.Body>
            <Form onSubmit={handleSubmit} className="mb-2">
              <div className="d-flex">
                <Form.Control
                  type="text"
                  size="sm"
                  value={cityName}
                  onChange={(e) => setCityName(e.target.value)}
                  placeholder="Unesite ime grada"
                  className="me-2"
                />
                <Button
                  type="submit"
                  variant="primary"
                  size="sm"
                  disabled={loading}
                >
                  {loading ? (
                    <Spinner size="sm" />
                  ) : (
                    <i className="bi bi-search"></i>
                  )}
                </Button>
              </div>
            </Form>

            {error && <div className="text-danger small mb-2">{error}</div>}

            {matchingCities.length > 0 && (
              <div>
                <small className="text-muted d-block mb-1">
                  Odaberite grad:
                </small>
                <div className="list-group list-group-flush">
                  {matchingCities.map((city, index) => (
                    <button
                      key={index}
                      className="list-group-item list-group-item-action py-1 small"
                      onClick={() => handleCitySelect(city)}
                    >
                      {city}
                    </button>
                  ))}
                </div>
              </div>
            )}
          </Popover.Body>
        </Popover>
      </Overlay>
    </>
  );
};

export default WeatherWidget;
