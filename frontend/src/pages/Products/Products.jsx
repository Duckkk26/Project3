import React from 'react'
import { Route, Routes } from 'react-router-dom'
import ProductsList from '../../components/ProductsList/ProducstList'
import CreateProduct from '../../components/CreateProduct/CreateProduct'
import ProductDetails from '../../components/ProductDetails/ProductDetails'
import ProductUpdate from '../../components/ProductDetails/ProductUpdate'

const Products = () => {
  return (
    <>
      <Routes>
        <Route path='/' element = {<ProductsList />}/>
        <Route path='/create' element={<CreateProduct />} />
        <Route path='/PRD/:productId' element={<ProductDetails />} />
        <Route path='/PRD/:productId/edit' element={<ProductUpdate />} />
      </Routes>
    </>
  )
}

export default Products