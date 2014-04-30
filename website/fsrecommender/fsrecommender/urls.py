from django.conf.urls import patterns, include, url
from visualizer.views import home, coldstart, login, locationtime

from django.contrib import admin
admin.autodiscover()

urlpatterns = patterns('',
    # Examples:
    # url(r'^$', 'fsrecommender.views.home', name='home'),
    # url(r'^blog/', include('blog.urls')),

    url(r'^admin/', include(admin.site.urls)),
    (r'^$', home),
    (r'^login/$', login),
    (r'^coldstart/$', coldstart),
    (r'^locationtime/$', locationtime),
)
