// components/shared/FileInput.jsx
import { Form } from "react-bootstrap";

export const FileInput = ({ id, accept, onChange, label }) => {
  const handleChange = (e) => {
    const file = e.target.files[0];
    if (file) {
      onChange(file);
    }
  };

  return (
    <Form.Group className="mb-3">
      <Form.Label>{label}</Form.Label>
      <Form.Control
        type="file"
        id={id}
        accept={accept}
        onChange={handleChange}
      />
    </Form.Group>
  );
};
