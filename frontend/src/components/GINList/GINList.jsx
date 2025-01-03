import React, { useEffect, useRef, useState } from "react";
import cn from "classnames";
import Cookies from "js-cookie";
// Import Components
import Header from "../Header/Header";
import { faXmark } from "@fortawesome/free-solid-svg-icons";
// Import Columns Info
import { col, gin_status } from "../../assets/colgroup/gin-list.js";

// Import Icons
import exportIcon from "../../assets/icons/ExportIcon";
import importIcon from "../../assets/icons/ImportIcon";
import filterIcon from "../../assets/icons/FilterIcon";
import settingFilterIcon from "../../assets/icons/SettingFilterIcon.jsx";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import {
	faAnglesRight,
	faCaretDown,
	faChevronLeft,
	faChevronRight,
	faMagnifyingGlass,
	faPlus,
} from "@fortawesome/free-solid-svg-icons";
import { useNavigate } from "react-router-dom";
import LimitSelectPopup from "../LimitSelectPopup/LimitSelectPopup.jsx";
import { exportData, getGINs } from "../../service/GINApi.jsx";
import GINStatusFilter from "./FiltersPopup/GINStatusFilter.jsx";
import CreatedAtFilter from "./FiltersPopup/CreatedAtFilter.jsx";
import { exportExcel } from "../../config/ExportExcel.jsx";
import { formatDate, formatDateTime } from "../../utils/DateUtils.jsx";
import { useDebouncedEffect } from "../../utils/CommonUtils.jsx";
import FilterPopup from "../FilterPopup/FilterPopup.jsx";
import SelectDatePopup from "../SelectDatePopup.jsx";
import { toast } from "react-toastify";
import Notification from "../Notification/Notification.jsx";

