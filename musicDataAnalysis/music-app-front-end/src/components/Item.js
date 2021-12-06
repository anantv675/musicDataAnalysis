import { useRef, useState,useEffect } from "react";
import React from "react";
import ReactDOM from 'react-dom';
import ItemService from '../services/ItemService';
import ItemCard from '../components/PopulateCard';

    function FetchDataById(){

        // Render the UI for your table

        const [asin, setAsin] = useState('');


        const inputRef = useRef();
        let apiData;

        const submitHandler = (e) => {
            e.preventDefault();
            setAsin(inputRef.current.value);
            apiData = ItemService.getItemByAsin(inputRef.current.value);
            console.log("inside item.js")
            const p = Promise.resolve(apiData);
            p.then(function(v) {
                const element = <ItemCard brandData={v}/>;
                ReactDOM.render(element,document.getElementById('item'))
            });

        };

        return (
            <div className="App">
                <form onSubmit={submitHandler}>
                    <input ref={inputRef} />
                    <button type="submit">Submit</button>
                        <div style={{display: 'flex',  justifyContent:'center', alignItems:'center', height: '70vh'}} id="item"/>
                </form>
            </div>
        );
    };

export default FetchDataById