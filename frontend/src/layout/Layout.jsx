import React from "react";
import { Route, Routes } from "react-router-dom";

// Import CSS
import s from "./Layout.module.scss";

// Import Components
import Sidebar from "../components/Sidebar/Sidebar";

// Import Pages
import Dashboard from "../pages/Dashboard/Dashboard";
import Products from "../pages/Products/Products";
import Variants from "../pages/Variants/Variants";
import OrderSuppliers from "../pages/OrderSuppliers/OrderSuppliers";
import PurchaseOrders from "../pages/PurchaseOrders/PurchaseOrders";
import StockAdjustments from "../pages/StockAdjustments/StockAdjustments";
import Suppliers from "../pages/Suppliers/Suppliers";
import PriceAdjustments from "../pages/PriceAdjustments/PriceAdjustments";
import ReceiptVouchers from "../pages/ReceiptVouchers/ReceiptVouchers";
import PaymentVouchers from "../pages/PaymentVouchers/PaymentVouchers";
import Reports from "../pages/Reports/Reports";
import GRNPage from "../pages/GRN/GRNPage";
import GINPage from "../pages/GIN/GINPage";
import CategoryPage from "../pages/Category/CategoryPage";
import SupplierGroups from "../pages/SupplierGroups/SupplierGroups";
import UserPage from "../pages/User/UserPage";
import Receipt_Groups from "../pages/Receipt_Groups/Receipt_Groups";
import Payment_Groups from "../pages/Payment_Groups/Payment_Groups";

const Layout = () => {
  return (
    <div className={s.container}>
      <Sidebar />
      <div className="right">
        <Routes>
          <Route path="/dashboard/*" element={<Dashboard />} />
          <Route path="/products/*" element={<Products />} />
          <Route path="/variants/*" element={<Variants />} />
          <Route path="/order_suppliers/*" element={<OrderSuppliers />} />
          <Route path="/purchase_orders/*" element={<PurchaseOrders />} />
          <Route path="/stock_adjustments/*" element={<StockAdjustments />} />
          <Route path="/suppliers/*" element={<Suppliers />} />
          {/* <Route path='/price_adjustments/*' element={<PriceAdjustments />} /> */}
          <Route path="/receipt_vouchers/*" element={<ReceiptVouchers />} />
          <Route path="/payment_vouchers/*" element={<PaymentVouchers />} />
          <Route path="/reports/*" element={<Reports />} />
          <Route path="/grns/*" element={<GRNPage />} />
          <Route path="/gins/*" element={<GINPage />} />
          <Route path="/categories/*" element={<CategoryPage />} />
          <Route path="/supplier_groups/*" element={<SupplierGroups />} />
          <Route path="/settings/users/*" element={<UserPage />} />
          {/* <Route path="/receipt_groups/*" element={<Receipt_Groups />} />
          <Route path="/payment_groups/*" element={<Payment_Groups />} /> */}
        </Routes>
      </div>
    </div>
  );
};

export default Layout;
