import React, { useEffect, useRef, useState } from 'react'
import { Link, useNavigate } from 'react-router-dom'
import cn from 'classnames'
import Cookies from 'js-cookie'

// Import Components
import Header from '../Header/Header'

// Import Columns Info
import col from '../../assets/colgroup/orders-list.js'

// Import Icons
import exportIcon from '../../assets/icons/ExportIcon'
import importIcon from '../../assets/icons/ImportIcon'
import filterIcon from '../../assets/icons/FilterIcon'
import settingFilterIcon from '../../assets/icons/SettingFilterIcon.jsx'
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome'
import { faAnglesRight, faCaretDown, faChevronLeft, faChevronRight, faMagnifyingGlass, faPlus, faXmark } from '@fortawesome/free-solid-svg-icons'
import LimitSelectPopup from '../LimitSelectPopup/LimitSelectPopup.jsx'
import StatusFilter from '../GRNList/FiltersPopup/StatusFilter.jsx'
import { getAllOrders, getDataExport } from '../../service/OrderAPI.jsx'
import { formatDateTime } from '../../utils/DateUtils.jsx'
import { exportExcel } from '../../config/ExportExcel.jsx'
import SelectDatePopup from '../SelectDatePopup.jsx'
import FilterPopup from '../FilterPopup/FilterPopup.jsx'
import { toast } from 'react-toastify'
import Notification from '../Notification/Notification.jsx'

