import { useEffect, useState } from "react";

function Alerts() {

    const [alerts, setAlerts] = useState([]);
    const [loading, setLoading] = useState(true);

    useEffect(() => {
        fetch("https://smartstock-backend-v853.onrender.com/api/alerts/low-stock")
            .then((response) => response.json())
            .then((data) => {
                setAlerts(data);
                setLoading(false);
            })
            .catch((error) => {
                console.error("Alert error:", error);
                setLoading(false);
            });
    }, []);
    const sendEmailAlerts = async () => {
        try {
            const response = await fetch(
                "https://smartstock-backend-v853.onrender.com/api/email/low-stock",
                {
                    method: "POST",
                }
            );

            const result = await response.text();

            console.log("Status:", response.status);
            console.log("Response:", result);

            if (!response.ok) {
                throw new Error(result);
            }

            alert(result);

        } catch (error) {
            console.error("Email error:", error);
            alert("Failed to send email alerts: " + error.message);
        }
    };

    return (
        <div className="page">

            <div className="page-header">
                <h2>Low Stock Alerts</h2>

                <p>
                    Products currently below their calculated reorder point.
                </p>

                <button
                    onClick={sendEmailAlerts}
                    style={{
                        marginTop: "15px",
                        padding: "11px 18px",
                        border: "none",
                        borderRadius: "8px",
                        cursor: "pointer",
                        background: "#4f46e5",
                        color: "white",
                        fontWeight: "600",
                    }}
                >
                    Send Email Alerts
                </button>
            </div>

            {loading ? (
                <p>Loading alerts...</p>
            ) : alerts.length === 0 ? (
                <div className="table-card">
                    <p>No low-stock products found.</p>
                </div>
            ) : (
                <div className="table-card">

                    <table>

                        <thead>
                        <tr>
                            <th>Product ID</th>
                            <th>Product</th>
                            <th>Current Stock</th>
                            <th>Reorder Point</th>
                            <th>Required Stock</th>
                            <th>Lead Time</th>
                            <th>Distributor</th>
                            <th>Status</th>
                        </tr>
                        </thead>

                        <tbody>

                        {alerts.map((alert) => (

                            <tr key={alert.productId}>

                                <td>{alert.productId}</td>

                                <td>
                                    <strong>{alert.productName}</strong>
                                </td>

                                <td>
                                    {alert.currentStock}
                                </td>

                                <td>
                                    {Math.ceil(alert.reorderPoint)}
                                </td>

                                <td>
                                    {Math.ceil(alert.requiredStock)}
                                </td>

                                <td>
                                    {alert.leadTimeDays} days
                                </td>

                                <td>
                                    {alert.distributorName}
                                </td>

                                <td>
                    <span className="low-stock">
                      {alert.status}
                    </span>
                                </td>

                            </tr>

                        ))}

                        </tbody>

                    </table>

                </div>
            )}

        </div>
    );
}

export default Alerts;