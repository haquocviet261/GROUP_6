import React from 'react'
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import { faRightToBracket } from "@fortawesome/free-solid-svg-icons";
import { NavDropdown } from 'react-bootstrap';
import { useNavigate } from 'react-router-dom';
const LoginButton = () => {
  const navigate = useNavigate();
  return (
    <>
<NavDropdown.Item onClick={()=>{navigate("/login")}}>
    <FontAwesomeIcon icon={faRightToBracket} /> Log in</NavDropdown.Item>
    </>
  )
}

export default LoginButton