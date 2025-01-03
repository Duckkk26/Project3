import React, { useEffect, useState } from 'react'
import { Link } from 'react-router-dom'

// Import CSS
import s from './SupplierInfo.module.scss'

// Import Icons
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome'
import { faXmark } from '@fortawesome/free-solid-svg-icons'
import classNames from 'classnames'
import { getSupplier } from '../../service/SuppliersAPI'

const SupplierInfo = ({ supplierId, handleCancel }) => {
  const [supplier, setSupplier] = useState({});

  const fetchSupplier = async () => {
    const response = await getSupplier(supplierId);
    setSupplier(response.data);
  }

  useEffect(() => {
    fetchSupplier();
  }, [supplierId]);
  

  return (
    <div className={s.container}>
      <div className={s.boxInfo}>
        <div className={s.boxName}>
          <div className={s.supplierName}>
            <p>
              <Link>
                {supplier.name}
              </Link>
            </p>
          </div>
          {
            handleCancel &&
            <div className={s.cancelIcon}>
              <button onClick={handleCancel} className={s.btnCancel}>
                <span className={s.btnLabel}>
                  <FontAwesomeIcon icon={faXmark} />
                </span>
              </button>
            </div>
          }
        </div>
        {
          supplier.phone &&
          <div className={s.boxPhone}>
            <p>{supplier.phone}</p>
          </div>
        }
        {
          supplier.address &&
          <div className={s.boxAddress}>
            <p>
              <span className={s.addressTitle}>Địa chỉ:&nbsp;</span>
              {supplier.address}
            </p>
          </div>
        }
      </div>
      <div className={s.boxInfo}>
        <div className={s.boxReport}>
          <div className={s.reportItem}>
            <p className={s.reportTitle}>Nợ hiện tại</p>
            <p className={classNames(s.reportValue, s.danger)}>{supplier.current_debt}</p>
          </div>
          <div className={s.reportItem}>
            <p className={s.reportTitle}>Tổng đơn nhập ({supplier.grn_count})</p>
            <p className={s.reportValue}>{supplier.grn_total_value?.toLocaleString('en-US')}</p>
          </div>
          <div className={s.reportItem}>
            <p className={s.reportTitle}>Trả hàng</p>
            <p className={classNames(s.reportValue, s.danger)}>{supplier.total_refund}</p>
          </div>
        </div>
      </div>
    </div>
  )
}

export default SupplierInfo