const GINList = () => {
	const [page, setPage] = useState(1);
	const [pageQuantiy, setPageQuantity] = useState(1);
	const [limit, setLimit] = useState(10);
	const [isOpenLimitPopup, setIsOpenLimitPopup] = useState(false);
	const limitBtnRef = useRef(null);
	const navigate = useNavigate();
	const [totalItems, setTotalItems] = useState(0);

	const [dataFilters, setdataFilters] = useState({
		keyword: null,
		statues: null,
		created_date_from: null,
		created_date_to: null,
		balanced_date_from: null,
		balanced_date_to: null,
		user_created_ids: null,
		user_balanced_ids: null,
		user_inspection_ids: null,
	});

	const [isOpenStatusPopup, setIsOpenStatusPopup] = useState(false);
	const [isOpenCreatedAtPopup, setIsOpenCreatedAtPopup] = useState(false);

	const [activeTab, setActiveTab] = useState("ALL");

	const handleChangeTab = (tab) => {
		setActiveTab(tab);
		if (tab == "ALL") {
			setdataFilters({ ...dataFilters, statues: null });
		} else if (tab == "CHECKING") {
			setdataFilters({ ...dataFilters, statues: ["CHECKING"] });
		} else if (tab == "BALANCED") {
			setdataFilters({ ...dataFilters, statues: ["BALANCED"] });
		}
	};

	const handeChangeDatafilter = (filters) => {
		const updateDataFilters = { ...dataFilters };
		for (const [key, value] of Object.entries(filters)) {
			updateDataFilters[key] = value;
		}		
		setdataFilters(updateDataFilters);
	};

	const [ginList, setGinList] = useState([]);
	// Get list of columns that need redering from Cookies
	const [isFilterPopup, setIsFilterPopup] = useState(false)

	const defaultCols = {
		id: true,
		sub_id: true,
		created_at: true,
		updated_at: true,
		status: true,
		user_created_name: true,
		user_inspection_name: true,
		user_balanced_name: true,
		note: true,
		balanced_at: true,
	};
	const [colsToRender, setColsToRender] = useState(() => {
		const storedCols = Cookies.get("filter_gins");
		return storedCols
			? JSON.parse(storedCols)
			: defaultCols
	});

	// Set required columns to Cookies
	useEffect(() => {
		Cookies.set("filter_gins", JSON.stringify(colsToRender));
		fetchGinList();
	}, [colsToRender]);

	const fetchGinList = async () => {
		const res = await getGINs(
			page,
			limit,
			"filter_gins",
			Cookies.get("filter_gins"),
			dataFilters
		);
		setGinList(res.data.data);
		setPageQuantity(res.data.total_page);
		setTotalItems(res.data.total_items);
	};

	useDebouncedEffect(() => {
		fetchGinList();
	}, 200, [page, limit, dataFilters]);

	const headersRef = useRef(null);
	const contentRef = useRef(null);
	const ginStatusRef = useRef(null);
	const createdAtRef = useRef(null);

	const handleScroll = (e, target) => {
		target.scrollLeft = e.target.scrollLeft;
	};

	const handleExportExcel = async () => {
		const responseAPI = await exportData("DEFAULT", dataFilters);

		const dataExport = responseAPI.data?.map((item, index) => {
			// For each item, map the products
			return item.products.map((product, indexProduct) => {
				return {
					"STT": indexProduct === 0 ? index + 1 : "",
					"Mã phiếu kiểm": item.sub_id,
					"Nhân viên kiểm": item.user_created_name,
					"Nhân viên tạo": item.user_created_name,
					"Nhân viên cân bằng": item.user_balanced_name,
					"Ngày cân bằng": item.balanced_at,
					"Trạng thái": gin_status[item.status],
					"Mã sản phẩm": product.product_sub_id,
					"Tên sản phẩm": product.product_name,
					"Đơn vị": product.unit,
					"Tồn trong kho": product.real_quantity,
					"Tồn thực tế": product.actual_stock,
					"Số lượng lệch": product.discrepancy_quantity,
					"Lí do": product.reason,
					"Ghi chú": product.note
				};
			});
		}).flat(); // Flatten the array because each item has multiple products

		exportExcel(dataExport, "Danh sách phiếu kiểm hàng");
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
	};

	const handlePrevPage = () => {
		if (page > 1) {
			setPage((prev) => prev - 1);
		}
	};

	const handleNextPage = () => {
		if (page < pageQuantiy) {
			setPage((prev) => prev + 1);
		}
	};

	return (
		<>
			<Header title={"Kiểm hàng"} />
			<div className="right__listPage">
				<div className="right__toolbar">
					<div className="btn-toolbar">
						<button className="btn btn-base btn-text" onClick={handleExportExcel}>
							<span className="btn__label">
								<span className="btn__icon">{exportIcon}</span>
								Xuất file
							</span>
						</button>
					</div>
					<div className="btn-toolbar">
						<button
							className="btn btn-primary"
							onClick={() => navigate("/admin/gins/create")}
						>
							<span className="btn__icon">
								<FontAwesomeIcon icon={faPlus} />
							</span>
							<span className="btn__title">Tạo phiếu kiểm hàng</span>
						</button>
					</div>
				</div>
				<div className="right__table">
					<div className="right__table-scroller">
						<div className="box-scroller">
							<div className="group-scroller-btns">
								<button
									onClick={() => handleChangeTab("ALL")}
									className={cn("btn-scroller", {
										active: activeTab === "ALL",
									})}
								>
									Tất cả phiếu kiểm hàng
								</button>
								<button
									onClick={() => handleChangeTab("CHECKING")}
									className={cn("btn-scroller", {
										active: activeTab === "CHECKING",
									})}
								>
									Đang kiểm
								</button>
								<button
									onClick={() => handleChangeTab("BALANCED")}
									className={cn("btn-scroller", {
										active: activeTab === "BALANCED",
									})}
								>
									Đã cân bằng
								</button>
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
										value={dataFilters.keyword || ""}
										onChange={(e) => {
											setPage(1)
											setdataFilters(prev => {
												return { ...prev, keyword: e.target.value }
											})
										}}
										placeholder="Tìm mã đơn nhập"
										type="text"
										name="keyword"
										id=""
										autoComplete="on"
									/>
									<fieldset className="input-field" />
								</div>
							</div>
							<div className="btn-group group-filter-btns">
								<button
									ref={ginStatusRef}
									className="btn btn-base btn-filter"
									onClick={() => setIsOpenStatusPopup(!isOpenStatusPopup)}
								>
									<span className="btn__label">
										Trạng thái
										<span className="btn__icon">
											<FontAwesomeIcon icon={faCaretDown} />
										</span>
									</span>
								</button>
								{isOpenStatusPopup && (
									<GINStatusFilter
										ginStatusRef={ginStatusRef}
										closePopup={() => setIsOpenStatusPopup(false)}
										handeChangeDatafilter={handeChangeDatafilter}
									/>
								)}
								<SelectDatePopup
                                    setDataFilters={(data) => setdataFilters(prev => {
                                        return {
                                            ...prev,
                                            created_date_from: data.date_from,
                                            created_date_to: data.date_to
                                        };
                                    })}
                                />

								<button className="btn btn-base btn-filter" onClick={() => setdataFilters({
									keyword: null,
									statues: null,
									created_date_from: null,
									created_date_to: null,
									balanced_date_from: null,
									balanced_date_to: null,
									user_created_ids: null,
									user_balanced_ids: null,
									user_inspection_ids: null,
								})}>
									<span className="btn__label">
										Xóa bộ lọc
									</span>
								</button>
							</div>
						</div>
						{(dataFilters.statues || (dataFilters.created_date_from && dataFilters.created_date_to))
							&& (<div className="box-show-selected-filter">
								<div className="box-show-selected-container">
									{dataFilters.statues && (<div className="box-show-selected-item">
										<span> Trạng thái: {dataFilters.statues.map((status, index) => (
											<span key={index}>{gin_status[status]}{index < dataFilters.statues.length - 1 ? ', ' : ''} </span>
										))}
										</span>
										<div className="box-remove-item">
											<button onClick={() => setdataFilters((prev) => ({ ...prev, statues: null }))} className="btn-remove-item" type="button">
												<span>
													<FontAwesomeIcon icon={faXmark} />
												</span>
											</button>
										</div>
									</div>)}
									{dataFilters.created_date_from && dataFilters.created_date_to && (<div className="box-show-selected-item">
										<span>Ngày tạo: (
											<span>{dataFilters.created_date_from}</span> -
											<span>{dataFilters.created_date_to}</span>
											)</span>
										<div className="box-remove-item">
											<button onClick={() => setdataFilters((prev) => ({ ...prev, created_date_from: null, created_date_to: null }))} className="btn-remove-item" type="button">
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
						className="right__table-headers"
					>
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
													width: col[key].width,
												}}
											/>
										);
									}
									return null;
								})}
							</colgroup>
							<thead>
								<tr className="group-table-headers">
									<th rowSpan={1} className="table-icon">
										<div className="group-icons">
											<button className="btn-icon" onClick={() => setIsFilterPopup(true)}>
												{settingFilterIcon}
											</button>
										</div>
									</th>
									{/* Render table headers for columns that exist in ginList */}
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
															<span className="box-icon">
																<FontAwesomeIcon icon={faCaretDown} />
															</span>
														</div>
													</th>
												);
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
											);
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
								className="table-data__container"
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
															width: col[key].width,
														}}
													/>
												);
											}
											return null;
										})}
									</colgroup>
									<tbody>
										{ginList.map((gin, index) => {
											return (
												<tr key={index} className="table-data-row">
													<td rowSpan={1} className="table-icon">
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
																		className={cn(
																			"table-data-item",
																			col[key].align
																		)}
																	>
																		<div
																			className={`box-status box-status--${gin[key].toLowerCase()}`}
																		>
																			<span>
																				{gin[key] == "CHECKING"
																					? "Đang kiểm kho"
																					: gin[key] == "BALANCED"
																						? "Đã cân bằng"
																						: "Đã xóa"}
																			</span>
																		</div>
																	</td>
																);
															} else if (key.includes("_at")) {
																return (
																	<td
																		key={key}
																		className={cn(
																			"table-data-item",
																			col[key].align
																		)}
																	>
																		<p className="box-text">
																			{gin[key] ? formatDateTime(gin[key]) : ""}
																		</p>
																	</td>
																);
															}
															return (
																<td
																	key={key}
																	className={cn(
																		"table-data-item",
																		col[key].align
																	)}
																>
																	<p className="box-text">
																		{key !== "sub_id" ? (
																			gin[key]
																		) : (
																			<a
																				onClick={() =>
																					navigate(
																						`/admin/gins/GIN/${gin.id}`
																					)
																				}
																				className="box-id"
																			>
																				{gin[key]}
																			</a>
																		)}
																	</p>
																</td>
															);
														}
														return null;
													})}
												</tr>
											);
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
									className={cn("btn-page-limit", {
										selected: isOpenLimitPopup,
									})}
								>
									{limit}
									<span>
										<FontAwesomeIcon icon={faCaretDown} />
									</span>
								</button>
								{isOpenLimitPopup && (
									<LimitSelectPopup
										btnRef={limitBtnRef}
										closePopup={() => setIsOpenLimitPopup(false)}
										limit={limit}
										handleChangeLimit={(limit) => {
											setPage(1);
											setLimit(limit)
										}}
									/>
								)}
							</div>
							<p>kết quả</p>
							<p className="item-quantity">
								Từ {(page - 1) * limit + 1} đến{" "}
								{(page - 1) * limit + ginList.length} trên tổng {totalItems}
							</p>
							<button
								className={cn("btn-icon", "btn-page", { inactive: page === 1 })}
								onClick={handlePrevPage}
							>
								<FontAwesomeIcon icon={faChevronLeft} />
							</button>
							{Array(pageQuantiy)
								.fill(null)
								.map((_, index) => (
									<div
										key={index}
										className={cn("box-page", { active: page === index + 1 })}
										onClick={() => setPage(index + 1)}
									>
										{index + 1}
									</div>
								))}
							<button
								className={cn("btn-icon", "btn-page", {
									inactive: page === pageQuantiy,
								})}
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
	);
};

export default GINList;
