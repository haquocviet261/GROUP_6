import React from 'react'
import NavbarHeader from './NavBar'
import VideoComponent from './VideoComponent'
import { getCategory } from '../Services/UserService'
import { useState } from 'react'

const HomePage = () => {
  const [categoryName, setcategoryName] = useState("");
  
  const handleCategory = async () => {
    const result = await getCategory();
    result.data.map(product => {
      console.log(product.catergory_name);
    })
  }
  
  handleCategory();
  return (
    <>
      <NavbarHeader />
      <VideoComponent></VideoComponent>
      
    </>
  )
}

export default HomePage