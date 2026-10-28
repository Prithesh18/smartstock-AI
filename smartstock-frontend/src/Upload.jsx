import { useState } from "react";

function Upload() {
    const [file, setFile] = useState(null);
    const [message, setMessage] = useState("");
    const [error, setError] = useState("");
    const [isUploading, setIsUploading] = useState(false);

    const handleUpload = async () => {

        if (!file) {
            setError("Please select a CSV file first.");
            setMessage("");
            return;
        }

        const formData = new FormData();
        formData.append("file", file);

        try {

            setError("");
            setMessage("Uploading...");
            setIsUploading(true);

            const response = await fetch(
                "https://smartstock-backend-v853.onrender.com/api/csv/upload",
                {
                    method: "POST",
                    body: formData
                }
            );

            const result = await response.text();

            if (!response.ok) {
                throw new Error(result);
            }

            setMessage(result);
            console.log("CSV upload response:", result);
            setFile(null);

            setTimeout(() => {
                window.location.href = "/";
            }, 1500);

        } catch (error) {

            console.error("Upload error:", error);

            setMessage("");
            setError(
                error.message || "Failed to upload CSV."
            );
        }
        finally {
            setIsUploading(false);
        }
    };

    return (
        <div className="page">

            <div className="page-header">
                <h2>Upload Dataset</h2>
                <p>
                    Upload sales and inventory data to SmartStock AI.
                </p>
            </div>

            <div className="table-card">

                <h3>CSV Dataset</h3>

                <p style={{ marginBottom: "20px" }}>
                    Upload a CSV file containing sales and inventory records.
                </p>

                <input
                    type="file"
                    accept=".csv"
                    disabled={isUploading}
                    onChange={(e) => {
                        setFile(e.target.files[0]);
                        setMessage("");
                        setError("");
                    }}
                />

                {file && (
                    <p style={{ marginTop: "15px" }}>
                        Selected file: <strong>{file.name}</strong>
                    </p>
                )}

                <button
                    onClick={handleUpload}
                    disabled={isUploading}
                    style={{
                        marginTop: "20px",
                        padding: "11px 20px",
                        border: "none",
                        borderRadius: "8px",
                        cursor: isUploading ? "not-allowed" : "pointer",
                        background: isUploading ? "#9ca3af" : "#4f46e5",
                        color: "white",
                        fontWeight: "600",
                    }}
                >
                    {isUploading ? "Uploading..." : "Upload CSV"}
                </button>

                {message && (
                    <p style={{ marginTop: "20px", color: "green" }}>
                        {message}
                    </p>
                )}

                {error && (
                    <p style={{ marginTop: "20px", color: "red" }}>
                        {error}
                    </p>
                )}

            </div>
        </div>
    );
}

export default Upload;