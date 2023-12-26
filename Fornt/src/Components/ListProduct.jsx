import React, { useEffect, useState } from "react";
import { getAllProduct } from "../Services/UserService";
import { getProductbySubCategory } from "../Services/UserService";
import {formatPrice} from "../Services/UserService";
import { useLocation } from 'react-router-dom'
import { useNavigate } from "react-router-dom";

const ListProduct = () => {
  const [listproduct, setlistproduct] = useState([]);
  const location = useLocation();
  const sc = location.state;
  
  const navigate =  useNavigate();

  const onclickImage =  (item) => {
    navigate('/productdetails', { state: item });
  }

  const fetchProducts = async () => {
    try {
      const response = await getAllProduct(sc.sub_id);
      if (!response || !Array.isArray(response)) {
        throw new Error("Invalid product data received");
      }
      setlistproduct(response);
      console.log("Fetched products:", response);
    } catch (error) {
      console.error("Error fetching products:", error.message || error);
    }
  };

  useEffect(() => {
    if (sc && sc.sub_id) {
      fetchProducts();
    }
  }, [sc && sc.sub_id]);
  return (
    <>
      <div className="products-heading">
        <h3>Danh sách sản phẩm</h3>
      </div>
      <div className="products">
        {listproduct.length > 0 ? (
          listproduct.map((item,index) => (
            <div className="card" key={index}>
            <div onClick={() => {onclickImage(item)}}>
              <img className="card-image" src={item.image+".jpg"} 
                // alt="Không có ảnh cho sản phẩm này"  
                onError={(e) => {
                  e.target.src = '/usecases/error.jpg';
                }}
              />
            </div>
              <div onClick={() => {onclickImage(item)}}>
                <p className="card-title">{item.product_name}</p>
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
                <button>Thêm vào giỏ hàng</button>
              </div>
          </div>
          ))
        ) : (
          <p>Đang tải sản phẩm!</p>
        )}
      </div>
    </>
  );
};

export default ListProduct;
