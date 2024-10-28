import React, { useState } from 'react';
  import '../../styles/admin.css';
  import SearchBar from '../../component/SearchBar';
  import TabSwitcherUser from '../../component/TabSwitcherUser';
  
  const Admin = () => {
    const [searchText, setSearchText] = useState(''); 
  

    const handleSearch = (value) => {
      setSearchText(value); 
    };
  
    return (
      <div className="admin-container">
        {/* Header Navigation */}
        <header className="navbar">
          <SearchBar onSearch={handleSearch} />
        </header>
  
        <div>

          <TabSwitcherUser searchText={searchText} />
        </div>
      </div>
    );
  };
  
  export default Admin;