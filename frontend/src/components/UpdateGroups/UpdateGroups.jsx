import { useEffect, useState } from "react";
import s from "./UpdateGroups.module.scss";
import { updateCategoryTransaction } from "../../service/CategoryTransaction";
import { toast } from "react-toastify";
import Notification from "../Notification/Notification";
const UpdateGroups = ({ type, handleOnClickBack, item }) => {
  const [data, setData] = useState({});

  useEffect(() => {
    setData(item);
  }, []);

  const handleUpdate = async () => {
    const response = await updateCategoryTransaction(data.id, {
      name: data.name,
      description: data.description,
    });
    if (response.status_code === 200) {
      if (type === "Loại phiếu thu") {
        toast(<Notification 
                type={"success"} 
                withIcon 
                message={"Cập nhật loại phiếu thu thành công"} 
            />,
            {
                autoClose: 4000,
                closeButton: false,
                hideProgressBar: true,
            }
        )
      } else {
        toast(<Notification 
                type={"success"} 
                withIcon 
                message={"Cập nhật loại phiếu chi thành công"} 
            />,
            {
                autoClose: 4000,
                closeButton: false,
                hideProgressBar: true,
            }
        )
      }
      handleOnClickBack();
    }
  };
  return (
    <div className={s["modal-content"]}>
      <div className={s["modal-header"]}>
        <div className={s["modal-box-header"]}>
          <h5>
            {type === "Loại phiếu thu"
              ? "Cập nhật loại phiếu thu"
              : "Cập nhật loại phiếu chi"}
          </h5>
        </div>
      </div>

      <div className={s["modal-body"]}>
        <div className={s["column-body"]}>
          <div className={s["row"]}>
            <label htmlFor="name" className={s["form-label"]}>
              Tên
              <span className="asterisk-icon">*</span>
            </label>
            <div className={s["form-textfield"]}>
              <input
                type="text"
                name="name"
                id="name"
                onChange={(e) =>
                  setData((prev) => {
                    return {
                      ...prev,
                      name: e.target.value,
                    };
                  })
                }
                value={data?.name}
              />
              <fieldset className="input-field"></fieldset>
            </div>
          </div>
          <div className={s["row"]}>
            <label htmlFor="sub_id" className={s["form-label"]}>
              Mã
            </label>
            <div className={s["form-textfield"]}>
              <input
                type="text"
                name="sub_id"
                id="sub_id"
                value={item.sub_id}
                disabled
              />
              <fieldset className="input-field"></fieldset>
            </div>
          </div>
        </div>

        <div className={s["column-body"]}>
          <div className={s["row"]}>
            <label htmlFor="note" className={s["form-label"]}>
              Mô tả
            </label>
            <div className={s["form-textfield"]}>
              <textarea
                type="text"
                name="description"
                id="description"
                value={data.description}
                onChange={(e) =>
                  setData((prev) => {
                    return {
                      ...prev,
                      description: e.target.value,
                    };
                  })
                }
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
        <button className="btn btn-primary" onClick={handleUpdate}>
          <span className="btn__title">Cập nhật</span>
        </button>
      </div>
    </div>
  );
};

export default UpdateGroups;
