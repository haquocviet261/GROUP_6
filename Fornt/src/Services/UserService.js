import axios from "axios";
import Axios from "./Axios";

const fetchAllUser = () => {
  return Axios.get("/api/users?page=1");
};

const getRandomProduct = () => {
  return Axios.get("/api/v1/home/random");
}

const getCategory = () => {
  const data = Axios.get("api/v1/home/all-category", {
    headers: {
      Authorization: `Bearer ${localStorage.getItem("token")}`,
    },
  });
  return data;
};



const getSubcategoryById = (category_id) => {
  const data = Axios.get('api/v1/home/find-subcategories',{
    params: {
      category_id  
    }
});
  return data;
}

const getSubCategory = () => {
  const data = Axios.get("/api/v1/home/all-subcategory");
  return data;
}

const postCreateUser = (name, job) => {
  return Axios.post("/api/users", { name, job });
};

const searchApi = async (productName) => {
  try {
    const response = await Axios.get("/api/v1/home/find", {
      params: {
        subcategory: productName
      }
    });

    

    return response.data;
  } catch (error) {
    console.error("Error fetching data: ", error);
    return [];
  }
};

const loginApi = (username, password) => {
  return Axios.post("/api/auth/authenticate", { username, password });
};

const logoutApi = (token) => {
  return fetch("/api/v1/user/logout", {
    headers: {
      Authorization: `Bearer ${token}`,
    },
  })
    .then((response) => {
      if (!response.ok) {
        throw new Error("Logout failed");
      }
      return response;
    })
    .then((response) => response.text()) // get text instead of json
    .then((data) => {
      // handle success
    })
    .catch((error) => {
      console.error(error);
      // handle error
    });
};

const getAllProduct = async (id) => {
    const response = await Axios.get("/api/v1/home/sub_category_id",{
        params: {
          sub_category_id:id  
        }
    });
    return response.data;
};

const getProductbySubCategory = async (sub_category_id) =>{
  const response = await Axios.get("/api/v1/home/sub_category_id",{sub_category_id});
  
  return response.data;
}
const formatPriceToVND = (number) => {
  return new Intl.NumberFormat('vi-VN', {
    style: 'currency',
    currency: 'VND',
    maximumFractionDigits: 0,
  }).format(number);
};
const formatPrice = (price, discount) => {
  discount=discount/100;
  if (discount && discount > 0) {
    const discountedPrice = price * (1 - discount);
    return {
      originalPrice: formatPriceToVND(price),
      discountedPrice: formatPriceToVND(discountedPrice),
      hasDiscount: true,
    };
  } else {
    return {
      originalPrice: formatPriceToVND(price),
      hasDiscount: false,
    };
  }
};
const getTopDiscount = async () => {
    const response = await Axios.get("/api/v1/home/sale");
    return response.data;
};


export {
  formatPrice,
  fetchAllUser,
  postCreateUser,
  loginApi,
  logoutApi,
  searchApi,
  getCategory,
  getSubcategoryById,
  getSubCategory,
  getRandomProduct,
  getAllProduct,
  getProductbySubCategory,
  getTopDiscount
};
