import Header from "../Header/Header";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import cn from "classnames";
import { formatDateTime } from "../../utils/DateUtils";
import {
  faPlus,
  faMagnifyingGlass,
  faCaretDown,
  faChevronRight,
  faChevronLeft,
} from "@fortawesome/free-solid-svg-icons";
import classNames from "classnames";
import { useEffect, useState, useRef } from "react";
import {
  createCategoryTransaction,
  getCategoryTransactionList,
} from "../../service/CategoryTransaction";
import { useDebouncedEffect } from "../../utils/CommonUtils";
import LimitSelectPopup from "../LimitSelectPopup/LimitSelectPopup";
import CreateGroups from "../CreateGroups/CreateGroups";
import { createTransaction } from "../../service/TransactionAPI";
import UpdateGroups from "../UpdateGroups/UpdateGroups";
import Notification from "../Notification/Notification";
import { toast } from "react-toastify";
const PaymentGroups = () => {
  const limitBtnRef = useRef(null);

  const [isOpenLimitPopup, setIsOpenLimitPopup] = useState(false);
  const [isCreateGroups, setIsCreateGroups] = useState(false);
  const [isUpdateGroups, setIsUpdateGroups] = useState(false);
  const [receiptGroupsList, setReceiptGroupsList] = useState([]);
  const [dataBody, setDataBody] = useState({
    keyword: null,
    type: "EXPENSE",
  });
  const [dataPage, setDataPage] = useState({
    page: 1,
    size: 10,
    totalPage: 1,
    totalItem: 0,
  });
  const [dataCreate, setDataCreate] = useState({
    sub_id: null,
    name: "",
    description: "",
    type: "EXPENSE",
  });
  const [limit, setLimit] = useState(10);

  const [selectedGroups, setSelectedGroups] = useState(null);

  const handleChangeData = (e) => {
    const { name, value } = e.target; // Lấy name và value từ input
    setDataCreate((prevData) => ({
      ...prevData,
      [name]: value, // Cập nhật giá trị tương ứng với name
    }));
  };
  const handleClickBack = () => {
    setIsCreateGroups(false);
  };
  const handleClickBackUpdate = () => {
    setIsUpdateGroups(false);
  };
  const handleCLickCreate = async () => {
    const response = await createCategoryTransaction(dataCreate);
    toast(<Notification 
        type={"success"} 
        withIcon 
        message={"Tạo phiếu chi thành công"} 
      />,
      {
        autoClose: 4000,
        closeButton: false,
        hideProgressBar: true,
      }
    )
    setDataCreate({
      sub_id: null,
      name: "",
      description: "",
      type: "EXPENSE",
    });
    setIsCreateGroups(false);
  };
  const handlePrevPage = () => {
    if (dataPage.page > 1) {
      setDataPage((prevData) => ({
        ...prevData,
        page: prevData.page - 1, // Giảm số trang đi 1
      }));
    }
  };

  const handleNextPage = () => {
    if (dataPage.page < dataPage.totalPage) {
      setDataPage((prevData) => ({
        ...prevData,
        page: prevData.page + 1, // Tăng số trang lên 1
      }));
    }
  };
  const fetchReceiptGroupsList = async () => {
    const response = await getCategoryTransactionList(
      dataPage.page,
      dataPage.size,
      dataBody.keyword,
      dataBody.type
    );
    setReceiptGroupsList(response.data.data);
    setDataPage((prevData) => ({
      ...prevData,
      totalPage: response.data.total_page,
      totalItem: response.data.total_items,
    }));
  };

  useDebouncedEffect(
    () => {
      fetchReceiptGroupsList();
    },
    300,
    [limit, dataPage.page, dataBody, isCreateGroups, isUpdateGroups]
  );
  
  return (
    <>
      {isCreateGroups && (
        <>
          <div className="overlay"></div>
          <CreateGroups
            type={"Loại phiếu chi"}
            handleOnClickBack={handleClickBack}
            handleOnChange={handleChangeData}
            handleOnClickCreate={handleCLickCreate}
          />
        </>
      )}
      {isUpdateGroups && (
        <>
          <div className="overlay"></div>
          <UpdateGroups
            type={"Loại phiếu chi"}
            item={selectedGroups}
            handleOnClickBack={handleClickBackUpdate}
          />
        </>
      )}
      <Header title={"Loại phiếu chi"} />
      <div className="right__listPage">
        <div className="right__toolbar">
          <div className="btn-toolbar">
            <button
              className="btn btn-primary"
              onClick={() => setIsCreateGroups(true)}
            >
              <span className="btn__icon">
                <FontAwesomeIcon icon={faPlus} />
              </span>
              <span className="btn__title">Thêm loại phiếu chi</span>
            </button>
          </div>
        </div>
        <div className="right__table">
          <div className="right__table-scroller">
            <div className="box-scroller">
              <div className="group-scroller-btns">
                <button className="btn-scroller active">
                  Tất cả loại phiếu chi
                </button>
              </div>
            </div>
          </div>
          <div className="right__table-search-filter">
            <div className="">
              <div className="box-search-filter-btns">
                <div className="box-search">
                  <div className="box-input">
                    <div className="search-icon">
                      <FontAwesomeIcon icon={faMagnifyingGlass} />
                    </div>
                    <input
                      placeholder="Tìm kiếm theo mã loại, tên loại"
                      type="text"
                      name="search"
                      id=""
                      autoComplete="on"
                      value={dataBody.keyword || ""} // Đảm bảo giá trị không bị undefined
                      onChange={(e) =>
                        setDataBody({ ...dataBody, keyword: e.target.value })
                      }
                    />
                    <fieldset className="input-field" />
                  </div>
                </div>
              </div>
            </div>
          </div>
          <div className="right__table-headers">
            <table className="box-table-headers">
              <colgroup>
                <col style={{ width: "233px" }} />
                <col style={{ width: "233px" }} />
                <col style={{ width: "233px" }} />
                <col style={{ width: "295px" }} />
              </colgroup>
              <thead>
                <tr className="group-table-headers">
                  <th
                    colSpan={1}
                    rowSpan={1}
                    className={cn("table-header-item", "text-start")}
                  >
                    Mã loại
                  </th>
                  <th
                    colSpan={1}
                    rowSpan={1}
                    className={cn("table-header-item", "text-start")}
                  >
                    Tên loại
                  </th>
                  <th
                    colSpan={1}
                    rowSpan={1}
                    className={cn("table-header-item", "text-center")}
                  >
                    Ngày tạo
                  </th>
                  <th
                    colSpan={1}
                    rowSpan={1}
                    className={cn("table-header-item", "text-start")}
                  >
                    Mô tả
                  </th>
                </tr>
              </thead>
            </table>
          </div>
          <div className="right__table-content">
            <div className="right__table-data">
              <div className="table-data__container">
                <table className="box-table-data">
                  <col style={{ width: "233px" }} />
                  <col style={{ width: "233px" }} />
                  <col style={{ width: "233px" }} />
                  <col style={{ width: "295px" }} />
                  <tbody>
                    {receiptGroupsList?.map((item, index) => {
                      return (
                        <tr
                          key={index}
                          className="table-data-row"
                          onClick={() => {
                            setIsUpdateGroups(true);
                            setSelectedGroups(item);
                          }}
                        >
                          <td className={cn("table-data-item", "text-start")}>
                            <p className="box-text">{item?.sub_id}</p>
                          </td>
                          <td className={cn("table-data-item", "text-start")}>
                            <p className="box-text">{item?.name}</p>
                          </td>
                          <td className={cn("table-data-item", "text-center")}>
                            <p className="box-text">
                              {formatDateTime(item?.created_at)}
                            </p>
                          </td>
                          <td className={cn("table-data-item", "text-start")}>
                            <p className="box-text">{item?.description}</p>
                          </td>
                        </tr>
                      );
                    })}
                  </tbody>
                </table>
              </div>
            </div>
            <div className="right__table-pagination">
              <p>Hiển thị</p>
              <div className="box-page-limit">
                <button
                  ref={limitBtnRef}
                  onClick={() => setIsOpenLimitPopup(!isOpenLimitPopup)}
                  className={cn("btn-page-limit", {
                    selected: isOpenLimitPopup,
                  })}
                >
                  {limit}
                  <span>
                    <FontAwesomeIcon icon={faCaretDown} />
                  </span>
                </button>
                {isOpenLimitPopup && (
                  <LimitSelectPopup
                    btnRef={limitBtnRef}
                    closePopup={() => setIsOpenLimitPopup(false)}
                    limit={limit}
                    handleChangeLimit={(limit) => {
                      setLimit(limit);
                    }}
                  />
                )}
              </div>
              <p>Kết quả</p>
              <p className="item-quantity">
                Từ {(dataPage.page - 1) * limit + 1} đến{" "}
                {(dataPage.page - 1) * limit + receiptGroupsList.length} trên
                tổng {dataPage.totalItem}
              </p>
              <button
                className={cn("btn-icon", "btn-page", {
                  inactive: dataPage.page === 1,
                })}
                onClick={handlePrevPage}
              >
                <FontAwesomeIcon icon={faChevronLeft} />
              </button>
              {Array(dataPage.totalPage)
                .fill(null)
                .map((_, index) => (
                  <div
                    key={index}
                    className={cn("box-page", {
                      active: dataPage.page === index + 1,
                    })}
                    onClick={() =>
                      setDataPage((prevData) => ({
                        ...prevData,
                        page: index + 1, // Cập nhật số trang
                      }))
                    }
                  >
                    {index + 1}
                  </div>
                ))}
              <button
                className={cn("btn-icon", "btn-page", {
                  inactive: dataPage.page === dataPage.totalPage,
                })}
                onClick={handleNextPage}
              >
                <FontAwesomeIcon icon={faChevronRight} />
              </button>
            </div>
          </div>
        </div>
      </div>
    </>
  );
};
export default PaymentGroups;
