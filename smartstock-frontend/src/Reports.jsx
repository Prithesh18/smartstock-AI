function Reports() {
    const downloadReport = async () => {
        try {
            const response = await fetch(
                "https://smartstock-backend-v853.onrender.com/api/reports/reorder"
            );

            if (!response.ok) {
                throw new Error(`Server returned ${response.status}`);
            }

            const blob = await response.blob();

            const downloadUrl = window.URL.createObjectURL(blob);

            const link = document.createElement("a");
            link.href = downloadUrl;
            link.download = "SmartStock_Reorder_Report.xlsx";

            document.body.appendChild(link);
            link.click();
            document.body.removeChild(link);

            window.URL.revokeObjectURL(downloadUrl);

        } catch (error) {
            console.error("Download error:", error);
            alert("Failed to download report. Check the browser console.");
        }
    };

    return (
        <div className="page">
            <div className="page-header">
                <h2>Reports</h2>
                <p>
                    Generate and download inventory reorder reports.
                </p>
            </div>

            <div className="table-card">
                <h3>Inventory Reorder Report</h3>

                <p style={{ marginTop: "10px", color: "#7f8ba8" }}>
                    Download the latest Excel report containing low-stock products,
                    reorder points, required stock, distributors, and lead times.
                </p>

                <button
                    onClick={downloadReport}
                    style={{
                        marginTop: "20px",
                        padding: "12px 20px",
                        border: "none",
                        borderRadius: "8px",
                        cursor: "pointer",
                        background: "#4f46e5",
                        color: "white",
                        fontWeight: "600",
                    }}
                >
                    Download Excel Report
                </button>
            </div>
        </div>
    );
}

export default Reports;