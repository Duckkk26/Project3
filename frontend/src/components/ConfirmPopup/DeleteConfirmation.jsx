import React from "react";
import s from "./DeleteConfirmation.module.scss";
import { faXmark } from "@fortawesome/free-solid-svg-icons";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
const DeleteConfirmation = ({
	action,
	type,
	handleConfirm,
	handleClose,
	description,
}) => {
	return (
		<>
			<div className="overlay"></div>
			<div className={s.container}>
				<div className={s.header}>
					<p className={s.titleHeader}>
						Bạn chắc chắn muốn {action + " " + type} này không?
					</p>
					<button
						className={s.btnCancel}
						id="close-button"
						color="primary"
						onClick={handleClose}
					>
						<FontAwesomeIcon icon={faXmark} />
					</button>
				</div>
				<div className={s.body}>
					<p className={s.description}>{description}</p>
					<p className={s.description}>
						Bạn muốn tiếp tục thao tác {action + " " + type} ?
					</p>
				</div>
				<div className={s.footer}>
					<button className={s.btnCancel} onClick={handleClose}>
						<span>Thoát</span>
					</button>
					<button className={s.btnConfirm} onClick={handleConfirm}>
						<span>
							{action.substring(0, 1).toUpperCase() +
								action.substring(1).toLowerCase()}{" "}
						</span>
					</button>
				</div>
			</div>
		</>

	);
};

export default DeleteConfirmation;
