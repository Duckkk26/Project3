import React from 'react'
import { useNavigate } from 'react-router-dom'

import { Button } from 'reactstrap'

import s from './UnauthorizedPage.module.scss'

import errorImage from '../../assets/error-image.svg'

const UnauthorizedPage = () => {
    const navigate = useNavigate();

    return (
        <div className={s.container}>
            <div className={s.wrapper}>
                <div className={s.errorImage}>
                    <img src={errorImage} alt="" />
                </div>
                <h6 className={s.errorInfo}>Bạn không có quyền để vào trang này</h6>
                <p className={s.errorHelp}>Bạn chưa được cấp quyền nên không thể vào được trang này. Vui lòng quay lại trang chủ và liên hệ với admin để được cấp quyền nếu cần thiết.</p>
                <div className={s.boxBtn}>
                    <Button
                    onClick={() => navigate('/')}
                        className={`${s.errorBtn} rounded-pill`}
                        type="submit"
                        color="secondary-red"
                    >
                        Back to Home
                    </Button>
                </div>
            </div>
        </div>
    )
}

export default UnauthorizedPage