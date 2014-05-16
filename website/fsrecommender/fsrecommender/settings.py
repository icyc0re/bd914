"""
Django settings for fsrecommender project.

For more information on this file, see
https://docs.djangoproject.com/en/1.6/topics/settings/

For the full list of settings and their values, see
https://docs.djangoproject.com/en/1.6/ref/settings/
"""

import os

# MANUAL SETTINGS
# foursquare settings

if 'STAGING' in os.environ:
    CLIENT_ID = 'SBUR2MWKKUYWPHJTZU4OLBAMFLGWKWZZJQKVGAAEJV1URHSP'
    CLIENT_SECRET = 'XZEO4JJQ2LYDX5GMTYQJ1S3AKUAHXTWZVOK2YBEOBRHNOFW4'
    REDIRECT_URI = 'http://zitazure.cloudapp.net/'
    DATA_ROOT = '/data'
else:
    CLIENT_ID = 'V2V5MKSOUAVDDPMPACRRQBTKFU3JQTRSMGEBMOXKGL0HX1FL'
    CLIENT_SECRET = 'BBL0BQUIXUEHJG0MSPTNDLC1HKQ2PHSOK3DFKRXANPCV2JYC'
    REDIRECT_URI = 'http://localhost:8000/'
    DATA_ROOT = '../../dataset/'


# access token session key
ACCESS_TOKEN = 'access_token'



# Build paths inside the project like this: os.path.join(BASE_DIR, ...)
BASE_DIR = os.path.dirname(os.path.dirname(__file__))

# Quick-start development settings - unsuitable for production
# See https://docs.djangoproject.com/en/1.6/howto/deployment/checklist/

# SECURITY WARNING: keep the secret key used in production secret!
SECRET_KEY = 'qn_n#a@+^ml40+yrlq8ymrr#ct2x)p)dp5!f0jvd77aoc4!x6s'

# SECURITY WARNING: don't run with debug turned on in production!
DEBUG = True

TEMPLATE_DEBUG = True

ALLOWED_HOSTS = []


# Application definition

INSTALLED_APPS = (
    'visualizer',
    'django.contrib.admin',
    'django.contrib.auth',
    'django.contrib.contenttypes',
    'django.contrib.sessions',
    'django.contrib.messages',
    'django.contrib.staticfiles',
)

MIDDLEWARE_CLASSES = (
    'django.contrib.sessions.middleware.SessionMiddleware',
    'django.middleware.common.CommonMiddleware',
    # 'django.middleware.csrf.CsrfViewMiddleware',
    'django.contrib.auth.middleware.AuthenticationMiddleware',
    'django.contrib.messages.middleware.MessageMiddleware',
    'django.middleware.clickjacking.XFrameOptionsMiddleware',
)

ROOT_URLCONF = 'fsrecommender.urls'

WSGI_APPLICATION = 'fsrecommender.wsgi.application'


# Database
# https://docs.djangoproject.com/en/1.6/ref/settings/#databases

DATABASES = {
    'default': {
        'ENGINE': 'django.db.backends.sqlite3',
        'NAME': os.path.join(BASE_DIR, 'fsrecommender.sqlite3'),
    }
}

# Internationalization
# https://docs.djangoproject.com/en/1.6/topics/i18n/

LANGUAGE_CODE = 'en-us'

TIME_ZONE = 'UTC'

USE_I18N = True

USE_L10N = True

USE_TZ = True


# Static files (CSS, JavaScript, Images)
# https://docs.djangoproject.com/en/1.6/howto/static-files/

STATIC_URL = '/static/'

TEMPLATE_DIRS = (
    os.path.join(BASE_DIR, 'templates'),
)

STATICFILES_DIRS = (
    os.path.join(BASE_DIR, 'static'),
)

TEMPLATE_CONTEXT_PROCESSORS = (
    "django.core.context_processors.request",
    "django.contrib.auth.context_processors.auth",
    "django.core.context_processors.debug",
    "django.core.context_processors.i18n",
    "django.core.context_processors.media",
    "django.core.context_processors.static",
    "django.core.context_processors.tz",
    "django.contrib.messages.context_processors.messages"
)