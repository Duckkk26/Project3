import React, { useState, useEffect, useRef } from "react";
import cn from "classnames"; // Đảm bảo bạn đã cài đặt classnames package: npm install classnames
import s from "./StatusFilter.module.scss"; // Đảm bảo bạn đã import CSS cho file này

const StatusFilter = ({ statusBtnRef, closePopup, setStatusList, type }) => {
    const popupRef = useRef(null);

    const [status, setStatus] = useState([]);
    const [isSelectAll, setIsSelectAll] = useState(false);

    // Các trạng thái có thể có
    const statuses =
        type === "GRN"
            ? {
                  ORDERING: "Đang đặt",
                  TRADING: "Đang giao dịch",
                  CANCELLED: "Đã hủy",
                  COMPLETED: "Đã hoàn thành",
              }
            : {
                  PENDING: "Chưa nhập",
                  PARTIAL: "Nhập một phần",
                  CANCELLED: "Đã hủy",
                  COMPLETED: "Đã hoàn thành",
              };

    // Hàm toggle trạng thái
    const toggleStatus = (status) => {
        setStatus((prevStatus) => {
            if (prevStatus.includes(status)) {
                return prevStatus.filter((s) => s !== status); // Xóa trạng thái nếu đã chọn
            } else {
                return [...prevStatus, status]; // Thêm trạng thái nếu chưa chọn
            }
        });
    };

    // Hàm chọn tất cả các trạng thái
    const selectAllStatuses = () => {
        const allStatuses = Object.keys(statuses);
        if (isSelectAll) {
            setStatus([]); // Nếu đã chọn tất cả thì xóa hết
        } else {
            setStatus(allStatuses); // Chọn tất cả các trạng thái
        }
        setIsSelectAll(!isSelectAll); // Chuyển đổi trạng thái chọn tất cả
    };

    // Đóng popup khi bấm ra ngoài
    const handleClickOutside = (e) => {
        if (popupRef.current && !popupRef.current.contains(e.target) && statusBtnRef.current && !statusBtnRef.current.contains(e.target)) {
            closePopup();
        }
    };

    const handleClickFilter = () => {
        setStatusList(status);
        closePopup();
    };

    useEffect(() => {
        document.addEventListener("mousedown", handleClickOutside);
        return () => {
            document.removeEventListener("mousedown", handleClickOutside);
        };
    }, []);

    useEffect(() => {
        // Cập nhật trạng thái isSelectAll khi status thay đổi
        const allStatuses = Object.keys(statuses);
        setIsSelectAll(allStatuses.length > 0 && allStatuses.every(s => status.includes(s)));
    }, [status]);

    return (
        <div ref={popupRef} className={s.container}>
            {/* Phần Chọn tất cả */}
            <div style={{ width: "100%" }} onClick={selectAllStatuses}>
                <button type="button" className={s.selectItem}>
                    <div className={s.checkContainer}>
                        <div className={s.checkBox}>
                            <input
                                type="checkbox"
                                checked={isSelectAll} // Gắn checked với isSelectAll
                                readOnly
                            />
                            <div className={cn({ [s.selected]: isSelectAll })}></div>
                        </div>
                    </div>
                    Chọn tất cả
                </button>
            </div>

            {/* Phần các trạng thái khác */}
            <div className={s.wrapper}>
                {Object.keys(statuses).map((key) => (
                    <div style={{ width: "100%" }} key={key} onClick={() => toggleStatus(key)}>
                        <button
                            type="button"
                            className={cn(s.selectItem, { [s.selected]: status.includes(key) })}
                        >
                            <div className={s.checkContainer}>
                                <div className={s.checkBox}>
                                    <input
                                        type="checkbox"
                                        checked={status.includes(key)} // Gắn checked với trạng thái hiện tại
                                        readOnly
                                    />
                                    <div className={cn({ [s.selected]: status.includes(key) })}></div>
                                </div>
                            </div>
                            {statuses[key]}
                        </button>
                    </div>
                ))}
            </div>

            <button className={cn(s.filter)} onClick={handleClickFilter}>
                Lọc
            </button>
        </div>
    );
};

export default StatusFilter;
