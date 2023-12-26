import { useState } from "react";
import Footer from './Footer';
import NavbarHeader from "./Navbar";

export default function ProfileUser() {
    const [isEditing, setIsEditing] = useState(false);
    const [userName, setUserName] = useState("tranquocviet");
    const [firstName, setFirstName] = useState("Viet");
    const [lastName, setLastName] = useState("Tran");
    const [phone, setPhonne] = useState("0123456789");
    const [email, setEmail] = useState("viettq@gmail.com");
    const [adress, setAdress] = useState("Địa chỉ");
    const [dateOfBirth, setDateOfBirth] = useState("10/12/2002");


    // const handleChangePassword = () => {

    //     history.push("/changePassword");
    // };

    return (
        <>
            <NavbarHeader />
            <div className="profileUserPage">

                <div className="title-profile">Profile</div>
                <form className="profilecontainer"
                    onSubmit={(e) => {
                        e.preventDefault();
                        setIsEditing(!isEditing);
                    }}>


                    <label className="label-profile">
                        User name: <b>{userName}</b>
                    </label>
                    <br />
                    <label className="label-profile">
                        First name:{" "}
                        {isEditing ? (
                            <input
                                value={firstName}
                                onChange={(e) => {
                                    setFirstName(e.target.value);
                                }}
                            />
                        ) : (
                            <b>{firstName}</b>
                        )}
                    </label>
                    <br />
                    <label className="label-profile">
                        Last name:{" "}
                        {isEditing ? (
                            <input
                                value={lastName}
                                onChange={(e) => {
                                    setLastName(e.target.value);
                                }}
                            />
                        ) : (
                            <b>{lastName}</b>
                        )}
                    </label>
                    <br />
                    <label className="label-profile">
                        Email: <b>{email}</b>
                    </label>
                    <br />
                    <label className="label-profile">
                        Phone number: <b>{phone}</b>
                    </label>
                    <br />

                    <label className="label-profile">
                        Adress:{" "}
                        {isEditing ? (
                            <input
                                value={adress}
                                onChange={(e) => {
                                    setAdress(e.target.value);
                                }}
                            />
                        ) : (
                            <b>{adress}</b>
                        )}
                    </label>
                    <br />
                    <label className="label-profile">
                        Date of birth:{" "}
                        {isEditing ? (
                            <input
                                type="date"
                                value={dateOfBirth}
                                onChange={(e) => {
                                    setDateOfBirth(e.target.value);
                                }}
                            />
                        ) : (
                            <b>{dateOfBirth}</b>
                        )}
                    </label>
                    <br />
                    <button className="button-profile" type="submit">{isEditing ? "save" : "edit"} Profile</button>
                    <button
                        className="button-profile-userChangePassword"
                        type="button"
                    // onClick={handleChangePassword}
                    >
                        Change Password
                    </button>
                    {}

                </form>
            </div>
            <Footer />
        </>
    );
}