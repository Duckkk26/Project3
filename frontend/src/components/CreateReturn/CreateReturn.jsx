import React, { useEffect, useRef, useState } from 'react'
import { Link, useLocation, useNavigate } from 'react-router-dom'
import cn from 'classnames';

import s from './CreateReturn.module.scss'

import { faCaretDown, faChevronLeft, faCreditCard } from '@fortawesome/free-solid-svg-icons';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import ReturnProductsTable from '../ReturnProductsTable/ReturnProductsTable';
import ListSelectPopup from '../ListSelectPopup/ListSelectPopup';
import { getGRNById } from '../../service/GRNApi';
import { formatPrice } from '../../utils/PriceUtils';
import { createReturn } from '../../service/RefundInformationAPI';

const CreateReturn = () => {
    const navigate = useNavigate();
    const location = useLocation();
    const queryParams = new URLSearchParams(location.search);
    const grnId = queryParams.get("grnId");

    const paymentMethods = [
        { id: 1, key: 'CREDIT_CARD', name: "Quẹt thẻ" },
        { id: 2, key: 'BANK_TRANSFER', name: "Chuyển khoản ngân hàng" },
        { id: 3, key: 'CASH', name: "Tiền mặt" },
    ];

    const [isPaymentMethodPopup, setIsPaymentMethodPopup] = useState(false);
    const paymentMethodBtnRef = useRef(null);

    const [dataDetail, setDataDetail] = useState({});
    const [dataBody, setDataBody] = useState({
        grn_id: grnId,
        total_cost: 0,
        total_tax: 0,
        total_discount: 0,
        reason: "",
        refund_detail: [],
        transaction: {
            amount: 0,
            payment_method: ""
        }
    })
    const [returnOrder, setReturnOrder] = useState({
        totalQuantity: 0,
        totalValue: 0,
        importCost: 0,
        totalTax: 0,
        totalDiscount: 0,
    })

    const fetchDetailGRN = async () => {
        const responseAPI = await getGRNById(grnId);
        const data = {
            ...responseAPI.data,
            products: responseAPI.data.products.map((product) => {
                return {
                    ...product,
                    imported_quantity: product.quantity
                }
            })
        };
        setDataDetail(data);

        setDataBody({
            ...dataBody,
            refund_detail: data.products.map((product) => ({
                product_id: product.sub_id,
                quantity: 0,
                refunded_price: product.price - product.discount - product.tax,
                amount: 0
            }))
        })
    }

    useEffect(() => {
        fetchDetailGRN();
    }, [])

    useEffect(() => {
        setReturnOrder({
            totalQuantity: dataBody.refund_detail.reduce((acc, product) => acc + product.quantity, 0) || 0,
            totalValue: dataBody?.refund_detail.reduce((acc, product) => acc + product.refunded_price * product.quantity, 0) || 0,
            importCost: Number((Number(dataDetail?.import_cost?.reduce((acc, item) => acc + item.value, 0)) / Number(dataDetail?.total_received_quantity || 1) * Number(dataBody.refund_detail.reduce((acc, product) => acc + product.quantity, 0))).toFixed(0)),
            totalTax: Number((Number(dataDetail?.tax_amount) / Number(dataDetail?.total_received_quantity || 1) * Number(dataBody.refund_detail.reduce((acc, product) => acc + product.quantity, 0))).toFixed(0)),
            totalDiscount: Number((Number(dataDetail?.discount) / Number(dataDetail?.total_received_quantity || 1) * Number(dataBody.refund_detail.reduce((acc, product) => acc + product.quantity, 0))).toFixed(0)),
        })
    }, [dataBody.refund_detail])

    useEffect(() => {
        setDataBody({
            ...dataBody,
            total_cost: returnOrder.totalValue + returnOrder.importCost + returnOrder.totalTax - returnOrder.totalDiscount,
            total_tax: returnOrder.totalTax,
            total_discount: returnOrder.totalDiscount
        })
    }, [returnOrder])

    const handleReturn = async () => {
        const response = await createReturn(dataBody);
        if (response.status_code === 201) {
            alert("Tạo hoàn trả thành công");
            navigate(`/admin/grns/GRN/${grnId}`);
        }
    }

    return (
        <>
            <div className="right__navbar">
                <div className="box-navbar">
                    <div className="btn-toolbar">
                        <Link to={`/admin/grns/GRN/${grnId}`} className='btn-back'>
                            <FontAwesomeIcon icon={faChevronLeft} />
                            <h6 className="btn-back__title">
                                Quay lại đơn nhập hàng {grnId}
                            </h6>
                        </Link>
                    </div>
                    <div className="btn-toolbar">
                        <button className="btn btn-primary" onClick={handleReturn}>
                            <span className="btn__title">Hoàn trả</span>
                        </button>
                    </div>
                </div>
            </div>
            <div className="right__createObjectPage">
                <div className="right__createObjectPage-wrapper">
                    <div className={cn("right__createObjectPage-container vouchers", s.container)}>
                        <div className="box-title">
                            <div className="group-details">
                                <h4 className='box-code'>Tạo hoàn trả cho đơn nhập {grnId}</h4>
                            </div>
                        </div>
                        <div className="box-maininfo">
                            <div className="box-paper box-info-item">
                                <div className="paper-header">
                                    <div className="box-header">
                                        <p>Thông tin sản phẩm trả</p>
                                    </div>
                                </div>
                                <div className="box-table">
                                    <ReturnProductsTable productsList={dataDetail.products} dataBody={dataBody} setDataBody={setDataBody} />
                                </div>
                                <div className="box-total">
                                    <div className="box-total__container">
                                        <div className="box-subinfo">
                                        </div>
                                        <div className="box-price-info p-0">
                                            <div className="info-item">
                                                <p className='total-price'>Số lượng hàng trả</p>
                                                <p className='total-price'>{returnOrder.totalQuantity || 0}</p>
                                            </div>
                                            <hr />
                                            <div className="info-item">
                                                <p>Giá trị hàng trả</p>
                                                <p>{formatPrice(returnOrder.totalValue) || 0}</p>
                                            </div>
                                            <div className="info-item">
                                                <p>Chi phí</p>
                                                <p>{formatPrice(returnOrder.importCost) || 0}</p>
                                            </div>
                                            <div className="info-item">
                                                <p>VAT</p>
                                                <p>{formatPrice(returnOrder.totalTax) || 0}</p>
                                            </div>
                                            <div className="info-item">
                                                <p>Chiết khấu</p>
                                                <p>{formatPrice(returnOrder.totalDiscount) || 0}</p>
                                            </div>
                                            <div className="info-item">
                                                <p className={cn('total-price', s.total)}>Tổng giá trị hàng trả</p>
                                                <p className={cn('total-price', s.total)}>{formatPrice(Number(dataBody.total_cost)) || 0}</p>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </div>
                            <div className="box-paper box-info-item">
                                <div className="paper-header">
                                    <FontAwesomeIcon icon={faCreditCard} />
                                    <p>Nhận tiền hoàn lại từ nhà cung cấp</p>
                                </div>
                                <div className="paper-content">
                                    {
                                        dataDetail?.payment_status === "UNPAID" ?
                                            <p>Bạn không thể nhận tiền hoàn cho đơn nhập chưa có thanh toán</p> :
                                            <>
                                                <div className={s["group-form-items"]}>
                                                    <div className="form-item">
                                                        <label htmlFor="unit" className="form-label">
                                                            Nhập số tiền nhận lại
                                                        </label>
                                                        <div className="form-textfield">
                                                            <input
                                                                value={dataBody?.transaction?.amount}
                                                                onChange={(e) => setDataBody(prev => {
                                                                    return ({
                                                                        ...prev,
                                                                        transaction: {
                                                                            ...prev.transaction,
                                                                            amount: e.target.value > Number(dataDetail.payment_method.reduce((acc, cur) => acc + cur.amount, 0)) ?
                                                                                Number(dataDetail.payment_method.reduce((acc, cur) => acc + cur.amount, 0)) :
                                                                                e.target.value
                                                                        }
                                                                    })
                                                                })}
                                                                type="text"
                                                                className='text-end'
                                                                name="amount"
                                                                id="amount"
                                                            />
                                                            <fieldset className="input-field"></fieldset>
                                                        </div>
                                                    </div>
                                                    <div className="form-item">
                                                        <div className="box-select">
                                                            <button ref={paymentMethodBtnRef} onClick={() => setIsPaymentMethodPopup(!isPaymentMethodPopup)} className="btn-select">
                                                                {paymentMethods.find(method => method.key === dataBody.transaction.payment_method)?.name || "Chọn phương thức thanh toán"}
                                                                <FontAwesomeIcon icon={faCaretDown} />
                                                            </button>
                                                            {
                                                                isPaymentMethodPopup &&
                                                                <ListSelectPopup
                                                                    dataList={paymentMethods}
                                                                    btnRef={paymentMethodBtnRef}
                                                                    closePopup={() => setIsPaymentMethodPopup(false)}
                                                                    handleSelect={(id) => {
                                                                        setDataBody(prev => {
                                                                            return ({
                                                                                ...prev,
                                                                                transaction: {
                                                                                    ...prev.transaction,
                                                                                    payment_method: paymentMethods.find(method => method.id === id)?.key
                                                                                }
                                                                            })
                                                                        })
                                                                    }}
                                                                />
                                                            }
                                                        </div>
                                                    </div>
                                                </div>
                                                <p>Số tiền có thể nhận lại: {formatPrice(Number(dataDetail.payment_method?.reduce((acc, cur) => acc + cur.amount, 0)))}</p>
                                            </>
                                    }
                                </div>
                            </div>
                        </div>
                        <div className="box-subinfo">
                            <div className="box-paper box-info-item">
                                <div className="paper-header">
                                    <p>Nhà cung cấp</p>
                                </div>
                                <div className="paper-content">
                                    <a className={s["supplier-info"]} href={`/admin/suppliers/SUP/${dataDetail.supplier_id}`} target='_blank'>
                                        <h4 className='margin-bottom-15'>{dataDetail.supplier_id}</h4>
                                    </a>
                                </div>
                            </div>
                            <div className="box-paper box-info-item">
                                <div className="paper-header">
                                    <p>Lý do hoàn trả</p>
                                </div>
                                <div className="paper-content">
                                    <div className="form-item p-0">
                                        <textarea name="reason" id="reason" onChange={e => setDataBody(prevState => {
                                            return {
                                                ...prevState,
                                                reason: e.target.value
                                            }
                                        })}></textarea>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </>
    )
}

export default CreateReturn