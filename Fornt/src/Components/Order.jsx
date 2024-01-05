import React, { useEffect } from "react";
import { useState } from "react";
import 'bootstrap/dist/css/bootstrap.min.css';
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import { faTruckFast, faCartShopping } from "@fortawesome/free-solid-svg-icons";

const Order =()=>{
    const [fullName, setFullName] = useState("");
    const [address, setAddress] = useState("");
    const [PhoneNumber,setPhoneNumber] = useState("");
const confirmorder =()=>{
    event.preventDefault();
    //ty xu ly backend
    console.log("Full Name:", fullName);
    console.log("Address:", address);
    console.log("PhoneNumber:", PhoneNumber);
}
return(
    <div className="order">

            <div className="container">
                <div className="row align-items-start billDetails mb-5">
                    <div className="col-3 ">
                        <b>BILLING DETAILS  <FontAwesomeIcon icon={faTruckFast} /></b>
                        <p style={{ border: 'none', marginTop: '20px' }}>Full name<span> *</span></p>

                        <input type="text" value={fullName}
                        onChange={(e) => setFullName(e.target.value)}/>

                        <p style={{ border: 'none', marginTop: '20px' }}>Phone number<span> *</span></p>

                        <input type="text" value={PhoneNumber}
                        onChange={(e) => setPhoneNumber(e.target.value)}/>

                        <p style={{ border: 'none', marginTop: '20px' }}>Address<span> *</span></p>
                        
                        <input type="text" value={address}
                        onChange={(e) => setAddress(e.target.value)}/>

                    </div>
                    <div className="col-3 mt-5">
                        <div>
                            <FontAwesomeIcon icon={faCartShopping} size="10x" color="#fcb000" />
                        </div>
                        <div className="mt-5">
                            <FontAwesomeIcon icon={faTruckFast} size="10x" color="#fcb000" />
                        </div>
                    </div>
                    <div className="col-6 form-order" style={{ padding: '30px' }}>
                        <form>
                            <b>YOUR ORDER <FontAwesomeIcon icon={faCartShopping} /></b>
                            <div className="row mt-3 ">
                                <div className="col-8">
                                    <p style={{ border: 'none', fontWeight: 'bold' }}>PRODUCT</p>
                                </div>
                                <div className="col-4 " style={{ textAlign: 'right' }}>
                                    <p style={{ border: 'none', fontWeight: 'bold' }}>SUBTOTAL</p>
                                </div>
                            </div>



                            <div className="row ">
                                <div>
                                <div style={{ display: 'flex', justifyContent: 'space-between', borderTop: '2px solid #d4cfcf' }}>
                                    <div>
                                        <p className='' >
                                            Pate cho chó Monge - Vị thịt vịt và cam ( 100g )
                                            <span style={{ color: 'gray', fontWeight: 'bold' }}> x 1</span>
                                        </p>
                                    </div>
                                    <div><span>27,000</span></div>
                                </div>
                                <div style={{ display: 'flex', justifyContent: 'space-between', borderTop: '2px solid #d4cfcf' }}>
                                    <div>
                                        <p className='' >
                                            Súp thưởng Wanpy nắp vặn - Tím ( 90G )
                                            <span style={{ color: 'gray', fontWeight: 'bold' }}> x 1</span>
                                        </p>
                                    </div>
                                    <div><span >20,000</span></div>
                                </div>
                                <div style={{ display: 'flex', justifyContent: 'space-between', borderTop: '2px solid #d4cfcf' }}>
                                    <div>
                                        <p className='' >
                                            Áo hoodie đồ bộ bốn chân quần yếm
                                            <span style={{ color: 'gray', fontWeight: 'bold' }}> x 1</span>
                                        </p>
                                    </div>
                                    <div><span>129,000</span></div>
                                </div>
                                </div>

                                <div style={{ display: 'flex', justifyContent: 'space-between', borderTop: '2px solid #d4cfcf' }}>
                                    <div>
                                        <p style={{ fontWeight: 'bold' }}>Total</p>
                                    </div>
                                    <div><span>174,000</span></div>
                                </div>
                                <p style={{ border: 'none', fontWeight: 'bold' }}>Cash on delivery</p>
                                <p style={{ border: 'none' }}>Pay with cash upon delivery.</p>
                            </div>

                            <div className="text-center">
                                <button onClick={confirmorder}>Confirm Order</button>
                            </div>
                        </form>
                    </div>
                </div>
            </div>
    </div>
)
}
export default Order