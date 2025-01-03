import React, { useState } from 'react'
import { Link, useNavigate } from 'react-router-dom'
import { Button, Col, Container, FormGroup, FormText, Input, Row } from 'reactstrap'

import Widget from '../../components/Widget/Widget'

// Import Icons
import sapoLogo from '../../assets/logo-sapo.webp'
import registerImage from '../../assets/register-image.svg'
import GoogleIcon from '../../assets/icons/GoogleIcon'
import TwitterIcon from '../../assets/icons/TwitterIcon'
import FacebookIcon from '../../assets/icons/FacebookIcon'
import GithubIcon from '../../assets/icons/GithubIcon'
import LinkedinIcon from '../../assets/icons/LinkedinIcon'
import { registerAccount } from '../../service/UserAPI'
import { toast } from 'react-toastify'
import Notification from '../../components/Notification/Notification'

const Register = () => {
    const [formData, setFormData] = useState({
        full_name: "",
        phone: "",
        password: "",
        role: "ADMIN",
        email: ""
    })
    const navigate = useNavigate();

    const doRegister = async (e) => {
        e.preventDefault();
        const response = await registerAccount(
            formData.full_name, 
            formData.phone, 
            formData.password,
            formData.role,
            formData.email
        );
        if (response.status_code === 201) {
            toast(<Notification 
                    type={"success"} 
                    withIcon 
                    message={"Bạn đã đăng ký thành công"} 
                />,
                {
                    autoClose: 4000,
                    closeButton: false,
                    hideProgressBar: true,
                }
            )
            navigate('/login');
        }
    }    

    const handleChange = (e) => {
        setFormData({...formData, [e.target.name]: e.target.value})
    }

  return (
    <div className="auth-page">
        <Container className='col-12 h-100'>
            <Row className='d-flex align-items-center h-100'>
                <Col xs={12} lg={6} className="left-column h-100">
                    <Widget className="widget-auth widget-p-lg">
                        <div className="d-flex align-items-center justify-content-between py-3">
                            <p className="auth-header mb-0">Đăng ký</p>
                            <div className="logo-block">
                                <img src={sapoLogo} alt="" />
                            </div>
                        </div>
                        <form onSubmit={(e) => doRegister(e)}>
                            <FormGroup className="my-3">
                                <FormText>Họ và tên</FormText>
                                <Input
                                    id="full_name"
                                    className="input-transparent pl-3 mt-2"
                                    value={formData.full_name}
                                    onChange={(e) => handleChange(e)}
                                    type="text"
                                    required
                                    name="full_name"
                                    placeholder="Nhập họ và tên"
                                />
                            </FormGroup>
                            <FormGroup className="my-3">
                                <FormText>Số điện thoại</FormText>
                                <Input
                                    id="phone"
                                    className="input-transparent pl-3 mt-2"
                                    value={formData.phone}
                                    onChange={(e) => handleChange(e)}
                                    type="tel"
                                    required
                                    name="phone"
                                    placeholder="Nhập số điện thoại"
                                />
                            </FormGroup>
                            <FormGroup className="my-3">
                                <FormText>Gmail</FormText>
                                <Input
                                    id="email"
                                    className="input-transparent pl-3 mt-2"
                                    value={formData.email}
                                    onChange={(e) => handleChange(e)}
                                    type="text"
                                    required
                                    name="email"
                                    placeholder="Nhập gmail"
                                />
                            </FormGroup>
                            <FormGroup className="my-3">
                                <div className="d-flex justify-content-between">
                                    <FormText>Mật khẩu</FormText>
                                </div>
                                <Input
                                    id="password"
                                    className="input-transparent pl-3 mt-2"
                                    value={formData.password}
                                    onChange={(e) => handleChange(e)}
                                    type="password"
                                    required
                                    name="password"
                                    placeholder="Nhập mật khẩu"
                                />
                            </FormGroup>
                            <div className="bg-widget d-flex justify-content-center">
                                <Button className="rounded-pill my-3" type="submit" color="secondary-red">Đăng ký</Button>
                            </div>
                            <p className="dividing-line my-3">&#8195;Or&#8195;</p>
                            <div className="d-flex align-items-center my-3">
                                <p className="social-label mb-0">Đăng nhập với</p>
                                <div className="socials">
                                    <a><GoogleIcon /></a>
                                    <a><TwitterIcon /></a>
                                    <a><FacebookIcon /></a>
                                    <a><GithubIcon /></a>
                                    <a><LinkedinIcon /></a>
                                </div>
                            </div>
                            Đã có tài khoản? <Link to="/login">Đăng nhập tại đây</Link>
                        </form>
                    </Widget>
                </Col>
                <Col xs={0} lg={6} className="right-column">
                    <div>
                        <img src={registerImage} />
                    </div>
                </Col>
            </Row>
        </Container>
    </div>
  )
}

export default Register