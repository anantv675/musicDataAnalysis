import { useRef, useState,useEffect } from "react";
import React from "react";
import ReactDOM from 'react-dom';
import ItemService from '../services/ItemService'
import ItemTable from "./PopulateTable";
import "../styleSheets/tablePage.css";

    function FetchList(){

        // Render the UI for your table

        const [brand, setBrand] = useState('');


        const inputRef = useRef();
        let apiData;

        const submitHandler = (e) => {
            e.preventDefault();
            setBrand(inputRef.current.value);
            apiData = ItemService.getAllItemsByBrand(inputRef.current.value);
            const p = Promise.resolve(apiData);
            p.then(function(v) {
                const element = <ItemTable brandData={v}/>;
                ReactDOM.render(element, document.getElementById('list'))
            });

        };

        return (
            <div className="App">
                <form onSubmit={submitHandler}>
                    <input ref={inputRef} />
                    <button type="submit">Submit</button>
                        <div style={{display: 'flex',  justifyContent:'center', alignItems:'center', height: '70vh'}} id="list"/>
                </form>
            </div>
        );
    };


    export default FetchList