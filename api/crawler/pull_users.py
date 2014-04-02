#!/usr/bin/python

import sys
import os.path
sys.path.append(os.path.join(os.path.dirname(os.path.abspath(__file__)),"foursquare/"))
import foursquare
import json
import time

def main():    
    
    if len(sys.argv) != 4:
        print 'usage: pull_users.py <login_file> <ids_file> <output_directory>'
        sys.exit (1)
        
    #retrieve credentials
    with open(sys.argv[1],'r') as login_file:
        login = login_file.readlines()
        
    #open id file
    with open(sys.argv[2],'r') as ids_file:
        ids = ids_file.read().splitlines();
    
    # Construct the client object
    client = foursquare.Foursquare(client_id=login[0], 
                                   client_secret=login[1], 
                                   redirect_uri=login[2])

    
    i = 0
    while(i < len(ids)):
        user_id = ids[i]
        try:
            user_path=os.path.join(sys.argv[3],user_id)
            if not os.path.isfile(user_path):
                user = client.users(user_id)
                with open(user_path,'a') as outfile:
                    json.dump(user, outfile)#, indent=4) #    to make it human readable, but oc use more space
                print 'user '+user_id+' saved in '+sys.argv[3]
            else:
                print user_path+' already exists'
            i += 1
        except foursquare.RateLimitExceeded:
            print 'Rate limit exceeded...waiting 60s'
            time.sleep(60)
        except foursquare.ServerError:
            print 'Foursquare servers are experiencing problems. Please retry and check status.foursquare.com for updates.'
            print '...waiting 60s'
            time.sleep(60)
        except foursquare.ParamError:
            print 'foursquare.ParamError: Must provide a valid user ID or \'self.\''
            i += 1
        except foursquare.Other:
            print 'error while fetching user...skipped'
            i += 1
            
            
        
if __name__ == '__main__':
    main()
