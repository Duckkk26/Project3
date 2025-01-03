import React, { useCallback, useEffect, useRef, useState } from 'react'
import cn from 'classnames'
import Cookies from 'js-cookie'
import { Link, useNavigate } from 'react-router-dom'

// Import Components
import Header from '../Header/Header'

// Import Columns Info
import col from '../../assets/colgroup/grn-list.js'

// Import Icons
import exportIcon from '../../assets/icons/ExportIcon'
import importIcon from '../../assets/icons/ImportIcon'
import filterIcon from '../../assets/icons/FilterIcon'
import settingFilterIcon from '../../assets/icons/SettingFilterIcon.jsx'
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome'
import { faAnglesRight, faCaretDown, faChevronLeft, faChevronRight, faMagnifyingGlass, faPlus, faXmark } from '@fortawesome/free-solid-svg-icons'
import StatusFilter from './FiltersPopup/StatusFilter.jsx'
import CreatedAtFilter from '../GINList/FiltersPopup/CreatedAtFilter.jsx'
import { getDataExport, getGRNs } from '../../service/GRNApi.jsx'
import { formatDateTime } from '../../utils/DateUtils.jsx'
import { exportExcel } from '../../config/ExportExcel.jsx'
import SelectDatePopup from '../SelectDatePopup.jsx'
import { useDebouncedEffect } from '../../utils/CommonUtils.jsx'
import FilterPopup from '../FilterPopup/FilterPopup.jsx'
import LimitSelectPopup from '../LimitSelectPopup/LimitSelectPopup.jsx'
import Notification from '../Notification/Notification.jsx'
import { toast } from 'react-toastify'

