#!/usr/bin/python

import sys
import os.path
sys.path.append(os.path.join(os.path.dirname(os.path.abspath(__file__)),"foursquare/"))
import foursquare
import json
import time

sys.path.append(os.path.join(os.path.dirname(os.path.abspath(__file__)),"foursquare/"))

def main():	
    
    if len(sys.argv) != 4:
        print 'usage: pull_ids.py <login_file> <ids_file> <output_directory>'
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

    for venue_id in ids:
        try:
            venue_path=os.path.join(sys.argv[3],venue_id)
            if not os.path.isfile(venue_path):
                venue = client.venues(venue_id)
                with open(venue_path,'a') as outfile:
                    json.dump(venue, outfile)#, indent=4) to make it human readable, but oc use more space
                print 'venue '+venue_id+' saved in '+sys.argv[3]
            else:
                print venue_path+' already exists'     
        except foursquare.RateLimitExceeded:
            print 'Rate limit exceeded...waiting 10s'
            time.sleep(10);
        except foursquare.ServerError:
            print 'Foursquare servers are experiencing problems. Please retry and check status.foursquare.com for updates.'
            print '...waiting 10s'
            time.sleep(10);
        except foursquare.Other:
            print 'error while fetching venue...skipped'
            
            
        
if __name__ == '__main__':
    main()
