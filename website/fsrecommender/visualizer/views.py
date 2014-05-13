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
		request.session["is_logged_in"] = True
	
	client = foursquare.Foursquare(access_token=request.session[ACCESS_TOKEN])
	access_token = request.session[ACCESS_TOKEN]

	user = client.users()
	request.session[USER] = user

	checkins = client.users.checkins()
	
	json_string = json.dumps(user)

# 		userPath = "./user.json" 
# 		with open(userPath,'a') as outfile:
# 			json.dump(user, outfile) 	# in file

	return redirect('/recommend/')

def recommend(request):
	#user not logged in
	if not request.session.has_key("is_logged_in"):
		return redirect('/login')
	
	# post
	if request.method == "POST":
		data = {
			'lat'  : request.POST["latitude"],
			'lng'  : request.POST["longitude"],
			'rad'  : request.POST["radius"],
			'time1': request.POST["time1"],
			'time2': request.POST["time2"],
			'user' : request.session[USER]
		}

		# url = 'http://bigdataivan.cloudapp.net:8090'
		url = 'http://localhost:8000/cluster-test/'
		r = requests.post(url, data=data, stream=True)

		if r.status_code == 200:
			client = foursquare.Foursquare(access_token=request.session[ACCESS_TOKEN])

			venues_id = json.loads(r.text)
			venues = list() 
			for venue_id in venues_id:
				venues.append(client.venues(venue_id)) 

			return render(request, 'map.html', {'venues':simplejson.dumps(venues), 'context': data})
		return HttpResponse("Oupsy.. That's an error. Could not access cluster")			
	return redirect('/locationtime')

def recommend_list(request):
	return render(request)

def coldstart(request):
	return render(request, 'start.html')

def locationtime(request):
	return render(request, 'locationtime.html')

@logged_in
def profile(request):
	user_json = json.dumps(request.session[USER], sort_keys=True, indent=2)
	return render(request, 'profile.html', {'user': user_json})

# Test method. Simulates response of the cluster server
def cluster_test(request):
	if request.method == "POST":
		venues_id = ["3fd66200f964a52005e71ee3","3fd66200f964a52008e81ee3","3fd66200f964a52023eb1ee3",
					"3fd66200f964a5200ae91ee3","3fd66200f964a52015e51ee3"]
		return HttpResponse(json.dumps(venues_id), content_type="application/json")
	return HttpResponse("Invalid request. This is a test method")

