import React, { useEffect, useState, useMemo } from 'react'
import { Link, useParams, useNavigate } from 'react-router-dom'
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome'
import { faChevronLeft, faCopy } from '@fortawesome/free-solid-svg-icons'
import cn from 'classnames'
import s from './SupplierDetails.module.scss'
import { getSupplier } from '../../service/SuppliersAPI'

const SupplierDetails = () =>{
      const navigate = useNavigate();

      const { supplierId } = useParams();

      const [dataDetail, setDataDetail] = useState({});

      const fetchSupplierDetails = async () =>{
            const response = await getSupplier(supplierId);
            setDataDetail(response.data);
      }

      useEffect(() => {
            fetchSupplierDetails();
        }, [])
      
      return(
            <>
                  <div className="right__navbar">
                        <div className="box-navbar">
                              <div className="btn-toolbar">
                                    <Link to='/admin/suppliers' className='btn-back'>
                                          <FontAwesomeIcon icon={faChevronLeft} />
                                          <h6 className="btn-back__title">
                                                Quay lại danh sách nhà cung cấp
                                          </h6>
                                    </Link>
                              </div>
                        </div>
                  </div>
                  <div className="right__paperPage">
                        <div className="right__paperPage-wrapper">
                              <div className={cn("right__paperPage-container", s.container)}>
                                    <div className="box-title">
                                          <div className="group-details">
                                                <h4 className='box-code'>{dataDetail.name}</h4>
                                          </div>
                                    </div>
                                    <div className="box-paper">
                                          <div className="paper-header">
                                                <p>Thông tin nhà cung cấp</p>
                                          </div>
                                          <div className={cn("paper-content", s.gridContainer)}>
                                                <div className="group-info">
                                                      <div className="info-item">
                                                            <p className="info-title">Mã</p>
                                                            <p className="info-value">
                                                                  :&nbsp;
                                                                  {dataDetail.id || '-----'}
                                                            </p>
                                                      </div>
                                                      <div className="info-item">
                                                            <p className="info-title">Số điện thoại</p>
                                                            <p className="info-value">
                                                                  :&nbsp;
                                                                  {dataDetail.phone || '-----'}
                                                            </p>
                                                      </div>
                                                      <div className="info-item">
                                                            <p className="info-title">Địa chỉ</p>
                                                            <p className="info-value">
                                                                  :&nbsp;
                                                                  {dataDetail.address || '-----'}
                                                            </p>
                                                      </div>
                                                </div>
                                                <div className="group-info">
                                                      <div className="info-item">
                                                            <p className="info-title">Công nợ</p>
                                                            <p className="info-value">
                                                                  :&nbsp;
                                                                  {dataDetail.current_debt || '-----'}
                                                            </p>
                                                      </div>
                                                      <div className="info-item">
                                                            <p className="info-title">Tổng giá trị nhập</p>
                                                            <p className="info-value">
                                                                  :&nbsp;
                                                                  {dataDetail.grn_total_value || '-----'}
                                                            </p>
                                                      </div>
                                                      <div className="info-item">
                                                            <p className="info-title">Trả hàng</p>
                                                            <p className="info-value">
                                                                  :&nbsp;
                                                                  {dataDetail.total_refund || '-----'}
                                                            </p>
                                                      </div>
                                                </div>
                                                <div className="group-info">
                                                      <div className="info-item">
                                                            <p className="info-title">Tổng đơn nhập</p>
                                                            <p className="info-value">
                                                                  :&nbsp;
                                                                  {dataDetail.grn_count || '-----'}
                                                            </p>
                                                      </div>
                                                </div>
                                          </div>
                                    </div>
                              </div>
                        </div>
                  </div>
            </>
      )
}

export default SupplierDetails;