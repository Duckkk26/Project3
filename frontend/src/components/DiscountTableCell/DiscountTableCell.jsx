import React, { useRef, useState } from 'react'
import cn from 'classnames'

// Import Components
import DiscountPopup from '../DiscountPopup/DiscountPopup'

// Import CSS
import s from '../ProductsTable/ProductsTable.module.scss'

const DiscountTableCell = ({ price, discount, handleChangeDiscount }) => {
    const [isDiscountPopup, setIsDiscountPopup] = useState(false)
    const discountBtnRef = useRef(null)

    const closeDiscountPopup = () => {
        setIsDiscountPopup(false);
    }

  return (
    <td className={cn(s.tableCell, s.tableCellBody, "text-end", "table-cell")}>
        <button ref={discountBtnRef} onClick={() => setIsDiscountPopup(true)} className={cn("btn-base", s.btnDiscount)}>
            <span className={s.btnLabel}>
                <p className={s.discountValue}>{discount}</p>
                {discount !== 0 && <p className={s.discountRate}>{(discount / price * 100).toFixed(2)}%</p>}
            </span>
        </button>
        {isDiscountPopup && <DiscountPopup price={price} discount={discount} btnRef={discountBtnRef} handleChangeDiscount={(value) => handleChangeDiscount(value)} closePopup={closeDiscountPopup} />}
    </td>
  )
}

export default DiscountTableCell