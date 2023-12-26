import React, { useState } from "react";
import NavbarHeader from "../Components/navbar";
import Footer from './Footer';
import { Link } from 'react-router-dom';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faArrowLeft, faLock } from '@fortawesome/free-solid-svg-icons';

const initFormValue = {
    email: "",
};

const isEmptyValue = (value) => {
    return !value || value.trim().length < 1;
};

const isEmailValid = (email) => {
    return /^\w+([\.-]?\w+)*@\w+([\.-]?\w+)*(\.\w{2,3})+$/.test(email);
}

const ForgotPassword = () => {
    const [formValue, setFormValue] = useState(initFormValue);
    const [formError, setFormError] = useState({});

    const validateForm = () => {
        const error = {};

        if (isEmptyValue(formValue.email)) {
            error["email"] = "Email is required";
        } else {
            if (!isEmailValid(formValue.email))
                error["email"] = "Email is invalid";
        }

        setFormError(error);
        return Object.keys(error).length === 0;
    };

    const handleChange = (event) => {
        const { value, name } = event.target;
        setFormValue({
            ...formValue,
            [name]: value,
        });
    };

    const handleSubmit = (event) => {
        event.preventDefault();

        if (validateForm()) {
            console.log("form value", formValue);
        } else {
            console.log("form invalid")
        }

    };

    console.log(formError);
    return (
        <>
            <NavbarHeader />
            <div className="login-container col-12 col-sm-4" style={{ marginBottom: "100px" }}>
                <i className="fas fa-spinner fa-spin"></i>
                <Link to="/login">
                    <FontAwesomeIcon icon={faArrowLeft} style={{ color: '#fcb900' }} />
                </Link>
                <div className="title-login">Forgot Password <FontAwesomeIcon icon={faLock} /></div>
                <form onSubmit={handleSubmit}>
                    <div className="mb-2-register">
                        <input
                            id="email"
                            className="input-register"
                            type="text"
                            name="email"
                            placeholder="Your email"
                            value={formValue.email}
                            onChange={handleChange}></input>
                        {formError.email && (
                            <div className="error-register">{formError.email}</div>
                        )}
                    </div>
                    <div style={{ display: "flex", justifyContent: "center", alignItems: "center" }}>
                        <button type="submit" className="button-forgotpassword" style={{ width: "150px", height: "30px", display: "flex", alignItems: "center", justifyContent: "center" }}>
                            Send
                        </button>
                    </div>
                </form>
            </div>

            <Footer />
        </>
    )
}

export default ForgotPassword;