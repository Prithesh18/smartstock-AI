import { useEffect, useState } from "react";

function Inventory() {

    const [inventory, setInventory] = useState([]);
    const [loading, setLoading] = useState(true);

    useEffect(() => {
        fetch("https://smartstock-backend-v853.onrender.com/api/inventory/optimization")
            .then((response) => response.json())
            .then((data) => {
                setInventory(data);
                setLoading(false);
            })
            .catch((error) => {
                console.error("Inventory error:", error);
                setLoading(false);
            });
    }, []);

    return (
        <div className="page">

            <div className="page-header">
                <h2>Inventory Optimization</h2>
                <p>
                    Reorder points, safety stock and replenishment requirements.
                </p>
            </div>

            {loading ? (
                <p>Loading inventory data...</p>
            ) : (
                <div className="table-card">

                    <table>

                        <thead>
                        <tr>
                            <th>Product ID</th>
                            <th>Product</th>
                            <th>Avg Daily Demand</th>
                            <th>Lead Time Demand</th>
                            <th>Safety Stock</th>
                            <th>Reorder Point</th>
                            <th>Current Stock</th>
                            <th>Required Stock</th>
                            <th>Status</th>
                        </tr>
                        </thead>

                        <tbody>

                        {inventory.map((item) => (

                            <tr key={item.productId}>

                                <td>{item.productId}</td>

                                <td>
                                    <strong>{item.productName}</strong>
                                </td>

                                <td>
                                    {item.averageDailyDemand.toFixed(2)}
                                </td>

                                <td>
                                    {item.leadTimeDemand.toFixed(2)}
                                </td>

                                <td>
                                    {item.safetyStock.toFixed(2)}
                                </td>

                                <td>
                                    {item.reorderPoint.toFixed(2)}
                                </td>

                                <td>
                                    {item.currentStock}
                                </td>

                                <td>
                                    {Math.ceil(item.requiredStock)}
                                </td>

                                <td>
                    <span
                        className={
                            item.status === "LOW STOCK"
                                ? "low-stock"
                                : "sufficient"
                        }
                    >
                      {item.status}
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

export default Inventory;