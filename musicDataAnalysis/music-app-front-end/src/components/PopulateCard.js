import Card from 'react-bootstrap/Card';
import Button from 'react-bootstrap/Button';
import { createApi } from "unsplash-js";
import React,{ useState } from 'react';



function ItemCard(props){

    const data = props.brandData
    const feature = data.feature[0]
    console.log(feature)
    const [imgUrl, setImgUrl] = useState('')
    const unsplash = createApi({ accessKey: "YTGW-AHiFKPpaK68bxy2MlMD8Jxvv62O-daJQ_rnZhk" });
    unsplash.search.getPhotos({
        query: feature
    }).then(result => {
        if (result.errors) {
          // handle error here
          console.log('error occurred: ', result.errors[0]);
        } else {
          const feed = result.response;
      
          // extract total and results array from response
          const { total, results } = feed;
      
          // handle success here
          // console.log(`received ${results.length} photos out of ${total}`);
          // console.log('first photo: ', results[0].urls.small);
          setImgUrl(results[0].urls.small)
        }
      });

    console.log("printing length")  
    console.log(data.description[0].length)

    const [showMore, setShowMore] = useState(false);
    
    const getText = () => {
        // For Text that is shorter than desired length
        if (data.description[0].length <= 500) return data.description;
        // If text is longer than desired length & showMore is true
        if (data.description[0].length > 500 && showMore) {
          return (
            <>
              <p>{data.description}</p>
    
              <button
                onClick={() => setShowMore(false)}>
                Show Less
              </button>
            </>
          );
        }
         // If text is longer than desired length & showMore is false
        if (data.description[0].length > 258) {
          return (
            <>
              <p>{data.description[0].slice(0, 258)}</p>
    
              <button
                onClick={() => setShowMore(true)}>
                Show Full Description
              </button>
            </>
          );
        }
      };

    return (
        <Card style={{ width: '18rem',marginTop:'10rem' }}>
            <Card.Img variant="top" src={imgUrl} />
                <Card.Body>
                    <Card.Title>{data.brand}</Card.Title>
                        <Card.Text>
                            {getText()}
                        </Card.Text>
                </Card.Body>
        </Card>
    )
}


export default ItemCard