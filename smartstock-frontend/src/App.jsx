import { useEffect, useState } from "react";
import {
    BarChart,
    Bar,
    XAxis,
    YAxis,
    CartesianGrid,
    Tooltip,
    ResponsiveContainer,
} from "recharts";
import { BrowserRouter, Routes, Route } from "react-router-dom";
import Forecasting from "./Forecasting";
import Inventory from "./Inventory";
import Alerts from "./Alerts";
import Reports from "./Reports";
import Layout from "./Layout";
import Upload from "./Upload";
import "./App.css";

function App() {
  const [forecast, setForecast] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState("");
  const [inventory, setInventory] = useState([]);
  const [inventoryLoading, setInventoryLoading] = useState(true);
  const [alerts, setAlerts] = useState([]);
  const [alertsLoading, setAlertsLoading] = useState(true);
  const chartData = forecast.map((item) => ({
      name: item.productId,
      demand: Number(item.predicted30DayDemand.toFixed(0)),
  }));
  const inventoryChartData = inventory.map((item) => ({
      name: item.productId,
      currentStock: item.currentStock,
      reorderPoint: Number(item.reorderPoint.toFixed(0)),
  }));
  const totalProducts = inventory.length;

  const lowStockProducts = inventory.filter(
      (item) => item.status === "LOW STOCK"
  ).length;

  const sufficientStockProducts = inventory.filter(
      (item) => item.status === "SUFFICIENT"
  ).length;

  const totalPredictedDemand = forecast.reduce(
      (total, item) => total + item.predicted30DayDemand,
      0
  );

  useEffect(() => {
    fetch("https://smartstock-backend-v853.onrender.com/api/forecast/30-days")
        .then((response) => {
          if (!response.ok) {
            throw new Error("Failed to fetch forecast data");
          }

          return response.json();
        })
        .then((data) => {
          setForecast(data);
          setLoading(false);
        })
        .catch((err) => {
          setError(err.message);
          setLoading(false);
        });
  }, []);
  useEffect(() => {
    fetch("https://smartstock-backend-v853.onrender.com/api/inventory/optimization")
        .then((response) => {
          if (!response.ok) {
            throw new Error("Failed to fetch inventory data");
          }

          return response.json();
        })
        .then((data) => {
          setInventory(data);
          setInventoryLoading(false);
        })
        .catch((err) => {
          console.error(err);
          setInventoryLoading(false);
        });
  }, []);
  useEffect(() => {
    fetch("https://smartstock-backend-v853.onrender.com/api/alerts/low-stock")
        .then((response) => {
          if (!response.ok) {
            throw new Error("Failed to fetch alert data");
          }

          return response.json();
        })
        .then((data) => {
          setAlerts(data);
          setAlertsLoading(false);
        })
        .catch((err) => {
          console.error(err);
          setAlertsLoading(false);
        });
  }, []);
  return (
      <div className="app">
          {/* Hero */}
          <section className="hero">
            <div>
              <p className="hero-label">SMART INVENTORY MANAGEMENT</p>

              <h1>
                Predict Demand.
                <br />
                <span>Stock Smarter.</span>
              </h1>

              <p>
                AI-powered demand forecasting and inventory optimization
                for hyperlocal supply chains.
              </p>
            </div>
          </section>
          <section className="dashboard-card">

            <h3>30-Day Demand Forecast</h3>

            {loading && <p>Loading forecast data...</p>}

            {error && <p>{error}</p>}

            {!loading && !error && (
                <div>
                  {forecast.map((item) => (
                      <div className="forecast-row" key={item.productId}>
                        <div>
                          <strong>{item.productName}</strong>
                          <small>{item.productId}</small>
                        </div>

                        <div>
            <span>
              Avg Daily: {item.averageDailyDemand.toFixed(2)}
            </span>

                          <span>
              30 Days: {item.predicted30DayDemand.toFixed(2)}
            </span>
                        </div>
                      </div>
                  ))}
                </div>

            )}
          </section>
              <section className="stats">

              <div className="stat-card">
                  <div>
                      <span>Total Products</span>
                      <h2>{totalProducts}</h2>
                  </div>
              </div>

              <div className="stat-card">
                  <div>
                      <span>Low Stock</span>
                      <h2>{lowStockProducts}</h2>
                  </div>
              </div>

              <div className="stat-card">
                  <div>
                      <span>Sufficient Stock</span>
                      <h2>{sufficientStockProducts}</h2>
                  </div>
              </div>

              <div className="stat-card">
                  <div>
                      <span>30-Day Predicted Demand</span>
                      <h2>{Math.round(totalPredictedDemand)}</h2>
                  </div>
              </div>
            </section>

          {/* Main Cards */}
          <section className="dashboard-grid">
              <div className="dashboard-card chart-card">
                  <div className="card-header">
                      <div>
                          <h3>30-Day Predicted Demand</h3>
                          <p>Predicted demand by product</p>
                      </div>
                  </div>

                  <div style={{ width: "100%", height: "350px" }}>
                      <ResponsiveContainer width="100%" height="100%">
                          <BarChart data={chartData}>
                              <CartesianGrid strokeDasharray="3 3" stroke="#334155"/>
                              <XAxis dataKey="name" />
                              <YAxis />
                              <Tooltip
                                  contentStyle={{
                                      background: "#111827",
                                      border: "1px solid #334155",
                                      borderRadius: "8px",
                                      color: "#ffffff",
                                  }}
                              />
                              <Bar dataKey="demand" fill="#4f46e5"/>
                          </BarChart>
                      </ResponsiveContainer>
                  </div>
              </div>
              <div className="dashboard-card chart-card">
                  <div className="card-header">
                      <div>
                          <h3>Current Stock vs Reorder Point</h3>
                          <p>Inventory level compared with calculated reorder point</p>
                      </div>
                  </div>

                  <div style={{ width: "100%", height: "350px" }}>
                      <ResponsiveContainer width="100%" height="100%">
                          <BarChart data={inventoryChartData}>
                              <CartesianGrid
                                  strokeDasharray="3 3"
                                  stroke="#334155"
                              />

                              <XAxis dataKey="name" />

                              <YAxis />

                              <Tooltip
                                  contentStyle={{
                                      background: "#111827",
                                      border: "1px solid #334155",
                                      borderRadius: "8px",
                                      color: "#ffffff",
                                  }}
                              />

                              <Bar
                                  dataKey="currentStock"
                                  fill="#4f46e5"
                                  name="Current Stock"
                              />

                              <Bar
                                  dataKey="reorderPoint"
                                  fill="#f59e0b"
                                  name="Reorder Point"
                              />
                          </BarChart>
                      </ResponsiveContainer>
                  </div>
              </div>

            <div className="dashboard-card">
              <h3>Demand Forecasting</h3>
              <p>
                Predict future product demand using historical sales data
                and machine-learning based forecasting.
              </p>
                <button onClick={() => window.location.href = "/forecasting"}>
                    View Forecast
                </button>
            </div>

            <div className="dashboard-card inventory-card">

              <h3>Inventory Optimization</h3>

              {inventoryLoading && (
                  <p>Loading inventory data...</p>
              )}

              {!inventoryLoading && inventory.length > 0 && (
                  <div className="inventory-list">

                    {inventory.map((item) => (
                        <div className="inventory-row" key={item.productId}>

                          <div>
                            <strong>{item.productName}</strong>
                            <small>{item.productId}</small>
                          </div>

                          <div className="inventory-details">
            <span>
              Stock: {item.currentStock}
            </span>

                            <span>
              Reorder: {item.reorderPoint.toFixed(2)}
            </span>

                            <span
                                className={
                                  item.status === "LOW STOCK"
                                      ? "low-stock"
                                      : "sufficient"
                                }
                            >
              {item.status}
            </span>
                          </div>

                        </div>
                    ))}

                  </div>
              )}

            </div>

            <div className="dashboard-card">
              <h3>Low Stock Alerts</h3>
              <p>
                Monitor products that have fallen below their calculated
                reorder point.
              </p>

                <button onClick={() => window.location.href = "/alerts"}>
                    View Alerts
                </button>
            </div>
            <section className="dashboard-card">

              <h3>Low Stock Alerts</h3>

              {alertsLoading && (
                  <p>Loading alerts...</p>
              )}

              {!alertsLoading && alerts.length === 0 && (
                  <p>No low-stock products.</p>
              )}

              {!alertsLoading && alerts.length > 0 && (
                  <div className="alert-list">

                    {alerts.map((alert) => (
                        <div className="alert-row" key={alert.productId}>

                          <div>
                            <strong>{alert.productName}</strong>
                            <small>{alert.productId}</small>
                          </div>

                          <div className="alert-details">
            <span>
              Stock: {alert.currentStock}
            </span>

                            <span>
              Reorder Point: {alert.reorderPoint.toFixed(2)}
            </span>

                            <span className="low-stock">
              {alert.status}
            </span>
                          </div>

                        </div>
                    ))}

                  </div>
              )}

            </section>

            <div className="dashboard-card">
              <h3>Reorder Reports</h3>
              <p>
                Generate an Excel reorder report for products requiring
                replenishment.
              </p>

                <button
                    onClick={() =>
                        window.open(
                            "https://smartstock-backend-v853.onrender.com/api/reports/reorder",
                            "_blank"
                        )
                    }
                >
                    Generate Report
                </button>
            </div>

          </section>
      </div>

  );
}

export default function AppRouter() {
  return (
      <BrowserRouter>
        <Layout>
          <Routes>
            <Route path="/" element={<App />} />
            <Route path="/forecasting" element={<Forecasting />} />
            <Route path="/inventory" element={<Inventory />} />
            <Route path="/alerts" element={<Alerts />} />
            <Route path="/reports" element={<Reports />} />
              <Route path="/upload" element={<Upload />} />
          </Routes>
        </Layout>
      </BrowserRouter>
  );
}