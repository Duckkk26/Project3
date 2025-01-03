import React from 'react'
import { Route, Routes } from 'react-router-dom'
import PriceAdjustmentsList from '../../components/PriceAdjustmentsList/PriceAdjustments'


const PriceAdjustments = () => {
  return (
    <>
      <Routes>
        <Route path='/' element={<PriceAdjustmentsList />} />
      </Routes>
    </>
  )
}

export default PriceAdjustments