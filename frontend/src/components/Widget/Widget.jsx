import React from 'react';
import classNames from 'classnames';

import s from './Widget.module.scss';

const Widget = (props) => {
  const {
    title = null,
    className = '',
    headerClass = '',
    children = [],
    options = {},
    ...restProps
  } = props;

  return (
    <>
      <section
        className={s.widget}
        {...restProps}
      >
        {title && (
          <div className={classNames(headerClass, s.title)}>
            {title}
          </div>)}
        <div className={className}>
          {children}
        </div>
      </section>
    </>
  )
}

export default Widget;
