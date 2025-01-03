import { faCaretDown } from '@fortawesome/free-solid-svg-icons';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import React, { useRef, useState } from 'react'
import CreatedAtFilter from './GINList/FiltersPopup/CreatedAtFilter';
import { formatDate } from '../utils/DateUtils';

const SelectDatePopup = ({ data, setDataFilters }) => {
    const createdAtRef = useRef(null);
    const [isOpenCreatedAtPopup, setIsOpenCreatedAtPopup] = useState(false);

    const handleChangeDataFilter = (filters) => {
		setDataFilters(filters);
	};

    return (
        <>
            <button
                ref={createdAtRef}
                onClick={() => setIsOpenCreatedAtPopup(!isOpenCreatedAtPopup)}
                className="btn btn-base btn-filter"
            >
                <span className="btn__label">
                    {
                        data ? 
                        `${formatDate(data.date_from)} - ${formatDate(data.date_to)}` :
                        "Ngày tạo"
                    }
                    <span className="btn__icon">
                        <FontAwesomeIcon icon={faCaretDown} />
                    </span>
                </span>
            </button>
            {isOpenCreatedAtPopup && (
                <CreatedAtFilter
                    createdRef={createdAtRef}
                    closePopup={() => setIsOpenCreatedAtPopup(false)}
                    handeChangeDatafilter={(data) => handleChangeDataFilter(data)}
                />
            )}
        </>
    )
}

export default SelectDatePopup