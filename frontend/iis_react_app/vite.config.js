import { defineConfig } from "vite";
import react from "@vitejs/plugin-react";

export default defineConfig({
  plugins: [react()],
  server: {
    proxy: {
      "/api": {
        target: "http://localhost:8087", // tvoj Spring Boot port
        changeOrigin: true,
        secure: false,
      },
    },
  },
});
