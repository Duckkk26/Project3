import React, { useEffect, useMemo, useState } from "react";
import { Link, useParams, useNavigate } from "react-router-dom";
import cn from "classnames";
import Cookies from "js-cookie";

import SupplierInfo from "../SupplierInfo/SupplierInfo";
import ProductsTable from "../ProductsTable/ProductsTable";

import s from "./GINDetail.module.scss";

import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import {
	faChevronLeft,
	faGear,
	faPrint,
} from "@fortawesome/free-solid-svg-icons";
import { faCopy } from "@fortawesome/free-regular-svg-icons";
import { balanceGIN, deleteGIN, getGINDetail } from "../../service/GINApi";
import GINProductsTable from "../GINProductsTable/GINProductsTable";
import { formatDateTime } from "../../utils/DateUtils";
import { Delete } from "ckeditor5";
import DeleteConfirmation from "../ConfirmPopup/DeleteConfirmation";
import { toast } from "react-toastify";
import Notification from "../Notification/Notification";

const GINDetail = () => {
	const [gin, setGin] = useState({});
	const [activeTab, setActiveTab] = useState("all");

	const [productsList, setProductsList] = useState([{}]);

	const navigate = useNavigate();


	const status = {
		COMPLETED: "Hoàn thành",
		BALANCED: "Đã cân bằng",
		DELETED: "Đã xóa",
		CHECKING: "Đang kiểm kho",
	};
	const { ginId } = useParams();

	const [isShowDeleteConfirmation, setIsShowDeleteConfirmation] = useState(false);

	const deleteConfirmation = useMemo(() => {
		return {
			action: "xóa",
			type: "phiếu kiểm",
			description: "Thao tác này sẽ xóa phiếu kiểm hàng của bạn. Phiếu kiểm đã xóa sẽ không thể cân bằng kho được nữa.",
			handleClose: () => setIsShowDeleteConfirmation(false),
			handleConfirm: async () => {
				try {
					setIsShowDeleteConfirmation(false);
					const response = await deleteGIN(ginId);
					if (response.status_code === 200) {
						toast(<Notification 
								type={"success"} 
								withIcon 
								message={"Đã xóa phiếu kiểm thành công!"} 
							/>,
							{
								autoClose: 4000,
								closeButton: false,
								hideProgressBar: true,
							}
						)
						setGin(prevGin => ({ ...prevGin, status: "DELETED" }));
					} else {
						toast(<Notification 
								type={"error"} 
								withIcon 
								message={"Xóa phiếu kiểm thất bại. Vui lòng thử lại!"} 
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
					console.error("Error during GIN deletion:", error);
					toast(<Notification 
							type={"error"} 
							withIcon 
							message={"Đã xảy ra lỗi khi xóa phiếu kiểm"} 
						/>,
						{
							autoClose: 4000,
							closeButton: false,
							hideProgressBar: true,
						}
					)
				}
			}
		};
	}, [ginId]);

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
			}
		} catch (error) {
			console.error(error);
		}
	};

	const [filteredProducts, setFilteredProducts] = useState([]);

	useEffect(() => {
		setFilteredProducts(filterProducts(productsList));
	}, [activeTab, productsList]);	

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

	const fetchGinDetail = async () => {
		const ginDetail = await getGINDetail(ginId);
		setGin(ginDetail.data);
		setProductsList(ginDetail.data.products);
	};
	useEffect(() => {
		fetchGinDetail();
	}, [ginId]);

	return (
		<>
			<div className="right__navbar">
				<div className="box-navbar">
					<div className="btn-toolbar">
						<Link to="/admin/gins" className="btn-back">
							<FontAwesomeIcon icon={faChevronLeft} />
							<h6 className="btn-back__title">
								Quay lại danh sách phiếu kiểm hàng
							</h6>
						</Link>
					</div>
					{(gin.status && gin.status === "CHECKING") && 
					<div className="btn-toolbar">
						<button onClick={() => setIsShowDeleteConfirmation(true)} className="btn btn-outline-danger">
							<span className="btn__title">Xóa</span>
						</button>
						<button onClick={() => navigate("edit")} className="btn btn-outline-primary">
							<span className="btn__title">Sửa</span>
						</button>
						<button onClick={handleBalance} className="btn btn-primary" style={{ color: "white" }}>
							<span className="btn__title">Cân bằng kho</span>
						</button>
					</div>}

				</div>
			</div>
			<div className="right__paperPage">
				<div className="right__paperPage-wrapper">
					<div className={cn("right__paperPage-container", s.container)}>
						<div className="box-title">
							<div className="group-details">
								<h4 className="box-code">{gin.id}</h4>
								<h6 className="box-date">{gin.created_at}</h6>
								<div
									className={cn(
										"box-status",
										`box-status--${gin?.status?.toLowerCase()}`
									)}
								>
									<span>{status[gin.status]}</span>
								</div>
							</div>
							<div className="btn-toolbar">

							</div>
						</div>
						<div className="box-supplier">
							<div className="box-paper">
								<div className="paper-header">
									<p>Thông tin phiếu</p>
								</div>
								<div className="paper-content">
									<div className="group-info">
										<div className="info-item">
											<p className="info-title">Nhân viên tạo</p>
											<p className="info-value">
												: {gin.user_created_name || "---"}
											</p>
										</div>
										<div className="info-item">
											<p className="info-title">Nhân viên cân bằng</p>
											<p className="info-value">
												: {gin.user_balanced_name || "---"}
											</p>
										</div>
										<div className="info-item">
											<p className="info-title">Ngày tạo</p>
											<p className="info-value">: {formatDateTime(gin.created_at) || "---"}</p>
										</div>
										<div className="info-item">
											<p className="info-title">Ngày cân bằng</p>
											<p className="info-value">: {formatDateTime(gin.balanced_at) || "---"}</p>
										</div>
									</div>
								</div>
							</div>
						</div>
						<div className="box-info">
							<div className="box-paper">
								<div className="paper-header">
									<p>Thông tin khác</p>
								</div>
								<div className="paper-content">
									<div className="group-info">
										<div className="info-item">
											<p className="info-title">Ghi chú</p>

											<p className="info-value">: {gin?.note}</p>

										</div>
										<div className="info-item">
											<p className="info-title">Tags</p>

											<p className="info-value">: {gin?.tags}</p>


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
												className={activeTab === "all" ? "active" : ""}
												onClick={() => setActiveTab("all")}
											>
												Tất cả
											</button>
											<button
												className={activeTab === "unchecked" ? "active" : ""}
												onClick={() => setActiveTab("unchecked")}
											>
												Chưa kiểm
											</button>
											<button
												className={activeTab === "matched" ? "active" : ""}
												onClick={() => setActiveTab("matched")}
											>
												Khớp
											</button>
											<button
												className={activeTab === "mismatched" ? "active" : ""}
												onClick={() => setActiveTab("mismatched")}
											>
												Lệch
											</button>
										</div>
									</div>
								</div>
								<div className="box-table">
									<GINProductsTable
										productsList={filteredProducts}
										isBalance={gin?.status === "BALANCED"}
										isView={true}
									/>
								</div>
							</div>
						</div>
					</div>
				</div>
			</div>
			{isShowDeleteConfirmation && (<DeleteConfirmation  {...deleteConfirmation} />)}
		</>
	);
};

export default GINDetail;
