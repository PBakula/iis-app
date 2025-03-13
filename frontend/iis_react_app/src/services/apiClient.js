// src/services/apiClient.js
import { getAccessToken, getRefreshToken } from "./authService";

// Pomoćne funkcije za logiranje (možete uključiti ili isključiti debug mode)
const DEBUG = true;
const log = (message, data) => {
  if (DEBUG) {
    console.log(`[apiClient] ${message}`, data || "");
  }
};

const error = (message, err) => {
  if (DEBUG) {
    console.error(`[apiClient] ${message}`, err || "");
  }
};

// Funkcija za refresh tokena
const refreshAccessToken = async () => {
  log("Pokušaj refresh tokena");
  try {
    const refreshToken = getRefreshToken();
    if (!refreshToken) {
      error("Nema refresh tokena u localStorage");
      return false;
    }
    log("Refresh token pronađen", refreshToken.substring(0, 10) + "...");

    const baseURL = import.meta.env.PROD ? "" : "http://localhost:8087";
    const refreshURL = `${baseURL}/rest/refreshToken`;
    log("Šaljem zahtjev na:", refreshURL);

    const requestBody = { token: refreshToken };
    log("Tijelo zahtjeva:", requestBody);

    const response = await fetch(refreshURL, {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
      },
      body: JSON.stringify(requestBody),
    });

    log("Status odgovora:", response.status);

    if (!response.ok) {
      error(`Refresh token zahtjev nije uspio - status: ${response.status}`);
      try {
        const errorData = await response.text();
        error("Detalji greške:", errorData);
      } catch (e) {
        error("Nije moguće pročitati odgovor greške");
      }
      return false;
    }

    const data = await response.json();
    log("Refresh odgovor primljen:", {
      accessToken: data.accessToken
        ? `${data.accessToken.substring(0, 10)}...`
        : "missing",
      token: data.token ? `${data.token.substring(0, 10)}...` : "missing",
    });

    if (!data.accessToken) {
      error("Nema access tokena u odgovoru");
      return false;
    }

    // Spremamo nove tokene
    localStorage.setItem("accessToken", data.accessToken);

    // Ako API vraća novi refresh token, spremamo i njega
    if (data.token) {
      localStorage.setItem("refreshToken", data.token);
    }

    log("Tokeni su uspješno osvježeni i spremljeni u localStorage");
    return true;
  } catch (e) {
    error("Greška prilikom refresh tokena:", e);
    return false;
  }
};

// Pomoćna funkcija za čišćenje podataka o autentikaciji
const clearAuthData = () => {
  log("Čišćenje auth podataka iz localStorage");
  localStorage.removeItem("accessToken");
  localStorage.removeItem("refreshToken");
};

// Glavna apiClient funkcija
export const apiClient = async (url, options = {}) => {
  log(`Početak zahtjeva na: ${url}`, options);

  // Dodajemo token u zaglavlje
  const addAuthHeader = () => {
    const token = getAccessToken();
    if (token) {
      log("Access token pronađen", token.substring(0, 10) + "...");
      return { Authorization: `Bearer ${token}` };
    } else {
      log("Access token nije pronađen");
      return {};
    }
  };

  // Priprema inicijalnog zahtjeva
  let headers = {
    ...options.headers,
    ...addAuthHeader(),
  };

  let isRefreshAttempted = false; // Zastava za praćenje je li refresh već pokušan

  try {
    // Prvi pokušaj s postojećim tokenom
    log("Šaljem inicijalni zahtjev s postojećim tokenom");
    let response = await fetch(url, { ...options, headers });
    log("Inicijalni odgovor status:", response.status);

    // Ako dobijemo 401 ili 403, pokušat ćemo refresh
    if (response.status === 401 || response.status === 403) {
      log("Dobiven 401/403, pokušavam refresh tokena");

      if (isRefreshAttempted) {
        error("Refresh već pokušan, preusmjeravam na login");
        clearAuthData();
        window.location.href = "/login";
        return null;
      }

      isRefreshAttempted = true;

      // Pokušaj refresh tokena
      const isRefreshed = await refreshAccessToken();

      if (isRefreshed) {
        log("Token uspješno refreshan, ponavljam zahtjev");

        // Ažuriramo zaglavlje s novim tokenom
        headers = {
          ...options.headers,
          ...addAuthHeader(), // Dohvaća novi token
        };

        // Ponovni pokušaj s novim tokenom
        log("Slanje ponovnog zahtjeva s novim tokenom");
        response = await fetch(url, { ...options, headers });
        log("Status odgovora nakon refresha:", response.status);

        // Ako i dalje dobijemo grešku, preusmjeravamo na login
        if (response.status === 401 || response.status === 403) {
          error(
            "Autentikacija opet neuspješna nakon refresha, preusmjeravam na login"
          );
          clearAuthData();
          window.location.href = "/login";
          return null;
        }

        log("Zahtjev uspješan nakon refresha tokena");
        return response;
      } else {
        // Refresh nije uspio, preusmjeravanje na login
        error("Refresh tokena nije uspio, preusmjeravam na login");
        clearAuthData();
        window.location.href = "/login";
        return null;
      }
    }

    log("Zahtjev uspješno završen sa statusom:", response.status);
    return response;
  } catch (err) {
    error("API Client greška:", err);
    throw err;
  }
};
