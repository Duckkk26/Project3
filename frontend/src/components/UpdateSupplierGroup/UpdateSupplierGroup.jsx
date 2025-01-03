import { useEffect, useState } from "react";
import s from "./UpdateSupplierGroup.module.scss";
import { updateSupplierGroup } from "../../service/SupplierGroupsAPI";
import { toast } from "react-toastify";
import Notification from "../Notification/Notification";
const UpdateSupplierGroup = ({ handleOnClickBack, item }) => {
  const [data, setData] = useState({});

  const handleUpdate = async () => {
    const response = await updateSupplierGroup(data.id, {
      sub_id: data.sub_id,
      name: data.name,
      note: data.note,
    });
    if (response.status_code === 200) {
      toast(<Notification 
                type={"success"} 
                withIcon 
                message={"Cập nhật nhóm nhà cung cấp thành công"} 
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

  useEffect(() => {
    setData(item);
  }, []);

  return (
    <div className={s["modal-content"]}>
      <div className={s["modal-header"]}>
        <div className={s["modal-box-header"]}>
          <h5>Cập nhật nhóm nhà cung cấp</h5>
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
                value={data.sub_id}
                onChange={(e) =>
                  setData((prev) => {
                    return {
                      ...prev,
                      sub_id: e.target.value,
                    };
                  })
                }
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
                name="note"
                id="note"
                value={data.note}
                onChange={(e) =>
                  setData((prev) => {
                    return {
                      ...prev,
                      note: e.target.value,
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

export default UpdateSupplierGroup;
