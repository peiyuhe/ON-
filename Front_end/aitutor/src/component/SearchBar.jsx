import React, { useState } from 'react';

const SearchBar = ({ onSearch, onMenuClick, onMailClick }) => {
  const [searchText, setSearchText] = useState('');

  const handleInputChange = (e) => {
    setSearchText(e.target.value);
  };


  const handleSearchClick = () => {
    onSearch(searchText); 
  };

  return (
    <div className="search-bar">

      {/* <button className="menu-button" onClick={onMenuClick}>
        ☰
      </button> */}


      <input
        type="text"
        value={searchText}
        placeholder="Hinted search text"
        onChange={handleInputChange} 
      />


      <button className="search-button" onClick={handleSearchClick}>
        🔍
      </button>

    </div>
  );
};

export default SearchBar;
