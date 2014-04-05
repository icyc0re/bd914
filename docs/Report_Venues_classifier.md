Report - Venue classifier
=========

This report specifies relevant attirbutes of 4SQ venues and their potential usage in content-based recommender system. For each attribute provided are  clarifications of its meaning, possible usage in recommender system and example JSON data structor that can be found in venues data. Eclosed JSON file fragments are part of the venue file that can be found [here][example venue].

Location
----
We are provided with latitude and longitude values. Therefore it would be possible to compare venue by their location and provide location-based recommendation. User could be recommended a venue based on the lcoation of his previously visited venues. Informative paper on location based recommendations in social networks can be found [here][location based].



```json
location: {
    	address: "4 Clinton St"
		crossStreet: "at E Houston St"
		lat: 40.721294
		lng: -73.983994
		postalCode: "10002"
		cc: "US"
		city: "New York"
		state: "NY"
		country: "United States"
		}
```

Categories
----

Each location can have several categories  assigned. One of the categories has to be the primary one. Example category values can be found in the example below.

```json
categories: [
    {
		id: "4bf58dd8d48988d143941735"
		name: "Breakfast Spot"
		pluralName: "Breakfast Spots"
		shortName: "Breakfast"
		icon: {
		    prefix: "https://ss1.4sqi.net/img/categories_v2/food/breakfast_"
		    suffix: ".png"
		}
		primary: true
    },
    ...
```

Statistics
----
Basic statiscs for each venue are provided. These statistics are: checkinsCount, usersCount, tipsCount, number of likes, rating. These all attributes can determine the popularity of a venue. Recommender system could offer user venues that are similar in popularity to his historic/query venues. 

```json
stats: {
    checkinsCount: 
	usersCount: 11192,
    tipCount: 395
}
```
```json
likes: {
    count: 342
	groups: [
	    {
            type: "others"
		    count: 342
            items: [ ]
        }
	],
	summary: "342 likes"
},
like: false,
dislike: false,
rating: 9.55
...
```
Price
----
This attribute contains the price tier from 1 (least pricey) - 4 (most pricey) and a message describing the price tier. User could be recommended venues that have similar price to his historic/query venue. 

```json
price: {
    tier: 2
	message: "Moderate"		
    currency: "$"
}
```
Information about users that visited venue
----
Available does not provide checkins for a scecific user. This information is confidential, therefore 4SQ does not allow public access to it. However, users that post a tip or a photo of a venue are publicaly identified by their id. Using this information we can gather information about the users that visited a venue. For example, a ratio of female/male for a venue can be roughly estimated.

```json
user: {
	id: "690170"
    firstName: "caroline"
	lastName: "c."
	gender: "female"
	photo: {
	prefix: "https://irs1.4sqi.net/img/user/"
	suffix: "/F5IKVUEOIEXEJN2E.png"
}		
```

Tips and lists
----

Tips can be one of the most interesting source of information. Users paste there their own specific opinions about venues. Moreover, each opinion can be ‘liked’ by other users. That can be used to weight tips importance. Timestamp of the tip can be also used to assess its relevance. Using this attribute in would require elements of natural language processing. 

Tips also include information about the gender of user. This can be used combined with gender in photos attribute. 

```json
tips: {
    count: 395
    groups: [
    {
        type: "others"
        name: "Tips from others"
        count: 395
        items: [
        {
            id: "4a7a511970c603bbd64e8eb4"
            createdAt: 1249530137
            text: "I don't care if you get the beloved blueberry pancakes or not, but the must have here is the SUGAR-CURED BACON!"
            canonicalUrl: "https://foursquare.com/item/4a7a511970c603bbd64e8eb4"
            likes: {
                count: 121
                groups: [
                    {
                        type: "others"
                        count: 121
                        items: [ ]
                    }
                ]
                summary: "121 likes"
            }
            like: false
            logView: true
            todo: {
                count: 47
            }
            user: {
                id: "27078"
                firstName: "Nina"
                lastName: "C."
                gender: "female"
                photo: {
                prefix: "https://irs1.4sqi.net/img/user/"
                suffix: "/YI54VKYBE1H0QZ00.jpg"
            }
        }
    },
    ...
```
Tags and phrases
----
Tags and phrases are assigned by users. Therefore these provide additional information that can be used in the process of venue classification. Phrases assigned by users are counted thus their importance can be meassured.

```json
tags: [
	"bakery"
	"baking"
	"breakfast"
	"brunch"
	"fried chicken"
	"pancakes"
	"scones"
	"southern"
]

phrases: [
	{
		phrase: "blueberry pancakes"
		sample: {
	    	entities: [
		    {
        		indices: [
        		    25
        		    43
        		]
    		    type: "keyPhrase"
    		}
		    ]
		    text: "... bacon was excellent. Blueberry pancakes not too impressed. Hot chocolate was..."
		}
		count: 73
    }
```

Other attributes
----
Other attributes that potential can be used within recommender system are:
 - Opening hours and *popular* hours
 - Menus, drins, dining options (takeout/delivery)
 - Possibility of reseration
 - Payment methods


[location based]:https://www.cl.cam.ac.uk/~cm542/papers/socialcom12_noulas.pdf
[example venue]:https://developer.foursquare.com/docs/explore#req=venues/40a55d80f964a52020f31ee3

    