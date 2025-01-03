import { Route, Routes } from "react-router-dom";
import ReceiptGroups from "../../components/ReceipGroups/ReceiptGroups";
const Receipt_Groups = () => {
  return (
    <>
      <Routes>
        <Route path="/" element={<ReceiptGroups />} />
      </Routes>
    </>
  );
};

export default Receipt_Groups;
