import Header from "../Header/Header";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import {
	faPlus,
	faMagnifyingGlass,
	faCaretDown,
	faChevronRight,
	faChevronLeft,
	faXmark,
} from "@fortawesome/free-solid-svg-icons";
import cn from "classnames";

import { useRef, useState, useMemo } from "react";
import { formatDateTime } from "../../utils/DateUtils";
import LimitSelectPopup from "../LimitSelectPopup/LimitSelectPopup";
import { useDebouncedEffect } from "../../utils/CommonUtils";
import { createNewUser, deleteAccount, getListUser } from "../../service/UserAPI";
import CreateUserPopup from "../UserListPopup/CreateUserPopup";
import s from "./UserList.module.scss";
import { Delete } from "ckeditor5";
import DeleteConfirmation from "../ConfirmPopup/DeleteConfirmation";
import { toast } from "react-toastify";
import Notification from "../Notification/Notification";
import { withAuthorization } from "../../hoc";
const UserList = () => {
	const limitBtnRef = useRef(null);

	const [isOpenLimitPopup, setIsOpenLimitPopup] = useState(false);
	const [limit, setLimit] = useState(10);
	const [page, setPage] = useState(1);
	const [pageQuantiy, setPageQuantity] = useState(1);
	const [usersQuantity, setUsersQuantity] = useState();
	const [usersList, setUsersList] = useState([]);
	const [dataBody, setDataBody] = useState({
		keyword: null,
		roles: null,
		start_created_date: null,
		end_created_date: null,
		active_status: null,
	});

	const headersRef = useRef(null);
	const contentRef = useRef(null);

	const handleScroll = (e, target) => {
		target.scrollLeft = e.target.scrollLeft;
	};

	const [isOpenCreatePopup, setIsOpenCreatePopup] = useState(false); // State cho popup tạo user
	const isActive = {
		true: "Đang làm việc",
		false: "Đã nghỉ việc",
	};

	const role = {
		COORDINATOR: "Thủ kho",
		WAREHOUSE_MANAGER: "Nhân viên điều phối",
		WAREHOUSE_STAFF: "Nhân viên kho",
	};
	const [isShowDeleteConfirmation, setIsShowDeleteConfirmation] = useState(false);

	const selectedId = useRef(null);

	const deleteConfirmation = useMemo(() => {
		return {
			action: "xóa",
			type: "tài khoản",
			description: "Thao tác này sẽ xóa tài khoản nhân viên này. Tài khoản đã xóa sẽ không thể đăng nhập vào trang được nữa.",
			handleClose: () => setIsShowDeleteConfirmation(false),
			handleConfirm: async () => {
				if (selectedId.current === localStorage.getItem("userId")) {
					toast(<Notification 
							type={"error"} 
							withIcon 
							message={"Không thể xóa tài khoản đang đăng nhập"} 
						/>,
						{
							autoClose: 4000,
							closeButton: false,
							hideProgressBar: true,
						}
					)
					setIsShowDeleteConfirmation(false);
					return;
				}
				try {
					setIsShowDeleteConfirmation(false);
					const response = await deleteAccount(selectedId.current);
					if (response.status_code === 200) {
						toast(<Notification 
								type={"success"} 
								withIcon 
								message={"Đã xóa tài khoản thành công"} 
							/>,
							{
								autoClose: 4000,
								closeButton: false,
								hideProgressBar: true,
							}
						)
						setUsersList((prev) => prev.filter((user) => user.id !== selectedId.current));
					}
				} catch (error) {
					console.error("Error during user deletion:", error);
					toast(<Notification 
							type={"success"} 
							withIcon 
							message={"Đã xảy ra lỗi khi xóa sản phẩm"} 
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
	}, [selectedId.current]);

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
	const fetchUserList = async () => {
		const users = await getListUser(page, limit, dataBody);
		setUsersList(users.data.data);
		setUsersQuantity(users.data.total_items);
		setPageQuantity(users.data.total_page);
	};

	const handleCreateUser = async (userData) => {
		const res = await createNewUser(userData);
		if (res.status_code === 201) {
			toast(<Notification 
					type={"success"} 
					withIcon 
					message={"Đã tạo tài khoản mới thành công"} 
				/>,
				{
					autoClose: 4000,
					closeButton: false,
					hideProgressBar: true,
				}
			)
			setIsOpenCreatePopup(false);
			fetchUserList();
		}
	};



	useDebouncedEffect(
		() => {
			fetchUserList();
		},
		300,
		[limit, page, dataBody]
	);

	return (
		<>
			<Header title={"Loại sản phẩm"} />
			<div className="right__listPage">
				<div className="right__toolbar">
					<div></div>
					<div className="btn-toolbar">
						<button
							onClick={() => setIsOpenCreatePopup(!isOpenCreatePopup)}
							className="btn btn-primary"
						>
							<span className="btn__icon">
								<FontAwesomeIcon icon={faPlus} />
							</span>
							<span className="btn__title">Thêm nhân viên mới</span>
						</button>
					</div>
				</div>
				<div className="right__table">
					<div className="right__table-scroller">
						<div className="box-scroller">
							<div className="group-scroller-btns">
								<button className="btn-scroller active">
									Tất cả nhân viên
								</button>
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
										<input
											placeholder="Tìm kiếm theo tên nhân viên, số điện thoại, email"
											type="text"
											name="search"
											id=""
											autoComplete="on"
											onChange={(e) =>
												setDataBody((prev) => {
													return {
														...prev,
														keyword: e.target.value,
													};
												})
											}
										/>
										<fieldset className="input-field" />
									</div>
								</div>
							</div>
						</div>
					</div>
					<div
						ref={headersRef}
						onScroll={(e) => handleScroll(e, contentRef.current)}
						className="right__table-headers"
					>
						<table className="box-table-headers">
							<colgroup>
								<col style={{ width: "150px" }} />
								<col style={{ width: "150px" }} />
								<col style={{ width: "180px" }} />
								<col style={{ width: "150px" }} />
								<col style={{ width: "150px" }} />
								<col style={{ width: "150px" }} />
								<col style={{ width: "150px" }} />
								<col style={{ width: "150px" }} />
								<col style={{ width: "150px" }} />
							</colgroup>
							<thead>
								<tr className="group-table-headers">
									<th rowSpan={1} className="table-icon">
										<div className="group-icons">
											<div className="checkbox__container">
												<div className="checkbox__wrapper">
													<input
														type="checkbox"
														name=""
														id=""
														className="checkbox__input"
													/>
													<div className="btn-checkbox"></div>
												</div>
											</div>
										</div>
									</th>
									<th
										colSpan={1}
										rowSpan={1}
										className={cn("table-header-item", "text-start")}
									>
										Tên nhân viên
									</th>
									<th
										colSpan={1}
										rowSpan={1}
										className={cn("table-header-item", "text-start")}
									>
										Số điện thoại
									</th>
									<th
										colSpan={1}
										rowSpan={1}
										className={cn("table-header-item", "text-start")}
									>
										Email
									</th>
									<th
										colSpan={1}
										rowSpan={1}
										className={cn("table-header-item", "text-start")}
									>
										Trạng thái
									</th>
									<th
										colSpan={1}
										rowSpan={1}
										className={cn("table-header-item", "text-start")}
									>
										Vai trò
									</th>

									<th
										colSpan={1}
										rowSpan={1}
										className={cn("table-header-item", "text-center")}
									>
										Ngày tạo
									</th>
									<th
										colSpan={1}
										rowSpan={1}
										className={cn("table-header-item", "text-start")}
									>
										Địa chỉ
									</th>

									<th
										colSpan={1}
										rowSpan={1}
										className={cn("table-header-item", "text-center")}
									>
										Thao tác
									</th>
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
										<col style={{ width: "150px" }} />
										<col style={{ width: "150px" }} />
										<col style={{ width: "180px" }} />
										<col style={{ width: "150px" }} />
										<col style={{ width: "150px" }} />
										<col style={{ width: "150px" }} />
										<col style={{ width: "150px" }} />
										<col style={{ width: "150px" }} />
										<col style={{ width: "150px" }} />
									</colgroup>
									<tbody>
										{usersList.map((user, index) => {
											return (
												<tr key={index} className="table-data-row">
													<td rowSpan={1} className="table-icon">
														<div className="group-icons">
															<div className="checkbox__container">
																<div className="checkbox__wrapper">
																	<input
																		type="checkbox"
																		name=""
																		id=""
																		className="checkbox__input"
																	/>
																	<div className="btn-checkbox"></div>
																</div>
															</div>
														</div>
													</td>
													<td className={cn("table-data-item", "text-start")}>
														<p className="box-text">{user.full_name}</p>
													</td>
													<td className={cn("table-data-item", "text-start")}>
														<p className="box-text">{user.phone}</p>
													</td>
													<td className={cn("table-data-item", "text-start")}>
														<p className="box-text">{user.email}</p>
													</td>
													<td className={cn("table-data-item", "text-start")}>
														<p
															className="box-text"
															style={{ color: "rgb(13, 180, 115)" }}
														>
															{isActive[user.is_active]}
														</p>
													</td>
													<td className={cn("table-data-item", "text-start")}>
														<p
															className="box-text"
															style={{ color: "#4d53E0" }}
														>
															{role[user.role]}
														</p>
													</td>
													<td
														className={cn("table-data-item", "text-center")}
													>
														<p className="box-text">
															{formatDateTime(user.created_at)}
														</p>
													</td>
													<td className={cn("table-data-item", "text-start")}>
														<p className="box-text">{user.address}</p>
													</td>

													<td
														rowSpan={1}
														className={cn("table-icon", "text-start")}
													>
														<div className="group-icons">
															<div className="checkbox__container">
																<div className="checkbox__wrapper">
																	<button
																		className="btn-icon"
																		onClick={() => {
																			selectedId.current = user.id;
																			setIsShowDeleteConfirmation(true);
																		}

																		}
																	>
																		<FontAwesomeIcon icon={faXmark} />
																	</button>
																</div>
															</div>
														</div>
													</td>
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
										}}
									/>
								)}
							</div>
							<p>Kết quả</p>
							<p className="item-quantity">
								Từ {(page - 1) * limit + 1} đến{" "}
								{(page - 1) * limit + usersList.length} trên tổng{" "}
								{usersQuantity}
							</p>
							<button
								className={cn("btn-icon", "btn-page", {
									inactive: page === 1,
								})}
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
			{isOpenCreatePopup && (
				<CreateUserPopup
					onCreate={handleCreateUser} // Truyền trạng thái isOpen
					onClose={() => setIsOpenCreatePopup(false)} // Đóng popup
				/>
			)}
			{isShowDeleteConfirmation && (<DeleteConfirmation {...deleteConfirmation} />)}
		</>
	);
};

export default withAuthorization(UserList, ["ADMIN"]);
