import React from 'react'
import cn from 'classnames'

import { formatDateTime } from '../../utils/DateUtils'

import s from './ImportHistoriesPopup.module.scss'

import { FontAwesomeIcon } from '@fortawesome/react-fontawesome'
import { faXmark } from '@fortawesome/free-solid-svg-icons'

const ImportHistoriesPopup = ({ histories, closePopup }) => {    
    return (
        <div className={s.container}>
            <div className="overlay"></div>
            <div className={s.wrapper}>
                <div className={cn("box-paper", s.content)}>
                    <div className={cn("paper-header", s.header)}>
                        <div className='box-header'>
                            <p className={s.headerTitle}>Lịch sử thao tác đơn nhập hàng</p>
                            <div className="btn-toolbar">
                                <button onClick={closePopup} className="btn-icon">
                                    <FontAwesomeIcon icon={faXmark} />
                                </button>
                            </div>
                        </div>
                    </div>
                    <div className="box-table">
                        <div className="right__table-headers">
                            <table className="box-table-headers">
                                <colgroup>
                                    <col style={{ width: "160px" }} />
                                    <col style={{ width: "180px" }} />
                                    <col style={{ width: "180px" }} />
                                    <col />
                                </colgroup>
                                <thead>
                                    <tr className="group-table-headers">
                                        <th colSpan={1} rowSpan={1} className="table-header-item text-start">Thời gian</th>
                                        <th colSpan={1} rowSpan={1} className="table-header-item text-start">Người thao tác</th>
                                        <th colSpan={1} rowSpan={1} className="table-header-item text-start">Chức năng</th>
                                        <th colSpan={1} rowSpan={1} className="table-header-item text-start">Thao tác</th>
                                    </tr>
                                </thead>
                            </table>
                        </div>
                        <div className="right__table-content">
                            <div className="right__table-data">
                                <div className="table-data__container">
                                    <table className="box-table-data">
                                        <colgroup>
                                            <col style={{ width: "160px" }} />
                                            <col style={{ width: "180px" }} />
                                            <col style={{ width: "180px" }} />
                                            <col />
                                        </colgroup>
                                        <tbody>
                                            {
                                                histories?.map((action, index) => {
                                                    return (
                                                        <tr key={index} className="table-data-row">
                                                            <td className="table-data-item text-start">
                                                                <p className='box-text'>{formatDateTime(action?.date)}</p>
                                                            </td>
                                                            <td className="table-data-item text-start">
                                                                <p className='box-text'>{action?.userExecuted}</p>
                                                            </td>
                                                            <td className="table-data-item text-start">
                                                                <p className='box-text'>{action?.function}</p>
                                                            </td>
                                                            <td className="table-data-item text-start">
                                                                <p className='box-text'>{action?.operation}</p>
                                                            </td>
                                                        </tr>
                                                    )
                                                })
                                            }
                                        </tbody>
                                    </table>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    )
}

export default ImportHistoriesPopup