import { Routes, Route } from "react-router-dom";

const PrivateRoutes = (props) => {
  return (
    <>
    <Routes>
      <Route path={props.path} element={props.children} />
      </Routes>
    </>
  );
};

export default PrivateRoutes;
