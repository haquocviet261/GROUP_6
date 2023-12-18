import React, { useEffect, useState } from "react";
import { Container, Form, Button, Row, Col } from "react-bootstrap";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import { toast } from "react-toastify";
import { loginApi } from "../Services/UserService";
import { faSpinner } from "@fortawesome/free-solid-svg-icons";
import { Navigate, useNavigate } from "react-router-dom";
import NavBar from "../Components/NavBar";
import NavbarHeader from "../Components/NavBar";
import Footer from './Footer'



function LoginPage() {
  const navigate = useNavigate();
  const [loadingApi, setloadingApi] = useState();
  const [email, setemail] = useState("");
  const [psw, setpsw] = useState("");

  useEffect(() => {
    let token = localStorage.getItem("token")
    if (token) {
      navigate("/");
    }
  }, [])


  const handleLogin = async () => {
    setloadingApi(true);
    let res = await loginApi(email, psw);
    // console.log("Response: ", res);
    // console.log("TOken:", res.data.access_token);

    if (res && res.data) {
      toast.success("Loign Succes");
      localStorage.setItem("token", res.data.access_token);
      navigate("/");
    } else {
      // if(res && res.status == 403){
      //     toast.error("user not found");
      toast.error("user not found");
    }
    setloadingApi(false);
  };

  return (
    <>
      <NavbarHeader />
      <div className="login-container col-12 col-sm-4">
        <i className="fas fa-spinner fa-spin"></i>
        <div className="title-login">Login</div>
        <input
          className="input-login"
          type="text"
          placeholder="Username..."
          value={email}
          onChange={(e) => {
            setemail(e.target.value);
          }}
        />
        <div>
          <input
            style={{ marginTop: "5px" }}
            className="input-login"
            type="password"
            placeholder="Password..."
            value={psw}
            onChange={(e) => {
              setpsw(e.target.value);
            }}
          />
        </div>
        <button
          disabled={email && psw ? false : true}
          className={email && psw ? "active" : "null"}
          onClick={() => handleLogin()}
          style={{ border: "none" }}
        >
          {loadingApi ? <FontAwesomeIcon icon={faSpinner} spin /> : null}
          Login
        </button>
        <div style={{ marginTop: "10px" }} >
          <a className="a-login" href="/register" rel="noopener noreferrer">
            Register
          </a>
          <a className="a-login" href="/forgotpassword" rel="noopener noreferrer" style={{ float: "right" }}>
            Forgot password
          </a>
        </div>
      </div>
      <Footer />
    </>
  );
}

export default LoginPage;
