import s from './CreateSupplierPopup.module.scss'
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome'
import { faCaretDown, faCaretUp } from '@fortawesome/free-solid-svg-icons'
import { useRef, useState, useEffect } from 'react'
import { getListCategory } from '../../service/CategoryAPI'
import { quickCreateProduct } from '../../service/ProductAPI'
import { getListSupplierGroups } from '../../service/SupplierGroupsAPI'
import SelectListSupplierGroupsPopup from './SelectListGroup/SelectListGroup'
import { createSupplier } from '../../service/SuppliersAPI'
import { toast } from 'react-toastify'
import Notification from '../Notification/Notification'

const CreateSupplierPopup = ({ handleCLickBack, setSupplerID }) => {


    const [isSelectSupplierGroupList, setIsSelectSupplierGroupList] = useState(false);

    const selectSupplierGroupRef = useRef(null);

    const [supplierGroupsList, setSupplierGroupsList] = useState([]);
    const [dataPageSupplierGroup, setDataPageSupplierGroup] = useState({
        currentPage: 1,
        totalPage: 1,
        currentSize: 7,
    });
    const [supplierGroupDataFilter, setsupplierGroupDataFilter] = useState({
        keyword: null,
        status: "ACTIVE",
    });
    const fetchSupplierGroupsList = async () => {
        const response = await getListSupplierGroups(dataPageSupplierGroup.currentPage, dataPageSupplierGroup.currentSize, supplierGroupDataFilter);
        setSupplierGroupsList(response.data.data);
        setDataPageSupplierGroup(prevState => {
            return {
                ...prevState,
                totalPage: response.data.total_page
            }
        });
    }
    const fetchMoreSuppliersGroup = async () => {
        if (dataPageSupplierGroup.currentPage < dataPageSupplierGroup.totalPage) {
            const response = await getListSupplierGroups(dataPageSupplierGroup.currentPage + 1, dataPageSupplierGroup.currentSize, supplierGroupDataFilter);
            setSupplierGroupsList(prevList => [...prevList, ...response.data.data]);
            setDataPageSupplierGroup(prevState => {
                return {
                    ...prevState,
                    currentPage: prevState.currentPage + 1,
                    totalPage: response.data.total_page
                }
            });
        }
    }
    const handleFetchMoreSuppliersGroup = () => {
        if (isSelectSupplierGroupList) {
            fetchSupplierGroupsList();
        } else {
            setsupplierGroupDataFilter((prev) => {
                return (
                    {
                        ...prev,
                        keyword: "",
                        status: "ACTIVE"
                    }
                )
            })
            setDataPageSupplierGroup(prevState => {
                return {
                    ...prevState,
                    currentPage: 1,
                    totalPage: 1
                }
            });
        }
    }
    useEffect(() => {
        handleFetchMoreSuppliersGroup();
    }, [isSelectSupplierGroupList])
    useEffect(() => {
        setDataPageSupplierGroup(prevState => {
            return {
                ...prevState,
                currentPage: 1
            }
        });
        handleFetchMoreSuppliersGroup();
    }, [supplierGroupDataFilter.keyword])

    const [dataBody, setDataBody] = useState({
        sub_id: null,
        name: "",
        phone: "",
        email: "",
        address: "",
        supplier_group_id: null,
        tags: null,
        note: null
    });

    const handleCreateSuppler = async () => {
        const responseAPI = await createSupplier(dataBody);
        if (responseAPI.status_code === 201) {
            setSupplerID(responseAPI.data);
            toast(<Notification 
                    type={"success"} 
                    withIcon 
                    message={"Thêm nhà cung cấp thành công"} 
                />,
                {
                    autoClose: 4000,
                    closeButton: false,
                    hideProgressBar: true,
                }
            )
            handleCLickBack();
        }
    }

    return (
        <div className={s["modal-content"]}>
            <div className={s["modal-header"]}>
                <div className={s["modal-box-header"]}>
                    <h5>Thêm mới nhà cung cấp</h5>
                </div>
            </div>

            <div className={s["modal-body"]}>
                <div className={s["row-content"]}>
                    <div className={s["content"]}>
                        <label htmlFor="name" className={s["form-label"]}>
                            Tên nhà cung cấp
                            <span className="asterisk-icon">*</span>
                        </label>
                        <div className={s["form-textfield"]}>
                            <input
                                type="text"
                                name="name"
                                id="name"
                                onChange={(e) => setDataBody((prev) => { return { ...prev, name: e.target.value } })}
                            />
                            <fieldset className="input-field"></fieldset>
                        </div>
                    </div>
                    <div className={s["content"]}>
                        <label htmlFor="name" className={s["form-label"]}>
                            Nhóm nhà cung cấp
                        </label>
                        <div className={s["box-select"]}>
                            <button id='category' ref={selectSupplierGroupRef} onClick={() => setIsSelectSupplierGroupList(!isSelectSupplierGroupList)}>
                                <span>{dataBody.supplier_group_id ? supplierGroupsList?.find(supplierGroup => supplierGroup.id === dataBody.supplier_group_id)?.name : "Chọn nhóm nhà cung cấp" }</span>
                                {isSelectSupplierGroupList ? <FontAwesomeIcon icon={faCaretUp} /> : <FontAwesomeIcon icon={faCaretDown} />}
                            </button>
                            {
                                isSelectSupplierGroupList && 
                                <SelectListSupplierGroupsPopup
                                    btnRef={selectSupplierGroupRef}
                                    title={"nhóm nhà cung cấp"}
                                    closePopup={() => setIsSelectSupplierGroupList(false)}
                                    dataList={supplierGroupsList}
                                    fetchMoreData={fetchMoreSuppliersGroup}
                                    handleSelect={id => setDataBody(prev => {return{...prev, supplier_group_id: id}})}
                                />
                            }
                        </div>
                    </div>
                </div>
                <div className={s["row-content"]}>
                    <div className={s["content"]}>
                        <label htmlFor="phone_number" className={s["form-label"]}>
                            số điện thoại
                        </label>
                        <div className={s["form-textfield"]}>
                            <input
                                type="text"
                                name="phone_number"
                                id="phone_number"
                                onChange={(e) => setDataBody((prev) => { return { ...prev, phone: e.target.value } })}
                            />
                            <fieldset className="input-field"></fieldset>
                        </div>
                    </div>
                    <div className={s["content"]}>
                        <label htmlFor="email" className={s["form-label"]}>
                            email
                        </label>
                        <div className={s["form-textfield"]}>
                            <input
                                type="email"
                                name="email"
                                id="email"
                                onChange={(e) => setDataBody((prev) => { return { ...prev, email: e.target.value } })}
                            />
                            <fieldset className="input-field"></fieldset>
                        </div>
                    </div>
                </div>
                <div className={s["row-name"]}>
                    <label htmlFor="address" className={s["form-label"]}>
                        Địa chỉ
                    </label>
                    <div className={s["form-textfield"]}>
                        <input
                            type="text"
                            name="address"
                            id="address"
                            placeholder='Nhập địa chỉ nhà cung cấp'
                            onChange={(e) => setDataBody((prev) => { return { ...prev, address: e.target.value } })}
                        />
                        <fieldset className="input-field"></fieldset>
                    </div>
                </div>

            </div>
            <div className={s["modal-footer"]}>
                <button className="btn btn-outline-primary" onClick={handleCLickBack} >
                    <span className="btn__title">Thoát</span>
                </button>
                <button className="btn btn-primary" onClick={handleCreateSuppler}>
                    <span className="btn__title">Thêm</span>
                </button>
            </div>
        </div>
    )
}
export default CreateSupplierPopup;