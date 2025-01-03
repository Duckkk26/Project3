import React from 'react'
import { Link } from 'react-router-dom'
import { useDispatch, useSelector } from 'react-redux'
import MenuListItem from '../MenuListItem/MenuListItem'

// Import CSS
import s from './Sidebar.module.scss'

// Import icons
import logoSapo from '../../assets/logo-sapo.webp'
import homeIcon from '../../assets/icons/HomeIcon.jsx'
import productIcon from '../../assets/icons/ProductIcon.jsx'
import reportIcon from '../../assets/icons/ReportIcon.jsx'

// Import actions
import { changeActiveSidebarItem, changeOpenedSidebarItem } from '../../actions/sidebar.js'
import userSettingIcon from '../../assets/icons/UserSettingIcon.jsx'

const Sidebar = () => {
  const sidebar = useSelector((state) => state.sidebar);
  const dispatch = useDispatch();

  return (
    <div className={s.container}>
      <nav>
        <header className={s.header}>
          <div className={s.logo}>
            <Link to='/admin'>
              <img src={logoSapo} width={130} />
            </Link>
          </div>
        </header>
        <div className={s.menuListContainer}>
          <div className={s.menuListWrapper}>
            <nav className={s.menuList}>
              <MenuListItem 
                index='dashboard'
                isHeader={true}
                icon={homeIcon}
                title="Tổng quan"
                link='/admin/dashboard'
                openedItem={sidebar.openedItem}
                handleOpenedSidebarItemChange={(openedItem) => dispatch(changeOpenedSidebarItem(openedItem))}
                activeItem={sidebar.activeItem}
                handleActiveItemChange={(activeItem) => dispatch(changeActiveSidebarItem(activeItem))}
              />
              <hr className={s.menuDivider} />
              <div className={s.menuListTitle}>
                <p>Menu</p>
              </div>
              <MenuListItem 
                isHeader={true}
                icon={productIcon}
                title="Sản phẩm"
                index='products'
                openedItem={sidebar.openedItem}
                handleOpenedSidebarItemChange={(openedItem) => dispatch(changeOpenedSidebarItem(openedItem))}
                activeItem={sidebar.activeItem}
                handleActiveItemChange={(activeItem) => dispatch(changeActiveSidebarItem(activeItem))}
                childItems={[
                  {
                    title: "Danh sách sản phẩm",
                    link: '/admin/products'
                  },
                  {
                    title: "Quản lý kho",
                    link: '/admin/variants'
                  },
                  {
                    title: "Đặt hàng nhập",
                    link: '/admin/order_suppliers'
                  },
                  {
                    title: "Nhập hàng",
                    link: '/admin/grns'
                  },
                  {
                    title: "Kiểm hàng",
                    link: '/admin/gins'
                  },
                  {
                    title: "Nhà cung cấp",
                    link: '/admin/suppliers'
                  },
                  // {
                  //   title: "Điều chỉnh giá vốn",
                  //   link: '/admin/price_adjustments'
                  // }
                ]}
              />
              <MenuListItem 
                isHeader={true}
                icon={reportIcon}
                title="Sổ quỹ"
                index='reports'
                openedItem={sidebar.openedItem}                
                handleOpenedSidebarItemChange={(openedItem) => dispatch(changeOpenedSidebarItem(openedItem))}
                activeItem={sidebar.activeItem}
                handleActiveItemChange={(activeItem) => dispatch(changeActiveSidebarItem(activeItem))}
                childItems={[
                  {
                    title: "Phiếu thu",
                    link: '/admin/receipt_vouchers'
                  },
                  {
                    title: "Phiếu chi",
                    link: '/admin/payment_vouchers'
                  },
                  {
                    title: "Sổ quỹ",
                    link: '/admin/reports'
                  }
                ]}
              />
              <hr className={s.menuDivider} />
              <div className={s.menuListTitle}>
                <p>Cấu hình</p>
              </div>
              <MenuListItem 
                index='settings'
                isHeader={true}
                icon={userSettingIcon}
                title="Nhân viên"
                link='/admin/settings/users'
                openedItem={sidebar.openedItem}
                handleOpenedSidebarItemChange={(openedItem) => dispatch(changeOpenedSidebarItem(openedItem))}
                activeItem={sidebar.activeItem}
                handleActiveItemChange={(activeItem) => dispatch(changeActiveSidebarItem(activeItem))}
              />
            </nav>
          </div>
        </div>
      </nav>
    </div>
  )
}

export default Sidebar