import React, { useState } from "react";
import { Alert } from "reactstrap";

import infoIcon from "../../assets/notificationsIcons/infoIcon.svg";
import warningIcon from "../../assets/notificationsIcons/warningIcon.svg";
import successIcon from "../../assets/notificationsIcons/successIcon.svg";
import dangerIcon from "../../assets/notificationsIcons/dangerIcon.svg";

import s from "./Notification.module.scss";

const typesIcons = {
  info: infoIcon,
  warning: warningIcon,
  success: successIcon,
  error: dangerIcon,
}

const colors = {
  info: "#00A5FF",
  warning: "#FFA100",
  success: "#43BC13",
  error: "#FF4B23",
}

const Notification = ({ type, withIcon, message }) => {

  const [notificationOpen, setNotificationClose] = useState(true)

  const icon = getIconByType(type)

  const closeNotification = () => {
    setNotificationClose(!notificationOpen)
  }

  return (
    <>
      <Alert
        className={s.notificationContainer}
        style={{backgroundColor: colors[type]}}
        isOpen={notificationOpen}
        toggle={() => closeNotification()}
      >
        <button type="button" className="btn-close btn-close-white" aria-label="Close" onClick={() => closeNotification()}></button>
        <div className={s.notificationIconContainer}>
          {withIcon && <img src={icon} alt="..."/>}
        </div>
        <div className={s.messageContainer}>
          <span>{message}</span>
        </div>
      </Alert>
    </>
  )
};

function getIconByType(type = "info") {
  return typesIcons[type];
}

export default Notification