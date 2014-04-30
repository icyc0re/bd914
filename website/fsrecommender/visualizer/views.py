from django.shortcuts import render, redirect
import foursquare
import json

# Create your views here.
def home(request):
	# signup - oauth

	# connect to (link below)
	
	# code maccari
	# TODO: get anchor from request path
	login_request = "#access_token=XFABOQ4PIP52UBOST0ZIVC2KKO002WPCUS4IXAV3LCPNNLUY"
	# https://foursquare.com/oauth2/authenticate?client_id=SBUR2MWKKUYWPHJTZU4OLBAMFLGWKWZZJQKVGAAEJV1URHSP&response_type=token&redirect_uri=http://localhost:8000

	if 'access_token' in login_request:
		accessToken = login_request[login_request.find("=")+1:]
		print accessToken
		client = foursquare.Foursquare(access_token=accessToken)
		user = client.users()
		
		jsonString = json.dumps(user) 	# as string

# 		userPath = "./user.json" 
# 		with open(userPath,'a') as outfile:
# 			json.dump(user, outfile) 	# in file

	return render(request, 'home.html')


def login(request):
	return redirect('https://foursquare.com/oauth2/authenticate?client_id=SBUR2MWKKUYWPHJTZU4OLBAMFLGWKWZZJQKVGAAEJV1URHSP&response_type=token&redirect_uri=http://localhost:8000')


def coldstart(request):
	return render(request, 'start.html')

# @matteo: please talk to emma & tiziano, maybe they know more about handling django forms
def locationtime(request):
	if request.method == "POST":
		# use a form object from django: https://docs.djangoproject.com/en/dev/topics/forms/
		return render(request, 'locationtime.html') # this should be the recomm page with results
	return render(request, 'locationtime.html')