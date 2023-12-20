import React from "react";
import { faSearch } from "@fortawesome/free-solid-svg-icons";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import { useState } from "react";
import { searchApi } from "../Services/UserService";
import zIndex from "@mui/material/styles/zIndex";
// Component
function SearchBar() {
  const [input, setinput] = useState();
  const [loadingAPI, setloadingAPI] = useState();

  const [searchResults, setSearchResults] = useState([]);

  const handleSearch = async (event) => {
    let search = await searchApi(event);

    let name = [{}];
    name = search.map((item) => ({
      id: item.product_id,
      subCateName: item.product_name,
    }));

    console.log(name);
    setSearchResults(name);
  };

  return (
    <>
      <div className="search-bar">
        <input
          type="text"
          value={input}
          onChange={(e) => handleSearch(e.target.value)}
          placeholder="Tìm kiếm"
        />

        <button className="button-search" type="submit" value="Tìm kiếm">
          <FontAwesomeIcon icon={faSearch} />
        </button>
        <div className="search-result">
        {searchResults.map((item, index) => {
          if (index < 6) {
            return <p className="nav-dropdown" key={index}>{item.subCateName}</p>;
          }
        })}
        </div>
      </div>
    </>
  );
}

export default SearchBar;
