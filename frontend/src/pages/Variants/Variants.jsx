import React from 'react'
import { Route, Routes } from 'react-router-dom'
import VariantsList from '../../components/VariantsList/VariantsList'
const Variants = () => {
  return (
    <>
      <Routes>
        <Route path='/' element = {<VariantsList />}/>
      </Routes>
    </>
  )
}

export default Variants