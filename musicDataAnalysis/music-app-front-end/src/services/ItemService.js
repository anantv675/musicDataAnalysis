import axios from 'axios';

const MUSIC_ITEM_API_BASE_URL = "http://localhost:8081/";


    function getAllItemsByBrand(brand){

        var params = new URLSearchParams();
        params.append("page", 0);
        params.append("size", 5);

        var request = {
            params: params
        };

        const promise = axios.get(`http://localhost:8082/getAllItemsByBrand/${brand}`,request)
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
    }



export default {
    getAllItemsByBrand,
    getItemByAsin
}



