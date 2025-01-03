import React, { useEffect, useRef, useState } from "react";
import Header from "../../components/Header/Header";
import exportIcon from "../../assets/icons/ExportIcon";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import cn from "classnames";

import s from './Reports.module.scss'

import {
  faAnglesRight,
  faCaretDown,
  faChevronLeft,
  faChevronRight,
  faMagnifyingGlass,
  faPlus,
} from "@fortawesome/free-solid-svg-icons";
import { getListTransaction, getTotalReport } from "../../service/TransactionAPI";
import { formatDateTime } from "../../utils/DateUtils";
import { useNavigate } from "react-router-dom";
import SelectDatePopup from "../../components/SelectDatePopup";
import ListSelectPopup from "../../components/ListSelectPopup/ListSelectPopup";

const Reports = () => {
  const navigate = useNavigate();
  const [colsToRender, setColsToRender] = useState({
    created_at: true, // Ngày tạo
    updated_at: true, // ngày cập nhật gần nhất
    id: false,
    sub_id: true, // mã phiếu
    category_name: true, // loại phiếu
    type: false,
    payment_method: true, // pt thanh toán
    amount_income: true,
    amount_expense: true,
    reference_code: false,
    reference_id: true, // mã chứng từ
    tags: false,
    note: true, // mô tả
    recipient_group: false,
    recipient_id: false,
    recipient_name: true, // người nộp/nhận
  });

  const mode = {
    INCOME: "Phiếu thu",
    EXPENSE: "Phiếu chi",
  };

  const modesTab = [
    { key: "all", label: "Tất cả", modes: null },
    { key: "income", label: "Phiếu thu", modes: "INCOME" },
    { key: "expense", label: "Phiếu chi", modes: "EXPENSE" },
  ];

  const [dataFilter, setDataFilter] = useState({
    mode: null,
    keyword: null,
    user_created_ids: null,
    payment_methods: null,
    date_from: "2023-10-10",
    date_to: "2024-10-10",
    date_type: "CREATED_AT",
  });

  const paymentMethods = {
    CASH: "Tiền mặt",
    BANK_TRANSFER: "Chuyển khoản",
    CREDIT_CARD: "Thẻ tín dụng",
  }

  const [modeActive, setModeActive] = useState("all");
  const [listTransactions, setListTransactions] = useState([]);
  const [dataPage, setDataPage] = useState({
    page: 1,
    size: 10,
    totalPage: 1,
    totalItem: 0,
  });

  const [isSelectPopup, setIsSelectPopup] = useState(false)
  const btnOpenPopupRef = useRef(null)

  const headersRef = useRef(null);
  const contentRef = useRef(null);

  const handleScroll = (e, target) => {
    target.scrollLeft = e.target.scrollLeft;
  };

  const handleTabClick = (key, mode) => {
    setModeActive(key);
    setDataFilter((prev) => ({
      ...prev,
      mode: mode,
    }));
  };

  const [dataTotal, setDataTotal] = useState({
    total_before: 0,
    total_income: 0,
    total_expense: 0,
  });

  const fetchTransactionLists = async () => {
    try {
      const response = await getTotalReport(
        dataPage.page,
        dataPage.size,
        dataFilter
      );
      setListTransactions(response.data.pagination.data);
      setDataPage((prevPage) => ({
        ...prevPage,
        totalPage: response.data.pagination.total_page,
        totalItem: response.data.pagination.total_items,
      }));
      setDataTotal({
        total_before: response.data.total_before,
        total_income: response.data.total_income,
        total_expense: response.data.total_expense,
      });
    } catch (error) {
      console.error("Failed to fetch transactions:", error);
    }
  };

  // Call fetchTransactionLists inside useEffect
  useEffect(() => {
    fetchTransactionLists();    
  }, [dataFilter, dataPage.page, dataPage.size, colsToRender]);

  return (
    <>
      <Header title={"Sổ quỹ"} />
      <div className="right__listPage">
        <div className="right__toolbar">
          <div className="btn-toolbar">
            <button ref={btnOpenPopupRef} onClick={() => setIsSelectPopup(true)} className="btn btn-base btn-filter">
              <span className="btn__label">
                Ngày tạo
                <span className="btn__icon">
                    <FontAwesomeIcon icon={faCaretDown} />
                </span>
              </span>
            </button>
            {
              isSelectPopup &&
              <ListSelectPopup 
                dataList={[
                  {name: "Ngày tạo"},
                  {name: "Ngày cập nhật"}
                ]}
                btnRef={btnOpenPopupRef}
                closePopup={() => setIsSelectPopup(false)}
              />
            }
            <SelectDatePopup 
              data={{
                date_from: dataFilter.date_from,
                date_to: dataFilter.date_to
              }}
              setDataFilters={(data) => setDataFilter(prev => {
                return {
                  ...prev,
                  date_from: data.date_from,
                  date_to: data.date_to
                };
              })}
            />
          </div>
        </div>
        <div className={s["box-statistics"]}>
          <div className={s["box-content"]}>
            <div className={s["title"]}>
              <p>Số dư đầu kì</p>  
            </div>                
            <div className={s["value"]}>
              <p>{dataTotal.total_before}</p>
            </div>
          </div>
          <div className={s["box-function"]}>
            <p>+</p>
          </div>
          <div className={s["box-content"]}>
            <div className={s["title"]}>
              <p>Tổng thu</p>  
            </div>              
            <div className={cn(s["value"], s["receipt"])}>
              <p>{dataTotal.total_income}</p>
            </div>
          </div>
          <div className={s["box-function"]}>
            <p>-</p>
          </div>
          <div className={s["box-content"]}>
            <div className={s["title"]}>
              <p>Tổng chi</p>  
            </div>              
            <div className={cn(s["value"], s["payment"])}>
              <p>{dataTotal.total_expense}</p>
            </div>
          </div>
          <div className={s["box-function"]}>
            <p>=</p>
          </div>
          <div className={s["box-content"]}>
            <div className={s["title"]}>
              <p>Tồn cuối kì</p>  
            </div>              
            <div className={cn(s["value"], s["result"])}>
              <p>{Number(dataTotal.total_before) + Number(dataTotal.total_income) - Number(dataTotal.total_expense)}</p>
            </div>
          </div>
        </div>
        <div className="right__table">
          <div className="right__table-scroller">
            <div className="box-scroller">
              <div className="group-scroller-btns">
                {modesTab.map(({ key, label, modes }) => (
                  <button
                    key={key}
                    className={`btn-scroller ${modeActive === key ? "active" : ""
                      }`}
                    onClick={() => handleTabClick(key, modes)}
                  >
                    {label}
                  </button>
                ))}
              </div>
            </div>
          </div>
          <div className="right__table-search-filter">
            <div className="box-search-filter-btns">
              <div className="box-search">
                <div className="box-input">
                  <div className="search-icon">
                    <FontAwesomeIcon icon={faMagnifyingGlass} />
                  </div>
                  <input
                    placeholder="Tìm theo mã phiếu"
                    type="text"
                    name="search"
                    id=""
                    autoComplete="on"
                    onChange={(e) =>
                      setDataFilter((prev) => {
                        return {
                          ...prev,
                          keyword: e.target.value,
                        };
                      })
                    }
                  />
                  <fieldset className="input-field" />
                </div>
              </div>
              <div className="btn-group group-filter-btns">
                <button className="btn btn-base btn-filter">
                  <span className="btn__label">
                    Người tạo
                    <span className="btn__icon">
                      <FontAwesomeIcon icon={faCaretDown} />
                    </span>
                  </span>
                </button>
                <button className="btn btn-base btn-filter">
                  <span className="btn__label">
                    Hình thức thanh toán
                    <span className="btn__icon">
                      <FontAwesomeIcon icon={faCaretDown} />
                    </span>
                  </span>
                </button>
              </div>
            </div>
          </div>
          <div
            ref={headersRef}
            onScroll={(e) => handleScroll(e, contentRef.current)}
            className="right__table-headers"
          >
            <table className="box-table-headers">
              <colgroup>
                <col style={{ width: "150px" }} />
                <col style={{ width: "200px" }} />
                <col style={{ width: "150px" }} />
                <col style={{ width: "150px" }} />
                <col style={{ width: "150px" }} />
                <col style={{ width: "120px" }} />
                <col style={{ width: "180px" }} />
                <col style={{ width: "250px" }} />
                <col style={{ width: "180px" }} />
                <col style={{ width: "150px" }} />
              </colgroup>
              <thead>
                <tr className="group-table-headers">
                  <th
                    colSpan={1}
                    rowSpan={1}
                    className={cn("table-header-item", "text-center")}
                  >
                    Mã phiếu
                  </th>
                  <th
                    colSpan={1}
                    rowSpan={1}
                    className={cn("table-header-item", "text-start")}
                  >
                    Loại phiếu
                  </th>
                  <th
                    colSpan={1}
                    rowSpan={1}
                    className={cn("table-header-item", "text-end")}
                  >
                    Tiền thu
                  </th>
                  <th
                    colSpan={1}
                    rowSpan={1}
                    className={cn("table-header-item", "text-end")}
                  >
                    Tiền chi
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
                    Mã chứng từ
                  </th>
                  <th
                    colSpan={1}
                    rowSpan={1}
                    className={cn("table-header-item", "text-start")}
                  >
                    Hình thức thanh toán
                  </th>
                  <th
                    colSpan={1}
                    rowSpan={1}
                    className={cn("table-header-item", "text-start")}
                  >
                    Mô tả
                  </th>
                  <th
                    colSpan={1}
                    rowSpan={1}
                    className={cn("table-header-item", "text-start")}
                  >
                    Người nộp/nhận
                  </th>
                  <th
                    colSpan={1}
                    rowSpan={1}
                    className={cn("table-header-item", "text-center")}
                  >
                    Ngày cập nhật
                  </th>
                </tr>
              </thead>
            </table>
          </div>
          <div className="right__table-content">
            <div className="right__table-data">
              <div
                className="table-data__container"
                ref={contentRef}
                onScroll={(e) => handleScroll(e, headersRef.current)}
              >
                <table className="box-table-data">
                  <colgroup>
                    <col style={{ width: "150px" }} />
                    <col style={{ width: "200px" }} />
                    <col style={{ width: "150px" }} />
                    <col style={{ width: "150px" }} />
                    <col style={{ width: "150px" }} />
                    <col style={{ width: "120px" }} />
                    <col style={{ width: "180px" }} />
                    <col style={{ width: "250px" }} />
                    <col style={{ width: "180px" }} />
                    <col style={{ width: "150px" }} />
                  </colgroup>
                  <tbody>
                    {listTransactions.map((transaction, index) => (
                      <tr key={index} className="table-data-row">
                        <td className="table-data-item text-center">
                          {transaction.sub_id}
                        </td>
                        <td className="table-data-item text-start">
                          {transaction.category_name}
                        </td>
                        <td className="table-data-item text-end">
                          {transaction.type === "INCOME" ? transaction.amount : "-----"}
                        </td>
                        <td className="table-data-item text-end">
                          {transaction.type === "EXPENSE" ? transaction.amount : "-----"}
                        </td>
                        <td className="table-data-item text-center">
                          {formatDateTime(transaction.created_at)}
                        </td>
                        <td className="table-data-item text-start">
                          <p className='box-text'>
                            <a className='box-id' onClick={() => navigate(`/admin/grns/GRN/${transaction.reference_id}`)}>{transaction.reference_id}</a>
                          </p>
                        </td>
                        <td className="table-data-item text-start">
                          {paymentMethods[transaction.payment_method]}
                        </td>
                        <td className="table-data-item text-start">
                          {transaction.note}
                        </td>
                        <td className="table-data-item text-start">
                          {transaction.recipient_name}
                        </td>
                        <td className="table-data-item text-center">
                          {formatDateTime(transaction.updated_at)}
                        </td>
                      </tr>
                    ))}
                  </tbody>
                </table>
              </div>
            </div>
          </div>
        </div>
      </div>
    </>
  );
};

export default Reports;
