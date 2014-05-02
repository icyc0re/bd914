from django.shortcuts import render, redirect
from django.conf import settings
import foursquare
import json

ACCESS_TOKEN = 'access_token'

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
	
	json_string = json.dumps(user)

# 		userPath = "./user.json" 
# 		with open(userPath,'a') as outfile:
# 			json.dump(user, outfile) 	# in file

	return render(request, 'home.html', {'user_data': user})


def login(request):
	client = foursquare.Foursquare(client_id=settings.CLIENT_ID, client_secret=settings.CLIENT_SECRET, redirect_uri=settings.REDIRECT_URI)
	auth_uri = client.oauth.auth_url()
	return redirect(auth_uri)


def logout(request):
	del request.session[ACCESS_TOKEN]
	return redirect('/')


def coldstart(request):
	return render(request, 'start.html')

# @matteo: please talk to emma & tiziano, maybe they know more about handling django forms
def locationtime(request):
	if request.method == "POST":
		# use a form object from django: https://docs.djangoproject.com/en/dev/topics/forms/
		return render(request, 'locationtime.html') # this should be the recomm page with results
	return render(request, 'locationtime.html')