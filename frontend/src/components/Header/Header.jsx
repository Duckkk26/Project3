import React, { useState } from 'react'
import { Dropdown, DropdownItem, DropdownMenu, DropdownToggle, Nav, Navbar, NavItem, NavLink } from 'reactstrap'
import classNames from 'classnames'
import { useDispatch } from 'react-redux'
import { logout } from '../../actions/auth'

import s from './Header.module.scss'

import { FontAwesomeIcon } from '@fortawesome/react-fontawesome'
import { faBell } from '@fortawesome/free-regular-svg-icons'
import basketIcon from '../../assets/icons/basket-icon.svg'
import calendarIcon from '../../assets/icons/calendar-icon.svg'
import envelopeIcon from '../../assets/icons/envelope-icon.svg'
import ProfileIcon from '../../assets/icons/ProfileIcon'
import TasksIcon from '../../assets/icons/TasksIcon'
import MessagesIcon from '../../assets/icons/MessagesIcon'
import logoutIcon from '../../assets/icons/logout-outline-icon.svg'

const Header = ({ title }) => {
  const [menuOpen, setMenuOpen] = useState(false);
  const [notificationsOpen, setNotificationsOpen] = useState(false);
  const fullName = localStorage.getItem('fullName');
  const dispatch = useDispatch();

  const toggleNotifications = () => {
    setNotificationsOpen(!notificationsOpen);
  }

  const toggleMenu = () => {
    setMenuOpen(!menuOpen);
  }

  const doLogout = () => {
    dispatch(logout());
  }

  return (
    <Navbar className={s.container}>
      <h3>{title}</h3>
      <Nav className="ms-auto">
        <Dropdown nav isOpen={notificationsOpen} toggle={() => toggleNotifications()} className="tutorial-dropdown me-2 me-sm-3">
          <DropdownToggle nav>
            <div className={s.navbarBlock}>
              <FontAwesomeIcon icon={faBell} />
              <div className={s.count}></div>
            </div>
          </DropdownToggle>
          <DropdownMenu end className="navbar-dropdown notifications-dropdown" style={{ width: "340px" }}>
            <DropdownItem>
              <img src={basketIcon} alt="Basket Icon"/>
              <span>12 new orders have arrived today</span>
            </DropdownItem>
            <DropdownItem>
              <img src={calendarIcon} alt="Calendar Icon"/>
              <span>1 event has been canceled and ...</span>
            </DropdownItem>
            <DropdownItem>
              <img src={envelopeIcon} alt="Envelope Icon"/>
              <span>you have 2 new messages</span>
            </DropdownItem>
          </DropdownMenu>
        </Dropdown>
        <Dropdown isOpen={menuOpen} toggle={() => toggleMenu()} nav id="basic-nav-dropdown" className="ms-3">
          <DropdownToggle nav caret className="navbar-dropdown-toggle">
            <span className={`${s.avatar} rounded-circle float-left me-2`}>
              {fullName?.[0] || ''}
            </span>
            <span className={classNames("d-none d-sm-block", s.fullName)}>{fullName || ''}</span>
          </DropdownToggle>
          <DropdownMenu className="navbar-dropdown profile-dropdown" style={{ width: "194px" }}>
            <DropdownItem className={s.dropdownProfileItem}>
              <ProfileIcon />
              <span>Profile</span>
            </DropdownItem>
            <DropdownItem className={s.dropdownProfileItem}>
              <TasksIcon />
              <span>Tasks</span>
            </DropdownItem>
            <DropdownItem className={s.dropdownProfileItem}>
              <MessagesIcon />
              <span>Messages</span>
            </DropdownItem>
            <NavItem>
              <NavLink onClick={() => doLogout()} href="#">
                <button className="btn btn-primary rounded-pill mx-auto logout-btn" type="submit">
                  <img src={logoutIcon} alt="Logout"/>
                  <span className="ms-1">Logout</span>
                </button>
              </NavLink>
            </NavItem>
          </DropdownMenu>
        </Dropdown>
      </Nav>
    </Navbar>
  )
}

export default Header