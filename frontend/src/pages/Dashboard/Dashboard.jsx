import React, { useEffect } from 'react'

import Header from '../../components/Header/Header'

import s from './Dashboard.module.scss'
import { Link } from 'react-router-dom'
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome'
import { faBox, faClipboardList, faSackDollar, faWallet } from '@fortawesome/free-solid-svg-icons'
import cn from 'classnames'
import { getDashboard } from '../../service/UserAPI'

const Dashboard = () => {
    const [data, setData] = React.useState([])

    const fetchDashboard = async () => {
        try {
            const response = await getDashboard();
            setData(response.data);
        } catch (error) {
            console.error(error);
            throw error;
        }
    }

    useEffect(() => {
        fetchDashboard();
    }, [])

    return (
        <>
            <Header title={"Tổng quan"} />
            <div className={s["container"]}>
                <div className={s["wrapper"]}>
                    <div className={s["box-statistics"]}>
                        <div className={s["box-title"]}>
                            <h6>Thống kê số liệu</h6>
                        </div>
                        <div className={s["group-statistics"]}>
                            <div className={cn(s["statistics-item"], s["reports-statistics"])}>
                                <Link to='/admin/reports'>
                                    <div className={s["statistics-icon"]}>
                                        <FontAwesomeIcon icon={faSackDollar} />
                                    </div>
                                    <div className={s["statistics-content"]}>
                                        <h6>Tiền vốn cửa hàng</h6>
                                        <h5>{(Number(data.total_income) || 0) - (Number(data.total_expense) || 0)}</h5>
                                    </div>
                                </Link>
                            </div>
                            <div className={cn(s["statistics-item"], s["orders-statistics"])}>
                                <Link to='/admin/order_suppliers'>
                                    <div className={s["statistics-icon"]}>
                                        <FontAwesomeIcon icon={faBox} />
                                    </div>
                                    <div className={s["statistics-content"]}>
                                        <h6>Đơn hàng đặt chờ nhập</h6>
                                        <h5>{data.count_order || 0}</h5>
                                    </div>
                                </Link>
                            </div>
                            <div className={cn(s["statistics-item"], s["grns-statistics"])}>
                                <Link to='/admin/grns'>
                                    <div className={s["statistics-icon"]}>
                                        <FontAwesomeIcon icon={faWallet} />
                                    </div>
                                    <div className={s["statistics-content"]}>
                                        <h6>Đơn hàng nhập chưa thanh toán</h6>
                                        <h5>{data.count_grn || 0}</h5>
                                    </div>
                                </Link>
                            </div>
                            <div className={cn(s["statistics-item"], s["gins-statistics"])}>
                                <Link to='/admin/gins'>
                                    <div className={s["statistics-icon"]}>
                                        <FontAwesomeIcon icon={faClipboardList} />
                                    </div>
                                    <div className={s["statistics-content"]}>
                                        <h6>Đơn kiểm chưa hoàn thành</h6>
                                        <h5>{data.count_gin || 0}</h5>
                                    </div>
                                </Link>
                            </div>
                        </div>
                    </div>
                    <div className={s["box-warehouse"]}>
                        <div className={s["box-title"]}>
                            <h6>Thông tin kho</h6>
                        </div>
                        <div className={s["group-info"]}>
                            <div className={s["info-item"]}>
                                <p className="info-title">Sản phẩm đang giao dịch</p>
                                <h5 className="info-value">{data.count_product || 0}</h5>
                            </div>
                            <div className={s["info-item"]}>
                                <p className="info-title">Số lượng tồn kho</p>
                                <h5 className="info-value">{data.sum_quantity || 0}</h5>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </>
    )
}

export default Dashboard