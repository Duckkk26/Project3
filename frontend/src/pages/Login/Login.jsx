import React, { useState, useEffect } from 'react'
import { Link, useNavigate } from 'react-router-dom'
import { login } from '../../actions/auth'
import { useDispatch, useSelector } from 'react-redux'

import { Button, Col, Container, FormGroup, FormText, Input, Row } from 'reactstrap'
import Widget from '../../components/Widget/Widget'

// Import Icons
import logo from '../../assets/logo.png'
import loginImage from '../../assets/login-image.svg'
import GoogleIcon from '../../assets/icons/GoogleIcon'
import TwitterIcon from '../../assets/icons/TwitterIcon'
import FacebookIcon from '../../assets/icons/FacebookIcon'
import GithubIcon from '../../assets/icons/GithubIcon'
import LinkedinIcon from '../../assets/icons/LinkedinIcon'

const Login = () => {
  const [formData, setFormData] = useState({
    phone: "",
    password: ""
  })
  const dispatch = useDispatch()
  const navigate = useNavigate()

  // Truy cập trạng thái xác thực từ Redux
  const isAuthenticated = useSelector((state) => state.auth.isAuthenticated);

  const handleChange = (e) => {
    setFormData({...formData, [e.target.name]: e.target.value})
  }

  const doLogin = (e) => {
    e.preventDefault();
    dispatch(login(formData.phone, formData.password));
  }

  // Sử dụng useEffect để điều hướng sau khi đăng nhập thành công
  useEffect(() => {
    if (isAuthenticated) {
      navigate('/');
    }
  }, [isAuthenticated, navigate]);

  return (
    <div className="auth-page">
      <Container className='col-12 h-100'>
        <Row className='d-flex align-items-center h-100'>
          <Col xs={12} lg={6} className="left-column h-100">
            <Widget className="widget-auth widget-p-lg">
              <div className="d-flex align-items-center justify-content-between py-3">
                <p className="auth-header mb-0">Đăng nhập</p>
                <div className="logo-block">
                  <img src={logo} alt="" />
                </div>
              </div>
              <form onSubmit={(e) => doLogin(e)}>
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
                  <Button className="rounded-pill my-3" type="submit" color="secondary-red">Đăng nhập</Button>
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
                Không có tài khoản? <Link to="/register">Đăng ký tại đây</Link>
              </form>
            </Widget>
          </Col>
          <Col xs={0} lg={6} className="right-column">
            <div>
              <img src={loginImage} />
            </div>
          </Col>
        </Row>
      </Container>
    </div>
  )
}

export default Login