import { Route, Routes, Link } from "react-router-dom";
import LoginPage from "../Components/LoginPage";
import HomePage from "../Components/HomePage";
import SearchBar from "../Components/SearchBar";
import PrivateRoutes from "./PrivateRoutes";
import AdminPage from "../Components/AdminPage";
import RegisterAccount from "../Components/AdminPage";
import ForgotPassword from "../Components/ForgotPasswordPage";
import NewPassword from "../Components/NewPassword";
import ProfileUser from "../Components/ProfileUser";
import Cart from "../Components/Cart";
import ListProduct from "../Components/ListProductPage";
import ProductDetails from "../Components/ProductDetails";
import OrderPage from "../Components/OrderPage";
const AppRoutes = () => {
  // if(localStorage.getItem("token") == null){
  //     return( <>
  //             You dont have permisson to access this page
  //     </>)
  // }

  return (
    <Routes>
      <>
        <Route path="/" element={<HomePage />} />
        <Route path="test" element={<SearchBar />} />
        <Route path="/login" element={<LoginPage />} />
        <Route path="/register" element={<RegisterAccount />}></Route>
        <Route path="/forgotpassword" element={<ForgotPassword />} />
        <Route path="/newpassword" element={<NewPassword />} />
        <Route path="/admin" element={<AdminPage />} />
        <Route path="/profile" element={<ProfileUser />} />
        <Route path="/cart" element={<Cart/>} />
        <Route path="/list" element={<ListProduct/>} />
        <Route path="/productdetails" element={<ProductDetails />} />
        <Route path="/order" element={<OrderPage/>} />
        {/*       
      <PrivateRoutes path="/Admin">
        <AdminPage />
      </PrivateRoutes> */}

      </>
    </Routes>
  );
};

export default AppRoutes;
