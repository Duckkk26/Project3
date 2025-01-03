import React, { useEffect, useRef, useState } from 'react'
import { Link, useNavigate } from 'react-router-dom'
import ListSelectPopup from '../ListSelectPopup/ListSelectPopup'
// Bootstrap
import { UncontrolledTooltip } from 'reactstrap'

// Import Icons
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome'
import { faCaretDown, faChevronLeft } from '@fortawesome/free-solid-svg-icons'
import infoIcon from '../../assets/icons/InfoIcon'
import { createSupplier, getAllSupplierGroup } from '../../service/SuppliersAPI';
import { withAuthorization } from '../../hoc'
import { toast } from 'react-toastify'
import Notification from '../Notification/Notification'

const CreateSupplier = () => {
    const navigate = useNavigate();

    const [dataGroup, setDataGroup] = useState({
        keyword: null,
        status: "ACTIVE",
        pageCurrent: 1,
        pageSize: 10,
        totalPage: 1,
    });

    const [dataBody, setDataBody] = useState({
        sub_id: null,
        name: null,
        phone: null,
        email: null,
        address: null,
        supplier_group_id: null,
        tags: null,
        note: null,
        status: "INACTIVE",
        supplier_group_name: null, // Không cần truyền vào API
    });

    const handleCreateSupplier = async () => {
        setDataBody(prevState => ({
            ...prevState,
        }));

        const response = await createSupplier({
            ...dataBody,
        });

        if (response.status_code === 201) {
            toast(<Notification 
                    type={"success"} 
                    withIcon 
                    message={"Tạo mới nhà cung cấp thành công"} 
                />,
                {
                    autoClose: 4000,
                    closeButton: false,
                    hideProgressBar: true,
                }
            )
            navigate('/admin/suppliers')
        }
    };

    const groupBtnRef = useRef(null);
    const [isShowGroupPopup, setIsShowGroupPopup] = useState(false);
    const [listGroup, setListGroup] = useState([]);
    const fetchSupplierGroup = async () => {
        const response = await getAllSupplierGroup(dataGroup.pageCurrent, dataGroup.pageSize, {
            keyword: dataGroup.keyword,
            status: dataGroup.status
        });

        if (response.status_code === 200) {
            setListGroup(response.data.data);
            setDataGroup(prevState => ({
                ...prevState,
                totalPage: response.data.total_page
            }));
        }
    }
    const fetchMoreSupplierGroup = async () => {
        if (dataGroup.pageCurrent < dataGroup.totalPage) {
            const response = await getAllSupplierGroup(dataGroup.pageCurrent + 1, dataGroup.pageSize, {
                keyword: dataGroup.keyword,
                status: dataGroup.status
            });

            if (response.status_code === 200) {
                setListGroup(prevState => ([...prevState, ...response.data.data]));
                setDataGroup(prevState => ({
                    ...prevState,
                    pageCurrent: prevState.pageCurrent + 1,
                    totalPage: response.data.total_page
                }));
            }
        }
    }
    const handleFetchGroup = () => {
        if (isShowGroupPopup) {
            fetchSupplierGroup();
        } else {
            setListGroup([]);
            setDataGroup(prevState => ({
                ...prevState,
                keyword: null,
                pageCurrent: 1,
                totalPage: 1
            }));
        }
    }
    useEffect(() => {
        handleFetchGroup();
    }, [isShowGroupPopup]);
    useEffect(() => {
        setDataGroup(prevState => ({
            ...prevState,
            pageCurrent: 1,
        }));
        handleFetchGroup();
    }, [dataGroup.keyword]);

    return (
        <>
            <div className="right__navbar">
                <div className="box-navbar">
                    <div className="btn-toolbar">
                        <Link to='/admin/suppliers' className='btn-back'>
                            <FontAwesomeIcon icon={faChevronLeft} />
                            <h6 className="btn-back__title">
                                Quay lại danh sách nhà cung cấp
                            </h6>
                        </Link>
                    </div>
                    <div className="btn-toolbar">
                        <button className="btn btn-outline-primary" onClick={() => navigate("/admin/suppliers")}>
                            <span className="btn__title">Thoát</span>
                        </button>
                        <button className="btn btn-primary" onClick={handleCreateSupplier}>
                            <span className="btn__title">Lưu</span>
                        </button>
                    </div>
                </div>
            </div>
            <div className="right__createObjectPage">
                <div className="right__createObjectPage-wrapper">
                    <div className="right__createObjectPage-container">
                        <div className="box-maininfo">
                            <div className="box-info-item box-general">
                                <div className="info-header">
                                    <div className="box-header">
                                        <h6>Thông tin chung</h6>
                                    </div>
                                </div>
                                <div className="info-content">
                                    <div className="grid-container">
                                        <div className="box-product-name">
                                            <div className="form-item">
                                                <label htmlFor="name" className="form-label">
                                                    Tên nhà cung cấp
                                                    <span className="asterisk-icon">*</span>
                                                    <span
                                                        id='name'
                                                        className="caption-icon"
                                                    >
                                                        {infoIcon}
                                                    </span>
                                                    <UncontrolledTooltip
                                                        placement="top"
                                                        target="name"
                                                    >
                                                        Tên nhà cung cấp
                                                    </UncontrolledTooltip>
                                                </label>
                                                <div className="form-textfield">
                                                    <input
                                                        type="text"
                                                        name="name"
                                                        id="name"
                                                        placeholder='Nhập tên sản phẩm'
                                                        onChange={e => setDataBody(prevState => ({
                                                            ...prevState,
                                                            name: e.target.value
                                                        }))}
                                                    />
                                                    <fieldset className="input-field"></fieldset>
                                                </div>
                                            </div>
                                        </div>
                                        <div className="box-product-id">
                                            <div className="form-item">
                                                <label htmlFor="id" className="form-label">
                                                    Mã nhà cung cấp
                                                    <span
                                                        id='sub_id'
                                                        className="caption-icon"
                                                    >
                                                        {infoIcon}
                                                    </span>
                                                    <UncontrolledTooltip
                                                        placement="top"
                                                        target="sub_id"
                                                    >
                                                        Mã <strong>không trùng lặp</strong> để định danh giữa các sản phẩm.<br />
                                                        Nếu để trống trường này, mã sản phẩm sẽ được tự sinh với <strong>tiền tố PVN</strong>
                                                    </UncontrolledTooltip>
                                                </label>
                                                <div className="form-textfield">
                                                    <input
                                                        type="text"
                                                        name="sub_id"
                                                        id="sub_id"
                                                        onChange={e => setDataBody(prevState => ({
                                                            ...prevState,
                                                            sub_id: e.target.value
                                                        }))} />
                                                    <fieldset className="input-field"></fieldset>
                                                </div>
                                            </div>
                                        </div>
                                        <div className="box-product-phone">
                                            <div className="form-item">
                                                <label htmlFor="phone" className="form-label">
                                                    Số điện thoại
                                                </label>
                                                <div className="form-textfield">
                                                    <input
                                                        type="text"
                                                        name="phone"
                                                        id="phone"
                                                        onChange={e => setDataBody(prevState => {
                                                            return {
                                                                ...prevState,
                                                                phone: e.target.value
                                                            }
                                                        })} />
                                                    <fieldset className="input-field"></fieldset>
                                                </div>
                                            </div>
                                        </div>
                                        <div className="box-product-email">
                                            <div className="form-item">
                                                <label htmlFor="email" className="form-label">
                                                    Email
                                                </label>
                                                <div className="form-textfield">
                                                    <input
                                                        type="text"
                                                        name="email"
                                                        id="email"
                                                        onChange={e => setDataBody(prevState => {
                                                            return {
                                                                ...prevState,
                                                                email: e.target.value
                                                            }
                                                        })} />
                                                    <fieldset className="input-field"></fieldset>
                                                </div>
                                            </div>
                                        </div>
                                        <div className="box-product-address">
                                            <div className="form-item">
                                                <label htmlFor="address" className="form-label">
                                                    Địa chỉ
                                                </label>
                                                <div className="form-textfield">
                                                    <input
                                                        type="text"
                                                        name="address"
                                                        id="address"
                                                        onChange={e => setDataBody(prevState => {
                                                            return {
                                                                ...prevState,
                                                                address: e.target.value
                                                            }
                                                        })} />
                                                    <fieldset className="input-field"></fieldset>
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
                                        <h6>Thông tin khác</h6>
                                    </div>
                                </div>
                                <div className="info-content">
                                    <div className="form-item">
                                        <label htmlFor="category" className="form-label">
                                            Nhóm nhà cung cấp
                                        </label>
                                        <div className="box-select">
                                            <button
                                                ref={groupBtnRef}
                                                id='category'
                                                className="btn-select"
                                                onClick={() => setIsShowGroupPopup(!isShowGroupPopup)}
                                            >
                                                {dataBody.supplier_group_id ? dataBody.supplier_group_name : 'Chọn nhóm nhà cung cấp'}
                                                <FontAwesomeIcon icon={faCaretDown} />
                                            </button>
                                            {isShowGroupPopup && <ListSelectPopup
                                                isLarge={true}
                                                isSearch={true}
                                                keyword={dataGroup.keyword}
                                                handleChangeKeyword={(e) => setDataGroup(prevState => ({
                                                    ...prevState,
                                                    keyword: e.target.value
                                                }))}
                                                handleSelect={(id) => setDataBody(prevState => ({
                                                    ...prevState,
                                                    supplier_group_id: id,
                                                    supplier_group_name: listGroup.find(item => item.id === id)?.name
                                                }))}
                                                dataList={listGroup}
                                                btnRef={groupBtnRef}
                                                closePopup={() => setIsShowGroupPopup(false)}
                                                fetchMoreData={fetchMoreSupplierGroup}
                                            />}

                                        </div>
                                    </div>
                                    <div className="form-item">
                                        <label htmlFor="tags" className="form-label">
                                            Mô tả
                                            <span
                                                id='tagsCaption'
                                                className="caption-icon"
                                            >
                                                {infoIcon}
                                            </span>
                                            <UncontrolledTooltip
                                                placement="top"
                                                target="tagsCaption"
                                            >
                                                Thêm thẻ cho sản phẩm
                                            </UncontrolledTooltip>
                                        </label>
                                        <div className="form-textfield">
                                            <input
                                                className='text-end'
                                                type="text"
                                                name="tags"
                                                id="tags"
                                                onChange={e => setDataBody(prevState => {
                                                    return {
                                                        ...prevState,
                                                        note: e.target.value
                                                    }
                                                })}
                                            />
                                            <fieldset className="input-field"></fieldset>
                                        </div>
                                    </div>
                                    <div className="form-item">
                                        <label htmlFor="tags" className="form-label">
                                            Tags
                                            <span
                                                id='tagsCaption'
                                                className="caption-icon"
                                            >
                                                {infoIcon}
                                            </span>
                                            <UncontrolledTooltip
                                                placement="top"
                                                target="tagsCaption"
                                            >
                                                Thêm thẻ cho sản phẩm
                                            </UncontrolledTooltip>
                                        </label>
                                        <div className="form-textfield">
                                            <input
                                                className='text-end'
                                                type="text"
                                                name="tags"
                                                id="tags"
                                                onChange={e => setDataBody(prevState => {
                                                    return {
                                                        ...prevState,
                                                        tags: e.target.value
                                                    }
                                                })}
                                            />
                                            <fieldset className="input-field"></fieldset>
                                        </div>
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

export default withAuthorization(CreateSupplier, ["ADMIN","WAREHOUSE_MANAGER"]);