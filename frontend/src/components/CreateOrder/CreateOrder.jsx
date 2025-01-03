import React, { useEffect, useRef, useState } from 'react'
import { Link, useNavigate } from 'react-router-dom'
import Cookies from 'js-cookie'

// Import Components
import SearchSupplier from '../SearchSupplier/SearchSupplier'
import SupplierInfo from '../SupplierInfo/SupplierInfo'
import ProductsTable from '../ProductsTable/ProductsTable.jsx'
import DiscountPopup from '../DiscountPopup/DiscountPopup.jsx'

// Import Icons
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome'
import { faCaretDown, faChevronLeft, faGear, faMagnifyingGlass, faXmark } from '@fortawesome/free-solid-svg-icons'
import infoIcon from '../../assets/icons/InfoIcon.jsx'
import ListSelectPopup from '../ListSelectPopup/ListSelectPopup.jsx'

import { LocalizationProvider, DateTimePicker } from '@mui/x-date-pickers'
import { AdapterDayjs } from '@mui/x-date-pickers/AdapterDayjs'
import { quickGetProductList } from '../../service/ProductAPI.jsx'
import { createNewOrder } from '../../service/OrderAPI.jsx'
import CreateProductFastlyPopup from '../CreateProductFastlyPopup/CreateProductFastlyPopup.jsx'
import CreateSupplierPopup from '../CreateSupplierPopup/CreateSupplierPopup.jsx'
import { useDebouncedEffect } from '../../utils/CommonUtils.jsx'
import withAuthorization from '../../hoc/withAuthorization.jsx'
import Notification from '../Notification/Notification.jsx'
import { toast } from 'react-toastify'

