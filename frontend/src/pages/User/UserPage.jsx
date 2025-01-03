import { Route, Routes } from 'react-router-dom'
import UserList from '../../components/UserList/UserList'


const UserPage = () => {
  return (
    <>
      <Routes>
        <Route path='/' element = {<UserList />}/>
      </Routes>
    </>
  )
}

export default UserPage
