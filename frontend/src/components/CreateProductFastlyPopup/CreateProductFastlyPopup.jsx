import s from './CreateProductFastlyPopup.module.scss'
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome'
import { faCaretDown, faCaretUp } from '@fortawesome/free-solid-svg-icons'
import { useRef, useState, useEffect, act } from 'react'
import SelectListCategoryPopup from './SelectListCategoryPopup/SelectListCategoryPopup'
import { getListCategory } from '../../service/CategoryAPI'
import { quickCreateProduct } from '../../service/ProductAPI'
import { toast } from 'react-toastify'
import Notification from '../Notification/Notification'

const CreateProductFastlyPopup = ({ handleCLickBack, setListProductDetail }) => {
    const [isSelectCategoryList, setIsSelectCategoryList] = useState(false);

    const selectCategoryRef = useRef(null);

    const [categoryList, setCategoryList] = useState([]);
    const [dataPageCategory, setDataPageCategory] = useState({
        currentPage: 1,
        totalPage: 1,
        currentSize: 5,
    });
    const [categoryDataFilter, setCategoryDataFilter] = useState({
        keyword: null
    });
    const fetchCategoryList = async () => {
        const response = await getListCategory(dataPageCategory.currentPage, dataPageCategory.currentSize, categoryDataFilter);
        setCategoryList(response.data.data);
        setDataPageCategory(prevState => {
            return {
                ...prevState,
                totalPage: response.data.total_page
            }
        });
    }
    const fetchMoreCategory = async () => {
        if (dataPageCategory.currentPage < dataPageCategory.totalPage) {
            const response = await getListCategory(dataPageCategory.currentPage + 1, dataPageCategory.currentSize, categoryDataFilter);
            setCategoryList(prevList => [...prevList, ...response.data.data]);
            setDataPageCategory(prevState => {
                return {
                    ...prevState,
                    currentPage: prevState.currentPage + 1,
                    totalPage: response.data.total_page
                }
            });
        }
    }
    const handleFetchMoreCategory = () => {
        if (isSelectCategoryList) {
            fetchCategoryList();
        } else {
            setCategoryDataFilter((prev) => {
                return (
                    {
                        ...prev,
                        keyword: ""
                    }
                )
            })
            setDataPageCategory(prevState => {
                return {
                    ...prevState,
                    currentPage: 1,
                    totalPage: 1
                }
            });
        }
    }
    useEffect(() => {
        handleFetchMoreCategory();
    }, [isSelectCategoryList])
    useEffect(() => {
        setDataPageCategory(prevState => {
            return {
                ...prevState,
                currentPage: 1
            }
        });
        handleFetchMoreCategory();
    }, [categoryDataFilter.keyword])

    const [dataBody, setDataBody] = useState({
        name: null,
        cost_price: null,
        retail_price: null,
        category_id: null,
        unit: null,
        quantity: null
    });

    const handleQuickCreateProduct = async () => {
        const responseAPI = await quickCreateProduct(dataBody);
        if (responseAPI.status_code === 201) {
            setListProductDetail((prev) => {
                return [...prev,
                {
                    product_id: responseAPI.data,
                    name: dataBody.name,
                    image: null,
                    unit: dataBody.unit || "-----",
                    ordered_quantity: dataBody.quantity,
                    real_quantity: dataBody.quantity,
                    actual_stock: 0,
                    discrepancy_quantity: 0,
                    price: dataBody.retail_price,
                    reason: "Khác",
                    discount: 0,
                    tax: 0,
                    total: 0
                }]
            });
            toast(<Notification 
                    type={"success"} 
                    withIcon 
                    message={"Thêm sản phẩm thành công"} 
                />,
                {
                    autoClose: 4000,
                    closeButton: false,
                    hideProgressBar: true,
                }
            )
            handleCLickBack();
        }
    }

    return (
        <div className={s["modal-content"]}>
            <div className={s["modal-header"]}>
                <div className={s["modal-box-header"]}>
                    <h5>Thêm nhanh sản phẩm</h5>
                </div>
            </div>

            <div className={s["modal-body"]}>
                <div className={s["row-name"]}>
                    <label htmlFor="name" className={s["form-label"]}>
                        Tên sản phẩm
                        <span className="asterisk-icon">*</span>
                    </label>
                    <div className={s["form-textfield"]}>
                        <input
                            type="text"
                            name="name"
                            id="name"
                            placeholder='Nhập tên sản phẩm'
                            onChange={(e) => setDataBody((prev) => {
                                return {
                                    ...prev,
                                    name: e.target.value
                                }
                            })}
                        />
                        <fieldset className="input-field"></fieldset>
                    </div>
                </div>
                <div className={s["row-content"]}>
                    <div className={s["content"]}>
                        <label htmlFor="gianhap" className={s["form-label"]}>
                            Giá nhập
                        </label>
                        <div className={s["form-textfield"]}>
                            <input
                                type="number"
                                name="gianhap"
                                id="gianhap"
                                onChange={(e) => setDataBody((prev) => { return { ...prev, cost_price: e.target.value } })}
                            />
                            <fieldset className="input-field"></fieldset>
                        </div>
                    </div>
                    <div className={s["content"]}>
                        <label htmlFor="giabanle" className={s["form-label"]}>
                            Giá bán lẻ
                        </label>
                        <div className={s["form-textfield"]}>
                            <input
                                type="number"
                                name="giabanle"
                                id="giabanle"
                                onChange={(e) => setDataBody((prev) => { return { ...prev, retail_price: e.target.value } })}
                            />
                            <fieldset className="input-field"></fieldset>
                        </div>
                    </div>
                </div>
                <div className={s["row-content"]}>
                    <div className={s["content"]}>
                        <label htmlFor="soluongdat" className={s["form-label"]}>
                            Số lượng đặt
                        </label>
                        <div className={s["form-textfield"]}>
                            <input
                                type="number"
                                name="soluongdat"
                                id="soluongdat"
                                onChange={(e) => setDataBody((prev) => { return { ...prev, quantity: e.target.value } })}
                            />
                            <fieldset className="input-field"></fieldset>
                        </div>
                    </div>
                    <div className={s["content"]}>
                        <label htmlFor="loaisanpham" className={s["form-label"]}>
                            Chọn loại sản phẩm
                        </label>
                        <div className={s["box-select"]}>
                            <button id='category' ref={selectCategoryRef} onClick={() => setIsSelectCategoryList(!isSelectCategoryList)}>
                                <span>{dataBody.category_id ? categoryList?.find(category => category.id === dataBody.category_id)?.name : "Chọn loại sản phẩm" }</span>
                                {isSelectCategoryList ? <FontAwesomeIcon icon={faCaretDown} /> : <FontAwesomeIcon icon={faCaretUp} />}
                            </button>
                            {
                                isSelectCategoryList &&
                                <SelectListCategoryPopup
                                    btnRef={selectCategoryRef}
                                    title={"Loại sản phẩm"}
                                    closePopup={() => setIsSelectCategoryList(false)}
                                    dataList={categoryList}
                                    fetchMoreData={fetchMoreCategory}
                                    handleSelect={(id) => setDataBody((prev) => { return { ...prev, category_id: id } })}
                                />

                            }
                        </div>
                    </div>
                </div>
                <div className={s["row-content"]}>
                    <div className={s["content"]}>
                        <label htmlFor="donvi" className={s["form-label"]}>
                            Đơn vị
                        </label>
                        <div className={s["form-textfield"]}>
                            <input
                                type="text"
                                name="donvi"
                                id="donvi"
                                onChange={(e) => setDataBody((prev) => { return { ...prev, unit: e.target.value } })}
                            />
                            <fieldset className="input-field"></fieldset>
                        </div>
                    </div>
                </div>
            </div>
            <div className={s["modal-footer"]}>
                <button className="btn btn-outline-primary" onClick={handleCLickBack} >
                    <span className="btn__title">Thoát</span>
                </button>
                <button className="btn btn-primary" onClick={handleQuickCreateProduct}>
                    <span className="btn__title">Thêm</span>
                </button>
            </div>
        </div>
    )
}
export default CreateProductFastlyPopup;