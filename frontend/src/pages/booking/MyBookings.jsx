import React, { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import { getMyBookings } from "../../api/bookingApi";
import "../../styles/MyBookings.css";

const PAGE_SIZE = 10;

function MyBookings() {
    const [bookings, setBookings] = useState([]);
    const [page, setPage] = useState(0);
    const [totalPages, setTotalPages] = useState(0);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState("");

    const navigate = useNavigate();

    useEffect(() => {
        fetchBookings(0);
    }, []);

    const fetchBookings = async (pageNumber = 0) => {
        try {
            setLoading(true);
            setError("");

            const response = await getMyBookings(
                pageNumber,
                PAGE_SIZE
            );

            console.log(
                "MY BOOKINGS RESPONSE:",
                response.data
            );

            const pageData = response.data?.data;

            const content = Array.isArray(pageData?.content)
                ? pageData.content
                : [];

            setBookings(content);
            setPage(pageData?.number ?? 0);
            setTotalPages(pageData?.totalPages ?? 0);

        } catch (error) {
            console.error(
                "Failed to fetch bookings:",
                error
            );

            setBookings([]);

            setError(
                error.response?.data?.message ||
                "Unable to fetch booking history."
            );
        } finally {
            setLoading(false);
        }
    };

    const handlePrevious = () => {
        if (page > 0 && !loading) {
            fetchBookings(page - 1);
        }
    };

    const handleNext = () => {
        if (
            page < totalPages - 1 &&
            !loading
        ) {
            fetchBookings(page + 1);
        }
    };

    const getStatusClass = (status) => {
        switch (status?.toUpperCase()) {
            case "CONFIRMED":
                return "confirmed";

            case "CANCELLED":
                return "cancelled";

            case "PENDING":
                return "pending";

            case "FAILED":
                return "failed";

            default:
                return "default";
        }
    };

    const formatAmount = (amount) => {
        if (amount == null) {
            return "--";
        }

        const numericAmount = Number(amount);

        if (Number.isNaN(numericAmount)) {
            return "--";
        }

        return `₹${numericAmount.toLocaleString("en-IN")}`;
    };

    const formatDate = (date) => {
        if (!date) {
            return "--";
        }

        const parsedDate = new Date(date);

        if (Number.isNaN(parsedDate.getTime())) {
            return date;
        }

        return parsedDate.toLocaleDateString(
            "en-IN",
            {
                day: "2-digit",
                month: "short",
                year: "numeric"
            }
        );
    };

    /*
     * Supports both possible backend field names.
     *
     * Preferred:
     * destinationAirport
     *
     * Fallback:
     * destinationAirportCode
     */
    const getDestination = (booking) => {
        return (
            booking.destinationAirport ||
            booking.destinationAirportCode ||
            "--"
        );
    };

    const getSource = (booking) => {
        return (
            booking.sourceAirport ||
            booking.sourceAirportCode ||
            "--"
        );
    };

    if (loading) {
        return (
            <main className="my-bookings-page">

                <div className="container">

                    <header className="bookings-header">

                        <div>
                            <h1>
                                My Bookings
                            </h1>

                            <p>
                                View and manage your flight bookings.
                            </p>
                        </div>

                    </header>

                    <div className="bookings-loading">

                        <div className="booking-spinner"></div>

                        <span>
                            Loading bookings...
                        </span>

                    </div>

                </div>

            </main>
        );
    }

    if (error) {
        return (
            <main className="my-bookings-page">

                <div className="container">

                    <header className="bookings-header">

                        <div>
                            <h1>
                                My Bookings
                            </h1>

                            <p>
                                View and manage your flight bookings.
                            </p>
                        </div>

                    </header>

                    <section className="bookings-error">

                        <div className="error-icon">
                            !
                        </div>

                        <h3>
                            Unable to load bookings
                        </h3>

                        <p>
                            {error}
                        </p>

                        <button
                            className="secondary-btn"
                            onClick={() =>
                                fetchBookings(page)
                            }
                        >
                            Try Again
                        </button>

                    </section>

                </div>

            </main>
        );
    }

    return (
        <main className="my-bookings-page">

            <div className="container">

                {/* ===============================
                    HEADER
                =============================== */}

                <header className="bookings-header">

                    <div>

                        <h1>
                            My Bookings
                        </h1>

                        <p>
                            View and manage your flight bookings.
                        </p>

                    </div>

                    {bookings.length > 0 && (
                        <span className="booking-count">
                            {bookings.length} booking
                            {bookings.length !== 1
                                ? "s"
                                : ""}
                        </span>
                    )}

                </header>


                {/* ===============================
                    EMPTY STATE
                =============================== */}

                {bookings.length === 0 ? (

                    <section className="bookings-empty">

                        <div className="empty-icon">
                            ✈
                        </div>

                        <h2>
                            No bookings yet
                        </h2>

                        <p>
                            Your booked flights will appear here.
                        </p>

                        <button
                            className="primary-btn"
                            onClick={() =>
                                navigate("/")
                            }
                        >
                            Search Flights
                        </button>

                    </section>

                ) : (

                    <>

                        {/* ===============================
                            BOOKING LIST
                        =============================== */}

                        <section className="booking-list">

                            {bookings.map((booking) => {

                                const status =
                                    booking.bookingStatus;

                                const source =
                                    getSource(booking);

                                const destination =
                                    getDestination(booking);

                                return (

                                    <article
                                        className="booking-item"
                                        key={booking.bookingId}
                                    >

                                        <div className="booking-main">

                                            {/* ===============================
                                                TOP SECTION
                                            =============================== */}

                                            <div className="booking-top">

                                                <div className="airline-info">

                                                    <div className="airline-icon">
                                                        ✈
                                                    </div>

                                                    <div>

                                                        <h2>
                                                            {booking.airlineName ||
                                                                "Airline"}
                                                        </h2>

                                                        <span>
                                                            {booking.flightNumber ||
                                                                "--"}
                                                        </span>

                                                    </div>

                                                </div>


                                                <span
                                                    className={`status-badge ${getStatusClass(
                                                        status
                                                    )}`}
                                                >
                                                    {status ||
                                                        "UNKNOWN"}
                                                </span>

                                            </div>


                                            {/* ===============================
                                                FLIGHT ROUTE
                                            =============================== */}

                                            <div className="flight-route">

                                                {/* FROM */}

                                                <div className="route-point">

                                                    <span className="route-label">
                                                        FROM
                                                    </span>

                                                    <strong className="airport-code">
                                                        {source}
                                                    </strong>

                                                </div>


                                                {/* FLIGHT LINE */}

                                                <div className="route-line">

                                                    <span className="route-plane">
                                                        ✈
                                                    </span>

                                                    <span className="route-line-track"></span>

                                                </div>


                                                {/* TO */}

                                                <div className="route-point destination">

                                                    <span className="route-label">
                                                        TO
                                                    </span>

                                                    <strong className="airport-code">
                                                        {destination}
                                                    </strong>

                                                </div>

                                            </div>


                                            {/* ===============================
                                                BOOKING META
                                            =============================== */}

                                            <div className="booking-meta">

                                                {/* Travel Date */}

                                                <div className="meta-item">

                                                    <span>
                                                        Travel Date
                                                    </span>

                                                    <strong>
                                                        {formatDate(
                                                            booking.travelDate
                                                        )}
                                                    </strong>

                                                </div>


                                                {/* PNR */}

                                                <div className="meta-item">

                                                    <span>
                                                        PNR
                                                    </span>

                                                    <strong className="pnr">
                                                        {booking.pnr ||
                                                            "--"}
                                                    </strong>

                                                </div>


                                                {/* Seat */}

                                                <div className="meta-item">

                                                    <span>
                                                        Seat
                                                    </span>

                                                    <strong>
                                                        {booking
                                                            .seatNumbers
                                                            ?.length
                                                            ? booking.seatNumbers.join(
                                                                ", "
                                                            )
                                                            : "--"}
                                                    </strong>

                                                </div>


                                                {/* Amount */}

                                                <div className="meta-item">

                                                    <span>
                                                        Amount
                                                    </span>

                                                    <strong className="amount">
                                                        {formatAmount(
                                                            booking.totalFare
                                                        )}
                                                    </strong>

                                                </div>

                                            </div>

                                        </div>


                                        {/* ===============================
                                            RIGHT ACTION SECTION
                                        =============================== */}

                                        <div className="booking-action">

                                            {/* Booking Reference */}

                                            <div className="reference">

                                                <span className="reference-label">
                                                    BOOKING REFERENCE
                                                </span>

                                                <strong>
                                                    {booking.bookingReference ||
                                                        "--"}
                                                </strong>

                                            </div>


                                            {/* View Details */}

                                            <button
                                                className="details-btn"
                                                onClick={() =>
                                                    navigate(
                                                        `/my-bookings/${booking.bookingId}`
                                                    )
                                                }
                                            >
                                                <span>
                                                    View Details
                                                </span>

                                                <span className="details-arrow">
                                                    →
                                                </span>

                                            </button>

                                        </div>

                                    </article>
                                );
                            })}

                        </section>


                        {/* ===============================
                            PAGINATION
                        =============================== */}

                        {totalPages > 1 && (

                            <nav
                                className="booking-pagination"
                                aria-label="Booking pagination"
                            >

                                <button
                                    className="pagination-btn"
                                    disabled={
                                        page === 0 ||
                                        loading
                                    }
                                    onClick={
                                        handlePrevious
                                    }
                                >
                                    ← Previous
                                </button>


                                <span className="pagination-info">

                                    Page{" "}

                                    <strong>
                                        {page + 1}
                                    </strong>

                                    {" "}of{" "}

                                    <strong>
                                        {totalPages}
                                    </strong>

                                </span>


                                <button
                                    className="pagination-btn"
                                    disabled={
                                        page >=
                                            totalPages - 1 ||
                                        loading
                                    }
                                    onClick={
                                        handleNext
                                    }
                                >
                                    Next →

                                </button>

                            </nav>

                        )}

                    </>

                )}

            </div>

        </main>
    );
}

export default MyBookings;