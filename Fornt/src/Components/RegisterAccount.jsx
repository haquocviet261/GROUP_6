import React, { useState } from "react";
// import "./RegisterAccount.css";
// import { BrowserRouter as Router, Route, Switch, Link } from 'react-router-dom';
// import { Link } from 'react-router-dom';
import NavbarHeader from "../Components/NavBar";
import Footer from './Footer'


const initFormValue = {
    userName: "",
    firstName: "",
    lastName: "",
    email: "",
    phone: "",
    date: "",
    address: "",
    password: "",
    confirmPassword: "",
};

const isEmptyValue = (value) => {
    return !value || value.trim().length < 1;
};

const isEmailValid = (email) => {
    return /^\w+([\.-]?\w+)*@\w+([\.-]?\w+)*(\.\w{2,3})+$/.test(email);
}
export default function RegisterPage() {
    const [formValue, setFormValue] = useState(initFormValue);
    const [formError, setFormError] = useState({});

    const validateForm = () => {
        const error = {};

        if (isEmptyValue(formValue.userName)) {
            error["userName"] = "User Name is required";
        }
        if (isEmptyValue(formValue.firstName)) {
            error["firstName"] = "First Name is required";
        }
        if (isEmptyValue(formValue.lastName)) {
            error["lastName"] = "Last Name is required";
        }
        if (isEmptyValue(formValue.phone)) {
            error["phone"] = "Phone is required";
        }
        if (isEmptyValue(formValue.email)) {
            error["email"] = "Email is required";
        } else {
            if (!isEmailValid(formValue.email))
                error["email"] = "Email is invalid";
        }
        if (isEmptyValue(formValue.address)) {
            error["address"] = "Address is required";
        }
        if (isEmptyValue(formValue.date)) {
            error["date"] = "Date of birth is required";
        }
        if (isEmptyValue(formValue.phone)) {
            error["phone"] = "Phone is required";
        }
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
            <div class="container register">
                <div className="register-page">
                    <div className="register-form-container">
                        <h1 className="h1-register">Đăng ký</h1>

                        <form onSubmit={handleSubmit}>
                            <div className="mb-2-register">
                                <label htmlFor="user-name" className="form-label-register">
                                    Tên tài khoàn
                                </label>
                                <input
                                    id="user-name"
                                    className="input-register"
                                    type="text"
                                    name="userName"
                                    value={formValue.userName}
                                    onChange={handleChange}></input>
                                {formError.userName && (
                                    <div className="error-register">{formError.userName}</div>
                                )}
                            </div>
                            <div className="mb-2-register">
                                <label htmlFor="first-name" className="form-label-register">
                                    Tên của bạn
                                </label>
                                <input
                                    id="first-name"
                                    className="input-register"
                                    type="text"
                                    name="firstName"
                                    value={formValue.firstName}
                                    onChange={handleChange}></input>
                                {formError.firstName && (
                                    <div className="error-register">{formError.firstName}</div>
                                )}
                            </div>

                            <div className="mb-2-register">
                                <label htmlFor="last-name" className="form-label-register">
                                    Họ của bạn
                                </label>
                                <input
                                    id="last-name"
                                    className="input-register"
                                    type="text"
                                    name="lastName"
                                    value={formValue.lastName}
                                    onChange={handleChange}></input>
                                {formError.lastName && (
                                    <div className="error-register">{formError.lastName}</div>
                                )}
                            </div>
                            <div className="mb-2-register">
                                <label htmlFor="email" className="form-label-register">
                                    Email
                                </label>
                                <input
                                    id="email"
                                    className="input-register"
                                    type="text"
                                    name="email"
                                    value={formValue.email}
                                    onChange={handleChange}></input>
                                {formError.email && (
                                    <div className="error-register">{formError.email}</div>
                                )}
                            </div>
                            <div className="mb-2-register">
                                <label htmlFor="phone" className="form-label-register">
                                    Số điện thoại
                                </label>
                                <input
                                    id="phone"
                                    className="input-register"
                                    type="text"
                                    name="phone"
                                    value={formValue.phone}
                                    onChange={handleChange}></input>
                                {formError.phone && (
                                    <div className="error-register">{formError.phone}</div>
                                )}
                            </div>
                            <div className="mb-2-register">
                                <label htmlFor="date" className="form-label-register">
                                    Ngày sinh
                                </label>
                                <input
                                    id="date"
                                    className="input-register"
                                    type="date"
                                    name="date"
                                    value={formValue.date}
                                    onChange={handleChange}></input>
                                {formError.date && (
                                    <div className="error-register">{formError.date}</div>
                                )}
                            </div>
                            <div className="mb-2-register">
                                <label htmlFor="address" className="form-label-register">
                                    Địa chỉ
                                </label>
                                <input
                                    id="address"
                                    className="input-register"
                                    type="text"
                                    name="address"
                                    value={formValue.address}
                                    onChange={handleChange}></input>
                                {formError.address && (
                                    <div className="error-register">{formError.address}</div>
                                )}
                            </div>
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
                            <button type="submit" className="button-register">
                                Đăng ký
                            </button>
                            <div style={{ marginTop: "15px" }} >
                                Bạn đã có tài khoản?
                                <a href="/login" rel="noopener noreferrer" style={{ textDecoration: "none" }}> Đăng nhập</a>
                            </div>

                        </form>


                    </div>
                </div>
            </div>
            <Footer />
        </>
    )
}