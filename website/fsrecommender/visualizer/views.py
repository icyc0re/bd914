from django.conf import settings
from django.shortcuts import render, redirect
from django.utils import simplejson
from django.http import HttpResponse
import foursquare
import json
import os
import pprint
import requests


ACCESS_TOKEN = 'access_token'
USER = 'user'
VENUES_DIRECTORY = '../../../dataset/sample/venues'


def logged_in(function):
	""" Authentication checker decorator """
	def wrap(request, *args, **kwargs):
		if request.session.has_key(ACCESS_TOKEN) and request.session[ACCESS_TOKEN]:
			return function(request, *args, **kwargs)
		else:
			return redirect('/')
	return wrap




def login(request):
	""" Login through foursquare """
	client = foursquare.Foursquare(client_id=settings.CLIENT_ID, client_secret=settings.CLIENT_SECRET, redirect_uri=settings.REDIRECT_URI)
	auth_uri = client.oauth.auth_url()
	return redirect(auth_uri)

def logout(request):
	""" Remove session data """
	request.session.clear()
	return redirect('/')
	


# Create your views here.
def home(request):
	""" Landing page """

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

	return render(request, 'home.html', {'logged_in': True, 'user_data': user})


def recommend(request):
	#user not logged in
	if not request.session.has_key(ACCESS_TOKEN):
		render(request, 'login.html')
	
	# post
	if request.method == "POST":
		data = {
			'lat'   : request.POST["latitude"],
			'lng'   : request.POST["longitude"],
			'rad'   : request.POST["radius"],
			'time1' : request.POST["time1"],
			'time2' : request.POST["time2"],
			'userId': request.session[USER]["user"]["id"]
		}

		# Get the JSON user file path and the checkin path?
		# ...

		url = 'http://bigdataivan.cloudapp.net:8090'
		r = requests.post(url, data=data)

		return HttpResponse(r.status_code)


		## Ask Bernard:
		###################################################
		
		if request.method == "POST":
			time = request.POST["time"]
			n = request.POST["n"]
			e = request.POST["e"]
			s = request.POST["s"]
			w = request.POST["w"]
		else:
			time = "20:00"
			n = "40.759"
			e = "-73.94"
			s = "40.711"
			w = "-74.04"

		url = 'http://bigdataivan.cloudapp.net:8090'
		payload = {'userId': userId, 'time': time, 'n': n, 'e': e, 's': s, 'w': w}
		#get json files
		userPath = "./user.json" 
		with open(userPath,'a') as outfile:
			json.dump(request.session[USER], outfile) 	# in file

		files = {'file': open(userPath, 'rb')}
		#send post request to ivan
		# r = requests.post(url, data=payload, files=files)
		
		client = foursquare.Foursquare(access_token=request.session[ACCESS_TOKEN])

		# results from requests	
		venues_id = ["3fd66200f964a52005e71ee3","3fd66200f964a52008e81ee3","3fd66200f964a52023eb1ee3",
					"3fd66200f964a5200ae91ee3","3fd66200f964a52015e51ee3"]
		
		# get venues from api
		venues = list() 
		for venue_id in venues_id:
			venues.append(client.venues(venue_id)) 
		
		#redirect to recommend_list that list the recommendations with data received from ivan
		return render(request, 'map.html', {'venues':simplejson.dumps(venues)})
		
	# display recommend.html
	# return render(request, 'recommend.html')


def recommend_list(request):
	return render(request)

def coldstart(request):
	return render(request, 'start.html')

# @matteo: please talk to emma & tiziano, maybe they know more about handling django forms
def locationtime(request):
	if request.method == "POST":
		# use a form object from django: https://docs.djangoproject.com/en/dev/topics/forms/
		return render(request, 'locationtime.html') # this should be the recomm page with results
	return render(request, 'locationtime.html')

@logged_in
def profile(request):
	user_json = json.dumps(request.session[USER], sort_keys=True, indent=2)
	return render(request, 'profile.html', {'user': user_json})
