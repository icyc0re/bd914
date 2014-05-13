Website requirements:
--

- Python 2.7
- Django (1.6)
- foursquare package (https://github.com/mLewisLogic/foursquare)


**start development server**
``` python
// python manage.py runserver
```
Website accessible locally to url: 127.0.0.1:8000


**Troubleshoot**
- if the database fsrecommender.sqlite3 does not exist (or its dimension is 0kb) run
```
// python manage.py syncdb
```
first.