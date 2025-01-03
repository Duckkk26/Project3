import React, { useEffect, useRef, useState } from "react";
import exportIcon from "../../assets/icons/ExportIcon.jsx";
import importIcon from "../../assets/icons/ImportIcon.jsx";
import filterIcon from "../../assets/icons/FilterIcon.jsx";
import col from "../../assets/colgroup/products-list.js";
import cn from "classnames";
import Cookies from "js-cookie";
import settingFilterIcon from "../../assets/icons/SettingFilterIcon.jsx";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import {
	faAnglesRight,
	faCaretDown,
	faChevronLeft,
	faChevronRight,
	faMagnifyingGlass,
	faPlus,
	faXmark,
} from "@fortawesome/free-solid-svg-icons";
import Header from "../Header/Header.jsx";
import { Link, useNavigate } from "react-router-dom";
import { getProductList } from "../../service/ProductAPI.jsx";
import LimitSelectPopup from "../LimitSelectPopup/LimitSelectPopup.jsx";
import SelectFilter from "../SelectFilter/SelectFilter.jsx";
import { getListCategory } from "../../service/CategoryAPI.jsx";
import { getListBrand } from "../../service/BrandAPI.jsx";
import { useDebouncedEffect } from "../../utils/CommonUtils.jsx";
import SelectDatePopup from "../SelectDatePopup.jsx";
import FilterPopup from "../FilterPopup/FilterPopup.jsx";

