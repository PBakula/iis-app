import { Navigate } from "react-router-dom";
import { getAccessToken } from "../../services/authService";

const ProtectedRoute = ({ children }) => {
  const token = getAccessToken();

  if (!token) {
    // Ako nema tokena, preusmjeri na login
    return <Navigate to="/login" replace />;
  }

  // Ako ima token, prikaži komponentu
  return children;
};

export default ProtectedRoute;
