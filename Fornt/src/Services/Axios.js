import axios from "axios";

const instance = axios.create({
    baseURL: 'http://localhost:9999',
    // timeout: 1000,
    // headers: {'X-Custom-Header': 'foobar'}
  });

  let res = []

  instance.interceptors.response.use(function (response) {
    // Any status code that lie within the range of 2xx cause this function to trigger
    // Do something with response data
    // res.header("Access-Control-Allow-Origin", "*");
    // res.header("Access-Control-Allow-Headers", "Origin, X-Requested-With, Content-Type, Accept");
    // next();

    return response.data;
  }, function (error) {
    //  res.data = error.response.data;
    //  res.status = error.response.status;
    //  res.headers = error.response.headers;
     return res;
    console.log("check" , error.response);
    // Any status codes that falls outside the range of 2xx cause this function to trigger
    // Do something with response error
    // return Promise.reject(error);
  });

  export default instance;