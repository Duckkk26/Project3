import React, { useEffect, useRef, useState } from 'react'
import { Link, useNavigate } from 'react-router-dom'
import { UncontrolledTooltip } from 'reactstrap'

import { faCaretDown, faChevronLeft } from '@fortawesome/free-solid-svg-icons'
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome'
import infoIcon from '../../assets/icons/InfoIcon'
import { createTransaction } from '../../service/TransactionAPI'
import { getCategoryTransactionList } from '../../service/CategoryTransaction'
import { getAllSupplierByName } from '../../service/SuppliersAPI'
import ListSelectPopup from '../ListSelectPopup/ListSelectPopup'
import { toast } from 'react-toastify'
import Notification from '../Notification/Notification'

const CreateReceiptVoucher = () => {
    const [keywordRecipient, setKeywordRecipient] = useState("");

    const [dataPageRecipient, setDataPageRecipient] = useState({
        currentPage: 1, 
        totalPage: 1,
        currentSize: 10,
    });
    const [dataPageCategory, setDataPageCategory] = useState({
        currentPage: 1,
        totalPage: 1,
        currentSize: 10,
    });

    const [isSelectRecipientGroup, setIsSelectRecipientGroup] = useState(false);
    const [isSelectRecipient, setIsSelectRecipient] = useState(false);
    const [isSelectCategory, setIsSelectCategory] = useState(false);
    const [isSelectPaymentMethod, setIsSelectPaymentMethod] = useState(false);

    const recipientGroupBtnRef = useRef(null);
    const recipientBtnRef = useRef(null);
    const categoryBtnRef = useRef(null);
    const paymentMethodBtnRef = useRef(null);

    const navigate = useNavigate();

    const [dataBody, setDataBody] = useState({
        sub_id: "",
        amount: "",
        payment_method: "CASH",
        tags: "",
        note: "",
        type: "INCOME",
        recipient_group: "SUP",
        recipient_id: "",
        recipient_name: "",
        transaction_category_id: "",
    })

    const [dataBodyCategory, setDataBodyCategory] = useState({
        name: "",
        keyword: "",
        type: "INCOME",
    })

    const listRecipientGroup = [{ id: "SUP", name: "Nhà cung cấp" }, { id: "USR", name: "Khách hàng" }, { id: "EMP", name: "Nhân viên" }];
    const listPaymentMethod = [{ id: "CASH", name: "Tiền mặt" }, { id: "BANK_TRANSFER", name: "Chuyển khoản" }, { id: "CREDIT_CARD", name: "Quẹt thẻ" }];

    const [listRecipient, setListRecipient] = useState([]);
    const fetchRecipient = async () => {
        if (dataBody.recipient_group === "SUP") {
            const response = await getAllSupplierByName(dataPageRecipient.currentPage, dataPageRecipient.currentSize, keywordRecipient);
            setListRecipient(response.data.data);
            setDataPageRecipient(prevState => {
                return {
                    ...prevState,
                    totalPage: response.data.total_page
                }
            });
        }
    }
    const fetchMoreRecipient = async () => {
        if (dataPageRecipient.currentPage < dataPageRecipient.totalPage) {
            const response = await getAllSupplierByName(dataPageRecipient.currentPage + 1, dataPageRecipient.currentSize, keywordRecipient);
            setListRecipient(prevList => [...prevList, ...response.data.data]);
            setDataPageRecipient(prevState => {
                return {
                    ...prevState,
                    currentPage: prevState.currentPage + 1,
                    totalPage: response.data.total_page
                }
            });
        }
    }

    const [listCategory, setListCategory] = useState([]);
    const fetchCategory = async () => {
        const response = await getCategoryTransactionList(dataPageCategory.currentPage, dataPageCategory.currentSize, dataBodyCategory.keyword, dataBodyCategory.type);
        setListCategory(response.data.data);
    }
    const fetchMoreCategory = async () => {
        if (dataPageCategory.currentPage < dataPageCategory.totalPage) {
            const response = await getCategoryTransactionList(dataPageCategory.currentPage + 1, dataPageCategory.currentSize, dataBodyCategory.keyword, dataBodyCategory.type);
            setListCategory(prevList => [...prevList, ...response.data.data]);
            setDataPageCategory(prevState => {
                return {
                    ...prevState,
                    currentPage: prevState.currentPage + 1,
                    totalPage: response.data.total_page
                }
            });
        }
    }

    const handleCreateTransaction = async () => {
        const response = await createTransaction(dataBody);
        if (response.status_code == 201) {
            toast(<Notification 
                    type={"success"} 
                    withIcon 
                    message={"Tạo phiếu thu thành công"} 
                />,
                {
                    autoClose: 4000,
                    closeButton: false,
                    hideProgressBar: true,
                }
            )
            navigate('/admin/receipt_vouchers');
        }
    }

    const handleFetchRecipient = () => {
        if (isSelectRecipient) {
            fetchRecipient();
        } else {
            setListRecipient([]);
            setKeywordRecipient("");
            setDataPageRecipient(prevState => {
                return {
                    ...prevState,
                    currentPage: 1,
                    totalPage: 1,
                }
            });
        }
    }
    useEffect(() => {
        handleFetchRecipient();
    }, [isSelectRecipient])
    useEffect(() => {
        setDataPageRecipient(prevState => {
            return {
                ...prevState,
                currentPage: 1
            }
        });
        handleFetchRecipient();
    }, [keywordRecipient])

    const handleFetchCategory = () => {
        if (isSelectCategory) {
            fetchCategory();
        } else {
            setListCategory([]);
            setDataBodyCategory(prevState => {
                return {
                    ...prevState,
                    keyword: ""
                }
            });
        }
    }
    useEffect(() => {
        handleFetchCategory();
    }, [isSelectCategory])
    useEffect(() => {
        setDataPageCategory(prevState => {
            return {
                ...prevState,
                currentPage: 1
            }
        });
        handleFetchCategory();
    }, [dataBodyCategory.keyword])

    return (
        <>
            <div className="right__navbar">
                <div className="box-navbar">
                    <div className="btn-toolbar">
                        <Link to='/admin/receipt_vouchers' className='btn-back'>
                            <FontAwesomeIcon icon={faChevronLeft} />
                            <h6 className="btn-back__title">
                                Quay lại danh sách phiếu thu
                            </h6>
                        </Link>
                    </div>
                    <div className="btn-toolbar">
                        <button className="btn btn-outline-primary" onClick={() => navigate("/admin/receipt_vouchers")}>
                            <span className="btn__title">Thoát</span>
                        </button>
                        <button className="btn btn-primary" onClick={handleCreateTransaction}>
                            <span className="btn__title">Lưu</span>
                        </button>
                    </div>
                </div>
            </div>
            <div className="right__createObjectPage">
                <div className="right__createObjectPage-wrapper">
                    <div className="right__createObjectPage-container vouchers">
                        <div className="box-maininfo">
                            <div className="box-info-item box-voucher">
                                <div className="info-header">
                                    <div className="box-header">
                                        <h6>Thông tin chung</h6>
                                    </div>
                                </div>
                                <div className="info-content">
                                    <div className="grid-container">
                                        <div className="box-group">
                                            <div className="form-item">
                                                <label htmlFor="group" className="form-label">
                                                    Nhóm người nhận
                                                    <span className="asterisk-icon">*</span>
                                                </label>
                                                <div className="box-select">
                                                    <button ref={recipientGroupBtnRef} id='group' className="btn-select" onClick={() => setIsSelectRecipientGroup(!isSelectRecipientGroup)}>
                                                        {dataBody.recipient_group === "SUP" ? "Nhà cung cấp" : dataBody.recipient_group === "USR" ? "Khách hàng" : "Nhân viên"}
                                                        <FontAwesomeIcon icon={faCaretDown} />
                                                    </button>
                                                    {isSelectRecipientGroup && (
                                                        <ListSelectPopup
                                                            title="nhóm người nhận"
                                                            dataList={listRecipientGroup}
                                                            handleSelect={id => setDataBody(prevState => {
                                                                return {
                                                                    ...prevState,
                                                                    recipient_group: id,
                                                                };
                                                            })}
                                                            closePopup={() => setIsSelectRecipientGroup(false)}
                                                            btnRef={recipientGroupBtnRef}
                                                        />
                                                    )}
                                                </div>
                                            </div>
                                        </div>
                                        <div className="box-name">
                                            <div className="form-item">
                                                <label htmlFor="name" className="form-label">
                                                    Tên người nộp&nbsp;
                                                    <span className="asterisk-icon">*</span>
                                                </label>
                                                <div className="box-select">
                                                    <button ref={recipientBtnRef} id='name' className="btn-select" onClick={() => setIsSelectRecipient(!isSelectRecipient)}>
                                                        {dataBody.recipient_name === ""
                                                            ? dataBody.recipient_group === "SUP"
                                                                ? "Chọn nhà cung cấp" : dataBody.recipient_group === "USR"
                                                                    ? "Chọn khách hàng" : "Chọn nhân viên" : dataBody.recipient_name}                                                        <FontAwesomeIcon icon={faCaretDown} />
                                                    </button>
                                                    {isSelectRecipient && <ListSelectPopup
                                                        title={"nhà cung cấp"}
                                                        isSearch={true}
                                                        dataList={listRecipient}
                                                        keyword={keywordRecipient}
                                                        handleChangeKeyword={e => setKeywordRecipient(e.target.value)}
                                                        handleSelect={id => {
                                                            setDataBody(prevState => {
                                                                return {
                                                                    ...prevState,
                                                                    recipient_id: id
                                                                }
                                                            });
                                                            setDataBody(prevState => {
                                                                return {
                                                                    ...prevState,
                                                                    recipient_name: listRecipient.find(item => item.id === id).name
                                                                }
                                                            });
                                                        }}
                                                        btnRef={recipientBtnRef}
                                                        closePopup={() => setIsSelectRecipient(false)}
                                                        fetchMoreData={fetchMoreRecipient}
                                                    />}
                                                </div>
                                            </div>
                                        </div>
                                        <div className="box-voucher-type">
                                            <div className="form-item">
                                                <label htmlFor="type" className="form-label">
                                                    Loại phiếu thu&nbsp;
                                                    <span className="asterisk-icon">*</span>
                                                </label>
                                                <div className="box-select">
                                                    <button ref={categoryBtnRef} id='type' className="btn-select" onClick={() => setIsSelectCategory(!isSelectCategory)}>
                                                        {dataBodyCategory.name === "" ? "Chọn loại phiếu chi" : dataBodyCategory.name}
                                                        <FontAwesomeIcon icon={faCaretDown} />
                                                    </button>
                                                    {isSelectCategory &&
                                                        <ListSelectPopup
                                                            dataList={listCategory}
                                                            isSearch={true}
                                                            keyword={dataBodyCategory.keyword}
                                                            handleChangeKeyword={e => setDataBodyCategory(prevState => {
                                                                return {
                                                                    ...prevState,
                                                                    keyword: e.target.value
                                                                }
                                                            })}
                                                            handleSelect={id => {
                                                                setDataBody(prevState => {
                                                                    return {
                                                                        ...prevState,
                                                                        transaction_category_id: id
                                                                    }
                                                                });
                                                                setDataBodyCategory(prevState => {
                                                                    return {
                                                                        ...prevState,
                                                                        name: listCategory.find(item => item.id === id).name
                                                                    }
                                                                });
                                                            }}
                                                            btnRef={categoryBtnRef}
                                                            closePopup={() => setIsSelectCategory(false)}
                                                            fetchMoreData={fetchMoreCategory}
                                                        />
                                                    }
                                                </div>
                                            </div>
                                        </div>
                                        <div className="box-voucher-id">
                                            <div className="form-item">
                                                <label htmlFor="id" className="form-label">
                                                    Mã phiếu&nbsp;
                                                    <span
                                                        id='idCaption'
                                                        className="caption-icon"
                                                    >
                                                        {infoIcon}
                                                    </span>
                                                    <UncontrolledTooltip
                                                        placement="top"
                                                        target="idCaption"
                                                    >
                                                        Mã phiếu thu không trùng lặp. Nếu để trống mã phiếu tự sinh với tiền tố <strong>RVN</strong>
                                                    </UncontrolledTooltip>
                                                </label>
                                                <div className="form-textfield">
                                                    <input
                                                        type="text"
                                                        name="id"
                                                        id="id"
                                                        onChange={e => setDataBody(prevState => ({
                                                            ...prevState,
                                                            sub_id: e.target.value
                                                        }))}
                                                    />
                                                    <fieldset className="input-field"></fieldset>
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </div>
                            <div className="box-info-item box-voucher-cost">
                                <div className="info-header">
                                    <div className="box-header">
                                        <h6>Giá trị ghi nhận</h6>
                                    </div>
                                </div>
                                <div className="info-content">
                                    <div className="grid-container">
                                        <div className="box-cost">
                                            <div className="form-item">
                                                <label htmlFor="cost" className="form-label">
                                                    Giá trị&nbsp;
                                                    <span className="asterisk-icon">*</span>
                                                </label>
                                                <div className="form-textfield">
                                                    <input
                                                        type="text"
                                                        name="cost"
                                                        id="cost"
                                                        onChange={e => setDataBody(prevState => ({
                                                            ...prevState,
                                                            amount: e.target.value
                                                        }))}
                                                        className='text-end'
                                                    />
                                                    <fieldset className="input-field"></fieldset>
                                                </div>
                                            </div>
                                        </div>
                                        <div className="box-method">
                                            <div className="form-item">
                                                <label htmlFor="method" className="form-label">
                                                    Hình thức thanh toán&nbsp;
                                                </label>
                                                <div className="box-select">
                                                    <button ref={paymentMethodBtnRef} id='method' className="btn-select" onClick={() => setIsSelectPaymentMethod(!isSelectPaymentMethod)}>
                                                        {dataBody.payment_method === "CASH"
                                                            ? "Tiền mặt" : dataBody.payment_method === "BANK_TRANSFER"
                                                                ? "Chuyển khoản" : "Quẹt thẻ"}
                                                        <FontAwesomeIcon icon={faCaretDown} />
                                                    </button>
                                                    {isSelectPaymentMethod &&
                                                        <ListSelectPopup
                                                            dataList={listPaymentMethod}
                                                            handleSelect={id => setDataBody(prevState => {
                                                                return {
                                                                    ...prevState,
                                                                    payment_method: id
                                                                }
                                                            })}
                                                            btnRef={paymentMethodBtnRef}
                                                            closePopup={() => setIsSelectPaymentMethod(false)}
                                                        />}
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                        <div className="box-subinfo">
                            <div className="box-info-item">
                                <div className="info-header">
                                    <div className="box-header">
                                        <h6>Thông tin bổ sung</h6>
                                    </div>
                                </div>
                                <div className="info-content">
                                    <div className="form-item">
                                        <label htmlFor="description" className="form-label">
                                            Mô tả
                                        </label>
                                        <textarea name="description" id="description" onChange={e => setDataBody(prevState => {
                                            return {
                                                ...prevState,
                                                note: e.target.value
                                            }
                                        })}></textarea>
                                    </div>
                                    <div className="form-item">
                                        <label htmlFor="tags" className="form-label">
                                            Tags
                                        </label>
                                        <textarea name="tags" id="tags" onChange={e => setDataBody(prevState => {
                                            return {
                                                ...prevState,
                                                tags: e.target.value
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

export default CreateReceiptVoucher