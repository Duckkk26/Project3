import React from 'react'
import { Route, Routes } from 'react-router-dom'
import SupplierList from '../../components/SupplierList/SupplierList'
import CreateSupplier from '../../components/CreateSupplier/CreateSupplier'
import SupplierDetails from '../../components/SupplierDetails/SupplierDetails'

const Suppliers = () => {
  return (
    <>
      <Routes>
        <Route path='/' element={<SupplierList />} />
        <Route path='/create' element={<CreateSupplier />} />
        <Route path='/create' element={<CreateSupplier />} />
        <Route path='/SUP/:supplierId' element={<SupplierDetails />} />
      </Routes>
    </>
  )
}

export default Suppliers