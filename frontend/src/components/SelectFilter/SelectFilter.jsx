
import { useEffect, useRef, useState,useCallback } from "react";
import cn from "classnames";
import s from "./SelectFilter.module.scss";
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faArrowLeft, faArrowRight, faMagnifyingGlass } from '@fortawesome/free-solid-svg-icons';
import { faSquare, faCheckSquare } from '@fortawesome/free-regular-svg-icons';

const SelectFilter = ({ 
    closePopup, 
    listObject, 
    btnRef, 
    currentPage, 
    totalPage,
    onSelectionChange, 
    handleOnClickButton,
    keyword,
    handleChangeKeyword,
    loadMoreData
}) => {
    const [loading, setLoading] = useState(false); // Trạng thái loading để kiểm soát tải thêm dữ liệu
    const listRef = useRef(null); // Tham chiếu đến danh sách cuộn

    // Hàm xử lý khi cuộn đến gần cuối của danh sách
    const handleScroll = useCallback(() => {
        const list = listRef.current;
        if (!list || loading) return; // Nếu đang loading hoặc không có listRef thì không làm gì
        
        // Lấy các giá trị của thanh cuộn
        const scrollTop = list.scrollTop;
        const scrollHeight = list.scrollHeight;
        const clientHeight = list.clientHeight;

        // Kiểm tra nếu đã cuộn đến 80% của danh sách
        if (scrollTop + clientHeight >= scrollHeight * 0.8) {
            setLoading(true); // Đặt trạng thái loading để ngăn chặn việc gọi API nhiều lần
            loadMoreData().then(() => {
                setLoading(false); // Sau khi tải xong, bỏ trạng thái loading
            });
        }
    }, [loading, loadMoreData]);

    // Thêm sự kiện cuộn để tải thêm dữ liệu
    useEffect(() => {
        const list = listRef.current;
        if (list) {
            list.addEventListener('scroll', handleScroll);
        }

        // Cleanup: Xóa sự kiện khi component bị hủy hoặc listRef thay đổi
        return () => {
            if (list) {
                list.removeEventListener('scroll', handleScroll);
            }
        };
    }, [handleScroll]);

    const popupRef = useRef(null);
    
    const [selectedCategories, setSelectedCategories] = useState([]);
    const [isAllSelected, setIsAllSelected] = useState(false);

    const handleClickOutside = (e) => {
        if (popupRef.current && !popupRef.current.contains(e.target) && btnRef.current && !btnRef.current.contains(e.target)) {
            closePopup();
        }
    };

    useEffect(() => {
        document.addEventListener('mousedown', handleClickOutside);
        return () => {
            document.removeEventListener('mousedown', handleClickOutside);
        };
    }, []);
    
    const handleOnclickButtonFilter = () => {
        onSelectionChange(selectedCategories);
        closePopup();
    }

    const toggleSelectAll = () => {
        if (isAllSelected) {
            setSelectedCategories([]); // Bỏ chọn tất cả
        } else {
            const allSelectedIds = listObject.map(item => item.id);
            setSelectedCategories(allSelectedIds); // Chọn tất cả
        }
        setIsAllSelected(!isAllSelected);
    };

    const toggleSelectCategory = (sub_id) => {
        let updatedObject;
        
        if (selectedCategories.includes(sub_id)) {
            // Bỏ chọn mục
            updatedObject = selectedCategories.filter(categoryId => categoryId !== sub_id);
        } else {
            // Chọn mục
            updatedObject = [...selectedCategories, sub_id];
        }
    
        setSelectedCategories(updatedObject);
    
        // Nếu không phải tất cả các mục đều được chọn, cập nhật trạng thái "Chọn tất cả"
        if (updatedObject.length !== listObject.length) {
            setIsAllSelected(false); // Trở về trạng thái chưa chọn tất cả
        } else {
            setIsAllSelected(true); // Nếu tất cả đều được chọn, thì đặt trạng thái là đã chọn tất cả
        }
    };

    return (
        <div ref={popupRef} className={s.container}>
            <div className={s.wrapper}>
                <div className={s.search}> 
                    <div>
                        <FontAwesomeIcon icon={faMagnifyingGlass} style={{ color: "#aaaeb5", fontSize: "14px" }} />
                    </div>
                    <input placeholder="Tìm kiếm" style={{ flex: 1 }} value={keyword} onChange={handleChangeKeyword}/>
                </div>

                <div className={s.wrapper_select} ref={listRef}>
                    <div className={s.select_all} onClick={toggleSelectAll}>
                        {
                            isAllSelected ?
                            (
                                <div>
                                    <FontAwesomeIcon icon={ faCheckSquare} style={{ color: "#4d53e0", fontSize: "18px" }} /> 
                                </div>
                            ) :
                            (
                                <div>
                                    <FontAwesomeIcon icon={faSquare} style={{ color: "#aaaeb5", fontSize: "18px" }} /> 
                                </div>
                            )
                        }
                        <div>Chọn tất cả</div>
                    </div>

                    {listObject.map((item,index) => (
                        <div className={s.select_all} key={index} onClick={() => toggleSelectCategory(item.sub_id)}>
                            { selectedCategories.includes(item.sub_id) ?(
                                <div>
                                    <FontAwesomeIcon icon= {faCheckSquare} style={{ color: "#4d53e0", fontSize: "18px" }} />
                                </div>
                            )
                            : 
                            (
                                <div>
                                    <FontAwesomeIcon icon= {faSquare} style={{ color: "#aaaeb5", fontSize: "18px" }} />
                                </div>
                            )
                            }
                            <div>{item.name}</div>
                        </div>
                    ))}
                </div>

                <button className={s.btn_select} onClick={handleOnclickButtonFilter}>Lọc</button>
            </div>
        </div>
    );
};

export default SelectFilter;

