import React from "react";
// import "./Footer.css";
import 'bootstrap/dist/css/bootstrap.min.css';
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import { faBone, faDog, faCat, faHeart, faPaw, faPhone, faEnvelope, faLocationDot, faShieldDog, faShieldCat } from "@fortawesome/free-solid-svg-icons";


const Footer = () => {
    return (
        <div className=" footer">
            <div className="row ms-4">
                <div className="col-md-4 mt-3">
                    <div className="d-flex align-items-center">
                        <FontAwesomeIcon icon={faPaw} size="2x" />
                        <h2 className="ms-2 mb-0">Pet shop</h2>
                    </div>
                    <p>Pet Shop - Nơi Hạnh Phúc Cho Bạn Và Thú Cưng!</p>
                    <p>Chúng tôi cam kết mang đến cho cộng đồng những dịch vụ chất lượng nhất và trải nghiệm mua sắm thú vị.</p>
                    <p>Chất lượng là ưu tiên hàng đầu của chúng tôi. Tất cả sản phẩm tại Pet Shop được lựa chọn kỹ lưỡng, đảm bảo an toàn và đáng tin cậy cho thú cưng yêu quý của bạn.</p>
                    <p>Cảm ơn bạn đã chọn Pet Shop - Nơi Hạnh Phúc Cho Bạn Và Thú Cưng! 🐶🐱🐾</p>
                </div>

                <div className="col-md-4 mt-4">
                    <h5>Contact Info</h5>
                    <div className="d-flex align-items-center">
                        <FontAwesomeIcon icon={faPhone} />
                        <p className="ms-2 mb-0">+84 123456789</p>
                    </div>
                    <div className="d-flex align-items-center">
                        <FontAwesomeIcon icon={faEnvelope} />
                        <p className="ms-2 mb-0">petshop@gmail.com</p>
                    </div>
                    <div className="d-flex align-items-center">
                        <FontAwesomeIcon icon={faLocationDot} />
                        <p className="ms-2 mb-0">Địa chỉ shop</p>
                    </div>
                </div>
                <div className="col-md-4 mt-5">
                    <div className="d-flex align-items-center">
                        <FontAwesomeIcon icon={faShieldDog} size="10x" className="me-5" />
                        <FontAwesomeIcon icon={faShieldCat} size="10x" />
                    </div>
                    <div className="d-flex align-items-center mt-4 ">
                        <FontAwesomeIcon icon={faBone} size="2x" className="ms-5" />
                        <FontAwesomeIcon icon={faDog} size="2x" className="ms-5" />
                        <FontAwesomeIcon icon={faCat} size="2x" className="ms-5" />
                        <FontAwesomeIcon icon={faHeart} size="2x" className="ms-5" />
                    </div>
                </div>
            </div>
        </div>
    )
}

export default Footer;