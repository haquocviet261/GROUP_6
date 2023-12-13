import { Route, Routes, Link } from "react-router-dom";
import LoginPage from "../Components/LoginPage";
import HomePage from "../Components/HomePage";
import SearchBar from "../Components/SearchBar";
import PrivateRoutes from "./PrivateRoutes";
import AdminPage from "../Components/AdminPage";

const AppRoutes = () => {
    // if(localStorage.getItem("token") == null){
    //     return( <>
    //             You dont have permisson to access this page
    //     </>)
    // }

  return (
    <>
      <Routes>
        <Route path="/" element={<HomePage />} />
        <Route path="test" element={<SearchBar />} />
        <Route path="/login" element={<LoginPage />} />
      </Routes>
      
      
      <PrivateRoutes path="/Admin">
        <AdminPage />
      </PrivateRoutes>
    </>
  );
};

export default AppRoutes;
