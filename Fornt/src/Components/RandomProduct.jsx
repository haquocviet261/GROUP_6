import React, { useEffect, useState } from "react";
import { getRandomProduct } from "../Services/UserService";
import { useNavigate } from "react-router-dom";
import {formatPrice} from "../Services/UserService";
const RandomProduct =  () => {
  const [randomProduct, setRandomProduct] = useState([]);
  const navigate =  useNavigate();
  const onclickImage =  (item) => {
    navigate('/productdetails', { state: item });
  }

  const handleProduct = async () => {
    try {
      const data = await getRandomProduct("/api/v1/home/random");
      const products = data.data.map((item,key) => ({
        id: item.product_id,
        name: item.product_name,
        quantity: item.quantity,
        price: item.price,
        description: item.description,
        image: item.image,
        discount: item.discount,
      }));
      setRandomProduct(products);
      console.log(products);
    } catch (error) {
      console.error("Error fetching random products:", error);
    }
  };

  useEffect(() => {
    handleProduct();
  }, []);

  return (
    <>
    <div className="products-heading">
    <h3>Những sản phẩm ngẫu nhiên</h3>
    </div>
      <div className="products">
        {randomProduct.map((item,index) => (
          <div className="card" key={item.id}>
            <div>
              <img className="card-image pointer" src={item.image+".jpg"} 
                // alt="Không có ảnh cho sản phẩm này"  
                onError={(e) => {
                  e.target.src = '/usecases/error.jpg';
                }}

                onClick={() => {onclickImage(item)}}
                // onClick = {() => navigate(`/product/${JSON.stringify(item)}`)}
                // navigate('/productDetails', { state: item });
              />
            </div>
              <div className="card-content pointer"onClick={() => {onclickImage(item)}}>
                <p className="card-title">{item.name}</p>
              </div>
              
              <div>
                {
                formatPrice(item.price, item.discount).hasDiscount ? (
                <>
                  <span style={{ textDecoration: "line-through", color: "red" }}>
                  {formatPrice(item.price, item.discount).originalPrice}
                  </span>{" "}
                  {formatPrice(item.price, item.discount).discountedPrice}
                </>
                ) : (
                <span>{formatPrice(item.price, item.discount).originalPrice}</span>
                )
                }
              </div>

              <div>
                <button>Add to Cart</button>
              </div>
          </div>
        ))}
      </div>
    </>
  );
};

export default RandomProduct;
