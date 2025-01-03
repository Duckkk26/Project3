import React from 'react'
import { Link } from 'react-router-dom'
import cn from 'classnames'

import { formatPrice } from '../../utils/PriceUtils'

import TransactionItem from '../TransactionItem/TransactionItem'

import s from './ReturnItemDetails.module.scss'

const ReturnItemDetails = ({ returnItem }) => {
    return (
        <tr className={s.container}>
            <td colSpan={7}>
                <div className={s.wrapper}>
                    <div className={s.gridContainer}>
                        <div className={cn(s.gridItem, s.boxInfo)}>
                            <div className={s.boxContent}>
                                <div className={s.groupInfo}>
                                    <div className={s.boxSupplier}>
                                        <p className={s.title}>Thông tin đơn trả hàng nhà cung cấp</p>
                                        <div className={s.textItem}>
                                            <p>Nhà cung cấp</p>
                                            :&nbsp;
                                            <Link to={`/admin/suppliers/SUP/${returnItem?.supplier_id}`}>{returnItem.supplier_name}</Link>
                                        </div>
                                    </div>
                                    <div className={s.boxReason}>
                                        <p className={s.title}>Lý do hoàn trả</p>
                                        <p className={s.content}>{returnItem?.reason}</p>
                                    </div>
                                    <div className={s.boxTransaction}>
                                        <p className={s.title}>Nhận hoàn tiền</p>
                                        <TransactionItem payment={{
                                            amount: returnItem.transaction.amount,
                                            date: returnItem.transaction.created_at,
                                            method: returnItem.transaction.payment_method
                                        }} />
                                    </div>
                                </div>
                            </div>                                           
                        </div>
                        <div className={cn(s.gridItem, s.boxBtn)}>
                            <button className={cn("btn btn-outline-primary", s.btn)}>
                                <span className="btn__title">In đơn trả</span>
                            </button>
                        </div>
                        <div className={cn(s.gridItem, s.boxProducts)}>
                            <div className={s.boxContent}>
                                <div className="box-paper">
                                    <div className={cn("paper-header", s.p16)}>
                                        <p className={s.title}>Thông tin sản phẩm trả</p>
                                    </div>
                                    <div className="box-table">
                                        <div className="right__table-headers">
                                            <table className="box-table-headers">
                                                <colgroup>
                                                    <col style={{ width: "60px" }} />
                                                    <col style={{ width: "80px" }} />
                                                    <col style={{ width: "120px" }} />
                                                    <col />
                                                    <col style={{ width: "110px" }} />
                                                    <col style={{ width: "110px" }} />
                                                    <col style={{ width: "120px" }} />
                                                </colgroup>
                                                <thead>
                                                    <tr className="group-table-headers">
                                                        <th colSpan={1} rowSpan={1} className='table-header-item text-center'>STT</th>
                                                        <th colSpan={1} rowSpan={1} className="table-header-item text-center">Ảnh</th>
                                                        <th colSpan={1} rowSpan={1} className="table-header-item text-start">Mã sản phẩm</th>
                                                        <th colSpan={1} rowSpan={1} className="table-header-item text-start">Tên sản phẩm</th>
                                                        <th colSpan={1} rowSpan={1} className="table-header-item text-center">SL trả</th>
                                                        <th colSpan={1} rowSpan={1} className="table-header-item text-end">Đơn giá trả</th>
                                                        <th colSpan={1} rowSpan={1} className="table-header-item text-end">Thành tiền</th>
                                                    </tr>
                                                </thead>
                                            </table>
                                        </div>
                                        <div className="right__table-content">
                                            <div className="right__table-data">
                                                <div className="table-data__container">
                                                    <table className="box-table-data">
                                                        <colgroup>
                                                            <col style={{ width: "60px" }} />
                                                            <col style={{ width: "80px" }} />
                                                            <col style={{ width: "120px" }} />
                                                            <col />
                                                            <col style={{ width: "110px" }} />
                                                            <col style={{ width: "110px" }} />
                                                            <col style={{ width: "120px" }} />
                                                        </colgroup>
                                                        <tbody>
                                                            {
                                                                returnItem?.refund_details?.map((product, index) => (
                                                                    <tr key={index} className="table-data-row">
                                                                        <td className="table-data-item text-center">
                                                                            <p className='box-text'>{index + 1}</p>
                                                                        </td>
                                                                        <td className="table-data-item text-center">
                                                                            <img src={product?.image?.url} alt={product?.image?.alt} />
                                                                        </td>
                                                                        <td className="table-data-item text-start">
                                                                            <p className='box-text'>
                                                                                <Link to={`/admin/products/PRD/${product?.productSubId}`} className='box-id'>{product?.productSubId}</Link>
                                                                            </p>
                                                                        </td>
                                                                        <td className="table-data-item text-start">
                                                                            <p className='box-text'>{product?.productName}</p>
                                                                        </td>
                                                                        <td className="table-data-item text-center">
                                                                            <p className='box-text'>{product?.quantity}</p>
                                                                        </td>
                                                                        <td className="table-data-item text-end">
                                                                            <p className='box-text'>{formatPrice(product?.amount)}</p>
                                                                        </td>
                                                                        <td className="table-data-item text-end">
                                                                            <p className='box-text'>{formatPrice(product?.amount * product?.quantity)}</p>
                                                                        </td>
                                                                    </tr>
                                                                ))
                                                            }
                                                        </tbody>
                                                    </table>
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                    <div className="box-total">
                                        <div className="box-total__container">
                                            <div className="box-subinfo"></div>
                                            <div className="box-price-info">
                                                <div className="info-item">
                                                    <p>Số lượng hàng trả</p>
                                                    <p>{returnItem?.total_refunded_quantity}</p>
                                                </div>
                                                <div className="info-item">
                                                    <p>Giá trị hàng trả</p>
                                                    <p>{formatPrice(returnItem?.total_refunded_value)}</p>
                                                </div>
                                                <div className="info-item">
                                                    <p>Chi phí</p>
                                                    <p>0</p>
                                                </div>
                                                <div className="info-item">
                                                    <p>Chiết khấu</p>
                                                    <p>0</p>
                                                </div>
                                                <div className="info-item">
                                                    <p>Thuế</p>
                                                    <p>0</p>
                                                </div>
                                                <div className="info-item">
                                                    <p className='total-price'>Tổng giá trị trả</p>
                                                    <p className='total-price'>{formatPrice(returnItem?.total_refunded_value)}</p>
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </div>                    
                        </div>
                    </div>
                </div>
            </td>
        </tr>
    )
}

export default ReturnItemDetails