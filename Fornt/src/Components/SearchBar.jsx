import React from "react";
import { faSearch } from "@fortawesome/free-solid-svg-icons";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import { useState } from "react";
import { searchApi } from "../Services/UserService";
// Component
function SearchBar() {

  const [input, setinput] = useState();

  

  const handleSearch = async () => {
    let search = await searchApi();
    console.log(search);
  }

  return (
    <>
      <div className="search-bar">
        <input type="text" value={input} onChange={(e) => handleSearch(e.target.value) } placeholder="Tìm kiếm" />
        
        <button className="button-search"
          type="submit"
          value="Tìm kiếm"
       >
          <FontAwesomeIcon icon={faSearch} />
        </button>
      </div>

      
        
      
    </>
  );
}

export default SearchBar;
