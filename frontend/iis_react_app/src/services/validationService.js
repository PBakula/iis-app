import { apiClient } from "./apiClient";

const baseURL = import.meta.env.PROD ? "/api" : "http://localhost:8087/api";

export const validateXML = async (file, validationType) => {
  const reader = new FileReader();

  return new Promise((resolve, reject) => {
    reader.onload = async (e) => {
      try {
        const xmlContent = e.target.result.trim();

        const response = await apiClient(
          `${baseURL}/xml/validateAndSave?validationType=${validationType.toLowerCase()}`,
          {
            method: "POST",
            headers: {
              "Content-Type": "application/xml",
            },
            body: xmlContent,
          }
        );

        const responseData = await response.json();

        if (!response.ok) {
          throw new Error(responseData.message);
        }

        resolve({
          message: responseData.message,
          xmlContent: responseData.xmlContent,
        });
      } catch (error) {
        reject(error);
      }
    };

    reader.onerror = () => reject(new Error("Greška pri čitanju datoteke"));
    reader.readAsText(file);
  });
};

export const validateCSV = async (file, type) => {
  const formData = new FormData();
  formData.append("file", file);
  formData.append("validationType", type);

  const response = await apiClient(`${baseURL}/xml/validateAndSaveCsv`, {
    method: "POST",
    body: formData,
  });

  if (!response.ok) {
    const errorMessage = await response.text();
    throw new Error(errorMessage);
  }

  return response.text();
};
