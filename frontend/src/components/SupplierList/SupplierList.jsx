import React, { useEffect, useRef, useState } from 'react'
import cn from 'classnames'
import Cookies from 'js-cookie'

// Import Components
import Header from '../Header/Header'

// Import Columns Info
import col from '../../assets/colgroup/suppliers-list.js'

// Import Icons
import exportIcon from '../../assets/icons/ExportIcon'
import filterIcon from '../../assets/icons/FilterIcon'
import settingFilterIcon from '../../assets/icons/SettingFilterIcon.jsx'
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome'
import { faAnglesRight, faCaretDown, faChevronLeft, faChevronRight, faMagnifyingGlass, faPlus, faXmark } from '@fortawesome/free-solid-svg-icons'
import { getAllSupplierByName, getDataExportExcel, getSupplierList } from '../../service/SuppliersAPI.jsx'
import { Link, useNavigate } from 'react-router-dom'
import LimitSelectPopup from '../LimitSelectPopup/LimitSelectPopup.jsx'
import { exportExcel } from '../../config/ExportExcel.jsx'
import { formatDateTime } from '../../utils/DateUtils.jsx'
import { useDebouncedEffect } from '../../utils/CommonUtils.jsx'
import SelectDatePopup from '../SelectDatePopup.jsx'
import FilterPopup from '../FilterPopup/FilterPopup.jsx'

