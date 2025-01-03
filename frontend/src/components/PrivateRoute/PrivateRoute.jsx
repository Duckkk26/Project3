import React, { useEffect } from 'react'
import { Navigate, Outlet, useNavigate } from 'react-router-dom';
import { logout } from '../../actions/auth';

const PrivateRoute = ({ isAuthenticated, dispatch }) => {
  useEffect(() => {
    if (!isAuthenticated) {
      dispatch(logout())
    }
  }, [isAuthenticated, dispatch]);

  if (!isAuthenticated) {
    return <Navigate to='/login' />
  } else {
    return <Outlet />
  }
}

export default PrivateRoute