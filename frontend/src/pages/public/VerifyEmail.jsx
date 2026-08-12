import React, { useEffect, useState } from "react";
import { useSearchParams, useNavigate } from "react-router-dom";

import { verifyEmail } from "../../api/authApi";


function VerifyEmail() {


    const [searchParams] = useSearchParams();

    const navigate = useNavigate();


    const [message, setMessage] = useState(
        "Verifying your email..."
    );


    useEffect(() => {


        const token =
            searchParams.get("token");



        if (!token) {

            setMessage(
                "Invalid verification link."
            );

            return;

        }




        verifyEmail(token)

            .then((response) => {


                console.log(
                    response.data
                );


                setMessage(
                    "Email verified successfully. Redirecting to login..."
                );



                setTimeout(() => {

                    navigate("/login");

                }, 2000);



            })


            .catch((error) => {


                console.error(error);


                setMessage(

                    error.response?.data?.message
                    ||
                    "Email verification failed."

                );


            });



    }, [searchParams, navigate]);





    return (


        <div className="container mt-5">


            <div className="row justify-content-center">


                <div className="col-md-6">


                    <div className="card shadow">


                        <div className="card-body text-center">


                            <h3>

                                Email Verification

                            </h3>



                            <p className="mt-3">

                                {message}

                            </p>



                        </div>


                    </div>


                </div>


            </div>


        </div>


    );


}


export default VerifyEmail;