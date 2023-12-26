import React, { useEffect, useState } from 'react';
import NavbarHeader from './Navbar';
import { getCategory } from '../Services/UserService';
import Footer from './Footer';
import ListProduct from './ListProduct';

const List = () => {
  const [categoryNames, setCategoryNames] = useState([]);

  useEffect(() => {
    const fetchCategory = async () => {
      try {
        const result = await getCategory();
        const categories = result.data.map(product => product.category_name);
        setCategoryNames(categories);
      } catch (error) {
        console.error('Error fetching categories:', error);
      }
    };

    fetchCategory();
  }, []);

  return (
    <>
      <NavbarHeader/>
      <ListProduct/>
      <Footer />
    </>
  );
};

export default List;
