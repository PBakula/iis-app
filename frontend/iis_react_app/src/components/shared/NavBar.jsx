import React from "react";
import { Navbar, Nav, Container } from "react-bootstrap";
import { Link, useNavigate } from "react-router-dom";
import { getAccessToken } from "../../services/authService";

const NavBar = () => {
  const navigate = useNavigate();
  const isLoggedIn = !!getAccessToken();

  const handleLogout = () => {
    // Očisti tokene
    localStorage.removeItem("accessToken");
    localStorage.removeItem("refreshToken");
    // Preusmjeri na login
    navigate("/login");
  };

  return (
    <Navbar bg="light" expand="lg" className="border-bottom">
      <Container>
        <Navbar.Brand>IIS Project</Navbar.Brand>
        <Nav className="ms-auto">
          {isLoggedIn ? (
            <Nav.Link onClick={handleLogout}>Logout</Nav.Link>
          ) : (
            <Nav.Link as={Link} to="/login">
              Login
            </Nav.Link>
          )}
        </Nav>
      </Container>
    </Navbar>
  );
};

export default NavBar;
