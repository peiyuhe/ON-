import React from 'react';
import { Pagination as AntPagination } from 'antd'; 

const Pagination = ({ current, pageSize, total, onChange, showSizeChanger, pageSizeOptions }) => {
  return (
    <div className="pagination">
      <AntPagination
        current={current}
        pageSize={pageSize}
        total={total}
        onChange={onChange} 
        showSizeChanger={showSizeChanger} 
        pageSizeOptions={pageSizeOptions}
      />
    </div>
  );
};

export default Pagination;
