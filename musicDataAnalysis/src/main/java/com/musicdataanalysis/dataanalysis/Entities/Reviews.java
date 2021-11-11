package com.musicdataanalysis.dataanalysis.Entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document("Reviews")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Reviews {

    @Id
    String _id;
    String reviewerName;
    int overall;
    String asin;
    String summary;
    String reviewText;

}
