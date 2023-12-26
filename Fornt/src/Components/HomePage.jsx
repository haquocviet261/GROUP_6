import React, { useEffect, useState } from "react";
import VideoComponent from './VideoComponent';
import RandomProduct from './RandomProduct';
import Footer from './Footer';
import NavbarHeader from './Navbar';
import Cart from "./Cart";


const HomePage = () => {


  return (
    <>
      <NavbarHeader/>
      <VideoComponent></VideoComponent>
      <RandomProduct></RandomProduct>
      <Cart></Cart>
      <Footer></Footer>
    </>
  )
}  
export default HomePage