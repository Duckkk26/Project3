import { useRef, useState } from "react";
import s from "./PaymentPopup.module.scss";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import { faCaretDown, faXmark } from '@fortawesome/free-solid-svg-icons'
import SelectDatePopup from "../SelectDatePopup";
import cn from "classnames";
import { DateTimePicker, LocalizationProvider } from "@mui/x-date-pickers";
import { AdapterDayjs } from "@mui/x-date-pickers/AdapterDayjs";
import ListSelectPopup from "../ListSelectPopup/ListSelectPopup";
import { createTransaction, paymentGRN } from "../../service/TransactionAPI";
import { toast } from "react-toastify";
import Notification from "../Notification/Notification";

const PaymentPopup = ({
    grnId,
    handleOnClickBack,
}) => {
    const paymentMethods = [
        { id: 1, key: 'CREDIT_CARD', name: "Quẹt thẻ" },
        { id: 2, key: 'BANK_TRANSFER', name: "Chuyển khoản ngân hàng" },
        { id: 3, key: 'CASH', name: "Tiền mặt" },
    ];

    const [dataCreate, setDataCreate] = useState({
        grn_id: grnId,
        payment_method: null,
        amount: null
    });

    const [isPaymentMethodPopup, setIsPaymentMethodPopup] = useState(false);
    const paymentMethodBtnRef = useRef(null);

    const handleCreate = async () => {
        if (!dataCreate.payment_method) {
            alert("Vui lòng chọn phương thức thanh toán");
            return;
        }
        if (!dataCreate.amount) {
            alert("Vui lòng nhập số tiền");
            return;
        }
        const response = await paymentGRN(dataCreate);
        if (response.status_code === 201) {
            toast(<Notification 
                    type={"success"} 
                    withIcon 
                    message={"Thanh toán đơn nhập hàng thành công"} 
                />,
                {
                    autoClose: 4000,
                    closeButton: false,
                    hideProgressBar: true,
                }
            )
            handleOnClickBack();
        }        
    };

    return (
        <div className={s.popup}>
            <div className="overlay"></div>
            <div className={s["container"]}>
                <div className={cn("box-info-item", s["wrapper"])}>
                    <div className={cn("info-header", s["header"])}>
                        <div className={s["box-header"]}>
                            <p>Xác nhận thanh toán</p>
                        </div>
                        <div className="btn-toolbar">
                            <button onClick={() => handleOnClickBack()} className="btn-icon">
                                <FontAwesomeIcon icon={faXmark} />
                            </button>
                        </div>
                    </div>

                    <div className={cn("info-content", s["content"])}>
                        <div className={s["group-form-items"]}>
                            <div className="form-item">
                                <label htmlFor="brand" className="form-label">
                                    Chọn phương thức thanh toán
                                </label>
                                <div className="box-select">
                                    <button ref={paymentMethodBtnRef} onClick={() => setIsPaymentMethodPopup(!isPaymentMethodPopup)} className="btn-select">
                                        {dataCreate.payment_method ? paymentMethods.find(item => item.key === dataCreate.payment_method).name : "Chọn phương thức thanh toán"}
                                        <FontAwesomeIcon icon={faCaretDown} />
                                    </button>
                                    {
                                        isPaymentMethodPopup &&
                                        <ListSelectPopup 
                                            dataList={paymentMethods}
                                            btnRef={paymentMethodBtnRef}
                                            closePopup={() => setIsPaymentMethodPopup(false)}
                                            handleSelect={(id) => setDataCreate({ ...dataCreate, payment_method: paymentMethods.find(item => item.id === id).key })}
                                        />
                                    }
                                </div>
                            </div>
                            <div className="form-item">
                                <label htmlFor="unit" className="form-label">
                                    Số tiền
                                </label>
                                <div className="form-textfield">
                                    <input
                                        type="text"
                                        name="amount"
                                        id="amount"
                                        onChange={(e) => setDataCreate({ ...dataCreate, amount: e.target.value })}
                                    />
                                    <fieldset className="input-field"></fieldset>
                                </div>
                            </div>
                        </div>
                    </div>
                    <div className={s["box-btns"]}>
                        <div className="btn-toolbar">
                            <button className="btn btn-outline-primary" onClick={handleOnClickBack}>
                                <span className="btn__title">Thoát</span>
                            </button>
                            <button className="btn btn-primary" onClick={handleCreate}>
                                <span className="btn__title">Thêm</span>
                            </button>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    );
};

export default PaymentPopup;
