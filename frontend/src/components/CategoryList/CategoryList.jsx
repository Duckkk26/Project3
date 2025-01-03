import Header from "../Header/Header"
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome'
import { faPlus, faMagnifyingGlass, faCaretDown, faChevronRight, faChevronLeft } from '@fortawesome/free-solid-svg-icons'
import cn from "classnames"
import { createCategory, getListCategory } from "../../service/CategoryAPI"
import { useEffect, useRef, useState } from "react"
import { formatDateTime } from "../../utils/DateUtils"
import LimitSelectPopup from "../LimitSelectPopup/LimitSelectPopup"
import CreateCategoryPopup from "../CreateCategoryPopup/CreateCategoryPopup"
import { useDebouncedEffect } from "../../utils/CommonUtils"
import Notification from "../Notification/Notification"
import { toast } from "react-toastify"

const CategoryList = () => {

    const limitBtnRef = useRef(null);

    const [isOpenLimitPopup, setIsOpenLimitPopup] = useState(false);
    const [limit, setLimit] = useState(10);
    const [page, setPage] = useState(1);
    const [pageQuantiy, setPageQuantity] = useState(1);
    const [categoriesQuantity, setCategoriesQuantity] = useState();
    const [categoriesList, setCategoriesList] = useState([]);
    const [dataBody, setDataBody] = useState(
        {
            keyword: null
        }
    );

    const [isCreateCategory, setIsCreateCategory] = useState(false);
    const [dataCreateCategory, setDataCreateCategory] = useState({
        name: "",
    })

    const handlePrevPage = () => {
        if (page > 1) {
            setPage(prev => prev - 1);
        }
    }

    const handleNextPage = () => {
        if (page < pageQuantiy) {
            setPage(prev => prev + 1);
        }
    }

    const handleChangeDataCreateSupplierGroup = (e) => {
        const { name, value } = e.target; // Lấy name và value từ input
        setDataCreateCategory((prevData) => ({
            ...prevData,
            [name]: value // Cập nhật giá trị tương ứng với name
        }));
    };

    const handleClickBack = () => {
        setIsCreateCategory(false);
    }

    const handleCLickCreate = async () => {
        const response = await createCategory(dataCreateCategory);
        if (response.status_code === 201) {
            toast(<Notification 
                type={"success"} 
                withIcon 
                message={"Tạo loại sản phẩm thành công!"} 
            />,
            {
                autoClose: 4000,
                closeButton: false,
                hideProgressBar: true,
            }
        )
            setDataCreateCategory({
                name: "",
            });
            setIsCreateCategory(false);
        }
        setIsCreateCategory(false);

    }

    const fetchCategoriesList = async () => {
        const categories = await getListCategory(page, limit, dataBody);
        setCategoriesList(categories.data.data);
        setCategoriesQuantity(categories.data.total_items);
        setPageQuantity(categories.data.total_page);
    }

    useDebouncedEffect(() => {
        fetchCategoriesList();
    }, 200, [limit, page, dataBody, isCreateCategory]);

    return (
        <>

            {isCreateCategory && (
                <>
                    <div className="overlay"></div>
                    <CreateCategoryPopup handleOnClickBack={handleClickBack} handleOnChange={handleChangeDataCreateSupplierGroup} handleOnClickCreate={handleCLickCreate} />
                </>
            )}

            <Header title={"Loại sản phẩm"} />
            <div className="right__listPage">
                <div className='right__toolbar'>
                    <div className="btn-toolbar">
                        <button className="btn btn-primary" onClick={() => setIsCreateCategory(true)}>
                            <span className="btn__icon">
                                <FontAwesomeIcon icon={faPlus} />
                            </span>
                            <span className="btn__title">Thêm loại sản phẩm</span>
                        </button>
                    </div>
                </div>
                <div className="right__table">
                    <div className="right__table-scroller">
                        <div className="box-scroller">
                            <div className="group-scroller-btns">
                                <button className="btn-scroller active">Tất cả loại sản phẩm</button>
                            </div>
                        </div>
                    </div>
                    <div className="right__table-search-filter">
                        <div className="">
                            <div className="box-search-filter-btns">
                                <div className="box-search">
                                    <div className="box-input">
                                        <div className="search-icon">
                                            <FontAwesomeIcon icon={faMagnifyingGlass} />
                                        </div>
                                        <input placeholder='Tìm kiếm theo mã sản phẩm, tên sản phẩm'
                                            type="text"
                                            name="search"
                                            id=""
                                            autoComplete='on'
                                            onChange={e => setDataBody(prev => {
                                                return {
                                                    ...prev,
                                                    keyword: e.target.value
                                                }
                                            })} />
                                        <fieldset className='input-field' />
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                    <div className="right__table-headers">
                        <table className="box-table-headers">
                            <colgroup>
                                <col style={{ width: "80px" }} />
                                <col style={{ width: "470px" }} />
                                <col style={{ width: "215px" }} />
                                <col style={{ width: "220px" }} />
                                <col style={{ width: "215px" }} />
                            </colgroup>
                            <thead>
                                <tr className="group-table-headers">
                                    <th rowSpan={1} className='table-icon'>
                                        <div className="group-icons">
                                        </div>
                                    </th>
                                    <th
                                        colSpan={1}
                                        rowSpan={1}
                                        className={cn("table-header-item", "text-start")}
                                    >
                                        Tên loại sản phẩm
                                    </th>
                                    <th
                                        colSpan={1}
                                        rowSpan={1}
                                        className={cn("table-header-item", "text-center")}
                                    >
                                        Mã loại
                                    </th>
                                    <th
                                        colSpan={1}
                                        rowSpan={1}
                                        className={cn("table-header-item", "text-start")}
                                    >
                                        Ngày sửa gần nhất
                                    </th>
                                    <th
                                        colSpan={1}
                                        rowSpan={1}
                                        className={cn("table-header-item", "text-center")}
                                    >
                                        Ngày tạo
                                    </th>
                                </tr>
                            </thead>
                        </table>
                    </div>
                    <div className="right__table-content">
                        <div className="right__table-data">
                            <div className="table-data__container">
                                <table className="box-table-data">
                                    <colgroup>
                                        <col style={{ width: "80px" }} />
                                        <col style={{ width: "470px" }} />
                                        <col style={{ width: "215px" }} />
                                        <col style={{ width: "220px" }} />
                                        <col style={{ width: "215px" }} />
                                    </colgroup>
                                    <tbody>
                                        {categoriesList.map((category, index) => {
                                            return (
                                                <tr key={index} className="table-data-row">
                                                    <td rowSpan={1} className='table-icon'>
                                                        <div className="group-icons">
                                                        </div>
                                                    </td>
                                                    <td className={cn("table-data-item", "text-start")}>
                                                        <p className="box-text">
                                                            {category.name}
                                                        </p>
                                                    </td>
                                                    <td className={cn("table-data-item", "text-center")}>
                                                        <p className="box-text">
                                                            {category.sub_id}
                                                        </p>
                                                    </td>
                                                    <td className={cn("table-data-item", "text-start")}>
                                                        <p className="box-text">
                                                            {formatDateTime(category.created_at)}
                                                        </p>
                                                    </td>
                                                    <td className={cn("table-data-item", "text-center")}>
                                                        <p className="box-text">
                                                            {formatDateTime(category.updated_at)}
                                                        </p>
                                                    </td>
                                                </tr>
                                            )
                                        })}
                                    </tbody>
                                </table>
                            </div>
                        </div>
                        <div className="right__table-pagination">
                            <p>Hiển thị</p>
                            <div className="box-page-limit">
                                <button
                                    ref={limitBtnRef}
                                    onClick={() => setIsOpenLimitPopup(!isOpenLimitPopup)}
                                    className={cn("btn-page-limit", { "selected": isOpenLimitPopup })}
                                >
                                    {limit}
                                    <span>
                                        <FontAwesomeIcon icon={faCaretDown} />
                                    </span>
                                </button>
                                {isOpenLimitPopup
                                    && <LimitSelectPopup
                                        btnRef={limitBtnRef}
                                        closePopup={() => setIsOpenLimitPopup(false)}
                                        limit={limit}
                                        handleChangeLimit={(limit) => {
                                            setPage(1);
                                            setLimit(limit)
                                        }} />}
                            </div>
                            <p>Kết quả</p>
                            <p className="item-quantity">Từ {(page - 1) * limit + 1} đến {(page - 1) * limit + categoriesList.length} trên tổng {categoriesQuantity}</p>
                            <button
                                className={cn('btn-icon', 'btn-page', { 'inactive': page === 1 })}
                                onClick={handlePrevPage}
                            >
                                <FontAwesomeIcon icon={faChevronLeft} />
                            </button>
                            {
                                Array(pageQuantiy).fill(null).map((_, index) => (
                                    <div
                                        key={index}
                                        className={cn("box-page", { 'active': page === index + 1 })}
                                        onClick={() => setPage(index + 1)}
                                    >
                                        {index + 1}
                                    </div>
                                ))
                            }
                            <button
                                className={cn('btn-icon', 'btn-page', { 'inactive': page === pageQuantiy })}
                                onClick={handleNextPage}
                            >
                                <FontAwesomeIcon icon={faChevronRight} />
                            </button>
                        </div>
                    </div>
                </div>
            </div>
        </>
    )
}

export default CategoryList
