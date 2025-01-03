import React from 'react'
import { Route, Routes } from 'react-router-dom'
import PaymentVouchersList from '../../components/PaymentVouchersList/PaymentVouchersList'
import CreatePaymentVoucher from '../../components/CreatePaymentVoucher/CreatePaymentVoucher'
import PaymentGroups from '../../components/PaymentGroups/PaymentGroups'

const PaymentVouchers = () => {
  return (
    <>
      <Routes>
        <Route path='/' element={<PaymentVouchersList />} />
        <Route path='/create' element={<CreatePaymentVoucher />} />
        <Route path='/groups' element={<PaymentGroups />} />
      </Routes>
    </>
  )
}

export default PaymentVouchers