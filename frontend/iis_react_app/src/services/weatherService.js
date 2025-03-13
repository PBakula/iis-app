import { apiClient } from "./apiClient";

const baseURL = import.meta.env.PROD ? "" : "http://localhost:8087";

export const getTemperature = async (cityName) => {
  try {
    const response = await apiClient(
      `${baseURL}/api/weather/temperature?cityName=${encodeURIComponent(
        cityName
      )}`
    );

    if (!response.ok) {
      throw new Error(
        `Error ${response.status}: Failed to fetch temperature data`
      );
    }

    return await response.text();
  } catch (error) {
    console.error("Error fetching temperature:", error);
    throw error;
  }
};

export const getMatchingCities = async (cityName) => {
  try {
    const response = await apiClient(
      `${baseURL}/api/weather/cities?cityName=${encodeURIComponent(cityName)}`
    );

    if (!response.ok) {
      throw new Error(
        `Error ${response.status}: Failed to fetch matching cities`
      );
    }

    return await response.json();
  } catch (error) {
    console.error("Error fetching matching cities:", error);
    throw error;
  }
};
