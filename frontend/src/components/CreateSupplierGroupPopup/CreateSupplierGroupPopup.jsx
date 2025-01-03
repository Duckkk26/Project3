
import s from "./CreateSupplierGroupPopup.module.scss"
const CreateSupplierGroupPopup = ({handleOnClickBack, handleOnClickCreate, handleOnChange} ) =>{
    return(

        <div className={s["modal-content"]}>
            <div className={s["modal-header"]}>
                <div className={s["modal-box-header"]}>
                    <h5>Thêm mới nhóm nhà cung cấp</h5>
                </div>
            </div>

            <div className={s["modal-body"]}>
                <div className={s["column-body"]}>
                    <div className={s["row"]}>
                        <label htmlFor="name" className={s["form-label"]}>
                            Tên nhóm nhà cung cấp
                            <span className="asterisk-icon">*</span>
                        </label>
                        <div className={s["form-textfield"]}>
                            <input
                                type="text"
                                name="name"
                                id="name"
                                placeholder='Nhập tên nhóm nhà cung cấp'
                                onChange={handleOnChange}
                            />
                            <fieldset className="input-field"></fieldset>
                        </div>
                    </div>
                    <div className={s["row"]}>
                        <label htmlFor="sub_id" className={s["form-label"]}>
                            Mã nhóm nhà cung cấp
                        </label>
                        <div className={s["form-textfield"]}>
                            <input
                                type="text"
                                name="sub_id"
                                id="sub_id"
                                placeholder='Nhập mã nhóm nhà cung cấp'
                                onChange={handleOnChange}
                            />
                            <fieldset className="input-field"></fieldset>
                        </div>
                    </div>
                    
                </div>

                <div className={s["column-body"]}>
                    <div className={s["row"]}>
                        <label htmlFor="note" className={s["form-label"]}>
                            Ghi chú
                        </label>
                        <div className={s["form-textfield"]}>
                            <textarea
                                type="text"
                                name="note"
                                id="note"
                                placeholder='Ghi chú'
                                onChange={handleOnChange}
                            />
                            <fieldset className="input-field"></fieldset>
                        </div>
                    </div>
                </div>
            </div>

            <div className={s["modal-footer"]}>
                <button className="btn btn-outline-primary" onClick={handleOnClickBack}>
                    <span className="btn__title">Thoát</span>
                </button>
                <button className="btn btn-primary" onClick={handleOnClickCreate}>
                    <span className="btn__title">Thêm</span>
                </button>
            </div>
        </div>
    )
}

export default CreateSupplierGroupPopup;