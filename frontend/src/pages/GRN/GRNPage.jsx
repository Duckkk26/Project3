import React from 'react'
import { Route, Routes } from 'react-router-dom'
import GRNList from '../../components/GRNList/GRNList'
import CreateGRN from '../../components/CreateGRN/CreateGRN'
import GRNDetails from '../../components/GRNDetails/GRNDetails'
import CreateReturn from '../../components/CreateReturn/CreateReturn'


const GRNPage = () => {
  return (
    <>
      <Routes>
        <Route path='/' element={<GRNList />} />
        <Route path='/create' element={<CreateGRN />} />
        <Route path='/GRN/:grnId' element={<GRNDetails />} />
        <Route path='/returns/create' element={<CreateReturn />} />
      </Routes>
    </>
  )
}

export default GRNPage