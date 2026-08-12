# ✈️ SkyWing Flight Booking Frontend

A modern flight booking frontend application built with **React + Vite** and integrated with a **Spring Boot Microservices Backend Architecture**.

The application provides a complete flight booking experience including flight search, passenger management, online payment, booking confirmation, and user booking management.

---

# 🚀 Project Overview

SkyWing is a full-stack flight booking platform where users can:

- Register and login securely
- Search available flights
- View flight details
- Select fares
- Add passenger information
- Create bookings
- Make online payments using Razorpay
- Receive booking confirmation with PNR
- View booking history

The frontend communicates with backend microservices through an API Gateway.

---

# ✨ Features

## 🔐 Authentication

- User registration
- User login
- JWT based authentication
- Protected routes
- Email verification flow
- Secure API communication using Axios interceptor


## ✈️ Flight Search

Users can:

- Search flights by source airport
- Search flights by destination airport
- Select travel date
- View available flights
- View airline details
- View route information
- View fare details


## 🛫 Flight Details

Displays:

- Flight number
- Airline name
- Source airport
- Destination airport
- Departure time
- Arrival time
- Duration
- Available fare classes


## 👤 Passenger Management

Users can:

- Add passenger details
- Validate passenger information
- Select passenger type
- Continue booking process


## 🎟️ Booking System

Complete booking workflow:

```
Search Flight
       |
       ↓
Select Flight
       |
       ↓
Passenger Details
       |
       ↓
Create Booking
       |
       ↓
Payment
       |
       ↓
Payment Verification
       |
       ↓
Booking Confirmation
       |
       ↓
PNR Generated
```

---

## 💳 Payment Integration

Integrated with:

- Razorpay Checkout

Features:

- Create payment order
- Open Razorpay payment window
- Verify payment
- Confirm booking after successful payment


Payment flow:

```
User
 |
 ↓
Frontend
 |
 ↓
Payment Service
 |
 ↓
Razorpay
 |
 ↓
Verify Payment
 |
 ↓
Booking Confirmation
```

---

## 🎫 Booking Confirmation

After successful payment:

Displays:

- Booking ID
- Booking Reference
- PNR Number
- Amount Paid
- Flight details
- Booking status

Example:

```
Payment Successful

Booking ID: 56

PNR: 876A4M

Status: CONFIRMED
```

---

# 🛠️ Tech Stack

## Frontend Technologies

- React 19
- Vite
- JavaScript (ES6+)
- React Router DOM
- Axios
- CSS3
- HTML5


## Development Tools

- VS Code
- npm
- Vite Development Server
- ESLint


## Backend Integration

Connected with Spring Boot Microservices:

```
                 React Frontend
                       |
                       |
                 API Gateway
                       |
 ------------------------------------------------
 |              |              |                |
User Service  Flight Service  Booking Service  Payment Service
                                                    |
                                                    |
                                             Notification Service
```

---

# 📂 Folder Structure

```
frontend
│
├── public
│
├── src
│   │
│   ├── api
│   │   ├── axiosInstance.js
│   │   ├── bookingApi.js
│   │   ├── flightApi.js
│   │   ├── paymentApi.js
│   │   └── userApi.js
│   │
│   ├── auth
│   │   ├── AuthContext.jsx
│   │   └── ProtectedRoute.jsx
│   │
│   ├── components
│   │
│   ├── layouts
│   │   └── PublicLayout.jsx
│   │
│   ├── pages
│   │   │
│   │   ├── public
│   │   │   ├── Home.jsx
│   │   │   ├── Login.jsx
│   │   │   ├── Register.jsx
│   │   │   └── VerifyEmail.jsx
│   │   │
│   │   ├── flight
│   │   │   ├── FlightSearch.jsx
│   │   │   └── FlightDetails.jsx
│   │   │
│   │   ├── booking
│   │   │   ├── PassengerDetails.jsx
│   │   │   └── BookingSuccess.jsx
│   │   │
│   │   └── payment
│   │       └── Payment.jsx
│   │
│   ├── routes
│   │   └── AppRoutes.jsx
│   │
│   ├── styles
│   │
│   ├── App.jsx
│   └── main.jsx
│
├── index.html
├── package.json
├── vite.config.js
└── README.md
```

---

# ⚙️ Installation

Clone repository:

```bash
git clone <repository-url>
```

Navigate to frontend:

```bash
cd frontend
```

Install dependencies:

```bash
npm install
```

---

# ▶️ Run Application

Start development server:

```bash
npm run dev
```

Frontend will run on:

```
http://localhost:5173
```

---

# 🔑 Environment Configuration

Create `.env` file:

```
VITE_API_URL=http://localhost:8080/api
```

---

# 🌐 API Communication

Frontend requests go through API Gateway:

```
React Application
        |
        |
        ↓
API Gateway
        |
        |
--------------------------------
|        |        |             |
User   Flight   Booking     Payment
API    API      API         API
```

Axios interceptor automatically adds JWT token:

```
Authorization: Bearer <JWT_TOKEN>
```

---

# 🔄 Booking Lifecycle

## Step 1: Search Flight

User searches:

```
Delhi → Mumbai
Date
```

Frontend calls:

```
Flight Management Service
```

---

## Step 2: Create Booking

Passenger details submitted:

```
Frontend
    |
    ↓
Booking Service
    |
    ↓
Booking Status: PENDING
```

---

## Step 3: Payment

User completes payment:

```
Frontend
    |
    ↓
Payment Service
    |
    ↓
Razorpay
```

---

## Step 4: Confirmation

After successful verification:

```
Payment SUCCESS
        |
        ↓
Booking CONFIRMED
        |
        ↓
PNR Generated
```

---

# 📦 Production Build

Create optimized build:

```bash
npm run build
```

Preview production build:

```bash
npm run preview
```

---

# 🔮 Future Enhancements

- Notification dashboard
- Real-time flight status
- WebSocket updates
- Admin dashboard
- Kafka event integration
- Cloud deployment
- CI/CD pipeline
- Mobile application


---

# 👨‍💻 Developer

**Vijay Kumar Soni**

Project:

**SkyWing Flight Booking System**

Architecture:

**Spring Boot Microservices + React + Docker + Kafka**