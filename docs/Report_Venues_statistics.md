Report - Venue classifier
=========

This report provides the insight into statistic of key feature of venues dataset.
Statistic where calculated on the set of 476315 venues. 

For optional attributes the percentage of occurances has been meassured. Example: at least one category is specified for 93% of venues.

Attribute     | Percentage of occurance 
:------------ | :------------:
Location      | 100%
Category      | 93%
Popular       | 8%
Rating        | 5%
Hours         | 3%
Price         | 3%

Average and median values per venue for both required and optional attributes:

Attribute       | Average         | Median 
:------------   | :------------: |
Checkins        | 237            | 9
Users          | 93  |3 
Tips         | 1.6 | 0
Categories      | 1.04 | 1
Likes       | 0.8 | 0
Tags           | 0.3 | 0


Distributions
----

Distribution of venues per category is available at /api/analyzer/venue_stats_results/categories. Other useful distribution charts are listed below.

![](../api/analyzer/venue_stats_results/checkins.png?raw=true)

![](../api/analyzer/venue_stats_results/users.png?raw=true)

![](../api/analyzer/venue_stats_results/tips.png?raw=true)

![](../api/analyzer/venue_stats_results/tags.png?raw=true)