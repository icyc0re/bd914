# Context Aware Recommender
---

In order to give good recommendations to the user, we are going to also take into account some context related aspects. In our venue recommender, it is easy to imagine how the time of the week or even the time of the day, the current location and perhaps even the currently visited venue can help to shape a decent recommendation into a nice custom-tailored one.

It is important to first note the difference between what is defined as a `user` feature and a `context` feature. The user features are static in nature and defined as a result of user profiling, while the context is always changing and needs to be taken into account at the current moment.


## Examples of context

We will define the context dimensions in our project as hierarchical trees going from general to more particular, on several categories:

1. TimeOfWeek: the time of the week is directly connected to the free time a users has and, as probably stated within the user features. A user may prefers caffees during the week and bars during the weekends.
⋅⋅* AnyDay -> Weekday -> Mon .. Fri
⋅⋅* AnyDay -> Holiday -> Weekend -> Sat .. Sun

2. TimeOfDay: this is a good way to take into account the venue opening hours into the recommendations made by the system.

3. CurrentVenue: you're probably not going to head out to the disco if your current location is a museum. You might however head to a fast food if you're currently grabbing coffee.
⋅⋅* AnyPlace -> Bar -> Pub .. Cafee
⋅⋅* AnyPlace -> Restaurant -> Fast Food .. Fancy Place
⋅⋅* AnyPlace -> Culture -> Museum .. Theatre
⋅⋅* AnyPlace -> ...

4. Company: who you're with should also matter when recommending a venue. If you're hanging out with your buddies, going out for beers may seem like a good idea, but maybe a nice place to eat out would be better suited for a family reunion.
⋅⋅* AnyCompany -> NotAlone -> Friends -> Girlfriend/Boyfriend
⋅⋅* NotAlone -> Co-workers

For the moment, these four dimensions seem most relevant for the user's context. They are also easy to acquire either directly by checking the date and location or by adding a choise for the Company. We will see later that by using the most general of contexts for each dimension we obtain a basic recommender that doesn't take user context into account.


## Paradigms

There are several context-aware paradigms that are used within context-aware recommendation systems:
* Contextual pre-filtering
* Contextual post-filtering
* Contextual modeling


### Contextual pre-filtering

Basically means filtering-out the non-contextual related data. It is a very popular approach, as it reduces the space dimensionality of the data and the system can use a traditional 2D recommender afterwards. 

One problem with this approach is however _data sparity_: trying to make the data fit to a given context query can quickly strip away most of the possible results. In order to combat this problem _context generalization_ can be used. This simply means that we go higher up the context hierarchy tree. So instead of using `<Girlfriend, Pub, Saturday>` we might use `<Girlfriend, __AnyPlace__, Saturday>` or `<__NotAlone__, Pub, __Weekend__>` in order to make sure we don't filter out some good results. Of course, some generalizations make more sense than others. To solve this we could apply some predefined generalization rules or some algorithmically deduced rules that provide the best performance.


### Contextual post-filtering

This requires ignoring the contextual data and calculating the ratings normally. After the ratings have been calculated, we can either:
* filter out the irrelevant venues, or
* adjust the ratings
based on the provided context.

This is another popular paradigm because it does not complicate the process of calculating the recommendations, and any 2D recommender can be used.

Heuristics can be used for adjusting the ratings before displaying the recommendations.


### Contextual modelling

This one is the most complicated of the three, as it requires integrating the context into the core of the recommender. This means that we end up with a multidimensional rating function. Heuristic approaches can be used to reduce the complexity of the recommender in this case, and it may in the end offer better results than the previously mentioned ones, but we will not employ it in our system for the time being.


## Final thoughts on contextual recommendation

Plugging-in a context-aware module in our current approach is pretty straight forwards and implies little overhead for the overall project. Intuitively we can see that context is a matter of interest in our venue recommender, with _pre_ and _post_ filtering being the simplest thing that we can add. We will thus make use of a hybrid approach by only using these two simple methods of filtering.

The traditional 2D recommender that we will employ requires collecting data of the form `<user, venue, rating>` and attempts to predict the missing ratings for the unrated venues of a given user. We should think of storing `<user, venue, context, rating>` as data for the user profiles. That is, also store a vector describing the partial context in which the user is situated when rating a certain venue. This will help in predicting the future ratings based on a context-aware approach in the future.

The __Pre-filtering__ module should have a small impact on the input data, as we want to maintain as flexible of an approach as possible on our recommendations. A good candidate here would be the filtering out of the closed venues based on the current time and the user's prefferrence of going out. There should be no use fo context generalization in this case. Other ideas here would be the Place, with a predefined context generalization (perhaps even a simple approach of going 1 step up the hierarchy would suffice).

The __Post-filtering__ module should not attempt to filter out any of the results, but simply adjust the ratings found by the system by using some simple heuristics. For example, if your current venue is a Pub, results that recommend going to the disco might rank higher than results recommending a theatre or a museum. A similar approach can be taken for going out with Friends: we might place a higher rating for places to be visited with Friends, but still include results of places to be visited NotAlone, but with a lower rating.

Contextual-aware recommender models can be easily plugged into the current approach of the Content-based recommender and would provide a more complete and relevant system overall.