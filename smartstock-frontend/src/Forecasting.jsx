import { useEffect, useState } from "react";

function Forecasting() {

    const [forecast, setForecast] = useState([]);
    const [loading, setLoading] = useState(true);

    useEffect(() => {
        fetch("http://localhost:8080/api/forecast/30-days")
            .then((response) => response.json())
            .then((data) => {
                setForecast(data);
                setLoading(false);
            })
            .catch((error) => {
                console.error("Forecast error:", error);
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

                        {forecast.map((item) => (

                            <tr key={item.productId}>

                                <td>{item.productId}</td>

                                <td>
                                    <strong>{item.productName}</strong>
                                </td>

                                <td>
                                    {item.averageDailyDemand.toFixed(2)}
                                </td>

                                <td>
                                    {item.predicted30DayDemand.toFixed(2)}
                                </td>

                                <td>
                                    {item.currentStock}
                                </td>

                                <td>
                                    {item.mae?.toFixed(2)}
                                </td>

                                <td>
                                    {item.rmse?.toFixed(2)}
                                </td>

                                <td>
                                    {item.demandTrend}
                                </td>

                                <td>
                                    {item.trendPercentage?.toFixed(1)}%
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