const GRNList = () => {
    const navigate = useNavigate();
    const [page, setPage] = useState(1);
    const [pageQuantiy, setPageQuantity] = useState(1);
    const [limit, setLimit] = useState(20);
    const [isOpenLimitPopup, setIsOpenLimitPopup] = useState(false);

    const [isOpenCreatedAtPopup, setIsOpenCreatedAtPopup] = useState(false);
    const [isOpenStatusPopup, setIsOpenStatusPopup] = useState(false);
    const [statusListFilter, setStatusListFilter] = useState(null);
    const [totalItems, setTotalItems] = useState(0);
    const limitBtnRef = useRef(null);

    const [grnList, setGrnList] = useState([])

    const status = {
        COMPLETED: "Hoàn thành",
        PENDING: "Chưa nhập",
        RETURNED: "Đã hoàn hàng",
        NOT_RETURNED: "Chưa hoàn hàng",
        REFUNDED: "Đã hoàn tiền",
        NOT_REFUNDED: "Chưa hoàn tiền",
        PARTIAL: "Nhập một phần",
        CANCELLED: "Đã hủy",
        ENTERED: "Đã nhập",
        NOT_ENTERED: "Chưa nhập",
        UNPAID: "Chưa thanh toán",
        PAID: "Đã thanh toán",
        PARTIAL_PAID: "Thanh toán một phần",
        ORDERING: "Đang đặt hàng",
        TRADING: "Đang giao dịch",
    }

    const [filterBody, setFilterBody] = useState({
        keyword: null,
        statuses: null,
        received_statuses: null,
        supplier_ids: null,
        start_created_at: null,
        end_created_at: null,
        start_expected_at: null,
        end_expected_at: null,
        product_ids: null,
        user_created_ids: null,
        user_completed_ids: null,
        user_cancelled_ids: null
    })

    const fetchGrnList = async () => {
        const responseAPI = await getGRNs(page, limit, "filter_grns", Cookies.get('filter_grns'), filterBody);
        setGrnList(responseAPI.data.data);
        setPageQuantity(responseAPI.data.total_page);
        setTotalItems(responseAPI.data.total_items);
    }

    const statusTab = [
        { key: "all", label: "Tất cả đơn nhập hàng", statuses: null },
        { key: "ordering", label: "Đang đặt hàng", statuses: ["ORDERING"] },
        { key: "trading", label: "Đang giao dịch", statuses: ["TRADING"] },
        { key: "completed", label: "Hoàn thành", statuses: ["COMPLETED"] }
    ];
    const [tabActive, setTabActive] = useState("all");
    const handleTabClick = (key, statuses) => {
        setTabActive(key);
        setFilterBody(prev => ({
            ...prev,
            statuses: statuses
        }));
    };

    useDebouncedEffect(() => {
        fetchGrnList();
    }, 200, [page, limit, filterBody])

    useEffect(() => {
        setFilterBody(prev => {
            return {
                ...prev,
                statuses: statusListFilter
            }
        })
    }, [statusListFilter])

    const [isFilterPopup, setIsFilterPopup] = useState(false)
    const defaultCols = {
        grn_id: true,
        grn_sub_id: true,
        grn_status: true,
        grn_receive_status: true,
        grn_payment_status: true,
        grn_return_status: false,
        // grn_refund_status: false,
        grn_received_at: true,
        grn_expected_at: false,
        grn_cancelled_at: true,
        grn_payment_at: false,
        grn_total_received_quantity: true,
        grn_total_value: true,
        grn_supplier_name: true,
        grn_supplier_sub_id: true,
        grn_supplier_phone: true,
        grn_supplier_email: true,
        grn_user_created_name: true,
        grn_user_completed_name: true,
        grn_user_cancelled_name: true,
        grn_note: false,
        grn_tags: false,
        grn_created_at: true,
        grn_updated_at: false,
        grn_order_sub_id: false
    };
    const [colsToRender, setColsToRender] = useState(() => {
        const storedCols = Cookies.get('filter_grns');
        return storedCols ? JSON.parse(storedCols) : defaultCols
    })

    useEffect(() => {
        Cookies.set('filter_grns', JSON.stringify(colsToRender));
        fetchGrnList();
    }, [colsToRender])

    const headersRef = useRef(null);
    const contentRef = useRef(null);
    const statusBtnRef = useRef(null);
    const createdAtRef = useRef(null)

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

    const handleColsChange = (name) => {
        setColsToRender({ ...colsToRender, [name]: !colsToRender[name] })
    }

    const handleExportData = async () => {
        const responseAPI = await getDataExport("DEFAULT", filterBody);

        const dataExport = responseAPI.data.map((grn, index) => {
            return {
                "STT": index + 1,
                "Mã đơn nhập": grn.sub_id,
                "Trạng thái": status[grn.status],
                "Trạng thái nhận hàng": status[grn.received_status],
                "Tên nhà cung cấp": grn.supplier_name,
                "Tên nhân viên nhập": grn.user_imported_name,
                "Tổng tiền": grn.total_value,
                "Ngày tạo": formatDateTime(grn.created_at),
                "Ngày nhập": formatDateTime(grn.received_at),
                "Ngày cập nhật": formatDateTime(grn.updated_at),
            }
        })

        exportExcel(dataExport, "Danh sách đơn nhập hàng");
        toast(<Notification 
                type={"success"} 
                withIcon 
                message={"Xuất file thành công"} 
            />,
            {
                autoClose: 4000,
                closeButton: false,
                hideProgressBar: true,
            }
        )
    }

    return (
        <>
            <Header title={"Danh sách đơn nhập hàng"} />
            <div className='right__listPage'>
                <div className='right__toolbar'>
                    <div className="btn-toolbar">
                        <button className="btn btn-base btn-text" onClick={handleExportData}>
                            <span className="btn__label">
                                <span className="btn__icon">
                                    {exportIcon}
                                </span>
                                Xuất file
                            </span>
                        </button>
                    </div>
                    <div className="btn-toolbar">
                        <button className="btn btn-primary" onClick={() => { navigate("/admin/grns/create") }}>
                            <span className="btn__icon">
                                <FontAwesomeIcon icon={faPlus} />
                            </span>
                            <span className="btn__title">Tạo phiếu nhập hàng</span>
                        </button>
                    </div>
                </div>
                <div className="right__table">
                    <div className="right__table-scroller">
                        <div className="box-scroller">
                            <div className="group-scroller-btns">
                                {statusTab.map(({ key, label, statuses }) => (
                                    <button
                                        key={key}
                                        className={`btn-scroller ${tabActive === key ? "active" : ""}`}
                                        onClick={() => handleTabClick(key, statuses)}
                                    >
                                        {label}
                                    </button>
                                ))}
                            </div>
                        </div>
                    </div>
                    <div className="right__table-search-filter">
                        <div className="box-search-filter-btns">
                            <div className="box-search">
                                <div className="box-input">
                                    <div className="search-icon">
                                        <FontAwesomeIcon icon={faMagnifyingGlass} />
                                    </div>
                                    <input
                                        placeholder='Tìm mã đơn nhập'
                                        type="text"
                                        name="search"
                                        id=""
                                        autoComplete='on'
                                        onChange={(e) => setFilterBody(prev => {
                                            return {
                                                ...prev,
                                                keyword: e.target.value
                                            }
                                        })
                                        }
                                    />
                                    <fieldset className='input-field' />
                                </div>
                            </div>
                            <div className="btn-group group-filter-btns">
                                <button ref={statusBtnRef} className="btn btn-base btn-filter" onClick={() => setIsOpenStatusPopup(!isOpenStatusPopup)}>
                                    <span className="btn__label">
                                        Trạng thái
                                        <span className="btn__icon">
                                            <FontAwesomeIcon icon={faCaretDown} />
                                        </span>
                                    </span>
                                </button>
                                {isOpenStatusPopup
                                    && <StatusFilter
                                        statusBtnRef={statusBtnRef}
                                        closePopup={() => setIsOpenStatusPopup(false)} type={"GRN"}
                                        setStatusList={setStatusListFilter}
                                    />
                                }
                                <SelectDatePopup
                                    setDataFilters={(data) => setFilterBody(prev => {
                                        return {
                                            ...prev,
                                            start_created_at: data.date_from,
                                            end_created_at: data.date_to
                                        };
                                    })}
                                />
                                <button className="btn btn-base btn-filter" onClick={() => setFilterBody({
                                    keyword: null,
                                    statuses: null,
                                    received_statuses: null,
                                    supplier_ids: null,
                                    start_created_at: null,
                                    end_created_at: null,
                                    start_expected_at: null,
                                    end_expected_at: null,
                                    product_ids: null,
                                    user_created_ids: null,
                                    user_completed_ids: null,
                                    user_cancelled_ids: null
                                })}>
                                    <span className="btn__label">
                                        Xóa bộ lọc
                                    </span>
                                </button>
                            </div>
                        </div>
                        {(filterBody.statuses || (filterBody.start_created_at && filterBody.end_created_at))
                            && (<div className="box-show-selected-filter">
                                <div className="box-show-selected-container">
                                    {filterBody.statuses && (<div className="box-show-selected-item">
                                        <span> Trạng thái: {filterBody.statuses.map((key, index) => (
                                            <span key={index}>{status[key]}{index < filterBody.statuses.length - 1 ? ', ' : ''} </span>
                                        ))}
                                        </span>
                                        <div className="box-remove-item">
                                            <button onClick={() => setFilterBody((prev) => ({ ...prev, statuses: null }))} className="btn-remove-item" type="button">
                                                <span>
                                                    <FontAwesomeIcon icon={faXmark} />
                                                </span>
                                            </button>
                                        </div>
                                    </div>)}
                                    {filterBody.start_created_at && filterBody.end_created_at && (<div className="box-show-selected-item">
                                        <span>Ngày tạo: (
                                            <span>{filterBody.start_created_at}</span> -
                                            <span>{filterBody.end_created_at}</span>
                                            )</span>
                                        <div className="box-remove-item">
                                            <button onClick={() => setFilterBody((prev) => ({ ...prev, start_created_at: null, end_created_at: null }))} className="btn-remove-item" type="button">
                                                <span>
                                                    <FontAwesomeIcon icon={faXmark} />
                                                </span>
                                            </button>
                                        </div>
                                    </div>)}

                                </div>
                            </div>)
                        }
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
                                    {/* Render table headers for columns that exist in grnList */}
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
                                        {grnList.map((grn, index) => {
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
                                                                        <div className={cn('box-status',
                                                                            `box-status--${grn[key.substring(4)]?.toLowerCase()}`

                                                                        )}>
                                                                            <span>{(status[grn[key.substring(4)]])}</span>
                                                                        </div>
                                                                    </td>
                                                                )
                                                            } else if (key === "grn_sub_id") {
                                                                return (
                                                                    <td
                                                                        key={key}
                                                                        className={cn("table-data-item", col[key].align)}
                                                                    >
                                                                        <Link to={`/admin/grns/GRN/${grn.id}`}>
                                                                            <p className='box-text'>
                                                                                <a className='box-id'>{grn[key.substring(4)]}</a>
                                                                            </p>
                                                                        </Link>

                                                                    </td>
                                                                )
                                                            } else if (key.includes("_at")) {
                                                                return (
                                                                    <td
                                                                        key={key}
                                                                        className={cn("table-data-item", col[key].align)}
                                                                    >
                                                                        <p className='box-text'>
                                                                            {formatDateTime(grn[key.substring(4)])}
                                                                        </p>
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
                                                                            key !== "id" ? grn[key.substring(4)] :
                                                                                <a className='box-id'>{grn[key.substring(4)]}</a>
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
                                <button className="btn-page-limit" onClick={() => setIsOpenLimitPopup(!isOpenLimitPopup)}>
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
                                            setPage(1)
                                            setLimit(limit)
                                        }}
                                    />}
                            </div>
                            <p>kết quả</p>
                            <p className="item-quantity">Từ {(page - 1) * limit + 1} đến {(page - 1) * limit + grnList.length} trên tổng {totalItems}</p>
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

export default GRNList;