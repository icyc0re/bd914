from collections import OrderedDict
from django.conf import settings
from django.http import HttpResponse
from django.shortcuts import render, redirect
from django.utils import simplejson
import foursquare
import json	
import os
import subprocess


ACCESS_TOKEN = 'access_token'
USER = 'user'

CLUSTER_DIRECTORY = '../../recommender_module/target/scala-2.10/'
SCALA_JAR = CLUSTER_DIRECTORY+'recommender_module-assembly-1.0.jar'


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
	user_id = user["user"]["id"]
	request.session[USER] = user

	checkins = client.users.checkins()
	
	with open(os.path.join(settings.NEW_USER_DIRECTORY, user_id), 'w+') as outfile:
		json.dump(user, outfile)

	with open(os.path.join(settings.CHECKINS_DIRECTORY, user_id), 'w+') as outfile:
		json.dump(checkins, outfile)    

	return redirect('/recommend/')

def recommend(request):
	#user not logged in
	if not request.session.has_key("is_logged_in"):
		return redirect('/login')
	
	# post
	if request.method == "POST":

		data = OrderedDict([
			('user_id', request.session[USER]["user"]["id"]),
			('lat', "~"),
			('lng', "~"),
			('rad', "~"),
			('time1', "~"),
			('time2', "~"),
			('days', "~"),
		])
		if "skip_location" not in request.POST:
			data.update({'lat': request.POST["latitude"]})
			data.update({'lng': request.POST["longitude"]})
			data.update({'rad': request.POST["radius"]})
		if "skip_time" not in request.POST:
			data.update({'time1': request.POST["time1"]})
			data.update({'time2': request.POST["time2"]})
		if "skip_days" not in request.POST:
			data.update({'days': request.POST["days"]})

		# call recommender
		output = subprocess.Popen(['java', '-jar', SCALA_JAR] + [str(d) for d in data.values()], stdout=subprocess.PIPE);
		streamdata = output.communicate()
		for line in streamdata:
			print line

		if not output.returncode:
			client = foursquare.Foursquare(access_token=request.session[ACCESS_TOKEN])
			user_recommendations_file = os.path.join(settings.RECOMMENDATIONS_DIRECTORY, data['user_id'])
			
			# for testing, dummy venues if there is no file output by the recommender
			if not os.path.exists(user_recommendations_file):			
				venues_id = ["3fd66200f964a52005e71ee3","3fd66200f964a52008e81ee3","3fd66200f964a52023eb1ee3",
							 "3fd66200f964a5200ae91ee3","3fd66200f964a52015e51ee3"]
				# simulate recommender provided results
				with open(user_recommendations_file,'w+') as recommendations_file:
					for venue_id in venues_id:
						recommendations_file.write(venue_id+'\n')

			# read results
			with open(user_recommendations_file,'r') as f:
				venues_id = f.read().splitlines()
			
			# crawl venues from venues ids
			venues = list() 
			for venue_id in venues_id:
				venues.append(client.venues(venue_id)) 
	
			# send results to user
			return render(request, 'map.html', {'venues':simplejson.dumps(venues), 'context': data})
	
	return redirect('/locationtime')


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

