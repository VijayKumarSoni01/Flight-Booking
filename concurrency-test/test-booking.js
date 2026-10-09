require('dotenv').config();

const { chromium, request } = require('playwright');

const BASE_URL = 'http://localhost:5173';
const API_URL = 'http://localhost:8080';

const FLIGHT_ID = 2;
const FARE_ID = 2;
const TOTAL_AMOUNT = 4200;
const FLIGHT_DATE = '2026-09-12';

function createBookingRequest(
    contactEmail,
    contactPhone,
    passportNumber
) {
    return {
        flightId: FLIGHT_ID,
        fareId: FARE_ID,
        totalAmount: TOTAL_AMOUNT,

        contactEmail: contactEmail,
        contactPhone: contactPhone,

        cabinClass: 'ECONOMY',

        // Empty = automatic seat assignment
        seatNumbers: [],

        specialRequest: null,
        couponCode: null,

        paymentMethod: 'UPI',

        passengers: [
            {
                title: 'MR',

                firstName: 'Vijay',

                middleName: null,

                lastName: 'Soni',

                dateOfBirth: '2002-05-15',

                gender: 'MALE',

                passengerType: 'ADULT',

                nationality: 'INDIAN',

                passportNumber: passportNumber,

                passportExpiry: '2026-09-30',

                passportIssuingCountry: 'INDIA',

                // No manually selected seat
                seatNumber: null,

                seatPreference: null,

                mealPreference: 'VEG',

                specialAssistance: null
            }
        ]
    };
}

async function login(
    page,
    email,
    password,
    userName
) {

    console.log(`${userName}: opening website...`);

    await page.goto(BASE_URL);

    // Flight search
    await page.getByRole('textbox', {
        name: 'Enter airport code (DEL)'
    }).fill('BLR');

    await page.getByRole('textbox', {
        name: 'Enter airport code (BOM)'
    }).fill('DEL');

    await page.locator('input[name="departureDate"]')
        .fill(FLIGHT_DATE);

    await page.getByRole('button', {
        name: 'Search Flights'
    }).click();

    console.log(`${userName}: flight search completed`);

    // Select flight
    await page.getByRole('button', {
        name: 'Select Flight'
    }).click();

    // Economy
    await page.getByRole('button', {
        name: 'Recommended Economy ₹4,200'
    }).click();

    // Continue booking
    await page.getByRole('button', {
        name: 'Continue booking'
    }).click();

    console.log(`${userName}: login page reached`);

    // Login
    await page.getByRole('textbox', {
        name: 'Enter email or phone number'
    }).fill(email);

    await page.getByRole('textbox', {
        name: 'Enter password'
    }).fill(password);

    await page.locator('form')
        .getByRole('button', {
            name: 'Sign In'
        })
        .click();

    console.log(`${userName}: logged in`);

    // Wait until passenger page loads
    await page.getByRole('textbox', {
        name: 'Email *'
    }).waitFor({
        state: 'visible',
        timeout: 30000
    });

    console.log(`${userName}: passenger page ready`);

    // Wait for localStorage access token
    await page.waitForFunction(() => {
        return !!localStorage.getItem('accessToken');
    }, null, {
        timeout: 30000
    });

    const accessToken = await page.evaluate(() => {
        return localStorage.getItem('accessToken');
    });

    if (!accessToken) {
        throw new Error(
            `${userName}: accessToken not found in localStorage`
        );
    }

    console.log(
        `${userName}: access token found`
    );

    return accessToken;
}