const OrdersList = () => {
    const [isOpenLimitPopup, setIsOpenLimitPopup] = useState(false);
    const limitBtnRef = useRef(null);
    const navigate = useNavigate()

    const [isOpenStatusPopup, setIsOpenStatusPopup] = useState(false);
    const [isFilterPopup, setIsFilterPopup] = useState(false)

    const defaultCols = {
        id: true,
        created_at: true,
        status: true,
        supplier_name: true,
        user_created_name: true,
        quantity: true,
        price: true,
        supplier_id: false,
        user_cancelled_name: false,
        user_completed_name: false,
        supplier_phone: false,
        supplier_address: false,
        supplier_email: false,
        note: false,
        tags: false,
        expected_at: false,
        completed_at: false,
        cancelled_at: false
    };
    // Get list of columns that need redering from Cookies
    const [colsToRender, setColsToRender] = useState(() => {
        const storedCols = Cookies.get('filter_orders');
        return storedCols ? JSON.parse(storedCols) : defaultCols;
    })

    // Set required columns to Cookies
    useEffect(() => {
        Cookies.set('filter_orders', JSON.stringify(colsToRender));
        fetchOrderList();
    }, [colsToRender])

    const headersRef = useRef(null);
    const contentRef = useRef(null);
    const orderStatusRef = useRef(null);

    const handleScroll = (e, target) => {
        target.scrollLeft = e.target.scrollLeft;
    }

    const handlePrevPage = () => {
        if (dataPage.page > 1) {
            setDataPage(prev => {
                return {
                    ...prev,
                    page: prev.page - 1
                }
            });
        }
    }

    const handleNextPage = () => {
        if (dataPage.page < dataPage.totalPage) {
            setDataPage(prev => {
                return {
                    ...prev,
                    page: prev.page + 1
                }
            });
        }
    }

    const status = {
        PENDING: "Chưa nhập",
        PARTIAL: "Nhập một phần",
        COMPLETED: "Hoàn thành",
        CANCELLED: "Đã hủy"
    }

    const statusTab = [
        { key: "all", label: "Tất cả đơn đặt hàng", statuses: null },
        { key: "pending", label: "Chưa nhập", statuses: ["PENDING"] },
        { key: "partial", label: "Nhập một phần", statuses: ["PARTIAL"] },
        { key: "completed", label: "Hoàn thành", statuses: ["COMPLETED"] }
    ];

    const [tabActive, setTabActive] = useState("all");

    const handleTabClick = (key, statuses) => {
        setTabActive(key);
        setDataFilter(prev => ({
            ...prev,
            statuses: statuses
        }));
    };

    const [ordersList, setOrdersList] = useState([]);
    const [dataFilter, setDataFilter] = useState({
        keyword: null,
        statuses: null,
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
    const [dataPage, setDataPage] = useState({
        page: 1,
        size: 10,
        totalPage: 1,
        totalItem: 0
    })
    const fetchOrderList = async () => {
        const response = await getAllOrders(dataPage.page, dataPage.size, "filter_orders", JSON.stringify(colsToRender), dataFilter);
        setOrdersList(response.data.data);
        setDataPage({
            ...dataPage,
            totalPage: response.data.total_page,
            totalItem: response.data.total_items
        })
    }

    useEffect(() => {
        fetchOrderList();
    }, [dataFilter, dataPage.page, dataPage.size])

    const handleExportData = async () => {
        const response = await getDataExport("DEFAULT", dataFilter);

        const dataExport = response.data.map((order, index) => {
            return {
                "STT": index + 1,
                "Mã đơn đặt hàng": order.sub_id,
                "Ngày tạo đơn": formatDateTime(order.created_at),
                "Trạng thái đơn": status[order.status],
                "Nhà cung cấp": order.supplier_name,
                "Nhân viên tạo": order.user_created_name,
                "Tổng số lượng đặt": order.total_quantity,
                "Tổng tiền": order.total_price
            }
        })

        exportExcel(dataExport, "Danh sách đơn đặt hàng nhập");
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
            <Header title={"Danh sách đơn đặt hàng nhập"} />
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
                        <button onClick={() => navigate('/admin/order_suppliers/create')} className="btn btn-primary">
                            <span className="btn__icon">
                                <FontAwesomeIcon icon={faPlus} />
                            </span>
                            <span className="btn__title">Tạo đơn đặt hàng</span>
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
                                        placeholder='Tìm theo mã đơn đặt hàng'
                                        type="text"
                                        name="search"
                                        id=""
                                        autoComplete='on'
                                        onChange={(e) => setDataFilter(prev => {
                                            return {
                                                ...prev,
                                                keyword: e.target.value
                                            }
                                        })
                                        }
                                    />
                                    < fieldset className='input-field' />
                                </div>
                            </div>
                            <div className="btn-group group-filter-btns">
                                <button ref={orderStatusRef} onClick={() => setIsOpenStatusPopup(!isOpenStatusPopup)} className="btn btn-base btn-filter">
                                    <span className="btn__label">
                                        Trạng thái
                                        <span className="btn__icon">
                                            <FontAwesomeIcon icon={faCaretDown} />
                                        </span>
                                    </span>
                                </button>
                                {isOpenStatusPopup
                                    && <StatusFilter
                                        statusBtnRef={orderStatusRef}
                                        closePopup={() => setIsOpenStatusPopup(false)}
                                        type={"Order"}
                                        setStatusList={(data) => setDataFilter(prev => {
                                            return {
                                                ...prev,
                                                statuses: data
                                            }
                                        })}
                                    />}
                                <SelectDatePopup
                                    setDataFilters={(data) => setDataFilter(prev => {
                                        return {
                                            ...prev,
                                            start_created_at: data.date_from,
                                            end_created_at: data.date_to
                                        };
                                    })}
                                />
                                <button className="btn btn-base btn-filter" onClick={() => setDataFilter({
                                    keyword: null,
                                    statuses: null,
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
                        {(dataFilter.statuses || (dataFilter.start_created_at && dataFilter.end_created_at))
                            && (<div className="box-show-selected-filter">
                                <div className="box-show-selected-container">
                                    {dataFilter.statuses && (<div className="box-show-selected-item">
                                        <span> Trạng thái: {dataFilter.statuses.map((key, index) => (
                                            <span key={index}>{status[key]}{index < dataFilter.statuses.length - 1 ? ', ' : ''} </span>
                                        ))}
                                        </span>
                                        <div className="box-remove-item">
                                            <button onClick={() => setDataFilter((prev) => ({ ...prev, statuses: null }))} className="btn-remove-item" type="button">
                                                <span>
                                                    <FontAwesomeIcon icon={faXmark} />
                                                </span>
                                            </button>
                                        </div>
                                    </div>)}
                                    {dataFilter.start_created_at && dataFilter.end_created_at && (<div className="box-show-selected-item">
                                        <span>Ngày tạo: (
                                            <span>{dataFilter.start_created_at}</span> -
                                            <span>{dataFilter.end_created_at}</span>
                                            )</span>
                                        <div className="box-remove-item">
                                            <button onClick={() => setDataFilter((prev) => ({ ...prev, start_created_at: null, end_created_at: null }))} className="btn-remove-item" type="button">
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
                                    {/* Render table headers for columns that exist in ordersList */}
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
                                        {ordersList.map((order, index) => {
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
                                                                            'box-status--pending': order[key] === "PENDING",
                                                                            'box-status--partial': order[key] === "PARTIAL",
                                                                            'box-status--completed': order[key] === "COMPLETED",
                                                                            'box-status--cancelled': order[key] === "CANCELLED",
                                                                        })}>
                                                                            <span>{status[order[key]]}</span>
                                                                        </div>
                                                                    </td>
                                                                )
                                                            } else if (key.includes("_at")) {
                                                                console.log(order);
                                                                
                                                                return (
                                                                    <td
                                                                        key={key}
                                                                        className={cn("table-data-item", col[key].align)}
                                                                    >
                                                                        <p className='box-text'>{formatDateTime(order[key])}</p>
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
                                                                            key !== "id" ? order[key] :
                                                                                <Link to={`/admin/order_suppliers/ORD/${order[key]}`} className='box-id'>{order[key]}</Link>
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
                                    {dataPage.size}
                                    <span>
                                        <FontAwesomeIcon icon={faCaretDown} />
                                    </span>
                                </button>
                                {isOpenLimitPopup
                                    && <LimitSelectPopup
                                        btnRef={limitBtnRef}
                                        closePopup={() => setIsOpenLimitPopup(false)}
                                        limit={dataPage.size}
                                        handleChangeLimit={(limit) => {
                                            setDataPage(prev => {
                                                return {
                                                    ...prev,
                                                    page: 1,
                                                    size: limit
                                                }
                                            });
                                        }}
                                    />}
                            </div>
                            <p>kết quả</p>
                            <p className="item-quantity">Từ {(dataPage.page - 1) * dataPage.size + 1} đến {(dataPage.page - 1) * dataPage.size + ordersList.length} trên tổng {dataPage.totalItem}</p>
                            <button
                                className={cn('btn-icon', 'btn-page', { 'inactive': dataPage.page === 1 })}
                                onClick={handlePrevPage}
                            >
                                <FontAwesomeIcon icon={faChevronLeft} />
                            </button>
                            {
                                Array(dataPage.totalPage).fill(null).map((_, index) => (
                                    <div
                                        key={index}
                                        className={cn("box-page", { 'active': dataPage.page === index + 1 })}
                                        onClick={() => setDataPage(prev => {
                                            return {
                                                ...prev,
                                                page: index + 1
                                            }
                                        })
                                        }
                                    >
                                        {index + 1}
                                    </div>
                                ))
                            }
                            <button
                                className={cn('btn-icon', 'btn-page', { 'inactive': dataPage.page === dataPage.totalPage })}
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

export default OrdersList;