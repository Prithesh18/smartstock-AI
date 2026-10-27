
import { useEffect, useState } from "react";

function Forecasting() {
    const [forecast, setForecast] = useState([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState("");

    useEffect(() => {
        fetch("https://smartstock-backend-v853.onrender.com/api/forecast/30-days")
            .then((response) => {
                if (!response.ok) {
                    throw new Error(`HTTP error: ${response.status}`);
                }
                return response.json();
            })
            .then((data) => {
                console.log("Forecast API response:", data);

                // Handle either an array or an object containing forecast data
                const records = Array.isArray(data)
                    ? data
                    : data.forecasts ?? data.data ?? [];

                setForecast(records);
            })
            .catch((error) => {
                console.error("Forecast error:", error);
                setError("Unable to load forecast data. Please try again.");
            })
            .finally(() => {
                setLoading(false);
            });
    }, []);

    return (
        <div className="page">
            <div className="page-header">
                <h2>Demand Forecasting</h2>
                <p>
                    30-day predicted demand based on recent sales data.
                </p>
            </div>

            {loading ? (
                <p>Loading forecast...</p>
            ) : error ? (
                <p>{error}</p>
            ) : forecast.length === 0 ? (
                <p>No forecast data available.</p>
            ) : (
                <div className="table-card">
                    <table>
                        <thead>
                        <tr>
                            <th>Product ID</th>
                            <th>Product</th>
                            <th>Average Daily Demand</th>
                            <th>30-Day Demand</th>
                            <th>Current Stock</th>
                            <th>MAE</th>
                            <th>RMSE</th>
                            <th>Demand Trend</th>
                            <th>Trend %</th>
                        </tr>
                        </thead>

                        <tbody>
                        {forecast.map((item, index) => (
                            <tr key={item.productId ?? index}>
                                <td>{item.productId ?? "-"}</td>
                                <td>
                                    <strong>
                                        {item.productName ?? "-"}
                                    </strong>
                                </td>
                                <td>
                                    {Number.isFinite(Number(item.averageDailyDemand))
                                        ? Number(item.averageDailyDemand).toFixed(2)
                                        : "-"}
                                </td>
                                <td>
                                    {Number.isFinite(Number(item.predicted30DayDemand))
                                        ? Number(item.predicted30DayDemand).toFixed(2)
                                        : "-"}
                                </td>
                                <td>{item.currentStock ?? "-"}</td>
                                <td>
                                    {item.mae != null &&
                                    Number.isFinite(Number(item.mae))
                                        ? Number(item.mae).toFixed(2)
                                        : "-"}
                                </td>
                                <td>
                                    {item.rmse != null &&
                                    Number.isFinite(Number(item.rmse))
                                        ? Number(item.rmse).toFixed(2)
                                        : "-"}
                                </td>
                                <td>{item.demandTrend ?? "-"}</td>
                                <td>
                                    {item.trendPercentage != null &&
                                    Number.isFinite(Number(item.trendPercentage))
                                        ? `${Number(item.trendPercentage).toFixed(1)}%`
                                        : "-"}
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

export default Forecasting;
