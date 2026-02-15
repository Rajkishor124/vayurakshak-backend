# 🌍 VayuRakshak
## Multi-Tenant Air Intelligence SaaS Platform

**AI-powered air quality monitoring & pollution hotspot detection system** built using Spring Boot.

---

## 🚨 Problem Statement

India — especially **Delhi NCR** — faces severe air pollution challenges.

Existing AQI apps only display numbers. They do **NOT**:

❌ Personalize health risk  
❌ Detect pollution hotspots  
❌ Provide actionable insights  
❌ Support organizational monitoring

---

## ✅ Our Solution

**VayuRakshak** bridges this gap by combining:

✔ Real-time AQI intelligence  
✔ Citizen pollution reports  
✔ Risk scoring engine  
✔ Smart alert generation  
✔ Multi-tenant SaaS architecture

---

## 🏗 Architecture Overview

### ⚙ Backend Stack
- **Java 21**
- **Spring Boot 3**
- **Spring Security (JWT)**
- **MySQL 8**
- **JPA / Hibernate**
- **OpenAPI (Swagger)**

### 🧱 Architecture Style
- Feature-based packaging
- Multi-tenant SaaS design
- Role-based access control
- Stateless JWT authentication
- Subscription-tier system

---

## 🔐 Authentication & Security

- JWT-based authentication
- Stateless session management
- BCrypt password encryption
- Role-based authorization

### 👥 Roles
- `ROLE_ADMIN`
- `ROLE_RESIDENT`

---

## 🏢 Multi-Tenant SaaS Model

Each organization has:

✔ Isolated data  
✔ Role-based users  
✔ Subscription plan  
✔ Independent dashboard  
✔ Hot spot analysis

### 💼 Subscription Plans

| Plan | Features |
|------|----------|
| FREE | Basic dashboard |
| PRO | Advanced analytics |
| ENTERPRISE | Predictive insights |

---

## 📊 Intelligent Dashboard

### Features

✔ Real-time AQI classification  
✔ Organization risk score  
✔ Pollution report aggregation  
✔ Hotspot detection engine  
✔ Smart alert generation  
✔ Tier-based analytics unlocking

---

## 🔥 Hotspot Detection Engine

Detects pollution clusters using:

- Location-based grouping
- Threshold-based detection
- Admin-only analytics visibility

---

## 🚨 Smart Alert System

Alerts are generated when:

✔ AQI exceeds safe threshold  
✔ Risk score becomes high  
✔ Pollution hotspots detected

### Alert Levels

- 🟢 INFO
- 🟠 WARNING
- 🔴 CRITICAL

---

## 📂 Project Structure

```
airquality
│
├── advice
├── alert
├── aqi
├── auth
├── organization
├── report
├── user
├── common
```

### Why Feature-Based Packaging?

✔ Scalable  
✔ Maintainable  
✔ Microservice-ready

---

## 🧪 API Documentation

Swagger UI:

👉 http://localhost:8080/swagger-ui/index.html

---

## 🚀 Running the Project

### 1️⃣ Clone Repository
```bash
git clone <repo-url>
```

### 2️⃣ Configure MySQL

Create database:

```sql
CREATE DATABASE vayurakshak_db;
```

Update `application.yml`.

### 3️⃣ Run Application
```bash
mvn clean install
mvn spring-boot:run
```

---

## 🔄 Authentication Flow

### Register
```
POST /api/v1/auth/register
```

### Login
```
POST /api/v1/auth/login
```

### Use JWT Token

```
Authorization: Bearer <token>
```

---

## 🏆 Why This Project Stands Out

⭐ Multi-tenant SaaS architecture  
⭐ Enterprise-level Spring Security  
⭐ Data-driven risk scoring  
⭐ Tier-based monetization model  
⭐ Pollution hotspot detection  
⭐ Intelligent alert system

---

## 📈 Future Improvements

- Real-time AQI API integration
- Geo-location pollution heatmap
- Mobile app integration
- Email/SMS alerts
- AI/ML pollution prediction

---

## 👨‍💻 Developed By

**Team VayuRakshak**  
Hackathon Project – *Green Bharat Initiative*

---

## ⭐ Impact Vision

> Empower communities, organizations, and cities with intelligent air quality insights to build a healthier India.
