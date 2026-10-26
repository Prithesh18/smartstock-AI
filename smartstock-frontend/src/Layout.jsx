import { Link, useLocation } from "react-router-dom";

function Layout({ children }) {
    const location = useLocation();

    const isActive = (path) => {
        return location.pathname === path ? "nav-item active" : "nav-item";
    };

    return (
        <div className="app">
            {/* Sidebar */}
            <aside className="sidebar">
                <div className="logo">
                    <div className="logo-icon">S</div>

                    <div>
                        <h2>SmartStock</h2>
                        <span>AI Inventory</span>
                    </div>
                </div>

                <nav className="sidebar-nav">
                    <p className="nav-title">MAIN</p>

                    <Link to="/" className={isActive("/")}>
                        <span>▦</span>
                        Dashboard
                    </Link>

                    <Link to="/forecasting" className={isActive("/forecasting")}>
                        <span>⌁</span>
                        Forecasting
                    </Link>

                    <Link to="/inventory" className={isActive("/inventory")}>
                        <span>▤</span>
                        Inventory
                    </Link>

                    <Link to="/alerts" className={isActive("/alerts")}>
                        <span>⚠</span>
                        Low Stock Alerts
                    </Link>

                    <Link to="/reports" className={isActive("/reports")}>
                        <span>▧</span>
                        Reports
                    </Link>
                    <Link to="/upload" className={isActive("/upload")}>
                        <span>⇧</span> Upload Dataset
                    </Link>
                </nav>

                <div className="sidebar-bottom">
                    <div className="system-status">
                        <span className="status-dot"></span>
                        System Online
                    </div>
                </div>
            </aside>

            {/* Main area */}
            <div className="main-area">
                <header className="top-header">
                    <div>
                        <h1>SmartStock AI</h1>
                        <p>Predictive Demand Intelligence</p>
                    </div>

                    <div className="user-info">
                        <div className="user-avatar">P</div>

                        <div>
                            <strong>Store Owner</strong>
                            <span>Admin</span>
                        </div>
                    </div>
                </header>

                <main className="content">
                    {children}
                </main>
            </div>
        </div>
    );
}

export default Layout;