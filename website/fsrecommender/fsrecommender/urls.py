from django.conf.urls import patterns, include, url
from django.contrib import admin

from visualizer.views import home, coldstart, login, logout, profile, \
	locationtime, recommend


admin.autodiscover()

urlpatterns = patterns('',
    #url(r'^admin/', include(admin.site.urls)),
    
    (r'^$', home),
    
    (r'^login/$', login),
    (r'^logout/$', logout),
    (r'^profile/$', profile),

    (r'^coldstart/$', coldstart),
    (r'^locationtime/$', locationtime),
    (r'^recommend/$', recommend),
)
