import React from 'react'
import NavbarHeader from './NavBar'
import VideoComponent from './VideoComponent'
import { getCategory } from '../Services/UserService'
import { useState } from 'react'

const HomePage = () => {
  const categoryNames = [];
  
  return (
    <>
      <NavbarHeader />
      <VideoComponent></VideoComponent>
      
    </>
  )
}

export default HomePage