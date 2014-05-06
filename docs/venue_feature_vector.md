Report - Venue Feature Vector Modelling
=========

In general: It should be easiest to work with normlized values in the range from 0-1 to avoid difference in balances due to their order. Thereafter a custom weight can be applied to the features for the ones we wish to increase or decrease importance of.

Some of these features, such as opening hours and Locations - are only interesting in relation to the user input (ie location is not relevant unless we know where the user is or wants to be). 

This means that they cannot be precomputed before having the user input, so we  might want to do this as we calculate the similarity in order to avoid passing over the data twice.


Vector (non-precomputable: np)


Attribute     | Type                 | Calculation  |
:------------ | :--------------: 	| -------------
latitude	  |	Numerical			|
longitude 	  | Numerical			|
(Distance (np)) | Numerical      		| See below
Price		  | "Ordinal"	   		| tier / 4
Rating 		  |	Numerical			| rating / 10
Check-ins     | "Ordinal"	   		| See below
Hours 		  | "Text"				|
(Hours 	 (np)) | True (1) unknown (0.5) False(0) 	|
Categories 	  | Seq[String] 	| See below


Each of this could/should be weighted. This can be done either as suggested in 
![](Report_filteringComponent_1.markdown?raw=true) with letting the user determince the importance, or by machine learning, or by setting appropriate values. 

As a notion though it is likely we want to set the importance of for example categories to be more important than other factors - however as a start all weights could equal 1. We may also want to give a lesser weight to factors that are not always available such as price or rating.


Categories
--------

From the statistics it has been reported an average of (more or less) 1 category per venue, which can vary to 0 (in a small set of cases) and more. The type of computation needs to be flexible to consider all these cases and let the possibility to compute venue category similarity with the habits of a user.
In order to do this we use the official category tree (described by foursquare [here](https://developer.foursquare.com/categorytree)), and we treat it as a forest of trees (every highest level category is a root of a tree).

Since the foursquare category tree is a static structure and we also work on a static set of data we treat the category similarity as a precomputed and static 1-to-1 relationship between every couple of category. We represent this in a file as a triangular matrix which contains as rows and columns the categories and for each couple of category their similarity.
NB: the diagonal will be filled with 1 and similarities will be >0 only within the same tree of the forest).

	| C1 | C2 | C3 | ... | Cn
:---: | :---: | :---: | :---: | :---: | :---:
**C1** | 1 | 0 | 0 | - | 0
**C2** | 0 | 1 | 0.8 | - | 0
**C3** | 0 | 0.8 | 1 | - | 0
**...** | - | - | - | - | -
**Cn** | 0 | 0 | 0 | - | 1

**Matrix building process**
1. Recreate the foursquare tree as a forest
2. Reduce the forest removing unused categories (to simplify and adapt to our data)
3. for each category c1 run the similarity script

**Similarity script**
Starting with a similarity of 1 on the node we have propagate the similarity with 2 operations:
- *downpropagation(similarity = k)*, called over all the node's children, applies to the children the similarity k * DOWNSCALE_K
- *uppropagation(similarity = k)*, called over the parent, applies to the parent the similarity k * UPSCALE_K

both these functions will then continue propagating on toward both the parent and the children *except* for the sender (not to loop and overwrite similarities).

The coefficients DOWNSCALE_K and UPSCALE_K can be changed in the script, but are initialized by default to, respectively, 0.95 and 0.5.

If a venue contains more N > 1 categories it's possible to join them simply by adding up the similarity rows (from the categories Cx and Cy) element by element and then normalizing the values with the normalization function:

math.log(x / N * (math.e - 1) + 1)


The same can be done with the user with the set of its venues and the categories of all its venues.



Hours
--------

For venues that have given hours the time should be checked against the time that user desires to go there (assuming he has given one). Good option would be to let the user feature = 1 (it can be assumed he wants a place open at the given time) and set the venue hours according.


Checkins / Users
--------

Both of these features have similar distribution with a few places having a lot of check-ins (100-1000) and most having very few (Average: 257, Median: 9). Therefore I suggest the number of checkins not be used directly proportionally to the number of checkins as this would heavily penalize any venue that is not a central hub (ie the best venues might be only a mcdonalds in the centre simply becuse a lot of people go there).

An alternative is to map them into tiers according to check-in traffic

```
Example:
Tier 1: 0 < check-ins < 3; //Low popularity
Tier 2: 3 < check-ins < 9; //Medium - low
Tier 3: 9 < check-ins < 12; //Medieum - high
Tier 4: 12 < check-int < 500; High
Tier 5: 500 < check-ins < max; Very high

Value = tier / number of tiers
```

And then calculate the value as tier / number of of tiers. 
The user could either predefine his choice but it might be better to set it to the middle tier (or slightly above) such as tier 3 or 4. 

The number of users follows quite closely the number of check-ins, therefore it might be unecessary to use both of these features. 


Location / Distance
--------

Location is the most fundamental feature of any venue. There are different strategies of using it in a recommender system: pre-filtering, in recommender or post-filtering. Pre-filtering of location is more likely to be interesting for a large scale system - as we only cover one city it may not be as interesting. Post-filtering might be interesting but should be in relation to how many results were achieved or result density (notice foursquare API gives venues from search in a circle based system where the radius is determined by the density of venues in the area). 


I suggest that Location can be used in the recommender system as a distance measure from the location of the user/his desired location, where a penalty is increased if the location is further away.


```
Variables:

C_A= [latitude 1, longitude 1] * PI / 180; //Location of user or desired location
C_B = [latitude 2, longitude 2] * PI / 180; //Location of venue

D = [latitude 2 - latitude 1, longitude 2 - longitude 1];
R = earths mean radius ≈  6371 (km);
```

A number of options are available as described [here](
 http://www.movable-type.co.uk/scripts/latlong.html):


#### Great Circle Distance

Distance between two points given in latitude/longitude [(wikipedia: Great circle distance)](http://en.wikipedia.org/wiki/Great-circle%5Fdistance "Great Circle Distance")

```
a = sin(D[0]/2)^2 + cos(C_A[0])*cos(C_B[0])*sin(D[1]/2)^2;
c = 2*atan2(sqrt(a), sqrt(a-1));
distance = R*c;
```

#### Spherical Law of Cosines
Simplification
```
distance = sin(C_A[0])*sin(C_A[1]) + cos(C_A[0])*cos(C_A[1])*cos(D_[1])*R;
```

#### Equirectangular approximation

Less accurate - only if performance is an issue
```
var x = (C_B[1]-C_A[1]) * Math.cos((C_A[0]+ C_B[0])/2);
var y = (C_B[0] - C_A[0]);
var d = Math.sqrt(x*x + y*y) * R;
```

### Determining value

Once the distance has been found the questions is how to use it. Assuming a scale of  I think it would be best to let all the locations within a certain distance -radius- have value 1 with decreasing values for everything with greater distance. The reason for setting the distance to 1 for locations within a certain radius is that it may be unreasonable to penalize a location for simply beeing a block away. 

```
if (distance < searchRadius) //say 2 km
	value = 1; 
else
	value = searchRadius/(alpha*distance);
1. 
```

This method will also automatically scale the feature to values between 0 and 1.

###Alternative

An alternative is to simply use the latitude and longitude values and have a +- allowance. However having a distance in kilometers might be more desirable if we would wish to have user input on area or postfilering controlled by user (ie keep only those within 3 km).

