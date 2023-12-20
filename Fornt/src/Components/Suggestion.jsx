import React from "react";

const Suggestion=()=>{
    return(
        <div className="product-suggestions">
                        <h4 className='title-goi-y'>Có thể bạn thích</h4>
                        <div className="row suggest-content">
                            <div className="col mb-4 text-center suggest-card">
                                <img className="" src={"/usecases/error.jpg"} alt="Description of the image" />
                                <h6>Mèo vippro</h6>
                                <p style={{ color: 'red', fontSize: '20px', fontWeight: 'bold' }}>300.000</p>
                            </div>
                        </div>
                    </div>
    )
}

export default Suggestion