const CreateOrder = () => {
    const navigate = useNavigate();
    const [order, setOrder] = useState({
        discount: 0,
        total: 350000,
    });

    const [tags, setTags] = useState([]);
    const [tagValue, setTagValue] = useState("");
    const [isDiscountPopup, setIsDiscountPopup] = useState(false)
    const discountBtnRef = useRef(null)

    // Get list of columns that need redering from Cookies
    const [colsToRender, setColsToRender] = useState(() => {
        const storedCols = Cookies.get('filter_products_table');
        return storedCols ? JSON.parse(storedCols) : {
            index: true,
            image: true,
            name: true,
            unit: true,
            ordered_quantity: true,
            price: true,
            discount: true,
            tax: true,
            total: true
        }
    })

    useEffect(() => {
        setColsToRender((prev) => {
            const { imported_quantity, ...rest } = prev; // Tách imported_quantity khỏi state
            return rest; // Trả về state mới mà không có imported_quantity
        });
    }, [])

    // Set required columns to Cookies
    useEffect(() => {
        Cookies.set('filter_products_table', JSON.stringify(colsToRender));
    }, [colsToRender])

    const addTag = (e) => {
        if (e.key === 'Enter' && !tags.includes(tagValue)) {
            setTags([...tags, tagValue])
            setTagValue("");
        }
    }

    const deleteTag = (tag) => {
        setTags(prev => prev.filter(item => item !== tag))
    }

    const closeDiscountPopup = () => {
        setIsDiscountPopup(false);
    }

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

    const [isCreateProductQuickly, setIsCreateProductQuickly] = useState(false);
    const [isCreateSupplier, setIsCreateSupplier] = useState(false);
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
    }, 200, [dataPageProduct.keyword])

    const [listProductDetail, setListProductDetail] = useState([]);

    const [dataBody, setDataBody] = useState({
        supplier_id: null,
        sub_id: null,
        expected_at: null,
        note: null,
        tags: null,
        discount: 0,
        products: []
    });
    const handleCreateOrder = async () => {
        const response = await createNewOrder(dataBody);

        if (response.status_code === 201) {
            toast(<Notification 
                type={"success"} 
                withIcon 
                message={'Tạo đơn đặt hàng thành công'} 
            />,
            {
                autoClose: 4000,
                closeButton: false,
                hideProgressBar: true,
            }
        )
            navigate('/admin/order_suppliers');
        }
    }

    const handleClickCreateQuicklylyProduct = () => {
        setIsCreateProductQuickly(true);
    }

    const handleClickBack = () => {
        setIsCreateProductQuickly(false);
    }

    const handleClickBackInSupplier = () => {
        setIsCreateSupplier(false);
    }

    const handleClickCreateSupplier = () => {
        setIsCreateSupplier(true);
    }

    useEffect(() => {
        setDataBody(prev => {
            return {
                ...prev,
                products: listProductDetail.map(product => {
                    return {
                        product_id: product.id,
                        quantity: product.ordered_quantity,
                        price: product.price,
                        discount: product.discount,
                        tax: product.tax,
                        total: (product.price - product.discount + product.tax) * product.ordered_quantity
                    }
                })
            }
        })
    }, [listProductDetail])
    return (
        <>
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
            </>
            <div className="right__navbar">
                <div className="box-navbar">
                    <div className="btn-toolbar">
                        <Link to='/admin/order_suppliers' className='btn-back'>
                            <FontAwesomeIcon icon={faChevronLeft} />
                            <h6 className="btn-back__title">
                                Quay lại danh sách đơn đặt hàng
                            </h6>
                        </Link>
                    </div>
                    <div className="btn-toolbar">
                        <button className="btn btn-outline-primary" onClick={() => navigate("/admin/order_suppliers")}>
                            <span className="btn__title">Thoát</span>
                        </button>
                        <button className="btn btn-primary" onClick={handleCreateOrder}>
                            <span className="btn__title">Tạo đơn đặt hàng</span>
                        </button>
                    </div>
                </div>
            </div>
            <div className="right__paperPage">
                <div className="right__paperPage-wrapper">
                    <div className="right__paperPage-container">
                        <div className="box-supplier">
                            <div className="box-paper">
                                <div className="paper-header">
                                    <p>Thông tin nhà cung cấp</p>
                                </div>
                                <div className="paper-content">
                                    {dataBody.supplier_id ?
                                        <SupplierInfo supplierId={dataBody.supplier_id} handleCancel={() => setDataBody(prev => {
                                            return {
                                                ...prev,
                                                supplier_id: null
                                            }
                                        })} /> :
                                        <SearchSupplier setSelectItem={id => setDataBody(prev => {
                                            return {
                                                ...prev,
                                                supplier_id: id
                                            }
                                        })}

                                            setCreateSupplier={handleClickCreateSupplier} />
                                    }
                                </div>
                            </div>
                        </div>
                        <div className="box-info">
                            <div className="box-paper">
                                <div className="paper-header">
                                    <p>Thông tin đơn đặt hàng</p>
                                </div>
                                <div className="paper-content">
                                    <div className="group-info">
                                        <div className="info-item">
                                            <p className="info-title">Nhân viên tạo </p>
                                            <div className="info-field">
                                                <div className="box-input">
                                                    <input
                                                        placeholder='Nhân viên tạo'
                                                        name='user_created_name'
                                                        type="text"
                                                        className="text-field"
                                                        defaultValue={localStorage.getItem("fullName")}
                                                        disabled={true}
                                                    />
                                                    <fieldset className='input-field'></fieldset>
                                                </div>
                                            </div>
                                        </div>
                                        <div className="info-item">
                                            <p className="info-title">Ngày nhập</p>
                                            <div className="info-field">
                                                <div className="box-input">
                                                    <LocalizationProvider dateAdapter={AdapterDayjs}>
                                                        <DateTimePicker
                                                            onChange={(value) => {
                                                                setDataBody((prev) => ({
                                                                    ...prev,
                                                                    expected_at: value ? value.format("YYYY-MM-DD") : null // Extract only the date part
                                                                }));
                                                            }}
                                                            renderInput={(params) => <TextField {...params} />} // Ensure the TextField input is rendered
                                                        />
                                                    </LocalizationProvider>
                                                </div>
                                            </div>
                                        </div>
                                        <div className="info-item">
                                            <p className="info-title">Mã đơn</p>
                                            <div className="info-field">
                                                <div className="box-input">
                                                    <input placeholder='Nhập mã' name='id' type="text" className="text-field" onChange={(e) => setDataBody(prev => {
                                                        return {
                                                            ...prev,
                                                            sub_id: e.target.value
                                                        }
                                                    })} />
                                                    <fieldset className='input-field'></fieldset>
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                        <div className="box-products">
                            <div className="box-paper">
                                <div className="paper-header">
                                    <div className="box-header">
                                        <p>Thông tin sản phẩm</p>
                                    </div>
                                </div>
                                <div className="paper-content">
                                    <div className="right__table">
                                        <div className="right__table-search-filter">
                                            <div className="box-search-filter-btns">
                                                <div className="box-search">
                                                    <div ref={productSelectBtnRef} onClick={() => setIsProductSelectPopup(true)} className="box-input">
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
                                                            })
                                                            }
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
                                                                console.log(productSelectList.find(product => product.id === id));
                                                                
                                                                return [...prev,
                                                                {
                                                                    id: id,
                                                                    name: productSelectList.find(product => product.id === id)?.name,
                                                                    image: productSelectList.find(product => product.id === id)?.image?.[0],
                                                                    unit: productSelectList.find(product => product.id === id)?.unit || "------",
                                                                    ordered_quantity: 0,
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
                                <div className="box-table">
                                    <ProductsTable
                                        productsList={listProductDetail}
                                        colsToRender={colsToRender}
                                        setProductList={setListProductDetail}
                                        setIsProductSelectPopup={() => setIsProductSelectPopup(true)}
                                    />
                                    <div className="box-total">
                                        <div className="box-total__container">
                                            <div className="box-subinfo">
                                                <div className="box-note">
                                                    <label className='input-label' htmlFor="note">Ghi chú đơn</label>
                                                    <div className="text-field">
                                                        <textarea name="note" id="note" placeholder='VD: Hàng tặng gói riêng' onChange={(e) => setDataBody(prev => {
                                                            return {
                                                                ...prev,
                                                                note: e.target.value
                                                            }
                                                        })}></textarea>
                                                        <fieldset className='input-field'></fieldset>
                                                    </div>
                                                </div>
                                                <div className="box-tags">
                                                    <label className='input-label' htmlFor="tags">Tags</label>
                                                    <div className="text-field">
                                                        <textarea name="tags" id="tags" placeholder='' onChange={(e) => setDataBody(prev => {
                                                            return {
                                                                ...prev,
                                                                tags: e.target.value
                                                            }
                                                        })}></textarea>
                                                        <fieldset className='input-field'></fieldset>
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

export default withAuthorization(CreateOrder, ["ADMIN","WAREHOUSE_MANAGER", "WAREHOUSE_STAFF"]);