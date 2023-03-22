import React from 'react';
import {useNavigate} from "react-router-dom"
  
const RedirectButton = (e) => {
  const navigate = useNavigate();
  return (
      <>
        <input type='button' className={e.className} onClick={(e.values) ? ()=>navigate(e.goTo+'/'+e.values) : ()=>navigate(e.goTo)} value={e.text} style={{marginLeft: '5px'}}/>
      </>
  )
};
  
export default RedirectButton;