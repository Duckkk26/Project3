import React from 'react'
import { Route, Routes } from 'react-router-dom'

// Import Components
import OrdersList from '../../components/OrdersList/OrdersList'
import CreateOrder from '../../components/CreateOrder/CreateOrder'
import OrderDetails from '../../components/OrderDetails/OrderDetails'
import OrderDetailsUpdate from '../../components/OrderDetailsUpdate/OrderDetailsUpdate'


const OrderSuppliers = () => {
  return (
    <>
      <Routes>
        <Route path='/' element={<OrdersList />} />
        <Route path='/create' element={<CreateOrder />} />
        <Route path='/ORD/:orderId' element={<OrderDetails />} />
        <Route path='/ORD/:orderId/edit' element={<OrderDetailsUpdate />} />
      </Routes>
    </>
  )
}

export default OrderSuppliers