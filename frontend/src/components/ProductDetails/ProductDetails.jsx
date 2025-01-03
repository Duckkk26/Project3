import React, { useEffect, useState, useMemo } from 'react'
import { Link, useParams, useNavigate } from 'react-router-dom'
import cn from 'classnames'

import s from './ProductDetails.module.scss'

import { FontAwesomeIcon } from '@fortawesome/react-fontawesome'
import { faChevronLeft, faCopy } from '@fortawesome/free-solid-svg-icons'

import { deleteProductById, getProductById } from '../../service/ProductAPI'
import { formatDateTime } from '../../utils/DateUtils'
import DeleteConfirmation from '../ConfirmPopup/DeleteConfirmation'
import { toast } from 'react-toastify'
import Notification from '../Notification/Notification'
const ProductDetails = () => {
    const navigate = useNavigate();
    const { productId } = useParams();

    const status = {

    }

    const [isShowDeleteConfirmation, setIsShowDeleteConfirmation] = useState(false);

    const [dataDetail, setDataDetail] = useState({});

    const fetchProductDetails = async () => {
        const response = await getProductById(productId);
        setDataDetail(response.data);
    }

    useEffect(() => {
        fetchProductDetails();
    }, [])

    const deleteComfimation = useMemo(() => {
        return {
            action: "xóa",
            type: "sản phẩm",
            description: "Thao tác này sẽ xóa các sản phẩm bạn đã chọn. Thao tác này không thể khôi phục.",
            handleClose: () => setIsShowDeleteConfirmation(false),
            handleConfirm: async () => {
                try {
                    setIsShowDeleteConfirmation(false);
                    const response = await deleteProductById(productId);
                    toast(<Notification 
                            type={"success"} 
                            withIcon 
                            message={"Đã xóa sản phẩm thành công"} 
                        />,
                        {
                            autoClose: 4000,
                            closeButton: false,
                            hideProgressBar: true,
                        }
                    )
                    navigate(-1);
                } catch (error) {
                    console.error("Error during product deletion:", error);
                    toast(<Notification 
                            type={"error"} 
                            withIcon 
                            message={"Đã xảy ra lỗi khi xóa sản phẩm"} 
                        />,
                        {
                            autoClose: 4000,
                            closeButton: false,
                            hideProgressBar: true,
                        }
                    )
                }
            }
        };
    }, [productId]);

    return (
        <>
            <div className="right__navbar">
                <div className="box-navbar">
                    <div className="btn-toolbar">
                        <Link to='/admin/products' className='btn-back'>
                            <FontAwesomeIcon icon={faChevronLeft} />
                            <h6 className="btn-back__title">
                                Quay lại danh sách sản phẩm
                            </h6>
                        </Link>
                    </div>
                    <div className="btn-toolbar">
                        <button className="btn btn-outline-primary" onClick={() => navigate("/admin/products")}>
                            <span className="btn__title">Thoát</span>
                        </button>
                        <button onClick={() => setIsShowDeleteConfirmation(true)} className="btn btn-outline-danger">
                            <span className="btn__title">Xóa</span>
                        </button>
                        <button className="btn btn-primary" onClick={() => navigate(`/admin/products/PRD/${productId}/edit`)}>
                            <span className="btn__title">Sửa sản phẩm</span>
                        </button>
                    </div>
                    {/* )} */}


                </div>
            </div>
            <div className="right__paperPage">
                <div className="right__paperPage-wrapper">
                    <div className={cn("right__paperPage-container", s.container)}>
                        <div className="box-title">
                            <div className="group-details">
                                <h4 className='box-code'>{dataDetail.name}</h4>
                            </div>
                        </div>
                        <div className="box-paper">
                            <div className="paper-header">
                                <p>Thông tin sản phẩm</p>
                                <button className={`btn-status btn-status--${dataDetail.status}`}>
                                    {status[dataDetail.status]}
                                </button>
                            </div>
                            <div className={cn("paper-content", s.gridContainer)}>
                                <div className="group-info">
                                    <div className="info-item">
                                        <p className="info-title">Loại sản phẩm</p>
                                        <p className="info-value">
                                            :&nbsp;
                                            {dataDetail.category_name || '-----'}
                                        </p>
                                    </div>
                                    <div className="info-item">
                                        <p className="info-title">Nhãn hiệu</p>
                                        <p className="info-value">
                                            :&nbsp;
                                            {dataDetail.brand_name || '-----'}
                                        </p>
                                    </div>
                                    <div className="info-item">
                                        <p className="info-title">Ghi chú</p>
                                        <p className="info-value">
                                            :&nbsp;
                                            {dataDetail.note || '-----'}
                                        </p>
                                    </div>
                                </div>
                                <div className="group-info">
                                    <div className="info-item">
                                        <p className="info-title">Ngày tạo</p>
                                        <p className="info-value">
                                            :&nbsp;
                                            {formatDateTime(dataDetail.created_at) || '-----'}
                                        </p>
                                    </div>
                                    <div className="info-item">
                                        <p className="info-title">Ngày cập nhật cuối</p>
                                        <p className="info-value">
                                            :&nbsp;
                                            {formatDateTime(dataDetail.updated_at) || '-----'}
                                        </p>
                                    </div>
                                    <div className="info-item">
                                        <p className="info-title">Tags</p>
                                        <p className="info-value">
                                            :&nbsp;
                                            {dataDetail.tags || '-----'}
                                        </p>
                                    </div>
                                </div>
                                <div className="box-image">
                                    <div className="image-item">
                                        <img className={s.image} src={dataDetail?.image?.url} alt={dataDetail?.image?.alt} />
                                    </div>
                                </div>
                            </div>
                        </div>
                        <div className="box-paper">
                            <div className="paper-header">
                                <p>Thông tin chi tiết</p>
                            </div>
                            <div className={cn("paper-content", s.detailsGrid)}>
                                <div className="info-item">
                                    <p className="info-title">Mã sản phẩm</p>
                                    <p className="info-value">
                                        :&nbsp;
                                        {dataDetail.sub_id}
                                    </p>
                                </div>
                                <div className="info-item">
                                    <p className="info-title">Tồn kho</p>
                                    <p className="info-value">
                                        :&nbsp;
                                        {dataDetail.quantity}
                                    </p>
                                </div>
                                <div className="info-item">
                                    <p className="info-title">Tên sản phẩm</p>
                                    <p className="info-value">
                                        :&nbsp;
                                        {dataDetail.name}
                                    </p>
                                </div>
                                <div className="info-item">
                                    <p className="info-title">Đơn vị tính</p>
                                    <p className="info-value">
                                        :&nbsp;
                                        {dataDetail.unit}
                                    </p>
                                </div>
                                {
                                    dataDetail.types?.map(({ name, value }) => {
                                        return (
                                            <div key={name} className="info-item">
                                                <p className="info-title">{name}</p>
                                                <p className="info-value">
                                                    :&nbsp;
                                                    {value}
                                                </p>
                                            </div>
                                        )
                                    })
                                }
                            </div>
                        </div>
                        <div className="box-paper box-price">
                            <div className="paper-header">
                                <p>Giá sản phẩm</p>
                            </div>
                            <div className="paper-content grid-container">
                                <div className="box-retail-price">
                                    <div className="info-item">
                                        <p className="info-title">Giá bản lẻ</p>
                                        <p className="info-value">
                                            :&nbsp;
                                            {dataDetail.cost_price}
                                        </p>
                                    </div>
                                </div>
                                <div className="box-wholesale-price">
                                    <div className="info-item">
                                        <p className="info-title">Giá bán buôn</p>
                                        <p className="info-value">
                                            :&nbsp;
                                            {dataDetail.wholesale_price}
                                        </p>
                                    </div>
                                </div>
                                <hr />
                                <div className="box-cost-price">
                                    <div className="info-item">
                                        <p className="info-title">Giá nhập</p>
                                        <p className="info-value">
                                            :&nbsp;
                                            {dataDetail.retail_price}
                                        </p>
                                    </div>
                                </div>
                            </div>
                        </div>
                        <div className="box-paper">
                            <div className="paper-header">
                                <p>Thông tin thêm</p>
                            </div>
                            <div className="paper-content">
                                <div className="switch-item">
                                    <p>Cho phép bán</p>
                                    <div className="box-switch">
                                        <div className="btn-switch">
                                            <input
                                                type="checkbox"
                                                name="status"
                                                className='switch-checkbox'
                                                id=""
                                                defaultChecked={true}
                                                value={dataDetail.status}
                                                disabled
                                            />
                                            <span className="switch-bar" ></span>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
            {isShowDeleteConfirmation && (<DeleteConfirmation  {...deleteComfimation} />)}
        </>
    )
}

export default ProductDetails