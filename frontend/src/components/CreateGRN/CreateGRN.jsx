/* eslint-disable no-unused-vars */
import React from 'react'
import Cookies from 'js-cookie'
import { Link, useLocation, useNavigate } from 'react-router-dom'
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome'
import { faCaretDown, faChevronLeft, faGear, faMagnifyingGlass, faXmark } from '@fortawesome/free-solid-svg-icons'
import infoIcon from '../../assets/icons/InfoIcon.jsx'
import SupplierInfo from '../SupplierInfo/SupplierInfo'
import SearchSupplier from '../SearchSupplier/SearchSupplier'
import { useState, useEffect, useRef } from 'react'
import ProductsTable from '../ProductsTable/ProductsTable.jsx'
import DiscountPopup from '../DiscountPopup/DiscountPopup.jsx'
import { DateTimePicker, LocalizationProvider } from '@mui/x-date-pickers'
import { AdapterDayjs } from '@mui/x-date-pickers/AdapterDayjs'
import '../../styles/_overrides.scss'
import { quickGetProductList } from '../../service/ProductAPI.jsx'
import ListSelectPopup from '../ListSelectPopup/ListSelectPopup.jsx'
import { createNewGRN } from '../../service/GRNApi.jsx'
import { getOrderById } from '../../service/OrderAPI.jsx'
import { useDebouncedEffect } from '../../utils/CommonUtils.jsx'
import CreateProductFastlyPopup from '../CreateProductFastlyPopup/CreateProductFastlyPopup.jsx'
import CreateSupplierPopup from '../CreateSupplierPopup/CreateSupplierPopup.jsx'
import { withAuthorization } from '../../hoc'
import { toast } from 'react-toastify'
import Notification from '../Notification/Notification.jsx'

