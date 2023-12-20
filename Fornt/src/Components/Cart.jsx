import React, { useEffect, useState } from 'react';
import { getCategory } from '../Services/UserService';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faShoppingCart } from '@fortawesome/free-solid-svg-icons';
import { Navigate } from 'react-router-dom';
function Cart() {
  const [categoryName, setcategoryName] = useState("");

  const handleCategory = async () => {
    const result = await getCategory();
    result.data.map(product => {
      console.log(product.category_name);
    });
  };

  handleCategory(); // You may need to manage state or utilize this data in your component

  return (
    <div className="cart-container">
      <div className="cart-icon" /*onClick={()=>{Navigate('')}}*/>
        <FontAwesomeIcon icon={faShoppingCart} className="cart-icon-svg" />
      </div>
    </div>
  );
}

export default Cart;