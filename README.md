# SMARTSTOCK AI  
### Predictive Demand Intelligence for Hyperlocal Supply Chains

SmartStock AI is a full-stack inventory management and demand forecasting application designed to help businesses predict product demand, optimize inventory levels, identify low-stock products, and make smarter restocking decisions.

The application combines a React frontend with a Java Spring Boot backend and a MySQL database to provide demand forecasts, inventory optimization insights, reports, and low-stock email alerts.

## 🚀 Live Demo

- **Frontend:** https://smartstock-n7m0kanh4-prithesh.vercel.app/
- **Backend API:** https://smartstock-backend-v853.onrender.com

## ✨ Features

- **Dashboard:** View sales data and key inventory information.
- **Demand Forecasting:** Generate 30-day product demand forecasts.
- **Inventory Optimization:** Calculate reorder points, safety stock, and required stock quantities.
- **Low-Stock Detection:** Identify products that require replenishment.
- **Email Alerts:** Send email notifications for low-stock products.
- **Reports:** Download reports for analysis and inventory planning.
- **CSV Upload:** Import sales datasets for processing and analysis.
- **Data Preprocessing:** Prepare sales data and calculate useful demand-related features.

## 🛠️ Tech Stack

### Frontend
- React.js
- JavaScript
- HTML5
- CSS3

### Backend
- Java
- Spring Boot
- Spring Data JPA
- REST APIs

### Database
- MySQL

### Tools and Deployment
- Git and GitHub
- IntelliJ IDEA
- Postman
- Vercel — Frontend deployment
- Render — Backend deployment
- Aiven — MySQL database hosting

## 🏗️ Architecture

The application follows a frontend–backend–database architecture.

1. **React frontend:** Provides the user interface for viewing forecasts, inventory status, dashboards, and reports.
2. **Spring Boot backend:** Exposes REST APIs to process uploaded sales data, calculate forecasts, optimize inventory, and send alerts.
3. **MySQL database:** Stores sales and inventory-related records.
4. **External services:** Vercel hosts the frontend, Render hosts the backend, and Aiven provides the managed MySQL database.

## 📊 Inventory Optimization

SmartStock AI uses recent sales data and lead-time information to estimate inventory requirements.

The inventory optimization process includes:

- Calculating average daily demand.
- Estimating demand during supplier lead time.
- Calculating safety stock.
- Determining the reorder point.
- Estimating 30-day demand and additional stock requirements.
- Classifying products as low stock or sufficient stock.

These calculations help users identify potential stock shortages and make informed replenishment decisions.

## 🔄 Application Workflow

1. Upload a sales dataset in CSV format.
2. Process and prepare the sales records.
3. Generate 30-day demand forecasts.
4. Calculate inventory requirements and reorder points.
5. Identify low-stock products.
6. View dashboards and download reports.
7. Send email alerts for products requiring replenishment.

## ⚙️ Local Setup

### Prerequisites

- Java and a compatible JDK
- Maven
- Node.js and npm
- MySQL
- Git

### 1. Clone the repository

```bash
git clone https://github.com/Prithesh18/smartstock-AI.git
cd smartstock-AI
```

### 2. Configure the backend

Open the backend project in IntelliJ IDEA.

Configure the following environment variables before running the Spring Boot application:

```text
DB_URL
DB_USERNAME
DB_PASSWORD
MAIL_USERNAME
MAIL_PASSWORD
```

Use your own MySQL connection details and email application credentials. Do not commit passwords or secrets to GitHub.

Run the Spring Boot application from your IDE or using Maven.

### 3. Run the frontend

Open a terminal in the frontend project directory:

```bash
cd smartstock-frontend
npm install
npm run dev
```

Open the local URL printed by Vite in the terminal.

**Note:** Configure the frontend API base URL for your local backend or deployed backend as appropriate. The backend also requires a reachable MySQL database and valid email configuration for those features to work.

## 🔐 Configuration and Security

- Store database and email credentials in environment variables.
- Avoid committing `.env` files containing secrets.
- Configure CORS for the frontend origins that need API access.
- Keep production credentials separate from local development settings.

## 🎯 Project Objective

The goal of SmartStock AI is to demonstrate how demand analysis, forecasting, and inventory optimization can be combined in a full-stack application to support data-driven inventory decisions for hyperlocal supply chains.

## 👨‍💻 Author

**Prithesh Devadiga**

Information Science and Engineering

GitHub: [Prithesh18](https://github.com/Prithesh18)

---

*SMARTSTOCK AI — Predict Demand. Stock Smarter.*
