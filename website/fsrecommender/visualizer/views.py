from django.shortcuts import render, redirect

# Create your views here.
def home(request):
	# signup - oauth

	# connect to (link below)

	# code maccari

	# https://foursquare.com/oauth2/authenticate?client_id=SBUR2MWKKUYWPHJTZU4OLBAMFLGWKWZZJQKVGAAEJV1URHSP&response_type=token&redirect_uri=http://localhost:8000


	# TODO: get user data

	return render(request, 'home.html')


def login(request):
	return redirect('https://foursquare.com/oauth2/authenticate?client_id=SBUR2MWKKUYWPHJTZU4OLBAMFLGWKWZZJQKVGAAEJV1URHSP&response_type=token&redirect_uri=http://localhost:8000')


def coldstart(request):
	return render(request, 'start.html')