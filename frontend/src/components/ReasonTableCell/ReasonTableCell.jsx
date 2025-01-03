import React, { useRef, useState } from 'react'
import cn from 'classnames'



// Import CSS
import s from '../GINProductsTable/GINProductsTable.module.scss'
import ReasonSelectPopup from '../ReasonSelectPopup/ReasonSelectPopup';
import { faCaretDown } from '@fortawesome/free-solid-svg-icons'
// ReasonTableCell.js
const ReasonTableCell = ({ reason, setReason }) => {
  const [isOpenReasonPopup, setIsOpenReasonPopup] = useState(false);
  const reasonBtnRef = useRef(null);

  const closeReasonPopup = () => {
    setIsOpenReasonPopup(false);
  }

  const handleChangeReason = (newReason) => {
    setReason(newReason);
    closeReasonPopup();
  }

  return (
    <td className={cn(s.tableCell, s.tableCellBody, "text-end", "table-cell")}>
      <div className={cn(s.boxReason)}>
        <button
          ref={reasonBtnRef}
          onClick={() => { setIsOpenReasonPopup(!isOpenReasonPopup) }}
          className={cn(s.btnReason, { "selected": isOpenReasonPopup })}
        >
          {reason}
        </button>
        {isOpenReasonPopup && (
          <ReasonSelectPopup 
            btnRef={reasonBtnRef} 
            reason={reason} 
            handleChangeReason={handleChangeReason} 
            closePopup={closeReasonPopup} 
          />
        )}
      </div>
    </td>
  )
}

export default ReasonTableCell;
