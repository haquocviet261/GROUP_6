import React from 'react'
import NavbarHeader from "./Navbar"
import { useState } from 'react'
import Footer from './Footer'
import { useLocation } from 'react-router-dom'
import {formatPrice} from "../Services/UserService";
import Suggestion from './Suggestion'

const productDetails = () => {

    const location = useLocation();
    const product = location.state;




    const [value, setValue] = useState(1);
    

    const handleInput = (e) => {
        let inputValue = parseInt(e.target.value, 10);
        if (isNaN(inputValue) || inputValue < 1) {
            inputValue = 1;
        }
        setValue(inputValue);
    };


    // id: item.product_id,
    // name: item.product_name,
    // quantity: item.quantity,
    // price: item.price,
    // description: item.description,
    // image: item.image,
    // discount: item.discount,

    return (
        <>
            <NavbarHeader />
            <div className="product-details-container">
                <div className="row productDetails">

                    <div className="col-6">
                        <img className="card-image" style={{marginLeft:'14%'}} src={product.image+".jpg"} 
                        // alt="Không có ảnh cho sản phẩm này"  
                        onError={(e) => {
                        e.target.src = '/usecases/error.jpg';
                        }}
                        />
                    </div>
                    <div className="col-6 products-details-description">
                        <form>
                            <label className="title-product-details">{product.name}</label>
                            <h3>Description</h3>
                            <h6>{product.description}</h6>
                            <div>
                            {
                                formatPrice(product.price, product.discount).hasDiscount ? (
                                    <>
                                        <span style={{ textDecoration: "line-through", color: "red" }}>
                                        {formatPrice(product.price, product.discount).originalPrice}
                                        </span>{" "}
                                        {formatPrice(product.price, product.discount).discountedPrice}
                                        </>
                                        ) 
                                        :
                                         (
                                        <span>{formatPrice(product.price, product.discount).originalPrice}</span>
                                )
                            }
                            </div>
                            <input type="number"
                                value={value}
                                onInput={handleInput}></input>
                            <button type="submit">Add to cart</button>
                        </form>
                    </div>

                </div>
                
            </div>
            <Suggestion/>

            <Footer />
        </>
    )
}

export default productDetails


