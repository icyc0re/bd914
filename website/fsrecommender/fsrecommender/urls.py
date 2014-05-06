from django.conf.urls import patterns, include, url
from django.contrib import admin

from visualizer.views import home, coldstart, login, logout, locationtime, \
    recommend


admin.autodiscover()

urlpatterns = patterns('',
    # Examples:
    # url(r'^$', 'fsrecommender.views.home', name='home'),
    # url(r'^blog/', include('blog.urls')),

    url(r'^admin/', include(admin.site.urls)),
    (r'^$', home),
    (r'^login/$', login),
    (r'^logout/$', logout),
    (r'^coldstart/$', coldstart),
    (r'^locationtime/$', locationtime),
    (r'^recommend/$', recommend),
)