const SupplierList = () => {
    const navigate = useNavigate();
    const headersRef = useRef(null);
    const contentRef = useRef(null);
    const handleScroll = (e, target) => {
        target.scrollLeft = e.target.scrollLeft;
    }

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

    const [isFilterPopup, setIsFilterPopup] = useState(false)
    const defaultCols = {
        id: true,
        name: true,
        phone: false,
        email: false,
        address: false,
        status: true,
        tags: false,
        note: false,
        total_refund: false,
        created_at: true,
        updated_at: false
    };
    const [colsToRender, setColsToRender] = useState(() => {
        const storedCols = Cookies.get('filter_suppliers');
        return storedCols ? JSON.parse(storedCols) : defaultCols
    })
    const [page, setPage] = useState(1);
    const [pageQuantiy, setPageQuantity] = useState(1);
    const [limit, setLimit] = useState(10);
    const [isOpenLimitPopup, setIsOpenLimitPopup] = useState(false);
    const [suppliersList, setSuppliersList] = useState([]);
    const [suppliersQuantity, setSuppliersQuantity] = useState();
    const limitBtnRef = useRef(null);

    const [dataFilter, setDataFilter] = useState({
        keyword: null,
        statuses: null,
        supplier_group_ids: null,
        created_date_from: null,
        created_date_to: null,
        tags: null
    });

    const fetchSupplierList = async () => {
        const suppliers = await getSupplierList(page, limit, "filter_suppliers", Cookies.get("filter_suppliers"), dataFilter);

        setSuppliersList(suppliers.data.data);
        setSuppliersQuantity(suppliers.data.total_items);
        setPageQuantity(suppliers.data.total_page)
    }

    useEffect(() => {
        Cookies.set('filter_suppliers', JSON.stringify(colsToRender));
    }, [colsToRender])

    useDebouncedEffect(() => {
        fetchSupplierList();
    }, 200, [page, limit, dataFilter, colsToRender])

    const handleExport = async () => {
        const responseAPI = await getDataExportExcel("DEFAULT", dataFilter);

        const dataExport = responseAPI.data.map((item) => {
            return {
                "Mã nhà cung cấp": item.sub_id,
                "Tên nhà cung cấp": item.name,
                "Số điện thoại": item.phone,
                "Email": item.email,
                "Địa chỉ": item.address,
                "Nhãn hiệu": item.tags,
                "Ghi chú": item.note,
                "Tên nhóm nhà cung cấp": item.name_group,
                "Mã nhóm nhà cung cấp": item.sub_id_group,
                "Ngày tạo": formatDateTime(item.created_at),
                "Ngày cập nhật": formatDateTime(item.updated_at)
            }
        });

        exportExcel(dataExport, "Danh sách nhà cung cấp");
    }

    return (
        <>
            <Header title={"Danh sách nhà cung cấp"} />
            <div className='right__listPage'>
                <div className='right__toolbar'>
                    <div className='btn-toolbar'>
                        <button className='btn btn-base btn-text' onClick={handleExport}>
                            <span className="btn__label">
                                <span className="btn__icon">
                                    {exportIcon}
                                </span>
                                Xuất file
                            </span>
                        </button>
                        <button className="btn btn-base btn-text" onClick={() => navigate("/admin/supplier_groups")}>
                            <span className="btn__label">
                                Nhóm nhà cung cấp
                            </span>
                        </button>
                    </div>
                    <div className="btn-toolbar">
                        <button className="btn btn-primary" onClick={() => navigate('/admin/suppliers/create')}>
                            <span className="btn__icon">
                                <FontAwesomeIcon icon={faPlus} />
                            </span>
                            <span className="btn__title">Thêm nhà cung cấp</span>
                        </button>
                    </div>
                </div>

                <div className='right__table'>
                    <div className="right__table-scroller">
                        <div className="box-scroller">
                            <div className="group-scroller-btns">
                                <button className="btn-scroller active">Tất cả sản phẩm</button>
                            </div>
                        </div>
                    </div>
                    <div className='right__table-search-filter'>
                        <div className='box-search-filter-btns'>
                            <div className="box-search">
                                <div className="box-input">
                                    <div className="search-icon">
                                        <FontAwesomeIcon icon={faMagnifyingGlass} />
                                    </div>
                                    <input
                                        placeholder='Tìm kiếm theo tên, mã hoặc số điện thoại nhà cung cấp'
                                        type="text"
                                        name="search"
                                        id=""
                                        autoComplete='on'
                                        onChange={(e) => setDataFilter(prev => {
                                            return {
                                                ...prev,
                                                keyword: e.target.value
                                            }
                                        })}
                                    />
                                    <fieldset className='input-field' />
                                </div>
                            </div>

                            <div className="btn-group group-filter-btns">
                                <SelectDatePopup
                                    setDataFilters={(data) => setDataFilter(prev => {
                                        return {
                                            ...prev,
                                            created_date_from: data.date_from,
                                            created_date_to: data.date_to
                                        };
                                    })}
                                />
                                <button className="btn btn-base btn-filter" onClick={() => setDataFilter({
                                    keyword: null,
                                    statuses: null,
                                    supplier_group_ids: null,
                                    created_date_from: null,
                                    created_date_to: null,
                                    tags: null
                                })}>
                                    <span className="btn__label">
                                        Xóa bộ lọc
                                    </span>
                                </button>
                            </div>
                        </div>
                        {(dataFilter.created_date_from && dataFilter.created_date_to)
                            && (
                                <div className="box-show-selected-filter">
                                    <div className="box-show-selected-container">
                                        {dataFilter.created_date_from && dataFilter.created_date_to && (
                                            <div className="box-show-selected-item">
                                                <span>
                                                    Ngày tạo: (<span>{dataFilter.created_date_from}</span> -
                                                    <span>{dataFilter.created_date_to}</span>)
                                                </span>
                                                <div className="box-remove-item">
                                                    <button
                                                        onClick={() =>
                                                            setDataBody((prev) => ({
                                                                ...prev,
                                                                created_date_from: null,
                                                                created_date_to: null,
                                                            }))
                                                        }
                                                        className="btn-remove-item"
                                                        type="button"
                                                    >
                                                        <span>
                                                            <FontAwesomeIcon icon={faXmark} />
                                                        </span>
                                                    </button>
                                                </div>
                                            </div>
                                        )}
                                    </div>
                                </div>
                            )}
                    </div>
                    <div
                        ref={headersRef}
                        onScroll={(e) => handleScroll(e, contentRef.current)}
                        className="right__table-headers">
                        <table className="box-table-headers">
                            <colgroup>
                                <col style={{ width: "80px" }} />
                                {/* Render the <colgroup> only for the columns that are in colsToRender */}
                                {Object.entries(colsToRender).map(([key, value]) => {
                                    if (value) {
                                        return (
                                            <col
                                                key={key}
                                                style={{
                                                    width: col[key].width
                                                }}
                                            />
                                        )
                                    }
                                    return null;
                                })}
                            </colgroup>
                            <thead>
                                <tr className="group-table-headers">
                                    <th rowSpan={1} className='table-icon'>
                                        <div className="group-icons">
                                            <button className="btn-icon" onClick={() => setIsFilterPopup(true)}>
                                                {settingFilterIcon}
                                            </button>
                                        </div>
                                    </th>
                                    {/* Render table headers for columns that exist in suppliersList */}
                                    {Object.entries(colsToRender).map(([key, value]) => {
                                        if (value) {
                                            if (key === "created_at") {
                                                return (
                                                    <th
                                                        key={key}
                                                        colSpan={1}
                                                        rowSpan={1}
                                                        className={cn("table-header-item", col[key].align)}
                                                    >
                                                        <div className="box-sort-date">
                                                            {col[key].name}
                                                            <span className='box-icon'>
                                                                <FontAwesomeIcon icon={faCaretDown} />
                                                            </span>
                                                        </div>
                                                    </th>
                                                )
                                            }
                                            return (
                                                <th
                                                    key={key}
                                                    colSpan={1}
                                                    rowSpan={1}
                                                    className={cn("table-header-item", col[key].align)}
                                                >
                                                    {col[key].name}
                                                </th>
                                            )
                                        }
                                        return null;
                                    })}
                                </tr>
                            </thead>
                        </table>
                    </div>
                    <div className="right__table-content">
                        <div className="right__table-data">
                            <div
                                ref={contentRef}
                                onScroll={(e) => handleScroll(e, headersRef.current)}
                                className='table-data__container'
                            >
                                <table className="box-table-data">
                                    <colgroup>
                                        <col style={{ width: "80px" }} />
                                        {/* Render the <colgroup> only for the columns that are in colsToRender */}
                                        {Object.entries(colsToRender).map(([key, value]) => {
                                            if (value) {
                                                return (
                                                    <col
                                                        key={key}
                                                        style={{
                                                            width: col[key].width
                                                        }}
                                                    />
                                                )
                                            }
                                            return null;
                                        })}
                                    </colgroup>
                                    <tbody>
                                        {suppliersList.map((supplier, index) => {
                                            return (
                                                <tr key={index} className="table-data-row">
                                                    <td rowSpan={1} className='table-icon'>
                                                        <div className="group-icons">
                                                            <button className="btn-icon">
                                                                <FontAwesomeIcon icon={faAnglesRight} />
                                                            </button>
                                                        </div>
                                                    </td>
                                                    {Object.entries(colsToRender).map(([key, value]) => {
                                                        if (value) {
                                                            if (key.includes("status")) {
                                                                return (
                                                                    <td
                                                                        key={key}
                                                                        className={cn("table-data-item", col[key].align)}
                                                                    >
                                                                        <div className={cn('box-status', {
                                                                            'box-status--completed': supplier[key] === "ACTIVE",
                                                                            'box-status--cancelled': supplier[key] === "INACTIVE",
                                                                        })}>
                                                                            <span>
                                                                                {supplier[key] === "ACTIVE" ? 'Đang hoạt động' : supplier[key] === "INACTIVE" ? 'Ngừng giao dịch' : supplier[key]}
                                                                            </span>
                                                                        </div>
                                                                    </td>
                                                                )
                                                            } else if (key === "images") {
                                                                return (
                                                                    <td
                                                                        key={key}
                                                                        className={cn("table-data-item", col[key].align)}
                                                                    >
                                                                        <img src={supplier.images[0]?.url} alt={supplier.images[0]?.alt} />
                                                                    </td>
                                                                )
                                                            } else if (key.includes("_at")) {
                                                                return (
                                                                    <td
                                                                        key={key}
                                                                        className={cn("table-data-item", col[key].align)}
                                                                    >
                                                                        {formatDateTime(supplier[key])}
                                                                    </td>
                                                                )
                                                            }
                                                            return (
                                                                <td
                                                                    key={key}
                                                                    className={cn("table-data-item", col[key].align)}
                                                                >
                                                                    <p className='box-text'>
                                                                        {
                                                                            key !== "id" ? supplier[key] :
                                                                            <Link
                                                                                to={`/admin/suppliers/SUP/${supplier?.id}`}
																				className="box-id"
                                                                            >
                                                                                {supplier[key]}
                                                                            </Link>
                                                                
                                                                        }
                                                                    </p>
                                                                </td>
                                                            )
                                                        }
                                                        return null;
                                                    })}
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
                            <p>kết quả</p>
                            <p className="item-quantity">Từ {(page - 1) * limit + 1} đến {(page - 1) * limit + suppliersList.length} trên tổng {suppliersQuantity}</p>
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
            {isFilterPopup
                && <FilterPopup
                    defaultCols={defaultCols}
                    colGroup={col}
                    colsToRender={colsToRender}
                    setColsToRender={setColsToRender}
                    closePopup={() => setIsFilterPopup(false)}
                />}
        </>
    )
}
export default SupplierList;