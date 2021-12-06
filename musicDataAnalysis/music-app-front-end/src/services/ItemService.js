import axios from 'axios';
import React from 'react'

// const MUSIC_ITEM_API_BASE_URL = "http://localhost:8083";


function getAllItemsByBrand(brand){

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


function getItemByAsin(asin){

        const promise = axios.get(`http://localhost:8083/getItemByAsin/${asin}`)
                .then(function (response) {
                    return response.data;
            }).catch(
                function (error) {
                    console.log(error)
                }
            );

            return promise
    };



export default {
    getAllItemsByBrand,
    getItemByAsin
}



