import cn from 'classnames';
import React, { useEffect, useState } from 'react'
import { NavLink, useLocation } from 'react-router-dom';
import { Collapse } from 'reactstrap';

// Import CSS
import s from './MenuListItem.module.scss'

// Import Icons
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faChevronRight } from '@fortawesome/free-solid-svg-icons';

const MenuListItem = ({
    index,
    isHeader,
    icon,
    title,
    link,
    childItems,
    openedItem,
    handleOpenedSidebarItemChange,
    activeItem,
    handleActiveItemChange,
}) => {
    const [isClicked, setIsClicked] = useState(false);
    const isOpen = openedItem && openedItem === index && isClicked;
    const location = useLocation();

    const togglePanelCollapse = (index) => {
        setIsClicked(!isClicked);
        handleOpenedSidebarItemChange(index);
    }

    useEffect(() => {
        if (openedItem !== index)
        setIsClicked(false);
    }, [openedItem])

    useEffect(() => {
        if (childItems?.some((child) => location.pathname.includes(child.link))) {
            setIsClicked(true);
            handleOpenedSidebarItemChange(index)
        }
        if (location.pathname.includes(link)) {
            handleActiveItemChange(title)
        }
    }, [location])

    if (!childItems) {
        if (isHeader) {
            return (
                <div className={s.headerItem}>
                    <NavLink
                        to={link}
                        className={({ isActive }) => 
                            isActive ? s.headerItemActive : ''
                        }
                        onClick={() => {
                            handleOpenedSidebarItemChange(index)
                            handleActiveItemChange(null)
                        }}
                    >
                        <span className={s.menuItemIcon}>
                            {icon}
                        </span>
                        <div className={s.menuItemTitle}>
                            <p>{title}</p>
                        </div>
                    </NavLink>
                </div>
            )
        }
        return (
            <NavLink
                to={link}
                className={({ isActive }) => 
                    cn(s.childItem, {[s.childItemActive]: isActive})
                }
                onClick={() => handleActiveItemChange(title)}
            >
                <div className={s.menuItemTitle}>
                    <p>{title}</p>
                </div>
            </NavLink>
        )
    }
    return (
        <div className={s.headerItem}>
            <a 
                className={cn(
                    "d-flex",
                    { [s.headerItemActive]: childItems?.some((child) => child.title === activeItem) },
                    { [s.collapsed]: isOpen }
                )}
                onClick={() => togglePanelCollapse(index)}
            >
                <span className={s.menuItemIcon}>
                    {icon}
                </span>
                <div className={s.menuItemTitle}>
                    <p>{title}</p>
                </div>
                <FontAwesomeIcon className={s.dropdownIcon} icon={faChevronRight} />
            </a>
            <Collapse className={s.panel} isOpen={isOpen}>
                {
                    childItems && childItems.map((childItem, index) => {
                        return (
                            <MenuListItem 
                                key={index}
                                isHeader={false}
                                icon={null}
                                title={childItem.title}
                                link={childItem.link}
                                activeItem={activeItem}
                                handleActiveItemChange={handleActiveItemChange}
                            />
                        )
                    })
                }
            </Collapse>
        </div>
    )
}

export default MenuListItem