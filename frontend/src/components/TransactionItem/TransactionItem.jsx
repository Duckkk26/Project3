import React, { useState } from 'react'
import cn from 'classnames';
import { Collapse } from 'reactstrap';

import s from './TransactionItem.module.scss'

import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faCaretDown, faCircle } from '@fortawesome/free-solid-svg-icons';

import { formatDateTime } from '../../utils/DateUtils';

const TransactionItem = ({ payment }) => {
    const [isOpen, setIsOpen] = useState(false);

    return (
        <div className={s.transactionItem}>
            <div className={s.dot}>
                <FontAwesomeIcon icon={faCircle} />
            </div>
            <div className={s.boxCollapse}>
                <div className={s.itemTitle}>
                    <button onClick={() => setIsOpen(!isOpen)} className={s.btnCollapse}>
                        <span className={s.btnTitle}>
                            <p>Tiền mặt {payment?.amount?.toLocaleString('en-US')}</p>
                        </span>
                        <span className={cn(s.btnIcon, { [s.collapseIcon]: isOpen })}>
                            <FontAwesomeIcon icon={faCaretDown} />
                        </span>
                    </button>
                    <p className={s.date}>{formatDateTime(payment?.date)}</p>
                </div>
                <Collapse className={s.panel} isOpen={isOpen}>
                    <div className={s.collapseItem}>
                        <div className={s.infoItem}>
                            <p>Phương thức thanh toán</p>
                            <p>
                                :&nbsp;
                                {payment?.method.toLowerCase() === "cash" ? "Tiền mặt" : "Chuyển khoản"}
                            </p>
                        </div>
                    </div>
                </Collapse>
            </div>
        </div>
    )
}

export default TransactionItem