import React from 'react';
import VideoComponent from './VideoComponent';
import RandomProduct from './RandomProduct';
import Footer from './Footer';
import NavbarHeader from './Navbar';


const HomePage = () => {
  return (
    <>
      <NavbarHeader/>
      <VideoComponent></VideoComponent>
      <RandomProduct></RandomProduct>
      <Footer/>
    </>
  )
}  
export default HomePage