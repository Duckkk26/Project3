import React, { useEffect, useRef, useState } from 'react'
import { Link } from 'react-router-dom'
import cn from 'classnames'

// Import Components
import DiscountTableCell from '../DiscountTableCell/DiscountTableCell'

// Import CSS
import s from './ProductsTable.module.scss'

// Import Columns Info
import col from '../../assets/colgroup/products-table'

// Import Icons
import infoIcon from '../../assets/icons/InfoIcon'
import boxOpenIcon from '../../assets/icons/BoxOpenIcon'
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome'
import { faCaretDown, faCircleMinus, faCirclePlus, faXmark } from '@fortawesome/free-solid-svg-icons'

const ProductsTable = ({ productsList, setProductList, colsToRender, isView, isDelete, setIsProductSelectPopup }) => {
    const handleChangeData = (index, key, value) => {
        const newProductsList = [...productsList];
        newProductsList[index][key] = value;
        setProductList(newProductsList);
    }    

    const currentUrl = window.location.href;
    const isEditUrl = currentUrl.includes("/edit");    

    return (
        <div className={s.container}>
            <table className={s.table}>
                <colgroup>
                    {/* Render the <colgroup> only for the columns that are in colsToRender */}
                    {Object.entries(colsToRender).map(([key, value]) => {
                        if (value) {
                            return (
                                <col
                                    key={key}
                                    style={{
                                        width: col[key]?.width,
                                        minWidth: col[key]?.minWidth || "unset"
                                    }}
                                />
                            )
                        }
                        return null;
                    })}
                    {!isView && <col style={{ width: "40px" }} />}
                </colgroup>
                <thead className={s.tableHeader}>
                    <tr className={s.tableRow}>
                        {/* Render table headers for columns that exist in ordersList */}
                        {Object.entries(colsToRender).map(([key, value]) => {
                            if (value) {
                                return (
                                    <th
                                        key={key}
                                        scope='col'
                                        className={cn(s.tableCell, s.tableCellHeader, col[key]?.align)}
                                    >
                                        <p>{col[key]?.name}</p>
                                    </th>
                                )
                            }
                            return null;
                        })}
                        {!isView && <th className={cn(s.tableCell, s.tableCellHeader, "text-center")}></th>}
                    </tr>
                </thead>
                <tbody className={s.tableBody}>
                    {productsList.map((product, index) => {
                        return (
                            <tr
                                key={index}
                                className={s.tableRow}
                            >
                                {Object.entries(colsToRender).map(([key, value]) => {                                    
                                    if (value) {
                                        if (key === "image") {
                                            return (
                                                <td
                                                    key={key}
                                                    className={cn(s.tableCell, s.tableCellBody, col[key].align)}
                                                >
                                                    <img src={product.image?.url} alt="" />
                                                </td>
                                            )
                                        } else if (key === "name") {
                                            return (
                                                <td
                                                    key={key}
                                                    className={cn(s.tableCell, s.tableCellBody, col[key].align)}
                                                >
                                                    <div className={s.boxInfo}>
                                                        <div className={s.boxName}>
                                                            <p>{product.name}</p>
                                                            <button className={s.btnIcon}>
                                                                <span className={s.btnIconLabel}>
                                                                    {infoIcon}
                                                                </span>
                                                            </button>
                                                        </div>
                                                        <div className={s.boxId}>
                                                            <Link to={`/admin/products/PRD/${product.id}`}>
                                                                <p>{product.id}</p>
                                                            </Link>
                                                        </div>
                                                    </div>
                                                </td>
                                            )
                                        } else if (key === "quantity") {
                                            if (isView) {
                                                return (
                                                    <td
                                                        key={key}
                                                        className={cn(s.tableCell, s.tableCellBody, col[key].align)}
                                                    >
                                                        <p>{product[key]}</p>
                                                    </td>
                                                )
                                            }
                                            return (
                                                <td
                                                    key={key}
                                                    className={cn(s.tableCell, s.tableCellBody, col[key].align)}
                                                >
                                                    <div className={s.boxQuantity}>
                                                        <button className={s.btnIcon} onClick={() => handleChangeData(index, key, Number(product.quantity) - 1)}>
                                                            <span className={s.btnIconLabel}>
                                                                <FontAwesomeIcon icon={faCircleMinus} />
                                                            </span>
                                                        </button>
                                                        <div className={s.boxFormControl}>
                                                            <div className={s.boxInput}>
                                                                <input
                                                                    className={cn(s.inputField, col[key].align)}
                                                                    type="text"
                                                                    value={product[key]}
                                                                    onChange={(e) => handleChangeData(index, key, e.target.value)}
                                                                />
                                                            </div>
                                                        </div>
                                                        <button className={s.btnIcon} onClick={() => handleChangeData(index, key, Number(product.quantity) + 1)}>
                                                            <span className={s.btnIconLabel}>
                                                                <FontAwesomeIcon icon={faCirclePlus} />
                                                            </span>
                                                        </button>
                                                    </div>
                                                </td>
                                            )
                                        } else if (key === "imported_quantity" && !isEditUrl) {
                                            if (isView) {
                                                return (
                                                    <td
                                                        key={key}
                                                        className={cn(s.tableCell, s.tableCellBody, col[key].align)}
                                                    >
                                                        <p>{product[key]}</p>
                                                    </td>
                                                )
                                            }
                                            return (
                                                <td
                                                    key={key}
                                                    className={cn(s.tableCell, s.tableCellBody, col[key].align)}
                                                >
                                                    <div className={s.boxQuantity}>
                                                        <button className={s.btnIcon} onClick={() => handleChangeData(index, key, Number(product.imported_quantity) - 1)}>
                                                            <span className={s.btnIconLabel}>
                                                                <FontAwesomeIcon icon={faCircleMinus} />
                                                            </span>
                                                        </button>
                                                        <div className={s.boxFormControl}>
                                                            <div className={s.boxInput}>
                                                                <input
                                                                    className={cn(s.inputField, col[key].align)}
                                                                    type="text"
                                                                    value={product[key]}
                                                                    onChange={(e) => handleChangeData(index, key, e.target.value)}
                                                                />
                                                            </div>
                                                        </div>
                                                        <button className={s.btnIcon} onClick={() => handleChangeData(index, key, Number(product.imported_quantity) + 1)}>
                                                            <span className={s.btnIconLabel}>
                                                                <FontAwesomeIcon icon={faCirclePlus} />
                                                            </span>
                                                        </button>
                                                    </div>
                                                </td>
                                            )
                                        } else if (key === "ordered_quantity") {
                                            if (isView) {
                                                return (
                                                    <td
                                                        key={key}
                                                        className={cn(s.tableCell, s.tableCellBody, col[key].align)}
                                                    >
                                                        <p>{product[key]}</p>
                                                    </td>
                                                )
                                            }
                                            return (
                                                <td
                                                    key={key}
                                                    className={cn(s.tableCell, s.tableCellBody, col[key]?.align)}
                                                >
                                                    <div className={s.boxQuantity}>
                                                        <button className={s.btnIcon} onClick={() => handleChangeData(index, key, Number(product.ordered_quantity) - 1)}>
                                                            <span className={s.btnIconLabel}>
                                                                <FontAwesomeIcon icon={faCircleMinus} />
                                                            </span>
                                                        </button>
                                                        <div className={s.boxFormControl}>
                                                            <div className={s.boxInput}>
                                                                <input
                                                                    className={cn(s.inputField, col[key]?.align)}
                                                                    type="text"
                                                                    value={product[key]}
                                                                    onChange={(e) => handleChangeData(index, key, e.target.value)}
                                                                />
                                                            </div>
                                                        </div>
                                                        <button className={s.btnIcon} onClick={() => handleChangeData(index, key, Number(product.ordered_quantity) + 1)}>
                                                            <span className={s.btnIconLabel}>
                                                                <FontAwesomeIcon icon={faCirclePlus} />
                                                            </span>
                                                        </button>
                                                    </div>
                                                </td>
                                            )
                                        } else if (key === "price") {
                                            if (isView) {
                                                return (
                                                    <td
                                                        key={key}
                                                        className={cn(s.tableCell, s.tableCellBody, col[key].align)}
                                                    >
                                                        <p>{product[key]}</p>
                                                    </td>
                                                )
                                            }
                                            return (
                                                <td
                                                    key={key}
                                                    className={cn(s.tableCell, s.tableCellBody, col[key]?.align)}
                                                >
                                                    <div className={s.boxPrice}>
                                                        <div className={s.boxInput}>
                                                            <input className={cn(s.inputField, col[key]?.align)} type="text" value={product[key]} onChange={e => handleChangeData(index, key, e.target.value)} />
                                                        </div>
                                                    </div>
                                                </td>
                                            )
                                        } else if (key === "discount") {
                                            if (isView) {
                                                return (
                                                    <td
                                                        key={key}
                                                        className={cn(s.tableCell, s.tableCellBody, col[key].align)}
                                                    >
                                                        <p>{product[key]}</p>
                                                    </td>
                                                )
                                            }
                                            return (
                                                <DiscountTableCell
                                                    key={key}
                                                    price={product.price}
                                                    discount={product.discount}
                                                    handleChangeDiscount={(value) => {
                                                        handleChangeData(index, key, value);
                                                    }}
                                                />
                                            )
                                        } else if (key === "tax") {
                                            if (isView) {
                                                return (
                                                    <td
                                                        key={key}
                                                        className={cn(s.tableCell, s.tableCellBody, col[key].align)}
                                                    >
                                                        <p>{product[key]}</p>
                                                    </td>
                                                )
                                            }
                                            return (
                                                <td
                                                    key={key}
                                                    className={cn(s.tableCell, s.tableCellBody, col[key].align)}
                                                >
                                                    <div className={s.boxPrice}>
                                                        <div className={s.boxInput}>
                                                            <input className={cn(s.inputField, col[key].align)} type="text" value={product[key]} onChange={e => handleChangeData(index, key, e.target.value)} />
                                                        </div>
                                                    </div>
                                                </td>
                                            )
                                        }
                                        return (
                                            <td
                                                key={key}
                                                className={cn(s.tableCell, s.tableCellBody, col[key].align)}
                                            >
                                                <p>
                                                    {
                                                        key === "total"
                                                            ? Number(!isNaN(product.ordered_quantity) ? product.ordered_quantity : product.imported_quantity) * (Number(product.price) - Number(product.discount) + Number(product.tax))
                                                            : key === "index" ? index + 1 : product.unit
                                                    }
                                                </p>

                                            </td>
                                        )
                                    }
                                    return null;
                                })}
                                {
                                    !isView && isDelete === undefined &&
                                    <td className={cn(s.tableCell, s.tableCellBody, "text-center")}>
                                        <button
                                            className="btn-icon"
                                            onClick={() => setProductList(prev => prev.filter((_, i) => i !== index))}
                                        >
                                            <FontAwesomeIcon icon={faXmark} />
                                        </button>
                                    </td>
                                }
                            </tr>
                        )
                    })}
                </tbody>
            </table>
            {
                productsList.length === 0 &&
                <div className={s.boxEmpty}>
                    {boxOpenIcon}
                    <p>{colsToRender["ordered_quantity"] ? "Đơn đặt hàng nhập" : "Đơn nhập hàng"} của bạn chưa có sản phẩm nào</p>
                    <button className="btn btn-outline-primary" onClick={setIsProductSelectPopup}>
                        <span className="btn__label">Thêm sản phẩm</span>
                    </button>
                </div>
            }
        </div>
    )
}

export default ProductsTable