const ProductList = () => {
	const formatDate = (dateString) => {
		const date = new Date(dateString);
		const day = date.getDate().toString().padStart(2, "0");
		const month = (date.getMonth() + 1).toString().padStart(2, "0"); // Tháng bắt đầu từ 0
		const year = date.getFullYear();

		return `${day}/${month}/${year}`;
	};

	const defaultCols = {
		id: true,
		sub_id: true,
		images: true,
		name: true,
		category_name: true,
		brand_name: true,
		created_at: true,
		status: false,
		updated_at: false,
		quantity: true,
	};
	const [colsToRender, setColsToRender] = useState(() => {
		const storedCols = Cookies.get("filter_products");
		return storedCols ? JSON.parse(storedCols) : defaultCols;
	});

	const handleScroll = (e, target) => {
		target.scrollLeft = e.target.scrollLeft;
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

	const navigate = useNavigate();

	const headersRef = useRef(null);
	const contentRef = useRef(null);
	const limitBtnRef = useRef(null);
	const filterCategoryBtnRef = useRef(null);
	const filterBrandBtnRef = useRef(null);

	const [page, setPage] = useState(1);
	const [pageQuantiy, setPageQuantity] = useState(1);
	const [limit, setLimit] = useState(10);
	const [productsList, setProductsList] = useState([]);
	const [productsQuantity, setProductsQuantity] = useState();
	const [isOpenLimitPopup, setIsOpenLimitPopup] = useState(false);
	const [dataBody, setDataBody] = useState({
		keyword: null,
		category_ids: null,
		created_date_from: null,
		created_date_to: null,
		brand_ids: null,
		statuses: ["ACTIVE"],
		tags: null,
	});

	// phan useState quan ly filter loai san pham
	const [selectedCategories, setSelectedCategories] = useState([]);
	const [listCategories, setListCategories] = useState([]);
	const [listCategoryShowFilter, setListCategoryShowFilter] = useState([]);
	const [isOpenFilterCategoryPopup, setIsOpenFilterCategoryPopup] =
		useState(false);
	const [dataFilterCategory, setDataFilterCategory] = useState({
		keyword: null,
	});
	const [categoryKeyword, setCategoryKeyword] = useState("");
	const [currentPageFilterCategory, setCurrentPageFilterCategory] = useState(1);
	const [totalPageFilterCategory, setTotalPageFilterCategory] = useState();

	//phan useState quan ly filter nhan hieu
	const [selectedBrands, setSelectedBrands] = useState([]);
	const [listBrands, setListBrands] = useState([]);
	const [listBrandShowFilter, setListBrandShowFilter] = useState([]);
	const [isOpenFilterBrandPopup, setIsOpenFilterBrandPopup] = useState(false);
	const [dataFilterBrand, setDataFilterBrand] = useState({
		keyword: null,
	});
	const [brandKeyword, setBrandKeyword] = useState("");
	const [currentPageFilterBrand, setCurrentPageFilterBrand] = useState(1);
	const [totalPageFilterBrand, setTotalPageFilterBrand] = useState();

	const [isFilterPopup, setIsFilterPopup] = useState(false);

	const handleSelectionChangeCategories = (selected) => {
		setSelectedCategories(selected);
		setDataBody((prevDataBody) => ({
			...prevDataBody,
			category_ids: selected,
		}));
	};
	const handleSelectionChangeBrands = (selected) => {
		setSelectedBrands(selected);
		setDataBody((prevDataBody) => ({
			...prevDataBody,
			brand_ids: selected,
		}));
	};

	const handleClickButtonFilterCategory = () => {
		setIsOpenFilterCategoryPopup(!isOpenFilterCategoryPopup);
	};

	const handleClickButtonFilterBrand = () => {
		setIsOpenFilterBrandPopup(!isOpenFilterBrandPopup);
	};

	const fetchProductList = async () => {
		const products = await getProductList(
			page,
			limit,
			"filter_products",
			Cookies.get("filter_products"),
			dataBody
		);

		setProductsList(products.data.data);
		setProductsQuantity(products.data.total_items);
		setPageQuantity(products.data.total_page);
	};

	const fetchCategoryList = async () => {
		const categories = await getListCategory(
			currentPageFilterCategory,
			10,
			dataFilterCategory
		);
		if (categories.status_code === 200) {
			setListCategories(categories.data.data);
			setListCategoryShowFilter(categories.data.data);
			setTotalPageFilterCategory(categories.data.total_page);
		}
	};

	const fetchBrandList = async () => {
		const brands = await getListBrand(
			currentPageFilterCategory,
			10,
			dataFilterCategory
		);
		setListBrands(brands.data.data);
		setListBrandShowFilter(brands.data.data);
		setTotalPageFilterBrand(brands.data.total_page);
	};

	const fetchMoreCategoryList = async () => {
		if (currentPageFilterCategory < totalPageFilterCategory) {
			const categories = await getListCategory(
				currentPageFilterCategory + 1,
				10,
				dataFilterCategory
			);
			setListCategories((prev) => [...prev, ...categories.data.data]);
			setListCategoryShowFilter((prev) => [...prev, ...categories.data.data]);
			setCurrentPageFilterCategory(currentPageFilterCategory + 1);
			setTotalPageFilterCategory(categories.data.total_page);
		}
	};

	const fetchMoreBrandsList = async () => {
		if (currentPageFilterBrand < totalPageFilterBrand) {
			const brands = await getListCategory(
				currentPageFilterBrand + 1,
				10,
				dataFilterBrand
			);
			setListBrands((prev) => [...prev, ...brands.data.data]);
			setListBrandShowFilter((prev) => [...prev, ...brands.data.data]);
			setCurrentPageFilterBrand(currentPageFilterBrand + 1);
			setTotalPageFilterBrand(brands.data.total_page);
		}
	};

	const handleFetchMoreCategoryList = () => {
		if (isOpenFilterCategoryPopup) {
			fetchCategoryList();
		} else {
			setListCategories([]);
			setCategoryKeyword("");
			setCurrentPageFilterCategory(1);
			setTotalPageFilterCategory(1);
		}
	};

	const handleFetchMoreBrandList = () => {
		if (isOpenFilterBrandPopup) {
			fetchBrandList();
		} else {
			setListBrands([]);
			setBrandKeyword("");
			setCurrentPageFilterBrand(1);
			setTotalPageFilterBrand(1);
		}
	};

	useEffect(() => {
		handleFetchMoreCategoryList();
	}, [isOpenFilterCategoryPopup]);
	useEffect(() => {
		setCurrentPageFilterCategory(1);
		handleFetchMoreCategoryList();
	}, [categoryKeyword]);

	useEffect(() => {
		handleFetchMoreBrandList();
	}, [isOpenFilterBrandPopup]);

	useEffect(() => {
		setCurrentPageFilterBrand(1);
		handleFetchMoreBrandList();
	}, [brandKeyword]);

	useEffect(() => {
		Cookies.set("filter_products", JSON.stringify(colsToRender));
		fetchProductList();
	}, [colsToRender]);

	useDebouncedEffect(() => {
		fetchProductList();
	}, 200, [limit, page, dataBody]);

	return (
		<>
			<Header title={"Danh sách sản phẩm"} />
			<div className="right__listPage">
				<div className="right__toolbar">
					<div className="btn-toolbar">
						<button
							className="btn btn-base btn-text"
							onClick={() => navigate("/admin/categories")}
						>
							<span className="btn__label">Loại sản phẩm</span>
						</button>
					</div>
					<div className="btn-toolbar">
						<button
							className="btn btn-primary"
							onClick={() => navigate("/admin/products/create")}
						>
							<span className="btn__icon">
								<FontAwesomeIcon icon={faPlus} />
							</span>
							<span className="btn__title">Thêm sản phẩm</span>
						</button>
					</div>
				</div>

				<div className="right__table">
					<div className="right__table-scroller">
						<div className="box-scroller">
							<div className="group-scroller-btns">
								<button className="btn-scroller active">Tất cả sản phẩm</button>
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
										placeholder="Tìm kiếm theo mã sản phẩm, tên sản phẩm"
										type="text"
										name="search"
										id=""
										autoComplete="on"
										onChange={(e) =>
											setDataBody({ ...dataBody, keyword: e.target.value })
										}
									/>
									<fieldset className="input-field" />
								</div>
							</div>
							<div className="btn-group group-filter-btns">
								<button
									className="btn btn-base btn-filter"
									onClick={() => {
										setIsOpenFilterCategoryPopup(!isOpenFilterCategoryPopup);
									}}
									ref={filterCategoryBtnRef}
								>
									<span className="btn__label">
										Loại sản phẩm
										<span className="btn__icon">
											<FontAwesomeIcon icon={faCaretDown} />
										</span>
									</span>
								</button>
								{isOpenFilterCategoryPopup && (
									<SelectFilter
										btnRef={filterCategoryBtnRef}
										closePopup={() => setIsOpenFilterCategoryPopup(false)}
										listObject={listCategories}
										currentPage={currentPageFilterCategory}
										totalPage={totalPageFilterCategory}
										onSelectionChange={handleSelectionChangeCategories}
										handleOnClickButton={handleClickButtonFilterCategory}
										keyword={categoryKeyword}
										handleChangeKeyword={(e) => {
											setCategoryKeyword(e.target.value);
										}}
										loadMoreData={fetchMoreCategoryList}
									/>
								)}
								<SelectDatePopup
									setDataFilters={(data) =>
										setDataBody((prev) => {
											return {
												...prev,
												created_date_from: data.date_from,
												created_date_to: data.date_to,
											};
										})
									}
								/>

								<button
									className="btn btn-base btn-filter"
									onClick={() => {
										setIsOpenFilterBrandPopup(!isOpenFilterBrandPopup);
									}}
									ref={filterBrandBtnRef}
								>
									<span className="btn__label">
										Nhãn hiệu
										<span className="btn__icon">
											<FontAwesomeIcon icon={faCaretDown} />
										</span>
									</span>
								</button>
								{isOpenFilterBrandPopup && (
									<SelectFilter
										btnRef={filterBrandBtnRef}
										closePopup={() => setIsOpenFilterBrandPopup(false)}
										listObject={listBrands}
										currentPage={currentPageFilterBrand}
										totalPage={totalPageFilterBrand}
										onSelectionChange={handleSelectionChangeBrands}
										handleOnClickButton={handleClickButtonFilterBrand}
										keyword={brandKeyword}
										handleChangeKeyword={(e) => {
											setBrandKeyword(e.target.value);
										}}
										loadMoreData={fetchMoreBrandsList}
									/>
								)}
								<button className="btn btn-base btn-filter" onClick={() => setDataBody({
									keyword: null,
									category_ids: null,
									created_date_from: null,
									created_date_to: null,
									brand_ids: null,
									statuses: ["ACTIVE"],
									tags: null,
								})}>
									<span className="btn__label">
										Xóa bộ lọc
									</span>
								</button>
							</div>
						</div>
						{((dataBody.category_ids && dataBody.category_ids.length > 0) ||
							(dataBody.created_date_from && dataBody.created_date_to) ||
							dataBody.brand_ids) && (
								<div className="box-show-selected-filter">
									<div className="box-show-selected-container">
										{dataBody.category_ids &&
											dataBody.category_ids.length > 0 && (
												<div className="box-show-selected-item">
													<span>
														{" "}
														Loại sản phẩm:{" "}
														{dataBody.category_ids.map((id, index) => {
															const category = listCategoryShowFilter.find(
																(cat) => cat.id == id
															);
															return (
																<span key={index}>
																	{category?.name}
																	{index < dataBody.category_ids.length - 1
																		? ", "
																		: ""}
																</span>
															);
														})}
													</span>
													<div className="box-remove-item">
														<button
															onClick={() =>
																setDataBody((prev) => ({
																	...prev,
																	category_ids: null,
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
										{dataBody.created_date_from && dataBody.created_date_to && (
											<div className="box-show-selected-item">
												<span>
													Ngày tạo: (<span>{dataBody.created_date_from}</span> -
													<span>{dataBody.created_date_to}</span>)
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
										{dataBody.brand_ids &&
											dataBody.brand_ids.length > 0 && (
												<div className="box-show-selected-item">
													<span>
														{" "}
														Thương hiệu:{" "}
														{dataBody.brand_ids.map((id, index) => {
															const brand = listBrandShowFilter.find(
																(cat) => cat.id == id
															);
															return (
																<span key={index}>
																	{brand?.name}
																	{index < dataBody.brand_ids.length - 1
																		? ", "
																		: ""}
																</span>
															);
														})}
													</span>
													<div className="box-remove-item">
														<button
															onClick={() =>
																setDataBody((prev) => ({
																	...prev,
																	brand_ids: null,
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
											<button
												onClick={() => setIsFilterPopup(true)}
												className="btn-icon"
											>
												{settingFilterIcon}
											</button>
										</div>
									</th>
									{/* Render table headers for columns that exist in productsList */}
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
										{productsList.map((product, index) => {
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
																			className={cn("box-status", {
																				//'box-status--pending': order[key] === "Chưa nhập",
																				"box-status--partial":
																					product[key] === "ACTIVE",
																				//'box-status--completed': order[key] === "INACTIVE",
																				"box-status--cancelled":
																					product[key] === "INACTIVE",
																			})}
																		>
																			<span>
																				{product[key] === "ACTIVE"
																					? "Đang hoạt động"
																					: product[key] === "INACTIVE"
																						? "Ngừng giao dịch"
																						: product[key]}
																			</span>
																		</div>
																	</td>
																);
															} else if (key === "images") {
																return (
																	<td
																		key={key}
																		className={cn(
																			"table-data-item",
																			col[key].align
																		)}
																	>
																		<img
																			src={product?.images[0]?.url}
																			alt={product?.images[0]?.alt}
																		/>
																	</td>
																);
															} else if (
																key === "updated_at" ||
																key === "created_at"
															) {
																return (
																	<td
																		key={key}
																		className={cn(
																			"table-data-item",
																			col[key].align
																		)}
																	>
																		<p className="box-text">
																			{formatDate(product[key])}
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
																		{key !== "name" ? (
																			product[key]
																		) : (
																			<Link
																				to={`/admin/products/PRD/${product?.id}`}
																				className="box-id"
																			>
																				{product[key]}
																			</Link>
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
											setLimit(limit);
											setPage(1);
										}}
									/>
								)}
							</div>
							<p>kết quả</p>
							<p className="item-quantity">
								Từ {(page - 1) * limit + 1} đến{" "}
								{(page - 1) * limit + productsList.length} trên tổng{" "}
								{productsQuantity}
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
			{isFilterPopup && (
				<FilterPopup
					defaultCols={defaultCols}
					colGroup={col}
					colsToRender={colsToRender}
					setColsToRender={setColsToRender}
					closePopup={() => setIsFilterPopup(false)}
				/>
			)}
		</>
	);
};
export default ProductList;
