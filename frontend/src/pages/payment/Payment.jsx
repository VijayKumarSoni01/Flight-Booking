import React, { useEffect, useState } from "react";

import { useLocation, useNavigate } from "react-router-dom";

import { createPaymentOrder, verifyPayment } from "../../api/paymentApi";

import { confirmBooking } from "../../api/bookingApi";

import "../../styles/payment.css";

function Payment() {
  const location = useLocation();

  const navigate = useNavigate();

  const { booking, flight, fare } = location.state || {};

  const [loading, setLoading] = useState(false);

  const [paymentStarted, setPaymentStarted] = useState(false);

  const [initialized, setInitialized] = useState(false);

  const [paymentAmount, setPaymentAmount] = useState(0);

  useEffect(() => {
    if (!booking) {
      alert("Booking information missing");

      navigate("/");

      return;
    }

    if (!initialized) {
      setInitialized(true);

      startPayment();
    }
  }, [booking]);

  const startPayment = async () => {
    try {
      setLoading(true);

      const paymentRequest = {
        bookingReference: booking.bookingReference,

        paymentMethod: "UPI",

        paymentGateway: "RAZORPAY",

        description: "Flight Booking Payment",
      };

      console.log("PAYMENT REQUEST:", paymentRequest);

      const response = await createPaymentOrder(paymentRequest);

      console.log("PAYMENT RESPONSE:", response.data);

      const order = response.data.data;

      if (!order || !order.gatewayOrderId) {
        throw new Error("Razorpay order creation failed");
      }

      setPaymentAmount(order.amount);

      if (!window.Razorpay) {
        alert("Razorpay SDK not loaded");

        return;
      }

      const options = {
        key: order.keyId,

        amount: order.amount * 100,

        currency: order.currency,

        name: "Flight Booking",

        description: `${flight?.airlineName || ""}
                    ${flight?.flightNumber || ""}`,

        order_id: order.gatewayOrderId,

        handler: async function (paymentResponse) {
          console.log("RAZORPAY RESPONSE:", paymentResponse);

          await verifyPaymentHandler(paymentResponse);
        },

        prefill: {
          email: booking.contactEmail || "",

          contact: booking.contactPhone || "",
        },

        theme: {
          color: "#2563eb",
        },
      };

      const razorpay = new window.Razorpay(options);

      razorpay.on("payment.failed", function (response) {
        console.log("PAYMENT FAILED:", response);

        alert("Payment failed");

        setPaymentStarted(false);
      });

      razorpay.open();

      setPaymentStarted(true);
    } catch (error) {
      console.error("PAYMENT ERROR:", error.response?.data || error);

      alert(error.response?.data?.message || error.message || "Payment failed");
    } finally {
      setLoading(false);
    }
  };

  const verifyPaymentHandler = async (payment) => {
    try {
      setLoading(true);

      const verifyRequest = {
        gatewayOrderId: payment.razorpay_order_id,

        gatewayPaymentId: payment.razorpay_payment_id,

        transactionId: payment.razorpay_payment_id,

        gatewayResponse: JSON.stringify(payment),

        signature: payment.razorpay_signature,
      };

      console.log("VERIFY REQUEST:", verifyRequest);

      const verifyResponse = await verifyPayment(verifyRequest);

      console.log("VERIFY RESPONSE:", verifyResponse.data);

      /*
                Payment verified successfully

                Confirm booking
            */

      const confirmResponse = await confirmBooking(booking.bookingId);

      console.log("CONFIRM RESPONSE:", confirmResponse.data);

      /*
              Depending on ApiResponse wrapper

              Case 1:
              {
                 data:{
                    bookingId,
                    pnr
                 }
              }


              Case 2:
              {
                 bookingId,
                 pnr
              }

            */

      const confirmedBooking =
        confirmResponse.data.data || confirmResponse.data;

      console.log("CONFIRMED BOOKING:", confirmedBooking);

      alert("Payment Successful");

      navigate("/booking-success", {
        state: {
          booking: confirmedBooking,

          flight,

          fare,
        },
      });
    } catch (error) {
      console.error("VERIFICATION ERROR:", error.response?.data || error);

      alert(error.response?.data?.message || "Payment verification failed");
    } finally {
      setLoading(false);
    }
  };

  if (!booking) {
    return (
      <div className="empty-box">
        <h2>Booking not found</h2>
      </div>
    );
  }

  return (
    <div className="payment-page">
      <div className="payment-card">
        <h2>Complete Payment</h2>

        <div className="flight-summary">
          <h3>{flight?.airlineName}</h3>

          <p>
            Flight:
            {flight?.flightNumber}
          </p>

          <p>
            Booking Reference:
            <b>{booking.bookingReference}</b>
          </p>

          <h2>
            ₹
            {paymentAmount > 0
              ? Number(paymentAmount).toLocaleString("en-IN")
              : "Loading..."}
          </h2>
        </div>

        {loading ? (
          <h3>Processing Payment...</h3>
        ) : (
          !paymentStarted && (
            <button className="payment-btn" onClick={startPayment}>
              Pay Now
            </button>
          )
        )}
      </div>
    </div>
  );
}

export default Payment;
