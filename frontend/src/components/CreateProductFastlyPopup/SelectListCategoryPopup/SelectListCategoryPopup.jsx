import React, { useEffect, useRef, useState, useCallback } from 'react';
import cn from 'classnames';

// Import CSS
import s from './SelectListCategoryPopup.module.scss';

// Import Icons
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faCircleUser, faMagnifyingGlass, faPlus } from '@fortawesome/free-solid-svg-icons';

const SelectListCategoryPopup = ({
    title,
    isLarge,
    isSearch,
    keyword,
    handleChangeKeyword,
    dataList,
    handleSelect,
    btnRef,
    closePopup,
    fetchMoreData,
}) => {
    const popupRef = useRef(null);
    const listRef = useRef(null); // Ref cho container của danh sách
    const [loading, setLoading] = useState(false); // Trạng thái loading để kiểm soát tải thêm dữ liệu

    // Xử lý sự kiện khi click ra ngoài popup để đóng
    const handleClickOutside = (e) => {
        if (popupRef.current && !popupRef.current.contains(e.target) && btnRef.current && !btnRef.current.contains(e.target)) {
            closePopup();
        }
    };

    // Hàm xử lý khi cuộn đến gần cuối của danh sách
    const handleScroll = useCallback(() => {
        const list = listRef.current;
        if (!list || loading) return; // Nếu đang loading hoặc không có listRef thì không làm gì

        const scrollTop = list.scrollTop;
        const scrollHeight = list.scrollHeight;
        const clientHeight = list.clientHeight;

        // Kiểm tra nếu đã cuộn đến 80% của danh sách 
        if (scrollTop + clientHeight >= scrollHeight * 0.8) {
            setLoading(true); // Đặt trạng thái loading để ngăn chặn việc gọi API nhiều lần
            fetchMoreData().then(() => {
                setLoading(false); // Sau khi tải xong, bỏ trạng thái loading
            });
        }
    }, [loading, fetchMoreData]);

    // Xử lý sự kiện cuộn để tải thêm dữ liệu
    useEffect(() => {
        const list = listRef.current;
        if (list) {
            list.addEventListener('scroll', handleScroll);
        }

        return () => {
            if (list) {
                list.removeEventListener('scroll', handleScroll);
            }
        };
    }, [handleScroll]);

    // Đóng popup khi click ngoài vùng của nó
    useEffect(() => {
        document.addEventListener('mousedown', handleClickOutside);
        return () => {
            document.removeEventListener('mousedown', handleClickOutside);
        };
    }, []);

    // Hàm xử lý khi chọn một item trong danh sách
    const onSelectItem = (id) => {
        handleSelect(id);
        closePopup();
    };

    return (
        <div ref={popupRef} className={cn(s.container, { [s.largePopup]: isLarge })}>
            <div className={s.wrapper}>                
                <div>
                    {dataList?.length > 0 ? (
                        <div ref={listRef} className={s.selectList} >
                            {dataList.map((item, index) => (
                                <div key={index} className={s.selectItem} onClick={() => onSelectItem(item?.id)}>
                                    {title === 'nhà cung cấp' && <FontAwesomeIcon className={s.supplierAvatar} icon={faCircleUser} />}
                                    <div className={s.selectContent}>
                                        <p className={s.itemName}>{item.name}</p>
                                    </div>
                                </div>
                            ))}
                        </div>
                    ) : (
                        <div className={s.emptyList}>Không có kết quả</div>
                    )}
                </div>
            </div>
        </div>
    );
};

export default SelectListCategoryPopup;