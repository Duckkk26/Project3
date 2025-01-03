
import s from "./CreateCategoryPopup.module.scss"
const CreateCategoryPopup = ({handleOnClickBack, handleOnClickCreate, handleOnChange} ) =>{
    return(

        <div className={s["modal-content"]}>
            <div className={s["modal-header"]}>
                <div className={s["modal-box-header"]}>
                    <h5>Thêm mới loại sản phẩm</h5>
                </div>
            </div>

            <div className={s["modal-body"]}>
                <div className={s["column-body"]}>
                    <div className={s["row"]}>
                        <label htmlFor="name" className={s["form-label"]}>
                            Tên loại sản phẩm
                            <span className="asterisk-icon">*</span>
                        </label>
                        <div className={s["form-textfield"]}>
                            <input
                                type="text"
                                name="name"
                                id="name"
                                placeholder='Nhập tên loại sản phẩm'
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

export default CreateCategoryPopup;