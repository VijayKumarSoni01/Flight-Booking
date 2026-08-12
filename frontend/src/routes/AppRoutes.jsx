import { BrowserRouter, Routes, Route } from "react-router-dom";

import Home from "../pages/public/Home";
import Login from "../pages/public/Login";
import Register from "../pages/public/Register";
import VerifyEmail from "../pages/public/VerifyEmail";

import ProtectedRoute from "../auth/ProtectedRoute";

import PublicLayout from "../layouts/PublicLayout";

import FlightSearch from "../pages/flight/FlightSearch";
import FlightDetails from "../pages/flight/FlightDetails";

import PassengerDetails from "../pages/booking/PassengerDetails";

import Payment from "../pages/payment/Payment";

import BookingSuccess from "../pages/booking/BookingSuccess";

function AppRoutes() {
  return (
    <BrowserRouter>
      <Routes>
        {/* =================
      PUBLIC
================= */}

        <Route
          path="/"
          element={
            <PublicLayout>
              <Home />
            </PublicLayout>
          }
        />

        <Route
          path="/login"
          element={
            <PublicLayout>
              <Login />
            </PublicLayout>
          }
        />

        <Route
          path="/register"
          element={
            <PublicLayout>
              <Register />
            </PublicLayout>
          }
        />

        <Route
          path="/verify-email"
          element={
            <PublicLayout>
              <VerifyEmail />
            </PublicLayout>
          }
        />

        {/* =================
      FLIGHTS
================= */}

        <Route
          path="/flights"
          element={
            <PublicLayout>
              <FlightSearch />
            </PublicLayout>
          }
        />

        <Route
          path="/flights/:id"
          element={
            <PublicLayout>
              <FlightDetails />
            </PublicLayout>
          }
        />

        {/* =================
      BOOKING FLOW
================= */}

        <Route
          path="/booking"
          element={
            <ProtectedRoute>
              <PublicLayout>
                <PassengerDetails />
              </PublicLayout>
            </ProtectedRoute>
          }
        />

        <Route
          path="/payment"
          element={
            <ProtectedRoute>
              <PublicLayout>
                <Payment />
              </PublicLayout>
            </ProtectedRoute>
          }
        />

        <Route
          path="/booking-success"
          element={
            <ProtectedRoute>
              <PublicLayout>
                <BookingSuccess />
              </PublicLayout>
            </ProtectedRoute>
          }
        />
      </Routes>
    </BrowserRouter>
  );
}

export default AppRoutes;
