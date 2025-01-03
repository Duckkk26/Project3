import React, { useEffect, useRef, useState } from 'react'

import s from './SearchSupplier.module.scss'

import { FontAwesomeIcon } from '@fortawesome/react-fontawesome'
import { faMagnifyingGlass } from '@fortawesome/free-solid-svg-icons'
import { faAddressCard } from '@fortawesome/free-regular-svg-icons'
import ListSelectPopup from '../ListSelectPopup/ListSelectPopup'
import { getAllSupplierByName } from '../../service/SuppliersAPI'
import { useDebouncedEffect } from '../../utils/CommonUtils'

const SearchSupplier = ({setSelectItem, setCreateSupplier}) => {
    const [isSupplierPopup, setIsSupplierPopup] = useState(false);
    const supplierBtnRef = useRef(null);

    const [dataFilter, setDataFilter] = useState({
        keyword: "",
        page: 1,
        size: 10,
        totalPage: 1,
        totalElement: 0,
    });
    const [supplierList, setSupplierList] = useState([]);

    const fetchSupplier = async () => {
        const response = await getAllSupplierByName(dataFilter.page, dataFilter.size, dataFilter.keyword);
        setSupplierList(response.data.data);
        setDataFilter({
            ...dataFilter,
            totalPage: response.data.total_page,
            totalElement: response.data.total_items
        });
    }

    useEffect(() => {
        if (isSupplierPopup) {
            fetchSupplier();
        }
    }, [isSupplierPopup]);

    useDebouncedEffect(() => {
        fetchSupplier();
    }, 200, [dataFilter.keyword]);

    return (
        <div className={s.container}>
            <div className="box-search">
                <div ref={supplierBtnRef} onClick={() => setIsSupplierPopup(true)} className="box-input">
                    <div className="search-icon">
                        <FontAwesomeIcon icon={faMagnifyingGlass} />
                    </div>
                    <input
                        placeholder='Tìm theo tên, SĐT, mã nhà cung cấp...'
                        type="text" name="search"
                        id=""
                        autoComplete='on'
                        onChange={e => setDataFilter(prev => {
                            return {
                                ...prev,
                                keyword: e.target.value
                            }
                        })} />
                    <fieldset className='input-field'></fieldset>
                </div>
                {
                    isSupplierPopup &&
                    <ListSelectPopup
                        title={"nhà cung cấp"}
                        isLarge={true}
                        isSearch={false}
                        isFastCreate={true}
                        dataList={supplierList}
                        handleSelect={id => setSelectItem(id)}
                        btnRef={supplierBtnRef}
                        closePopup={() => setIsSupplierPopup(false)}
                        handleCLickCreateProductQuickly={setCreateSupplier}
                    />
                }
            </div>
            <div className={s.boxEmpty}>
                <FontAwesomeIcon icon={faAddressCard} />
                <p>Chưa có thông tin nhà cung cấp</p>
            </div>
        </div>
    )
}

export default SearchSupplier