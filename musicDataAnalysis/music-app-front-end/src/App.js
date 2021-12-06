import React from "react";
import ReactDOM from 'react-dom';
import { useState } from "react";
import FetchList from "./components/ItemList";
import FetchDataById from "./components/Item";
import "bootstrap/dist/css/bootstrap.min.css";
import { Switch, Route, Link } from "react-router-dom";
import "./App.css";
import Button from 'react-bootstrap/Button';

function App() {
  
  const displayList  = () => {
    ReactDOM.render(<FetchList />, document.getElementById('root'));
  };

  const displayItemById  = () => {
      ReactDOM.render(<FetchDataById />, document.getElementById('root'));
    };

  return (
      <body>
        <Button class= "bt1" variant="primary" id="root" onClick={displayList}>Fetch data by brand !</Button>
        <Button class= "bt2" variant="primary" id="root" onClick={displayItemById}>Fetch data by Item ID !</Button>
      </body>
  );
}

export default App;
