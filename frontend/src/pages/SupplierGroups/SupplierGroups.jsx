import { Route, Routes } from 'react-router-dom'
import SupplierGroupsList from '../../components/SupplierGroupsList/SupplierGroupsList'

const SupplierGroups = () =>{
    return(
        <>  
            <Routes>
                <Route path='/' element = {<SupplierGroupsList/>}/>
            </Routes>
        </>
    )
}

export default SupplierGroups