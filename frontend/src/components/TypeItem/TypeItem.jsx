import React from 'react'

// Import Icons
import { faXmark } from '@fortawesome/free-solid-svg-icons'
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome'

const TypeItem = ({ index, type, handleUpdateType, handleDeleteType }) => {
  return (
    <div className="grid-container type-item" style={ index === 0 ? {marginTop: "10px"} : {}}>
      <div className="box-type-name">
        <div className="form-item">
          <div className="form-textfield">
            <input 
              value={type.name || ''} 
              onChange={(e) => handleUpdateType(e.target.name, e.target.value)} 
              type="text" 
              name="name"
              id="type-name" 
            />
            <fieldset className="input-field"></fieldset>
          </div>
        </div>
      </div>
      <div className="box-type-value">
        <div className="form-item">
          <div className="form-textfield">
            <input 
              value={type.value || ''} 
              onChange={(e) => handleUpdateType(e.target.name, e.target.value)} 
              type="text" 
              name="value" 
              id="type-value" 
            />
            <fieldset className="input-field"></fieldset>
          </div>
        </div>
      </div>
      {
        index !== 0 &&
        <div onClick={handleDeleteType} className="btn-delete-type">
          <FontAwesomeIcon icon={faXmark} />
        </div>
      }
    </div>
  )
}

export default TypeItem