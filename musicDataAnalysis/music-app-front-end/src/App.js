import React from "react";
import ReactDOM from 'react-dom';
import { useState } from "react";
import FetchData from "./components/ItemList";
import "bootstrap/dist/css/bootstrap.min.css";
import { Switch, Route, Link } from "react-router-dom";
import "./App.css";

function App() {
  const display  = () => {
    ReactDOM.render(<FetchData />, document.getElementById('root'));
  };

  return (
      <body>
        <button id="root" onClick={display}>Fetch data by brand !</button>
      </body>
  );
}

export default App;