async function sendBookingRequest(
    apiContext,
    accessToken,
    userName,
    bookingRequest
) {

    console.log('');
    console.log('==============================================');
    console.log(`${userName} BOOKING REQUEST`);
    console.log('==============================================');

    console.log(
        JSON.stringify(
            bookingRequest,
            null,
            2
        )
    );

    try {

        const response = await apiContext.post(
            `${API_URL}/api/private/user/bookings`,
            {
                headers: {
                    'Content-Type': 'application/json',

                    Authorization: `Bearer ${accessToken}`
                },

                data: bookingRequest
            }
        );

        const status = response.status();

        let body;

        try {
            body = await response.json();
        } catch {
            body = await response.text();
        }

        console.log('');
        console.log('==============================================');
        console.log(`${userName} BOOKING RESPONSE`);
        console.log('==============================================');

        console.log(
            'HTTP Status:',
            status
        );

        console.log(
            JSON.stringify(
                body,
                null,
                2
            )
        );

        return {
            status,
            body
        };

    } catch (error) {

        console.log('');
        console.log('==============================================');
        console.log(`${userName} REQUEST ERROR`);
        console.log('==============================================');

        console.log(error.message);

        return {
            status: null,
            body: null,
            error: error.message
        };
    }
}

function extractBookingData(result) {

    if (!result || !result.body) {
        return null;
    }

    if (result.body.data) {
        return result.body.data;
    }

    return result.body;
}

function extractSeats(data) {

    if (!data) {
        return [];
    }

    const possibleFields = [
        data.seats,
        data.seatNumbers,
        data.selectedSeats,
        data.bookingSeats,
        data.seatDetails
    ];

    for (const seats of possibleFields) {

        if (!Array.isArray(seats)) {
            continue;
        }

        return seats
            .map(seat => {

                if (typeof seat === 'string') {
                    return seat;
                }

                return (
                    seat.seatNumber ||
                    seat.number ||
                    seat.seat ||
                    null
                );

            })
            .filter(Boolean);
    }

    return [];
}

