Big Data Project 2014 - Foursquare project
====


# Goal

The goal of the project is to create a venue recommender system for Forsquare venues.


# Basic usecase

Users login/signup in a simple web interface.

If they login with their Foursquare account basic data and detailed user profile is pulled in. Else, user needs to input basic information.

Every new user needs to go through a short questionary in order to create the initial user profiling used by the recommender system. Foursquare users need to answer even less questions.

Based on his profile the user gets recommended new venues to go to.


# Technical description

We are going to use a basic content based recommender system that computes the similarity between user vectors as the scalar product between them (cosine). On top of this we stack a simple context aware layer that does pre-filtering and post-filtering to the available data, and afterwards computes the final rating of venues displayed to the user.


# If time allows

Further developments that we have in mind once this basic scenario is set up would be:
* creating a mobile app to interface with the user
* improve the recommender from the basic cosine approach by adding machine learning techniques
* implicit feedback loop based on the user's interaction with the displayed results