const CreateGRN = () => {
    const navigate = useNavigate();
    const [tags, setTags] = useState([]);
    const [isDiscountPopup, setIsDiscountPopup] = useState(false)
    const [order, setOrder] = useState({
        discount: 0,
        total: 350000,
    });

    const location = useLocation();
    const { orderId } = location.state || {};

    const fetchOrderDetail = async () => {
        const responseAPI = await getOrderById(orderId)
        setDataBody(prev => {
            return {
                ...prev,
                supplier_id: responseAPI.data.supplier_id,
            }
        })
        setListProductDetail(responseAPI.data.order_details.map(detail => {
            return {
                id: detail.id,
                name: detail.name,
                image: detail.image.url,
                price: detail.price,
                imported_quantity: 0,
                unit: detail.product_unit ? detail.product_unit : "------",
                discount: detail.discount,
                tax: detail.tax,
                total: 0
            }
        }))
    }

    // Get list of columns that need redering from Cookies
    const [colsToRender, setColsToRender] = useState(() => {
        const storedCols = Cookies.get('filter_products_table_grn');
        return storedCols ? JSON.parse(storedCols) : {
            index: true,
            image: true,
            name: true,
            unit: true,
            imported_quantity: true,
            price: true,
            discount: true,
            tax: true,
            total: true
        }
    })

    useEffect(() => {
        setColsToRender((prev) => {
            const { ordered_quantity, ...rest } = prev;
            return rest;
        });
        if (orderId) {
            fetchOrderDetail();
        }
    }, [])

    const discountBtnRef = useRef(null)

    const deleteTag = (tag) => {
        setTags(prev => prev.filter(item => item !== tag))
    }

    const closeDiscountPopup = () => {
        setIsDiscountPopup(false);
    }

    // Set required columns to Cookies
    useEffect(() => {
        Cookies.set('filter_products_table_grn', JSON.stringify(colsToRender));
    }, [colsToRender])

    const [isCreateProductQuickly, setIsCreateProductQuickly] = useState(false);
    const [isCreateSupplier, setIsCreateSupplier] = useState(false);
    const [isProductSelectPopup, setIsProductSelectPopup] = useState(false)
    const [dataPageProduct, setDataPageProduct] = useState({
        page: 1,
        size: 10,
        totalPage: 1,
        totalElement: 1,
        keyword: ""
    })
    const productSelectBtnRef = useRef(null);
    const [productSelectList, setProductSelectList] = useState([])
    const fetchProductList = async () => {
        const response = await quickGetProductList(dataPageProduct.page, dataPageProduct.size, dataPageProduct.keyword);
        setProductSelectList(response.data.data);
        setDataPageProduct(prev => {
            return {
                ...prev,
                totalPage: response.data.totalPage,
                totalElement: response.data.totalElement
            }
        })
    }
    useEffect(() => {
        if (isProductSelectPopup) {
            fetchProductList();
        }
    }, [isProductSelectPopup])

    useDebouncedEffect(() => {
        fetchProductList();
    }, 500, [dataPageProduct.keyword])

    const [listProductDetail, setListProductDetail] = useState([]);

    const [dataBody, setDataBody] = useState({
        sub_id: null,
        expected_delivery_at: null,
        supplier_id: null,
        tags: null,
        note: null,
        status: "TRADING",
        discount: 0,
        import_cost: [],
        payment_method: [],
        products: [],
        order_id: orderId || null,
        received_status: null
    })

    useEffect(() => {
        setDataBody(prev => {
            return {
                ...prev,
                products: listProductDetail.map(product => {
                    return {
                        product_id: product.id,
                        quantity: product.imported_quantity,
                        price: product.price,
                        discount: product.discount,
                        tax: product.tax,
                        total: (product.price - product.discount + product.tax) * product.imported_quantity,
                        image: product.image.url
                    }
                })
            }
        })
    }, [listProductDetail])

    const handleCreateNewGRN = (statusReceived) => {
        setDataBody(prev => {
            return {
                ...prev,
                received_status: statusReceived
            }
        })
    }
    const handleClickBack = () => {
        setIsCreateProductQuickly(false);
    }

    const handleClickBackInSupplier = () => {
        setIsCreateSupplier(false);
    }
    const handleClickCreateQuicklylyProduct = () => {
        setIsCreateProductQuickly(true);
    }
    const createGRN = async () => {
        const responseAPI = await createNewGRN(dataBody);
        if (responseAPI.status_code === 201) {
            toast(<Notification 
                type={"success"} 
                withIcon 
                message={"Tạo đơn nhập hàng thành công!"} 
            />,
            {
                autoClose: 4000,
                closeButton: false,
                hideProgressBar: true,
            }
        )
            navigate('/admin/grns');
        }
        setDataBody(prev => {
            return {
                ...prev,
                received_status: null
            }
        })
    }

    useEffect(() => {
        if (dataBody.received_status !== null) {
            createGRN();
        }
    }, [dataBody.received_status])
    
    return (
        <>
                {isCreateProductQuickly && (
                    <>
                        <div className="overlay"></div>
                        <CreateProductFastlyPopup
                            handleCLickBack={handleClickBack}
                            setListProductDetail={setListProductDetail}
                        />
                    </>
                )}
                {isCreateSupplier && (
                    <>
                        <div className="overlay"></div>
                        <CreateSupplierPopup
                            handleCLickBack={handleClickBackInSupplier}
                            setSupplerID={(id) => setDataBody((prev) => {
                                return {
                                    ...prev,
                                    supplier_id: id
                                }
                            })}
                        />
                    </>
                )

                }
            <div className="right__navbar">
                <div className="box-navbar">
                    <div className="btn-toolbar">
                        <Link to='/admin/grns' className='btn-back'>
                            <FontAwesomeIcon icon={faChevronLeft} />
                            <h6 className="btn-back__title">
                                Quay lại danh sách đơn nhập hàng
                            </h6>
                        </Link>
                    </div>
                    <div className="btn-toolbar">
                        <button className="btn btn-outline-primary">
                            <span className="btn__title">Thoát</span>
                        </button>
                        <button className="btn btn-outline-primary" onClick={() => handleCreateNewGRN("NOT_ENTERED")}>
                            <span className="btn__title">Tạo & Chưa nhập</span>
                        </button>
                        <button className="btn btn-primary" onClick={() => handleCreateNewGRN("ENTERED")}>
                            <span className="btn__title">Tạo & và Nhập hàng</span>
                        </button>
                    </div>
                </div>
            </div>

            <div className='right__paperPage'>
                <div className='right__paperPage-wrapper'>
                    <div className="right__paperPage-container">
                        <div className="box-supplier">
                            <div className="box-paper">
                                <div className="paper-header">
                                    <p>Thông tin nhà cung cấp</p>
                                </div>
                                <div className="paper-content">
                                    {
                                        dataBody.supplier_id ?
                                            <SupplierInfo supplierId={dataBody.supplier_id} handleCancel={() => setDataBody(prev => {
                                                return { ...prev, supplier_id: null }
                                            })} /> :
                                            <SearchSupplier setSelectItem={id => setDataBody(prev => {
                                                return { ...prev, supplier_id: id }
                                            })} 
                                            setCreateSupplier={() => setIsCreateSupplier(true)}
                                            />
                                    }
                                </div>
                            </div>
                        </div>
                        <div className="box-info">
                            <div className="box-paper">
                                <div className="paper-header">
                                    <p>Thông tin đơn nhập hàng</p>
                                </div>
                                <div className="paper-content">
                                    <div className="group-info">
                                        <div className="info-item">
                                            <p className="info-title">Nhân viên</p>
                                            <div className="info-field">
                                                <div className="box-input">
                                                    <input value={localStorage.getItem("fullName")} name='user_created_name' type="text" className="text-field" disabled />
                                                    <fieldset className='input-field'></fieldset>
                                                </div>
                                            </div>
                                        </div>
                                        <div className="info-item">
                                            <p className="info-title">Ngày hẹn giao</p>
                                            <div className="info-field">
                                                <div className="box-input">
                                                    <LocalizationProvider dateAdapter={AdapterDayjs}>
                                                        <DateTimePicker
                                                            onChange={(value) => {
                                                                setDataBody((prev) => ({
                                                                    ...prev,
                                                                    expected_delivery_at: value ? value.format("YYYY-MM-DD") : null // Extract only the date part
                                                                }));
                                                            }}
                                                            renderInput={(params) => <TextField {...params} />} />
                                                    </LocalizationProvider>
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                        <div className='box-products'>
                            <div className="box-paper">
                                <div className="paper-header">
                                    <div className="box-header">
                                        <p>Thông tin sản phẩm</p>
                                        <div className="btn-toolbar">
                                        </div>
                                    </div>
                                </div>
                                <div className="paper-content">
                                    <div className="right__table">
                                        <div className="right__table-search-filter">
                                            <div className="box-search-filter-btns">
                                                <div className="box-search">
                                                    <div className="box-input" ref={productSelectBtnRef} onClick={() => setIsProductSelectPopup(true)}>
                                                        <div className="search-icon">
                                                            <FontAwesomeIcon icon={faMagnifyingGlass} />
                                                        </div>
                                                        <input
                                                            placeholder='Tìm theo tên hoặc mã sản phẩm'
                                                            type="text"
                                                            name="search"
                                                            id=""
                                                            autoComplete='on'
                                                            onChange={(e) => setDataPageProduct(prev => {
                                                                return {
                                                                    ...prev,
                                                                    keyword: e.target.value
                                                                }
                                                            })}
                                                        />
                                                        <fieldset className='input-field' />
                                                    </div>
                                                    {
                                                        isProductSelectPopup &&
                                                        <ListSelectPopup
                                                            title={"sản phẩm"}
                                                            isLarge={true}
                                                            isSearch={false}
                                                            isFastCreate={true}
                                                            dataList={productSelectList}
                                                            handleCLickCreateProductQuickly={handleClickCreateQuicklylyProduct}
                                                            handleSelect={(id) => setListProductDetail(prev => {
                                                                return [...prev,
                                                                {
                                                                    id: id,
                                                                    name: productSelectList.find(product => product.id === id)?.name,
                                                                    image: productSelectList.find(product => product.id === id)?.image?.[0].url,
                                                                    unit: productSelectList.find(product => product.id === id)?.unit || "------",
                                                                    imported_quantity: 0,
                                                                    price: productSelectList.find(product => product.id === id)?.cost_price,
                                                                    discount: 0,
                                                                    tax: 0,
                                                                    total: 0
                                                                }]
                                                            })}
                                                            btnRef={productSelectBtnRef}
                                                            closePopup={() => setIsProductSelectPopup(false)}
                                                        />
                                                    }
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                                <div className='box-table'>
                                    <ProductsTable
                                        productsList={listProductDetail}
                                        setProductList={setListProductDetail}
                                        colsToRender={colsToRender}
                                        setIsProductSelectPopup={setIsProductSelectPopup}
                                    />
                                    <div className='box-total'>
                                        <div className="box-total__container">
                                            <div className="box-subinfo">
                                                <div className="box-note">
                                                    <label className='input-label' htmlFor="note">Ghi chú đơn</label>
                                                    <div className="text-field">
                                                        <textarea
                                                            name="note"
                                                            id="note"
                                                            placeholder='VD: Hàng tặng gói riêng'
                                                            onChange={(e) => setDataBody(prev => {
                                                                return { ...prev, note: e.target.value }

                                                            })}
                                                        />
                                                        <fieldset className='input-field'></fieldset>
                                                    </div>
                                                </div>
                                                <div className="box-tags">
                                                    <label className='input-label' htmlFor="tags">Tags</label>
                                                    <div className="tags-field">
                                                        <div className="group-tags">
                                                            {
                                                                tags.map((tag, index) => {
                                                                    return (
                                                                        <div key={index} role='button' className="btn-tag-item">
                                                                            <span className="tag-label">{tag}</span>
                                                                            <FontAwesomeIcon onClick={() => deleteTag(tag)} icon={faXmark} />
                                                                        </div>
                                                                    )
                                                                })
                                                            }
                                                            <textarea
                                                                name="Tags"
                                                                id="note"
                                                                placeholder='VD: Hàng tặng'
                                                                onChange={(e) => setDataBody(prev => {
                                                                    return { ...prev, tags: e.target.value }

                                                                })}
                                                            />
                                                            <fieldset className='input-field'></fieldset>
                                                        </div>
                                                    </div>
                                                </div>
                                            </div>
                                            <div className="box-price-info">
                                                <div className="info-item">
                                                    <p>Số lượng</p>
                                                    <p>{listProductDetail.map(prod => !isNaN(prod.ordered_quantity) ? prod.ordered_quantity : prod.imported_quantity).reduce((acc, curr) => acc + curr, 0)}</p>
                                                </div>
                                                <div className="info-item">
                                                    <p>Tổng tiền</p>
                                                    <p>{listProductDetail.map(prod => (Number(prod.price) - Number(prod.discount) + Number(prod.tax)) * Number(!isNaN(prod.ordered_quantity) ? prod.ordered_quantity : prod.imported_quantity)).reduce((acc, curr) => acc + curr, 0)}</p>
                                                </div>
                                                <div className="info-item">
                                                    <button onClick={() => setIsDiscountPopup(true)} ref={discountBtnRef} className="btn-base">
                                                        <span className="btn__label">
                                                            <p>Chiết khấu (F6)</p>
                                                        </span>
                                                    </button>
                                                    {isDiscountPopup && <DiscountPopup
                                                        price={order.total}
                                                        discount={order.discount}
                                                        btnRef={discountBtnRef}
                                                        closePopup={closeDiscountPopup}
                                                        handleChangeDiscount={(value) => {
                                                            setDataBody(prev => {
                                                                return {
                                                                    ...prev,
                                                                    discount: value
                                                                }
                                                            })
                                                        }}
                                                    />}
                                                    <p>{dataBody.discount}</p>
                                                </div>
                                                <div className="info-item">
                                                    <div className="d-flex">
                                                        <p>Thuế</p>
                                                        <p>{infoIcon}</p>
                                                    </div>
                                                    <p>{listProductDetail.map(prod => Number(prod.tax)).reduce((acc, curr) => acc + curr, 0)}</p>
                                                </div>
                                                <div className="info-item">
                                                    <p className='total-price'>Tiền cần trả</p>
                                                    <p className='total-price'>{listProductDetail.map(prod => (Number(prod.price) - Number(prod.discount) + Number(prod.tax)) * Number(!isNaN(prod.ordered_quantity) ? prod.ordered_quantity : prod.imported_quantity)).reduce((acc, curr) => acc + curr, 0) - Number(dataBody.discount)}</p>
                                                </div>
                                            </div>
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

export default withAuthorization(CreateGRN, ["ADMIN", "COORDINATOR"])