async function main() {

    if (
        !process.env.USER_A_EMAIL ||
        !process.env.USER_A_PASSWORD ||
        !process.env.USER_B_EMAIL ||
        !process.env.USER_B_PASSWORD
    ) {

        throw new Error(
            'USER_A_EMAIL, USER_A_PASSWORD, USER_B_EMAIL and USER_B_PASSWORD must be present in .env'
        );
    }

    console.log('');
    console.log('==============================================');
    console.log('       AUTOMATIC SEAT CONCURRENCY TEST');
    console.log('==============================================');
    console.log('');

    console.log(`Flight ID      : ${FLIGHT_ID}`);
    console.log(`Fare ID        : ${FARE_ID}`);
    console.log(`Flight Date    : ${FLIGHT_DATE}`);
    console.log('Route          : BLR -> DEL');
    console.log('Cabin          : ECONOMY');
    console.log('Seat Selection : AUTOMATIC');
    console.log('');

    const browser = await chromium.launch({
        headless: false
    });

    const contextA = await browser.newContext();
    const contextB = await browser.newContext();

    const pageA = await contextA.newPage();
    const pageB = await contextB.newPage();

    let apiContextA;
    let apiContextB;

    try {

        console.log('==============================================');
        console.log('LOGIN');
        console.log('==============================================');
        console.log('');

        /*
         * Both users login independently.
         *
         * Each browser context has its own localStorage,
         * therefore each user gets their own JWT.
         */

        const [
            accessTokenA,
            accessTokenB
        ] = await Promise.all([

            login(
                pageA,
                process.env.USER_A_EMAIL,
                process.env.USER_A_PASSWORD,
                'User A'
            ),

            login(
                pageB,
                process.env.USER_B_EMAIL,
                process.env.USER_B_PASSWORD,
                'User B'
            )

        ]);

        console.log('');
        console.log('==============================================');
        console.log('AUTHENTICATION SUCCESSFUL');
        console.log('==============================================');
        console.log('');

        console.log('User A JWT found.');
        console.log('User B JWT found.');
        console.log('');

        /*
         * Create independent API clients.
         */

        apiContextA = await request.newContext();

        apiContextB = await request.newContext();

        /*
         * Create booking requests.
         *
         * seatNumbers = []
         *
         * This is the important part.
         */

        const bookingRequestA = createBookingRequest(
            process.env.USER_A_PASSENGER_EMAIL ||
                'testusera@example.com',

            process.env.USER_A_PHONE ||
                '9000000001',

            'N1234567'
        );

        const bookingRequestB = createBookingRequest(
            process.env.USER_B_PASSENGER_EMAIL ||
                'testuserb@example.com',

            process.env.USER_B_PHONE ||
                '9000000002',

            'N7654321'
        );

        console.log('');
        console.log('==============================================');
        console.log('REQUEST CONFIGURATION');
        console.log('==============================================');
        console.log('');

        console.log(
            'User A seatNumbers:',
            JSON.stringify(
                bookingRequestA.seatNumbers
            )
        );

        console.log(
            'User B seatNumbers:',
            JSON.stringify(
                bookingRequestB.seatNumbers
            )
        );

        console.log('');
        console.log(
            'No seat is manually selected.'
        );

        console.log(
            'Flight Management must automatically'
        );

        console.log(
            'assign the seats.'
        );

        console.log('');

        console.log('==============================================');
        console.log('SENDING BOTH REQUESTS SIMULTANEOUSLY');
        console.log('==============================================');
        console.log('');

        const results = await Promise.all([

            sendBookingRequest(
                apiContextA,
                accessTokenA,
                'User A',
                bookingRequestA
            ),

            sendBookingRequest(
                apiContextB,
                accessTokenB,
                'User B',
                bookingRequestB
            )

        ]);

        const resultA = results[0];
        const resultB = results[1];

        const bookingA =
            extractBookingData(resultA);

        const bookingB =
            extractBookingData(resultB);

        const seatsA =
            extractSeats(bookingA);

        const seatsB =
            extractSeats(bookingB);

        console.log('');
        console.log('==============================================');
        console.log('FINAL RESULT');
        console.log('==============================================');
        console.log('');

        console.log(
            `User A HTTP Status: ${resultA.status}`
        );

        console.log(
            `User B HTTP Status: ${resultB.status}`
        );

        console.log('');

        console.log(
            'User A assigned seats:',
            seatsA
        );

        console.log(
            'User B assigned seats:',
            seatsB
        );

        console.log('');

        /*
         * Expected result:
         *
         * User A -> 201
         * User B -> 201
         *
         * AND
         *
         * Different seats.
         */

        if (
            resultA.status === 201 &&
            resultB.status === 201
        ) {

            console.log('==============================================');
            console.log('BOTH BOOKINGS CREATED');
            console.log('==============================================');

            if (
                seatsA.length > 0 &&
                seatsB.length > 0
            ) {

                const duplicateSeats =
                    seatsA.filter(
                        seat => seatsB.includes(seat)
                    );

                if (duplicateSeats.length === 0) {

                    console.log('');
                    console.log(
                        'SUCCESS: DIFFERENT SEATS ASSIGNED'
                    );

                    console.log('');
                    console.log(
                        `User A → ${seatsA.join(', ')}`
                    );

                    console.log(
                        `User B → ${seatsB.join(', ')}`
                    );

                } else {

                    console.log('');
                    console.log(
                        'FAILURE: SAME SEAT ASSIGNED'
                    );

                    console.log(
                        'Duplicate seats:',
                        duplicateSeats
                    );
                }

            } else {

                console.log('');
                console.log(
                    'Bookings succeeded, but assigned'
                );

                console.log(
                    'seat information was not found'
                );

                console.log(
                    'in the booking response.'
                );
            }

        } else {

            console.log('');
            console.log('==============================================');
            console.log('CONCURRENCY TEST FAILED');
            console.log('==============================================');
            console.log('');

            console.log(
                'User A status:',
                resultA.status
            );

            console.log(
                'User B status:',
                resultB.status
            );
        }

        console.log('');
        console.log('==============================================');
        console.log('DO NOT MAKE PAYMENT');
        console.log('==============================================');
        console.log('');

        console.log(
            'Both browser windows will remain open briefly.'
        );

        await pageA.waitForTimeout(15000);

    } catch (error) {

        console.log('');
        console.log('==============================================');
        console.log('TEST ERROR');
        console.log('==============================================');
        console.log('');

        console.error(error);

    } finally {

        if (apiContextA) {
            await apiContextA.dispose();
        }

        if (apiContextB) {
            await apiContextB.dispose();
        }

        await browser.close();
    }
}

main();