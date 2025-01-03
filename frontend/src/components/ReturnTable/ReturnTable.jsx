import React, { useEffect, useState } from 'react'
import ReturnTableItem from '../ReturnTableItem/ReturnTableItem'
import { getListByGRN } from '../../service/RefundInformationAPI'

const ReturnTable = ({grnId}) => {
    const [returnList, setReturnList] = useState([]);

    const fetchData = async () => {
        const responseAPI = await getListByGRN(grnId);
        setReturnList(responseAPI.data);
    }

    useEffect(() => {
        fetchData();
    }, [grnId])

    return (
        <>
            <div className="right__table-headers">
                <table className="box-table-headers">
                    <colgroup>
                        <col style={{ width: "50px" }} />
                        <col style={{ width: "150px" }} />
                        <col style={{ width: "150px" }} />
                        <col style={{ width: "150px" }} />
                        <col style={{ width: "150px" }} />
                        <col style={{ width: "155px" }} />
                        <col style={{ width: "200px" }} />
                    </colgroup>
                    <thead>
                        <tr className="group-table-headers">
                            <th rowSpan={1} className='table-icon'></th>
                            <th colSpan={1} rowSpan={1} className="table-header-item text-start">Ngày tạo</th>
                            <th colSpan={1} rowSpan={1} className="table-header-item text-center">SL hoàn</th>
                            <th colSpan={1} rowSpan={1} className="table-header-item text-end">Giá trị hoàn trả</th>
                            <th colSpan={1} rowSpan={1} className="table-header-item text-start">Trạng thái hoàn</th>
                            <th colSpan={1} rowSpan={1} className="table-header-item text-start">Hoàn tiền</th>
                            <th colSpan={1} rowSpan={1} className="table-header-item text-start">Nhân viên tạo</th>
                        </tr>
                    </thead>
                </table>
            </div>
            <div className="right__table-content">
                <div className="right__table-data">
                    <div className="table-data__container">
                        <table className="box-table-data">
                            <colgroup>
                                <col style={{ width: "50px" }} />
                                <col style={{ width: "150px" }} />
                                <col style={{ width: "150px" }} />
                                <col style={{ width: "150px" }} />
                                <col style={{ width: "150px" }} />
                                <col style={{ width: "155px" }} />
                                <col style={{ width: "200px" }} />
                            </colgroup>
                            <tbody>
                                {
                                    returnList?.map((returnItem, index) => (
                                        <ReturnTableItem key={index} returnItem={returnItem} />
                                    ))
                                }
                            </tbody>
                        </table>
                    </div>
                </div>
            </div>
        </>
    )
}

export default ReturnTable