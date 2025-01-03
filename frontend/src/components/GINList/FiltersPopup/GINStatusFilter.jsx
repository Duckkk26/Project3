import React, {useState, useEffect, useRef } from 'react';
import cn from 'classnames'; // Đảm bảo bạn đã cài đặt classnames package: npm install classnames
import s from './GINStatusFilter.module.scss'; // Đảm bảo bạn đã import CSS cho file này

const GINStatusFilter = ({ ginStatusRef,closePopup, handeChangeDatafilter }) => {
  const popupRef = useRef(null);

  const [status,setStatus] = useState([])
  // Các trạng thái có thể có
  const statuses = {
    BALANCED: 'Đã cân bằng',
    CHECKING: 'Đang kiểm kho',
    DELETED: 'Đã xóa',
  };

  // Hàm toggle trạng thái
  const toggleStatus = (status) => {
    setStatus((prevStatus) => {
      if (prevStatus.includes(status)) {
        return prevStatus.filter((s) => s !== status); // Xóa trạng thái nếu đã chọn
      } else {
        return [...prevStatus, status]; // Thêm trạng thái nếu chưa chọn
      }
    });
  };

  // Đóng popup khi bấm ra ngoài
  const handleClickOutside = (e) => {
    if (
      popupRef.current &&
      !popupRef.current.contains(e.target) &&
      ginStatusRef.current &&
      !ginStatusRef.current.contains(e.target)
    ) {
      closePopup();
    }
  };

  const hanldeClickFilter = ()=> {
    handeChangeDatafilter({statues:[...status]})
    closePopup()
  }

  useEffect(() => {
    document.addEventListener('mousedown', handleClickOutside);
    return () => {
      document.removeEventListener('mousedown', handleClickOutside);
    };
  }, []);

  return (
    <div ref={popupRef} className={s.container}>
      <div className={s.wrapper}>
        <button
          onClick={() => toggleStatus('BALANCED')}
          className={cn(s.selectItem, { [s.selected]: status.includes('BALANCED') })}
        >
          {statuses.BALANCED}
        </button>
        <button
          onClick={() => toggleStatus('CHECKING')}
          className={cn(s.selectItem, { [s.selected]: status.includes('CHECKING') })}
        >
          {statuses.CHECKING}
        </button>
        <button
          onClick={() => toggleStatus('DELETED')}
          className={cn(s.selectItem, { [s.selected]: status.includes('DELETED') })}
        >
          {statuses.DELETED}
        </button>
      </div>
      <button onClick={hanldeClickFilter}>Lọc</button>
    </div>
  );
};

export default GINStatusFilter;
