import axios from 'axios';
import React from 'react'

const MUSIC_ITEM_API_BASE_URL = "http://localhost:8083/";


function getAllItemsByBrand(brand){


        // var params = new URLSearchParams();
        // params.append("pageIndex", 0);
        // params.append("pageSize", 5);
        //
        // var request = {
        //     params: params
        // };

        const promise = axios.get(`http://localhost:8083/getAllItemsByBrand/${brand}`)
            .then(function (response) {
                return response.data;
        }).catch(
            function (error) {
                console.log(error)
            }
        );

        return promise
    }

    const getItemByAsin= (asin) => {
        return axios.get(MUSIC_ITEM_API_BASE_URL + '/getItemByAsin/' + asin);
    };



export default {
    getAllItemsByBrand,
    getItemByAsin
}



