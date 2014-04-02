#### Report 1 on the filtering component 
---
##### Insights 
* For a content based recommender we will have to consider the features/venues matrix : 

                f1    f2    f3    ....    fn
        v1                                         = V         
        v2                                             
        ...
    where _fi_ denotes feature _i_ and _vj_ venue _j_  

    We will also have to consider the user rating vector: 

        U = [r1 , ... , rn]

    where _ri_ represent the rating associated with the feature _i_. 

* What we want is to filter the venues considering the preferences of the user. To do so we can use several metrics : 
    - cosine distance : for one venue vector v and a user rating vector u we compute the cosine 
        _cos(u,v)=dot(u,v)/(||u||.||v||)_
        Typically the length of a venue vector is proportional to the number of reviews, so to the popularity of the place (this depend on how the data is represented though). The cosine metric has for advantage that less popular venues are just as likely to be chosen as popular ones when the content is relevant i.e. u and v have same orientation.

  - dot product distance : same as above but without normalization. Allow to take into account the popularity of a venue.

* With these metrics we want to compute the score for all venues. If we consider the row of _V_ and _U_ normalized then we can write for the cosine distance : _score = V.U'_. We return the_M_ best scores. 

* From this we can see that the result of the filtering part is __heavily__ dependent on the user and venue profiling parts. The filtering part just tells us how close two vectors are.

* One drawback of content-based filtering is that it will very likely give "no surprises" results. Indeed, to outsmart the user on his tastes and recommend something really great and unexpected the standard method is to study the interactions between users (collaborative filtering). Once the basic content-based recommender system will be implemented it might be good to think of an hybrid approach... 

##### Other ideas I'd like to consider

* For the user profiling part I think it could be great to use some simple mechanism where the user select in several lists what he is looking for. For each choice he also select if this feature is mandatory, appreciated, or not superficial. This allow us to weight each rating of our user profile vector. 

* When the recommendation system will be done it might be cool to allow the user to "swim" in the recommendation space by letting him refine the result and ask for example for cheaper venues or more popular ones ...

##### Next step

 The next step for me is to keep reviewing filtering and profiling methods. I will also join Sprlye to implement a basic version of the computation presented in the insights part. 

##### References
[1] [coursera videos](https://class.coursera.org/recsys-001/lecture/preview) modules 2 & 3
[2] Recommender Systems: An Introduction, chapter 3
[3] Recommender systems handbook, chapter 2,3
[4] Mining of Massive Datasets, chapter 9 


