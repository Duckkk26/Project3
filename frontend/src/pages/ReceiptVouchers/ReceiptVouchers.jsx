import React from 'react'
import { Route, Routes } from 'react-router-dom'
import ReceiptVouchersList from '../../components/ReceiptVouchersList/ReceiptVouchersList'
import CreateReceiptVoucher from '../../components/CreateReceiptVoucher/CreateReceiptVoucher'
import ReceiptGroups from '../../components/ReceipGroups/ReceiptGroups'

const ReceiptVouchers = () => {
  return (
    <>
        <Routes>
            <Route path='/' element={<ReceiptVouchersList />} />
            <Route path='/create' element={<CreateReceiptVoucher />} />
            <Route path='/groups' element={<ReceiptGroups />} />
        </Routes>
    </>
  )
}

export default ReceiptVouchers