import React, { useState, useRef } from "react";
import { Link } from "react-router-dom";
import cn from "classnames";

// Import CSS
import s from "./GINProductsTable.module.scss";

// Import Columns Info
import productsTableColgroup from "../../assets/colgroup/gin-products-table";

// Import Icons
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import {
	faCircleMinus,
	faCirclePlus,
	faXmark,
} from "@fortawesome/free-solid-svg-icons";
import dropdownicon from "../../assets/icons/TriangleDropdown"; // Adjust path as needed
import ReasonSelectPopup from "../ReasonSelectPopup/ReasonSelectPopup";
import ReasonTableCell from "../ReasonTableCell/ReasonTableCell";

const GINProductsTable = ({
	productsList,
	setProductList,
	isView,
	isBalance,
	onClickShowAdd,
	setIsProductSelectPopup
}) => {
	const handleChangeData = (index, key, value) => {
		const newValue =
			key === "reason" || key === "note" ? value : parseFloat(value);

		setProductList((prevProductsList) =>
			prevProductsList.map((product, i) => {
				if (i === index) {
					const updatedProduct = { ...product, [key]: newValue };

					// Tự động cập nhật discrepancy_quantity nếu key là actual_stock
					if (key === "actual_stock") {
						updatedProduct.discrepancy_quantity =
							newValue - updatedProduct.real_quantity;
					}

					return updatedProduct;
				}
				return product;
			})
		);
	};	

	return (
		<div className={s.container}>
			<table className={s.table}>
				<colgroup>
					{/* Render the <colgroup> based on productsTableColgroup */}
					{Object.entries(productsTableColgroup).map(([key, col]) => (
						<col
							key={key}
							style={{ width: col.width, minWidth: col.minWidth || "unset" }}
						/>
					))}
					<col style={{ width: "40px" }} />
				</colgroup>
				<thead className={s.tableHeader}>
					<tr className={s.tableRow}>
						{/* Render table headers based on productsTableColgroup */}
						{Object.entries(productsTableColgroup).map(([key, col]) => (
							<th
								key={key}
								scope="col"
								className={cn(s.tableCell, s.tableCellHeader, col.align)}
							>
								<p>{col.name}</p>
							</th>
						))}
						<th
							className={cn(s.tableCell, s.tableCellHeader, "text-center")}
						></th>
					</tr>
				</thead>
				<tbody className={s.tableBody}>
					{productsList.map((product, index) => (
						<tr key={index} className={s.tableRow}>
							{Object.entries(productsTableColgroup).map(([key, col]) => {
								if (key === "image") {
									return (
										<td
											key={key}
											className={cn(s.tableCell, s.tableCellBody, col.align)}
										>
											<img src={product.image?.url} alt="" />
										</td>
									);
								} else if (key === "name") {
									return (
										<td
											key={key}
											className={cn(s.tableCell, s.tableCellBody, col.align)}
										>
											<div className={s.boxInfo}>
												<div className={s.boxName}>
													<p>{product.name}</p>
												</div>
												<div className={s.boxId}>
													<Link to={`/admin/products/PRD/${product.product_id}`}>
														<p>{product.product_id}</p>
													</Link>
												</div>
											</div>
										</td>
									);
								} else if (key === "actual_stock") {
									if (isView) {
										return (
											<td
												key={key}
												className={cn(s.tableCell, s.tableCellBody, col.align)}
											>
												<p>{product[key]}</p>
											</td>
										);
									}
									return (
										<td
											key={key}
											className={cn(s.tableCell, s.tableCellBody, col.align)}
										>
											<div className={s.boxQuantity}>
												<button
													className={s.btnIcon}
													onClick={() =>
														handleChangeData(
															index,
															key,
															product.actual_stock - 1
														)
													}
												>
													<span className={s.btnIconLabel}>
														<FontAwesomeIcon icon={faCircleMinus} />
													</span>
												</button>
												<div className={s.boxFormControl}>
													<div className={s.boxInput}>
														<input
															className={cn(s.inputField, col.align)}
															type="text"
															value={product[key]}
															onChange={(e) =>
																handleChangeData(index, key, e.target.value)
															}
														/>
													</div>
												</div>
												<button
													className={s.btnIcon}
													onClick={() =>
														handleChangeData(
															index,
															key,
															product.actual_stock + 1
														)
													}
												>
													<span className={s.btnIconLabel}>
														<FontAwesomeIcon icon={faCirclePlus} />
													</span>
												</button>
											</div>
										</td>
									);
								} else if (key === "reason") {
									if (isView) {
										return (
											<td
												key={key}
												className={cn(s.tableCell, s.tableCellBody, col.align)}
											>
												<p>{product.reason}</p>
											</td>
										);
									}
									return (
										<ReasonTableCell
											key={key}
											reason={product.reason}
											setReason={(newReason) =>
												handleChangeData(index, "reason", newReason)
											}
										/>
									);
								} else if (key === "note") {
									if (isView) {
										return (
											<td
												key={key}
												className={cn(s.tableCell, s.tableCellBody, col.align)}
											>
												<p>{product.note}</p>
											</td>
										);
									}
									return (
										<td
											key={key}
											className={cn(s.tableCell, s.tableCellBody, col.align)}
										>
											<input
												name="note"
												value={product.note}
												onChange={(e) =>
													handleChangeData(index, "note", e.target.value)
												}
												className={s.boxNote}
												type="text"
												placeholder="Nhập ghi chú"
											/>
										</td>
									);
								} else if (key === "real_quantity") {
									
										return (	<td
											key={key}
											className={cn(s.tableCell, s.tableCellBody, col.align)}
										>
											<p>{isBalance? product.actual_stock-product.discrepancy_quantity : product[key]}</p>
										</td>)
									
								
								} else if (key === "discrepancy_quantity") {
									return (
										<td
											key={key}
											className={cn(s.tableCell, s.tableCellBody, col.align)}
										>
											<p>{isBalance? product[key] : product.actual_stock-product.real_quantity}</p>
										</td>
									);
								}
								return (
									<td
										key={key}
										className={cn(s.tableCell, s.tableCellBody, col.align)}
									>
										<p>{key === "index" ? index + 1 : product[key]}</p>
									</td>
								);
							})}

							{!isView && (
								<td className={cn(s.tableCell, s.tableCellBody, "text-center")}>
									<button
										className="btn-icon"
										onClick={() =>
											setProductList((prev) =>
												prev.filter((_, i) => i !== index)
											)
										}
									>
										<FontAwesomeIcon icon={faXmark} />
									</button>
								</td>
							)}
						</tr>
					))}
				</tbody>
			</table>
			{productsList.length === 0 && (
				<div className={s.boxEmpty}>
					{isView ? (
						<p>Không tìm thấy dữ liệu phù hợp với kết quả tìm kiếm</p>
					) : (
						<>
							<p>Bảng chưa có sản phẩm nào</p>
							<button className="btn btn-outline-primary" onClick={onClickShowAdd}>
								<span className="btn__label">Thêm sản phẩm</span>
							</button>
						</>
					)}
				</div>
			)}
		</div>
	);
};

export default GINProductsTable;
