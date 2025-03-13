import { useState } from "react";
import reactLogo from "./assets/react.svg";
import viteLogo from "/vite.svg";
import "./App.css";
import { BrowserRouter as Router, Routes, Route } from "react-router-dom";
import { Navigate, useLocation } from "react-router-dom";
import ValidationPage from "./pages/ValidationPage";
import LoginPage from "./pages/LoginPage";
import ProtectedRoute from "./components/auth/ProtectedRoute";
import NavBar from "./components/shared/NavBar";
import WeatherWidget from "./components/weather/WeatherWidget";
import "./index.css";

const AppLayout = ({ children }) => {
  const location = useLocation();
  const isLoginPage = location.pathname === "/login";

  return (
    <div>
      {!isLoginPage && <NavBar />}
      {children}
    </div>
  );
};

function App() {
  return (
    <Router>
      <AppLayout>
        <Routes>
          {/* Root redirect */}
          <Route path="/" element={<Navigate to="/login" replace />} />

          {/* Javne rute */}
          <Route path="/login" element={<LoginPage />} />

          {/* Zaštićene rute */}
          <Route
            path="/validation"
            element={
              <ProtectedRoute>
                <ValidationPage />
              </ProtectedRoute>
            }
          />
        </Routes>
      </AppLayout>
    </Router>
  );
}

export default App;
