requirements:
- Python 2.7
- Django (1.6)
- Django Rest Framework (pip install djangorestframework)
- foursquare package (https://github.com/mLewisLogic/foursquare) [modified by bernard?]

# start development server
python manage.py runserver

url: 127.0.0.1:8000


# Troubleshoot
- if the database fsrecommender.sqlite3 does not exist (or its dimension is 0kb) run
```python manage.py syncdb
first