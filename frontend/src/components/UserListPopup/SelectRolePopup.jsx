
import React, { useState, useEffect, useRef } from "react";
import cn from "classnames"; // Đảm bảo bạn đã cài đặt classnames package: npm install classnames
import s from "./SelectRolePopup.module.scss"; // Đảm bảo bạn đã import CSS cho file này

const SelectRolePopup = ({ roleBtnRef, closePopup, handleSelect }) => {
    const popupRef = useRef(null);

    const roles = {
        COORDINATOR: "Thủ kho",
        WAREHOUSE_MANAGER: "Nhân viên điều phối",
        WAREHOUSE_STAFF: "Nhân viên kho"
    }


    // Đóng popup khi bấm ra ngoài
    const handleClickOutside = (e) => {
        if (popupRef.current && !popupRef.current.contains(e.target) && roleBtnRef.current && !roleBtnRef.current.contains(e.target)) {
            closePopup();
        }
    };



    useEffect(() => {
        document.addEventListener("mousedown", handleClickOutside);
        return () => {
            document.removeEventListener("mousedown", handleClickOutside);
        };
    }, []);


    return (
        <div ref={popupRef} className={s.container}>
        
            {/* Phần các trạng thái khác */}
            <div className={s.wrapper}>
                {Object.keys(roles).map((key) => (
                    <div style={{ width: "100%" }} key={key} onClick={() => {handleSelect('role',key) 
                        closePopup()}
                    }>
                        <button
                            type="button"
                            className={cn(s.selectItem)}
                        >

                            {roles[key]}
                        </button>
                    </div>
                ))}
            </div>
        </div>
    );
};

export default SelectRolePopup;
