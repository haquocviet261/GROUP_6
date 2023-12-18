import React from 'react'
import NavbarHeader from './navbar';
import VideoComponent from './VideoComponent'
import { getCategory } from '../Services/UserService'
import { useState } from 'react'
import RandomProduct from './RandomProduct'
import Footer from './Footer'

const HomePage = () => {
  const [categoryName, setcategoryName] = useState("");

  const handleCategory = async () => {
    const result = await getCategory();
    result.data.map(product => {
      console.log(product.catergory_name);
    })
  }
  const categoryNames = [];
  handleCategory();
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