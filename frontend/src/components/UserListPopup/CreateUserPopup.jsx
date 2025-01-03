import React, { useState, useRef } from "react";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import { faXmark } from "@fortawesome/free-solid-svg-icons";
import cn from "classnames";
import s from "./CreateUserPopup.module.scss";
import SelectRolePopup from "./SelectRolePopup";
import { faCaretDown } from "@fortawesome/free-solid-svg-icons";
import infoIcon from "../../assets/icons/InfoIcon";
import { UncontrolledTooltip } from 'reactstrap';

const CreateUserPopup = ({ onClose, onCreate }) => {
	const [userData, setUserData] = useState({
		full_name: "",
		phone: "",
		password: "",
		note: "",
		role: "",
	});

	const [errors, setErrors] = useState({
		full_name: "",
		phone: "",
		password: "",
		role: ""
	});

	const role = {
		COORDINATOR: "Thủ kho",
		WAREHOUSE_MANAGER: "Nhân viên điều phối",
		WAREHOUSE_STAFF: "Nhân viên kho",
	};

	const roleBtnRef = useRef(null);
	const [isOpenRolePopup, setIsOpenRolePopup] = useState(false);

	// Hàm validate từng trường
	const validate = () => {
		let newErrors = {};

		if (!userData.full_name.trim()) {
			newErrors.full_name = "Họ tên không được để trống";
		}
		if (!/^0\d{9}$/.test(userData.phone)) {
			newErrors.phone = "Số điện thoại phải có 10 chữ số và bắt đầu bằng số 0";
		}
		if (!userData.password || userData.password.length < 8 || !/[A-Z]/.test(userData.password)) {
			newErrors.password = "Password phải có ít nhất 8 ký tự và chứa chữ in hoa";
		}
		if (!userData.role) {
			newErrors.role = "Vai trò không được để trống";
		}

		setErrors(newErrors);

		return Object.keys(newErrors).length === 0; // Trả về true nếu không có lỗi
	};

	const handleChange = (name, value) => {
		setUserData((prevData) => ({
			...prevData,
			[name]: value,
		}));
		setErrors((prevErrors) => ({
			...prevErrors,
			[name]: "", // Xóa lỗi khi người dùng thay đổi input
		}));
	};

	const handleCreate = () => {
		if (validate()) {
			onCreate(userData);
			// onClose(); // Đóng popup sau khi tạo
		}
	};

	return (
		<>
			<div className="overlay"></div>
			<div className={s.container}>
				<div className={s.content}>
					<div className={s.header}>
						<span className={s.title}>Thêm mới nhân viên</span>
						<span
							className="modal-close-wrapper"
							role="button"
							aria-label="Close dialog"
							onClick={onClose}
						>
							<FontAwesomeIcon icon={faXmark} />
						</span>
					</div>
					<div className={s.body}>
						<div className={s.row}>
							<div className={s.infoGroup}>
								<div>
									<label>Họ tên
										<span id="nameCaptionFullName" className="caption-icon">
											{infoIcon}
										</span>
										<UncontrolledTooltip placement="top" target="nameCaptionFullName">
											Họ tên không được để trống
										</UncontrolledTooltip>
										<span className="asterisk-icon">*</span>
									</label>
									<input
										name="full_name"
										value={userData.full_name}
										onChange={(e) => handleChange(e.target.name, e.target.value)}
										placeholder="Nhập họ tên người dùng"
									/>
									{errors.full_name && <div className={s.error}>{errors.full_name}</div>}
								</div>
								<div>
									<label>Số điện thoại
										<span id="phoneCaption" className="caption-icon">
											{infoIcon}
										</span>
										<UncontrolledTooltip placement="top" target="phoneCaption">
											Số điện thoại không được để trống, có 10 chữ số bắt đầu từ 0
										</UncontrolledTooltip>
										<span className="asterisk-icon">*</span>
									</label>
									<input
										name="phone"
										value={userData.phone}
										onChange={(e) => handleChange(e.target.name, e.target.value)}
										placeholder="Nhập số điện thoại người dùng"
									/>
									{errors.phone && <div className={s.error}>{errors.phone}</div>}
								</div>
							</div>
							<div className={s.infoGroup}>
								<div>
									<label>Password
										<span id="passwordCaption" className="caption-icon">
											{infoIcon}
										</span>
										<UncontrolledTooltip placement="top" target="passwordCaption">
											Password không được để trống, ít nhất 8 ký tự và có chữ in hoa
										</UncontrolledTooltip>
										<span className="asterisk-icon">*</span>
									</label>
									<input
										name="password"
										type="password"
										value={userData.password}
										onChange={(e) => handleChange(e.target.name, e.target.value)}
										placeholder="Nhập password người dùng"
									/>
									{errors.password && <div className={s.error}>{errors.password}</div>}
								</div>

								<div style={{ position: "relative" }}>
									<label>Vai trò
										<span id="roleCaption" className="caption-icon">
											{infoIcon}
										</span>
										<UncontrolledTooltip placement="top" target="roleCaption">
											Vai trò không được để trống
										</UncontrolledTooltip>
										<span className="asterisk-icon">*</span>
									</label>
									{isOpenRolePopup && (
										<SelectRolePopup
											roleBtnRef={roleBtnRef}
											closePopup={() => setIsOpenRolePopup(false)}
											handleSelect={handleChange}
										/>
									)}
									<div
										ref={roleBtnRef}
										className={cn(s.role, { [s.active]: isOpenRolePopup })}
										onClick={() => setIsOpenRolePopup(!isOpenRolePopup)}
									>
										{role[userData?.role] || "Chọn vai trò"} <FontAwesomeIcon className={cn({ [s.active]: isOpenRolePopup })} icon={faCaretDown} />
									</div>
									{errors.role && <div className={s.error}>{errors.role}</div>}
								</div>
							</div>
						</div>
					</div>
					<div className={s.footer}>
						<div className={s.clearfix}>
							<button className={s.back} onClick={onClose}>
								Thoát
							</button>

							<button className={s.create} onClick={handleCreate}>
								Thêm
							</button>
						</div>
					</div>
				</div>
			</div>
		</>

	);
};

export default CreateUserPopup;
