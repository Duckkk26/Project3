import React, { useEffect, useRef, useState } from "react";
import { Link, useNavigate, useParams } from "react-router-dom";
import Cookies from "js-cookie";

import ListSelectPopup from "../ListSelectPopup/ListSelectPopup.jsx";
// Import Icons
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import {
	faCaretDown,
	faChevronLeft,
	faMagnifyingGlass,
} from "@fortawesome/free-solid-svg-icons";
import importIcon from "../../assets/icons/ImportIcon.jsx";
import GINProductsTable from "../GINProductsTable/GINProductsTable.jsx";
import { updateGIN, getGINDetail, balanceGIN } from "../../service/GINApi.jsx";
import { quickGetProductList } from "../../service/ProductAPI.jsx";

import cn from "classnames";
import CreateProductFastlyPopup from "../CreateProductFastlyPopup/CreateProductFastlyPopup.jsx";
import { toast } from "react-toastify";
import Notification from "../Notification/Notification.jsx";

const GINDetailUpdate = () => {
	const [gin, setGin] = useState({})

	const [activeTab, setActiveTab] = useState("all");

	const navigate = useNavigate();

	const status = {
		COMPLETED: "Hoàn thành",
		BALANCED: "Đã cân bằng",
		DELETED: "Đã xóa",
		CHECKING: "Đang kiểm kho",
	};
	const { ginId } = useParams();

	const handleProductRequestChange = (e) => {
		const { name, value } = e.target;
		setDataBody({ ...dataBody, [name]: value });
	};

	const [listProductDetail, setListProductDetail] = useState([]);


	// Get list of columns that need redering from Cookies
	const [colsToRender, setColsToRender] = useState(() => {
		const storedCols = Cookies.get("filter_products_table");
		return storedCols
			? JSON.parse(storedCols)
			: {
				index: true,
				image: true,
				name: true,
				unit: true,
				ordered_quantity: true,
				price: true,
				discount: true,
				tax: true,
				total: true,
			};
	});

	// Set required columns to Cookies
	useEffect(() => {
		Cookies.set("filter_products_table", JSON.stringify(colsToRender));
	}, [colsToRender]);

	useEffect(() => {
		const fetchGinDetail = async () => {
			try {
				const ginDetail = await getGINDetail(ginId);
				setGin(ginDetail.data);
				setListProductDetail(ginDetail.data.products);

				setDataBody((prev) => ({
					...prev,
					sub_id: ginDetail.data.sub_id,
					isBalance: ginDetail.data.status === "BALANCED",
					note: ginDetail.data.note,
					tags: ginDetail.data.tags,
					products: ginDetail.data.products.map((product) => ({
						product_id: product.product_id,
						unit: product.unit,
						actual_stock: product.actual_stock,
						real_quantity: product.real_quantity,
						discrepancy_quantity: product.discrepancy_quantity,
						reason: product.reason,
						note: product.note,
					})),
				}));
			} catch (error) {
				console.error(error);
			}
		};
		fetchGinDetail();
	}, [ginId]);

	const [filteredProducts, setFilteredProducts] = useState([]);

	// Khi tab thay đổi, áp dụng bộ lọc
	useEffect(() => {
		setFilteredProducts(filterProducts(listProductDetail));
	}, [activeTab, listProductDetail]);


	const filterProducts = (products) => {
		switch (activeTab) {
			case "unchecked":
				return products.filter((product) => product.actual_stock === 0);
			case "matched":
				return products.filter(
					(product) => product.actual_stock === product.quantity
				);
			case "mismatched":
				return products.filter(
					(product) => product.actual_stock !== product.quantity
				);
			default:
				return products;
		}
	};

	const [isProductSelectPopup, setIsProductSelectPopup] = useState(false);
	const [dataPageProduct, setDataPageProduct] = useState({
		page: 1,
		size: 10,
		totalPage: 1,
		totalElement: 1,
		keyword: "",
	});
	const productSelectBtnRef = useRef(null);
	const [productSelectList, setProductSelectList] = useState([]);
	const [isCreateProductQuickly, setIsCreateProductQuickly] = useState(false);
	const fetchProductList = async () => {
		const response = await quickGetProductList(
			dataPageProduct.page,
			dataPageProduct.size,
			dataPageProduct.keyword
		);
		setProductSelectList(response.data.data);
		setDataPageProduct((prev) => {
			return {
				...prev,
				totalPage: response.data.totalPage,
				totalElement: response.data.totalElement,
			};
		});
	};
	useEffect(() => {
		if (isProductSelectPopup) {
			fetchProductList();
		}
	}, [isProductSelectPopup]);


	const [dataBody, setDataBody] = useState({
		sub_id: null,
		note: "",
		tags: "",
		user_inspection_id: localStorage.getItem("userId"),
		isBalance: false,
		products: [

		],
	});


	const handleUpdateGIN = async () => {
		const response = await updateGIN( dataBody, ginId);

		if (response.status_code === 200) {
			toast(<Notification 
					type={"success"} 
					withIcon 
					message={"Đã cập nhật phiếu kiểm thành công"} 
				/>,
				{
					autoClose: 4000,
					closeButton: false,
					hideProgressBar: true,
				}
			)
			navigate(-1);
		}
	};

	const handleBalance = async () => {
		try {
			const response = await balanceGIN(ginId);
			if (response.status_code === 200) {
				setGin(prevGin => ({ ...prevGin, status: "BALANCED" }));
				toast(<Notification 
						type={"success"} 
						withIcon 
						message={"Cân bằng kho thành công"} 
					/>,
					{
						autoClose: 4000,
						closeButton: false,
						hideProgressBar: true,
					}
				)
				navigate(-1);
			}
		} catch (error) {
			console.error(error);
		}
	};

	useEffect(() => {
		setDataBody((prev) => {
			return {
				...prev,
				products: listProductDetail.map((product) => {
					return {
						product_id: product.product_id,
						unit: product.unit,
						actual_stock: product.actual_stock,
						real_quantity: product.real_quantity,
						discrepancy_quantity: product.discrepancy_quantity,
						reason: product.reason,
						note: product.note,
					};
				}),
			};
		});
	}, [listProductDetail]);

	return (
		<>
			{isCreateProductQuickly && (
                    <>
                        <div className="overlay"></div>
                        <CreateProductFastlyPopup
                            handleCLickBack={() => setIsCreateProductQuickly(false)}
                            setListProductDetail={setListProductDetail}
                        />
                    </>
                	)
			}
			<div className="right__navbar">
				<div className="box-navbar">
					<div className="btn-toolbar">
						<Link to="/admin/gins" className="btn-back">
							<FontAwesomeIcon icon={faChevronLeft} />
							<h6 className="btn-back__title">Quay lại phiếu kiểm hàng</h6>
						</Link>
					</div>
					<div className="btn-toolbar">
						<button onClick={() => navigate(-1)} className="btn btn-outline-danger">
							<span className="btn__title">Hủy</span>
						</button>
						<button className="btn btn-outline-primary" onClick={handleUpdateGIN}>
							<span className="btn__title">Lưu</span>
						</button>
						<button onClick={handleBalance}
							className="btn btn-secondary-cyan"
							style={{ color: "white" }}
						>
							<span className="btn__title">Cân bằng kho</span>
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
									<p>Thông tin phiếu</p>
								</div>
								<div className="paper-content">
									<div className="group-info">
										<div className="info-item" style={{justifyContent:"flex-start" , columnGap:"70px"}}>
											<p className="info-title">Mã phiếu</p>
											<div className="info-field" style={{width: "60%"}}>
												<div className="box-input">
													<input
														placeholder="Nhập mã phiếu"
														name="sub_id"
														value={dataBody?.sub_id}
														onChange={handleProductRequestChange}
														type="text"
														className="text-field"
														readOnly
													/>
													<fieldset className="input-field"></fieldset>
												</div>
											</div>
										</div>
									</div>
								</div>
							</div>
						</div>

						<div className="box-info">
							<div className="box-paper">
								<div className="paper-header">
									<p>Thông tin bổ sung</p>
								</div>
								<div className="paper-content">
									<div className="group-info">
										<div className="info-item">
											<p className="info-title">Ghi chú</p>
											<div className="info-field">
												<div className="box-input">
													<input
														placeholder="Kiểm hàng ngày 13/9/2024"
														name="note"
														value={dataBody?.note}
														onChange={handleProductRequestChange}
														type="text"
														className="text-field"
													/>
													<fieldset className="input-field"></fieldset>
												</div>
											</div>
										</div>

										<div className="info-item">
											<p className="info-title">Tags</p>
											<div className="info-field">
												<div className="box-input">
													<input
														placeholder="Nhập Ký tự và enter"
														name="tags"
														value={dataBody?.tags}
														onChange={handleProductRequestChange}
														type="text"
														className="text-field"
													/>
													<fieldset className="input-field"></fieldset>
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
										<div className="btn-tab">
											<button
												className={cn("btn-scroller", { "active": activeTab === "all" })}
												onClick={() => setActiveTab("all")}
											>
												Tất cả
											</button>
											<button
												className={cn("btn-scroller", { "active": activeTab === "unchecked" })}
												onClick={() => setActiveTab("unchecked")}
											>
												Chưa kiểm
											</button>
											<button
												className={cn("btn-scroller", { "active": activeTab === "matched" })}
												onClick={() => setActiveTab("matched")}
											>
												Khớp
											</button>
											<button
												className={cn("btn-scroller", { "active": activeTab === "mismatched" })}
												onClick={() => setActiveTab("mismatched")}
											>
												Lệch
											</button>
										</div>
									</div>
								</div>
								<div className="paper-content">
									<div className="right__table">
										<div className="right__table-search-filter">
											<div className="box-search-filter-btns">
												<div className="box-search">
													<div
														ref={productSelectBtnRef}
														onClick={() => setIsProductSelectPopup(true)}
														className="box-input"
													>
														<div className="search-icon">
															<FontAwesomeIcon icon={faMagnifyingGlass} />
														</div>
														<input
															placeholder="Tìm theo tên hoặc mã sản phẩm"
															type="text"
															name="search"
															id=""
															autoComplete="on"
														/>
														<fieldset className="input-field" />
													</div>
													{isProductSelectPopup && (
														<ListSelectPopup
															title={"sản phẩm"}
															isLarge={true}
															isSearch={false}
															isFastCreate={true}
															dataList={productSelectList}
															handleCLickCreateProductQuickly={() => setIsCreateProductQuickly(true)}
															handleSelect={(id) =>
																setListProductDetail((prev) => {
																	return [
																		...prev,
																		{
																			product_id: id,
																			name: productSelectList.find(
																				(product) => product.id === id
																			)?.name,
																			image: productSelectList.find(
																				(product) => product.id === id
																			)?.images?.[0],
																			unit:
																				productSelectList.find(
																					(product) => product.id === id
																				)?.unit || "------",
																			real_quantity:
																				productSelectList.find(
																					(product) => product.id === id
																				)?.quantity || 0,
																			actual_stock: 0,
																			discrepancy_quantity: 0,
																			reason: "Khác",
																			note: "",
																		},
																	];
																})
															}
															btnRef={productSelectBtnRef}
															closePopup={() => setIsProductSelectPopup(false)}
														/>
													)}
													<button className="btn btn-base">
														<span className="btn__label">
															<p>Chọn nhanh</p>
														</span>
													</button>
												</div>
												<div className="btn-group group-filter-btns">
													<button className="btn btn-base btn-filter">
														<span className="btn__label">
															Giá nhập
															<span className="btn__icon">
																<FontAwesomeIcon icon={faCaretDown} />
															</span>
														</span>
													</button>
												</div>
											</div>
										</div>
									</div>
								</div>
								<div className="box-table">
									<GINProductsTable isView={false}
										productsList={filteredProducts}
										isBalance={gin?.status === "BALANCED"}
										setProductList={setListProductDetail}
									/>
								</div>
							</div>
						</div>
					</div>
				</div>
			</div>
		</>
	);
};

export default GINDetailUpdate;
