import React, { useEffect, useRef, useState } from 'react'
import cn from 'classnames'

// Import CSS
import s from './DiscountPopup.module.scss'

const DiscountPopup = ({ price, discount, btnRef, handleChangeDiscount, closePopup }) => {
  const [textValue, setTextValue] = useState("0");
  const [isValue, setIsValue] = useState(true);
  const popupRef = useRef(null);

  const hasOneDotAtEnd = (str) => {
    const dotPattern = /^[^.]*\.$/; // Có thể có bất kỳ ký tự nào trước dấu chấm và chỉ một dấu chấm ở cuối
    return dotPattern.test(str);
  }

  const formatNumber = (number) => {
    return number.toLocaleString('en-US');
  }

  const convertNumber = (string) => {
    return parseFloat(string.replace(/,/g, ''))
  }

  const handleChangeValue = (e) => {
    const value = e.target.value.replace(/[^0-9.]/g, '');
    if (isValue) {
      if (value.trim() === '' || value.trim() === '.') {
        setTextValue("")
      } else {
        setTextValue(formatNumber(convertNumber(value)))
      }
    } else {
      if (hasOneDotAtEnd(value)) {
        setTextValue(value);
      } else {
        if (value.trim() === '' || value.trim() === '.') {
          setTextValue("")
        } else if (convertNumber(value) <= 100) {
          setTextValue(formatNumber(convertNumber(value)))
        }
      }
    }
  }
  
  useEffect(() => {
    if (isValue) {
      handleChangeDiscount(convertNumber(textValue))
    } else {
      handleChangeDiscount(convertNumber(textValue) / 100 * price)
    }
  }, [textValue])

  useEffect(() => {
    setTextValue("0");
  }, [isValue])

  useEffect(() => {
    setTextValue(formatNumber(discount));
  }, [])

  const handleClickOutside = (e) => {
    if (popupRef.current && !popupRef.current.contains(e.target) && btnRef.current && !btnRef.current.contains(e.target)) {
      if (isValue) {
        handleChangeDiscount(convertNumber(textValue))
      } else {
        handleChangeDiscount(price * convertNumber(textValue) / 100);
      }
      closePopup();
    }
  }

  useEffect(() => {
    document.addEventListener('mousedown', handleClickOutside);

    return () => {
      document.removeEventListener('mousedown', handleClickOutside);
    };
  }, [textValue])

  return (
    <div ref={popupRef} className="discount-popup">
      <div className={s.container}>
        <div className={s.toggleBtnGroup} role='group'>
          <button
            onClick={() => setIsValue(true)}
            className={cn(s.toggleBtn, { [s.selected]: isValue })}
          >
            <span className={s.toggleBtnLabel}>Giá trị</span>
          </button>
          <button
            onClick={() => setIsValue(false)}
            className={cn(s.toggleBtn, { [s.selected]: !isValue })}
          >
            <span className={s.toggleBtnLabel}>%</span>
          </button>
        </div>
        <div className={s.boxValue}>
          <input onChange={handleChangeValue} className={s.inputField} type="text" value={textValue} />
          <fieldset></fieldset>
        </div>
      </div>
      {convertNumber(textValue) > price && <p className={s.error}>Chiết khấu lớn hơn giá trị sản phẩm</p>}
    </div>
  )
}

export default DiscountPopup