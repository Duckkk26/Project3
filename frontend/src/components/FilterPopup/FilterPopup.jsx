import React, { useState } from 'react'

import s from './FilterPopup.module.scss'

import { FontAwesomeIcon } from '@fortawesome/react-fontawesome'
import { faXmark } from '@fortawesome/free-solid-svg-icons'
import cn from 'classnames'

const FilterPopup = ({ defaultCols, colGroup, colsToRender, setColsToRender, closePopup }) => {
    const [colsList, setColsList] = useState(colsToRender);

    const handleCheck = (name) => {
        setColsList({
            ...colsList,
            [name]: !colsList[name]
        })
    }

    const handleChangeColsToRender = () => {
        setColsToRender(colsList);
        closePopup();
    }

    return (
        <div className={s.popup}>
            <div className='overlay'>
            </div>
            <div className={s.container}>
                <div className={s.wrapper}>
                    <div className={s.header}>
                        <div className={s["box-header"]}>
                            <p>Điều chỉnh cột hiển thị</p>
                        </div>
                        <div className="btn-toolbar">
                            <button onClick={closePopup} className="btn-icon">
                                <FontAwesomeIcon icon={faXmark} />
                            </button>
                        </div>
                    </div>
                    <div className={s["filter-container"]}>
                        <div className={s["filter-wrapper"]}>
                            <div className={s["box-filter"]}>
                                <p className={s.title}>Thêm cột hiển thị</p>
                                <div className={s["filter-content"]}>
                                    <div className={s["filter-list"]}>
                                        {
                                            Object.entries(colsList).map(([key, value]) => {
                                                return (
                                                    <div className={s["filter-item"]}>
                                                        <div className="checkbox__container">
                                                            <div className={cn("checkbox__wrapper", s.checkbox)}>
                                                                <input 
                                                                    disabled={key === "id" || key == "sub_id" || colGroup[key].name === "Sản phẩm"}
                                                                    checked={value}
                                                                    onChange={(e) => handleCheck(e.target.name)}
                                                                    type="checkbox" 
                                                                    name={key} 
                                                                    id="" 
                                                                    className='checkbox__input' />
                                                                <div className="btn-checkbox"></div>
                                                            </div>
                                                        </div>
                                                        <p className={s["filter-value"]}>{colGroup[key]?.name}</p>
                                                    </div>
                                                )
                                            })
                                        }
                                    </div>
                                </div>
                            </div>
                            <div className={s["box-filter"]}>
                                <p className={s.title}>Cột hiển thị</p>
                                <div className={s["filter-content"]}>
                                    <div className={s["filter-list"]}>
                                        {
                                            Object.entries(colsList).map(([key, value]) => {
                                                if (value) {
                                                    return (
                                                        <div className={s["col-item"]}>
                                                            <p className="col-title">{colGroup[key].name}</p>
                                                            {
                                                                !(key === "id" || key === "sub_id" || colGroup[key].name === "Sản phẩm") &&
                                                                <button onClick={() => handleCheck(key)} className="btn-icon">
                                                                    <FontAwesomeIcon icon={faXmark} />
                                                                </button>
                                                            }
                                                        </div>
                                                    )
                                                }
                                            })
                                        }
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                    <div className={s["group-btns"]}>
                        <button onClick={() => setColsList(defaultCols)} class="btn btn-outline-primary">
                            <span class="btn__title">Quay về mặc định</span>
                        </button>
                        <button onClick={() => closePopup()} class="btn btn-outline-primary">
                            <span class="btn__title">Thoát</span>
                        </button>
                        <button onClick={() => handleChangeColsToRender()} class="btn btn-primary">
                            <span class="btn__title">Lưu</span>
                        </button>
                    </div>
                </div>
            </div>
        </div>
    )
}

export default FilterPopup