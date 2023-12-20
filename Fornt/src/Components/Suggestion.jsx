import React, { useEffect, useState } from "react";
import { getTopDiscount } from "../Services/UserService";
import {formatPrice} from "../Services/UserService";
import { useNavigate } from "react-router-dom";
const Suggestion = () => {
    const [listdiscount, setlistdiscount] = useState([]);
    const navigate =  useNavigate();
    const fetchProducts = async () => {
        try {
            const data = await getTopDiscount();
            const top10product = data.slice(0, 8);
            setlistdiscount(top10product);
        } catch (error) {
            console.error("Error fetching products:", error.message || error);
        }
    };
    const onclickImage =  (item) => {
        navigate('/productdetails', { state: item });
      }
    useEffect(() => {
        fetchProducts();
    }, []);

    return (
        <div className="product-suggestions">
            <h4 className='title-goi-y'>Có thể bạn thích</h4>
            <div className="row suggest-content">
                {listdiscount.map((product, index) => (
                    <div className="col mb-4 text-center suggest-card" key={index} onClick={() => {onclickImage(product)}}>

                    <img className="card-image" src={product.image+".jpg"} 
                    // alt="Không có ảnh cho sản phẩm này"  
                    onError={(e) => {
                        e.target.src = '/usecases/error.jpg';
                    }}
                    />                        
                        <h6>{product.product_name}</h6>
                            <div style={{marginBottom:'20px'}}>
                                {formatPrice(product.price, product.discount).hasDiscount ? (
                                <>
                                    <span style={{ textDecoration: "line-through", color: "red", fontSize: '20px', fontWeight: 'bold' }}>
                                    {formatPrice(product.price, product.discount).originalPrice}
                                    </span>{" "}
                                    {formatPrice(product.price, product.discount).discountedPrice}
                                </>
                                ) : 
                                (
                                <span style={{fontSize: '20px', fontWeight: 'bold'}}>{formatPrice(product.price, product.discount).originalPrice}</span>
                                )}
                            </div>

                    </div>
                ))}
            </div>
        </div>
    );
}

export default Suggestion;



