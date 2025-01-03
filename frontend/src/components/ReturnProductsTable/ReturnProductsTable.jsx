import React, { useEffect, useState } from 'react'

const ReturnProductsTable = ({ productsList, dataBody, setDataBody }) => {
    const handleChangeQuantity = (index, value) => {
        const updatedRefundDetail = [...dataBody.refund_detail];
        updatedRefundDetail[index] = {
            ...updatedRefundDetail[index], 
            quantity: value > productsList[index].imported_quantity ? Number(productsList[index].imported_quantity) : Number(value),
            amount: Number(value) * Number(updatedRefundDetail[index].refunded_price),
        };

        setDataBody({
            ...dataBody,
            refund_detail: updatedRefundDetail,
        });
    }

    return (
        <>
            <div className="right__table-headers">
                <table className="box-table-headers">
                    <colgroup>
                        <col style={{ width: "125px" }} />
                        <col />
                        <col style={{ width: "150px" }} />
                        <col style={{ width: "120px" }} />
                        <col style={{ width: "120px" }} />
                    </colgroup>
                    <thead>
                        <tr className="group-table-headers">
                            <th colSpan={1} rowSpan={1} className="table-header-item text-start">Mã sản phẩm</th>
                            <th colSpan={1} rowSpan={1} className="table-header-item text-start">Tên sản phẩm</th>
                            <th colSpan={1} rowSpan={1} className="table-header-item text-end">Số lượng</th>
                            <th colSpan={1} rowSpan={1} className="table-header-item text-end">Giá hàng trả</th>
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
                                <col style={{ width: "125px" }} />
                                <col />
                                <col style={{ width: "150px" }} />
                                <col style={{ width: "120px" }} />
                                <col style={{ width: "120px" }} />
                            </colgroup>
                            <tbody>
                                {
                                    productsList?.map((item, index) => (
                                        <tr key={index} className="table-data-row">
                                            <td className="table-data-item text-start">
                                                <p className='box-text'>
                                                    <a href={`/admin/products/PRD/${item.sub_id}`} target='_blank' className='box-id'>{item?.sub_id}</a>
                                                </p>
                                            </td>
                                            <td className="table-data-item text-start">
                                                <p className='box-text'>{item?.name}</p>
                                            </td>
                                            <td className="table-data-item text-end box-quantity">
                                                <div className="input-quantity">
                                                    <div className="form-item p-0">
                                                        <div className="form-textfield">
                                                            <input
                                                                value={dataBody?.refund_detail[index].quantity}
                                                                onChange={(e) => handleChangeQuantity(index, e.target.value)}
                                                                className='text-end'
                                                                type="number"
                                                                name="price"
                                                                id="price"
                                                                />
                                                            <fieldset className="input-field"></fieldset>
                                                        </div>
                                                    </div>
                                                </div>
                                                <div className="quantity-text">/</div>
                                                <div className="quantity-text">{item?.imported_quantity}</div>
                                            </td>
                                            <td className="table-data-item text-end">
                                                <p className='box-text'>{dataBody?.refund_detail[index].refunded_price}</p>
                                            </td>
                                            <td className="table-data-item text-end">
                                                <p className='box-text'>{Number(dataBody?.refund_detail[index].quantity) * Number(dataBody.refund_detail[index].refunded_price)}</p>
                                            </td>
                                        </tr>
                                    ))
                                }
                            </tbody>
                        </table>
                    </div>
                </div>
            </div>
        </>
    )
}

export default ReturnProductsTable