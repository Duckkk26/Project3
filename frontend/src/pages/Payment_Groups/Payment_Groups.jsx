import { Route, Routes } from "react-router-dom";
import PaymentGroups from "../../components/PaymentGroups/PaymentGroups";
const Payment_Groups = () => {
  return (
    <>
      <Routes>
        <Route path="/" element={<PaymentGroups />} />
      </Routes>
    </>
  );
};

export default Payment_Groups;
