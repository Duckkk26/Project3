import { Route, Routes } from 'react-router-dom'
import CategoryList from '../../components/CategoryList/CategoryList'

const CategoryPage = () => {
  return (
    <>
      <Routes>
        <Route path='/' element = {<CategoryList />}/>
      </Routes>
    </>
  )
}

export default CategoryPage
