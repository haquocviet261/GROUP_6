import React, { useState } from "react";
import NavbarHeader from "../Components/navbar";
import Footer from './Footer';
import { Link } from 'react-router-dom';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faArrowLeft, faLock } from '@fortawesome/free-solid-svg-icons';

const initFormValue = {
    password: "",
    confirmPassword: "",
};

const isEmptyValue = (value) => {
    return !value || value.trim().length < 1;
};



const NewPassword = () => {
    const [formValue, setFormValue] = useState(initFormValue);
    const [formError, setFormError] = useState({});

    const validateForm = () => {
        const error = {};

        if (isEmptyValue(formValue.password)) {
            error["password"] = "Password is required";
        }
        if (isEmptyValue(formValue.confirmPassword)) {
            error["confirmPassword"] = "Confirm password is required";
        } else if (formValue.confirmPassword !== formValue.password) {
            error["confirmPassword"] = "Confirm password not match";
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
            <div className="login-container col-12 col-sm-4">
                <i className="fas fa-spinner fa-spin"></i>
                <Link to="/login">
                    <FontAwesomeIcon icon={faArrowLeft} style={{ color: '#fcb900' }} />
                </Link>
                <div className="title-login">New password <FontAwesomeIcon icon={faLock} /></div>
                <form onSubmit={handleSubmit}>
                    <div className="mb-2-register">
                        <label htmlFor="password" className="form-label-register">
                            Mật khẩu
                        </label>
                        <input
                            id="password"
                            className="input-register"
                            type="password"
                            name="password"
                            value={formValue.password}
                            onChange={handleChange}></input>
                        {formError.password && (
                            <div className="error-register">{formError.password}</div>
                        )}
                    </div>
                    <div className="mb-2-register">
                        <label htmlFor="confirm-password" className="form-label-register">
                            Nhập lại mật khẩu
                        </label>
                        <input
                            id="confirm-password"
                            className="input-register"
                            type="password"
                            name="confirmPassword"
                            value={formValue.confirmPassword}
                            onChange={handleChange}></input>
                        {formError.confirmPassword && (
                            <div className="error-register">{formError.confirmPassword}</div>
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

export default NewPassword;