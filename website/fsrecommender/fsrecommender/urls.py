from django.conf.urls import patterns, include, url
from django.contrib import admin

from visualizer.views import home, coldstart, login, logout, profile, cluster_test, \
	locationtime, recommend


urlpatterns = patterns('',
    (r'^$', home),
    
    (r'^login/$', login),
    (r'^logout/$', logout),
    (r'^profile/$', profile),

    (r'^coldstart/$', coldstart),
    (r'^locationtime/$', locationtime),
    (r'^recommend/$', recommend),

    (r'^cluster-test/$', cluster_test)
)
