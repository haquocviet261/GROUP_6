import React, { useEffect, useState } from "react";
import { Container, Form, Button, Row, Col } from "react-bootstrap";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import { toast } from "react-toastify";
import { loginApi } from "../Services/UserService";
import { faSpinner } from "@fortawesome/free-solid-svg-icons";
import { Navigate, useNavigate } from "react-router-dom";
import NavBar from "../Components/NavBar";
import NavbarHeader from "../Components/NavBar";


function LoginPage() {
  const navigate = useNavigate();
  const [loadingApi, setloadingApi] = useState();
  const [email, setemail] = useState("");
  const [psw, setpsw] = useState("");

  useEffect(() => {
    let token = localStorage.getItem("token")
    if(token){
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
    <NavbarHeader/>
       {/* <NavBar />  */}
      <div className="login-container col-12 col-sm-4">
        <i className="fas fa-spinner fa-spin"></i>
        <div className="title">Login</div>
        <div className="text">Username</div>
        <input
          type="text"
          placeholder="Username..."
          value={email}
          onChange={(e) => {
            setemail(e.target.value);
          }}
        />
        <div>
          <input
            type="text"
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
        >
          
          {loadingApi ? <FontAwesomeIcon icon={faSpinner} spin /> : null}
          Login
        </button>
        <div className="back">Go back</div>
      </div>
    </>
  );
}

export default LoginPage;
