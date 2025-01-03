import React, { useState } from 'react'
import cn from 'classnames'

import { formatDateTime } from '../../utils/DateUtils'
import { formatPrice } from '../../utils/PriceUtils'

import { faAnglesRight } from '@fortawesome/free-solid-svg-icons'
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome'
import ReturnItemDetails from '../ReturnItemDetails/ReturnItemDetails'

const ReturnTableItem = ({ returnItem }) => {
    const [isDetails, setIsDetails] = useState(false)

    const status = {
        FULL: "Hoàn tiền toàn bộ",
        PARTIAL: "Hoàn tiền một phần",
        NOT_REFUNDED: "Chưa hoàn tiền"
    } 
    
    return (
        <>
            <tr className={cn("table-data-row", { "item-details": isDetails })}>
                <td rowSpan={1} className='table-icon'>
                    <div className="group-icons">
                        <button onClick={() => setIsDetails(!isDetails)} className="btn-icon">
                            <FontAwesomeIcon icon={faAnglesRight} />
                        </button>
                    </div>
                </td>
                <td className="table-data-item text-start">
                    <p className='box-text'>{formatDateTime(returnItem?.created_at)}</p>
                </td>
                <td className="table-data-item text-center">
                    <p className='box-text'>{returnItem?.total_refunded_quantity}</p>
                </td>
                <td className="table-data-item text-end">
                    <p className='box-text'>{formatPrice(returnItem?.total_refunded_value)}</p>
                </td>
                <td className="table-data-item text-start">
                    {
                        returnItem?.total_refunded_quantity === 0 ?
                        <p className='box-text'>---</p> :
                        <div className='box-status box-status--refunded'>
                            <span>Đã hoàn</span>
                        </div>
                    }
                </td>
                <td className="table-data-item text-start">
                    <div className={`box-status box-status--${returnItem?.refund_payment_status.toLowerCase()}`}>
                        <span>{status[returnItem?.refund_payment_status]}</span>
                    </div>
                </td>
                <td className="table-data-item text-start">
                    <p className='box-text'>{returnItem?.user_created_name}</p>
                </td>
            </tr>
            {isDetails && <ReturnItemDetails returnItem={returnItem} />}
        </>
    )
}

export default ReturnTableItem