import { useState } from "react";
import "./App.scss";
import LoginPage from "./Components/LoginPage";

import { Container } from "react-bootstrap";
import { ToastContainer } from "react-toastify";
import "react-toastify/dist/ReactToastify.css";
import HomePage from "./Components/HomePage";
import NavbarHeader from "./Components/NavBar";
// import { UserProvider } from "./Context/UserContext.jsX";
import { UserContext } from "./Context/UserContext.jsX";
import { useContext } from "react";
import SearchBar from "./Components/SearchBar";
import AppRoutes from "./Routes/AppRoutes";
import { Route, Routes, Link } from "react-router-dom";
import RegisterAccount from "./Components/RegisterAccount";
import ForgotPassword from "./Components/ForgotPasswordPage";
import NewPassword from "./Components/NewPassword";
function App() {
  const [count, setCount] = useState(0);

  
  return (
    // <UserProvider>
      <div className="app-container">
          
          <Routes>
        <Route path="/" element={<HomePage />} />
        <Route path="test" element={<SearchBar />} />
        <Route path="/login" element={<LoginPage />} />
        <Route path="/register" element={<RegisterAccount/>}></Route>
        <Route path="/forgotpassword" element={<ForgotPassword/>}/>
        <Route path="/newpassword" element={<NewPassword/>}/>
      </Routes>

          {/* Same as */}
          <ToastContainer
            position="top-right"
            autoClose={5000}
            hideProgressBar={false}
            newestOnTop={false}
            closeOnClick
            rtl={false}
            pauseOnFocusLoss
            draggable
            pauseOnHover
            theme="light"
          />
        
        {/* <NavbarHeader /> */}
        {/* <LoginPage /> */}
       
      </div>
    // </UserProvider>
  );
}

export default App;
