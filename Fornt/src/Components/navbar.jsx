import React, { useEffect } from "react";
import "bootstrap/dist/css/bootstrap.min.css";
import "bootstrap-icons/font/bootstrap-icons.css";
import NavDropdown from "react-bootstrap/NavDropdown";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import { faRightToBracket } from "@fortawesome/free-solid-svg-icons";
import { faCartShopping } from "@fortawesome/free-solid-svg-icons";
import { toast } from "react-toastify";
import { getSubcategoryById, logoutApi } from "../Services/UserService";
import SearchBar from "./SearchBar";
import { getCategory } from "../Services/UserService";
import { useState } from "react";
import { useNavigate } from 'react-router-dom';


const handleCategory = async (getcategory) => {
  const data = await getCategory();
  const name = data.data.map(item => item.catergory_name);
  getcategory(name);
} 

const handleSubCategory = async (getsubcategory, id) => {
  const data = await getSubcategoryById(id);
  const name = data.data.map(item => item.sub_category_name);
  getsubcategory(name);
} 

 



// const [subcategory, setsubcategory] = useState([]);



const NavbarHeader = () => {
  const navigate = useNavigate();
  const [category, setcategory] = useState([]);
  const [subcategory, setsubcategory] = useState([]);

  useEffect(() => {
    handleCategory(setcategory);
    // handleLogout();
    
  }, []); 
  
  const handleLogout = async () => {
    try {
      const response = await logoutApi(localStorage.getItem("token"));

      localStorage.removeItem("token");
      toast.success("Log out");
      navigate("/");
    } catch (error) {
      toast.error(error);
    }
  };

  return (
    <>
    
      <nav
        style={{ marginBottom: 0 }}
        className="navbar navbar-expand-lg bg-white navbar-light shadow-sm py-3 py-lg-0 px-3 px-lg-0 mb-5"
      >
        <a href="index.html" className="navbar-brand ms-lg-5">
          <h1 className="m-0 text-uppercase text-dark">
            <i
              style={{ color: "green" }}
              className="bi bi-shop fs-1 text-primary me-3"
            ></i>
            Pet Shop
          </h1>
        </a>

        <div className="collapse navbar-collapse" id="navbarCollapse">
          <SearchBar></SearchBar>

          {/* <div className="navbar-nav ms-auto py-0"> */}
          <a href="index.html" className="nav-item nav-link">
            Home
          </a>
          
          {category.map((name, index) => (
            <NavDropdown title={name} key={index+1}>
              <NavDropdown.Item></NavDropdown.Item>
            </NavDropdown>
            })
          }
          */}
          

          <NavDropdown title="User" id="basic-nav-dropdown">
            

            <NavDropdown.Item
              onClick={() => {
                {
                  handleLogout();
                }
              }}
            >
              <FontAwesomeIcon
                onClick={() => {
                  handleLogout();
                }}
                icon={faRightToBracket}
                style={{ transform: "rotateY(180deg)" }}
              ></FontAwesomeIcon>
              Log out
            </NavDropdown.Item>

            <NavDropdown.Divider />
            <NavDropdown.Item href="#action/3.4">
              <FontAwesomeIcon icon={faCartShopping} />
              Cart
            </NavDropdown.Item>
          </NavDropdown>
          {/* </div> */}
        </div>
      </nav>
    </>
  );
};

export default NavbarHeader;
