import React, { useEffect, useRef } from 'react'
import cn from 'classnames'

import s from './ReasonSelectPopup.module.scss'

// ReasonSelectPopup.js
const ReasonSelectPopup = ({ btnRef, reason, handleChangeReason, closePopup }) => {
    const popupRef = useRef(null);
  
    const handleClickOutside = (e) => {
      if (popupRef.current && !popupRef.current.contains(e.target) && btnRef.current && !btnRef.current.contains(e.target)) {
        closePopup();
      }
    }
  
    useEffect(() => {
      document.addEventListener('mousedown', handleClickOutside);
      return () => {
        document.removeEventListener('mousedown', handleClickOutside);
      };
    }, []);
  
    const handleChange = (newReason) => {
      handleChangeReason(newReason);
      closePopup();
    }
  
    return (
      <div ref={popupRef} className={s.container}>
        <div className={s.wrapper}>
          <button onClick={() => handleChange("Khác")} className={cn(s.selectItem, { [s.selected]: reason === "Khác" })}>Khác</button>
          <button onClick={() => handleChange("Hư hỏng")} className={cn(s.selectItem, { [s.selected]: reason === "Hư hỏng" })}>Hư hỏng</button>
          <button onClick={() => handleChange("Hao mòn")} className={cn(s.selectItem, { [s.selected]: reason === "Hao mòn" })}>Hao mòn</button>
          <button onClick={() => handleChange("Trả hàng")} className={cn(s.selectItem, { [s.selected]: reason === "Trả hàng" })}>Trả hàng</button>
        </div>
      </div>
    )
  }
  
  export default ReasonSelectPopup;
  