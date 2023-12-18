import React, { useEffect, useState } from "react";
import { getRandomProduct } from "../Services/UserService";
const RandomProduct = () => {
  const [randomProduct, setRandomProduct] = useState([]);

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
    <h3>Gợi ý sản phẩm</h3>
    </div>
      <div className="products">
        {randomProduct.map((item,index) => (
          
          <div className="card" key={item.id}>
            <div>
              <img className="card-image" src={item.image+".jpg"} 
                // alt="Không có ảnh cho sản phẩm này"  
                onError={(e) => {
                  e.target.src = '/usecases/error.jpg';
                }}
              />
            </div>
              <div className="card-content">
                <p className="card-title">{item.name}</p>
              </div>
              <div>
                <p className="card-text">{item.price}đ</p>
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
