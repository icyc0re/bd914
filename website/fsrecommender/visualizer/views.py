from django.conf import settings
from django.shortcuts import render, redirect
import foursquare
import json
import os
import requests


ACCESS_TOKEN = 'access_token'
USER = 'user'
VENUES_DIRECTORY = '../../../dataset/sample/venues'

# Create your views here.
def home(request):

	# complete oauth retrieving token (unless already in session)
	login_code = request.GET.get('code', '')
	if not request.session.has_key(ACCESS_TOKEN) and not login_code:
		return render(request, 'home.html')
	elif not request.session.has_key(ACCESS_TOKEN) and login_code:
		client = foursquare.Foursquare(client_id=settings.CLIENT_ID, client_secret=settings.CLIENT_SECRET, redirect_uri=settings.REDIRECT_URI)
		access_token = client.oauth.get_token(login_code)
		request.session[ACCESS_TOKEN] = access_token
	
	client = foursquare.Foursquare(access_token=request.session[ACCESS_TOKEN])
	access_token = request.session[ACCESS_TOKEN]

	user = client.users()
	request.session[USER] = user

	checkins = client.users.checkins()
	
	json_string = json.dumps(user)

# 		userPath = "./user.json" 
# 		with open(userPath,'a') as outfile:
# 			json.dump(user, outfile) 	# in file

	return render(request, 'home.html', {'user_data': user})


def login(request):
	client = foursquare.Foursquare(client_id=settings.CLIENT_ID, client_secret=settings.CLIENT_SECRET, redirect_uri=settings.REDIRECT_URI)
	auth_uri = client.oauth.auth_url()
	return redirect(auth_uri)

def recommend(request):
	# not post
	
	#user not logged in
	if not request.session.has_key(ACCESS_TOKEN):
		render(request, 'login.html')
	
	# post
	if request.method == "POST":
		#receive value from post
		userId = request.session[USER]["user"]["id"]
		time = request.POST["time"]
		lat = request.POST["lat"]
		lng = request.POST["lng"]
		
		url = 'http://bigdataivan.cloudapp.net:8090'
		payload = {'userId': userId, 'time': time, 'lat': lat, 'lng': lng}
		#get json files
		userPath = "./user.json" 
		with open(userPath,'a') as outfile:
			json.dump(request.session[USER], outfile) 	# in file

		files = {'file': open(userPath, 'rb')}
		#send post request to ivan
		#r = requests.post(url, data=payload, files=files)
		
		client = foursquare.Foursquare(access_token=request.session[ACCESS_TOKEN])

		# save results from requests
		venues = list()
		venues_id = ["3fd66200f964a52005e71ee3"]
		for venue_id in venues_id:
			venues.append(client.venues(venue_id))
		
		#redirect to recommend_list that list the recommendations with data received from ivan
		return render(request, 'map.html', {'venues':venues})
		
	# display recommend.html
	return render(request, 'recommend.html')


def recommend_list(request):
	return render(request)
	
def logout(request):
	request.session.clear()
	return redirect('/')


def coldstart(request):
	return render(request, 'start.html')

# @matteo: please talk to emma & tiziano, maybe they know more about handling django forms
def locationtime(request):
	if request.method == "POST":
		# use a form object from django: https://docs.djangoproject.com/en/dev/topics/forms/
		return render(request, 'locationtime.html') # this should be the recomm page with results
	return render(request, 'locationtime.html')