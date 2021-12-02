import React from "react";
import ReactDOM from 'react-dom';
import { useState } from "react";
import FetchData from "./components/ItemList";
import "bootstrap/dist/css/bootstrap.min.css";
import { Switch, Route, Link } from "react-router-dom";
import "./App.css";
import Button from '@mui/material/Button';

function App() {
  
  const display  = () => {
    ReactDOM.render(<FetchData />, document.getElementById('root'));
  };

  return (
      <body>
        <Button variant="contained" id="root" onClick={display}>Fetch data by brand !</Button>
      </body>
  );
}

export default App;
