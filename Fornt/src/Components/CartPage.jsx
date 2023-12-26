import React, { useEffect, useState } from "react";
import VideoComponent from './VideoComponent';
import RandomProduct from './RandomProduct';
import Footer from './Footer';
import NavbarHeader from './Navbar';
import Cart from "./Cart";


const CartPage = () => {


  return (
    <>
      <NavbarHeader/>
      <Cart></Cart>
      <Footer></Footer>
    </>
  )
}  
export default CartPage