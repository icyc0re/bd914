#!/usr/bin/python

import sys
import os.path
sys.path.append(os.path.join(os.path.dirname(os.path.abspath(__file__)),"foursquare/"))
import foursquare
import json
import time

def main():    
    
    if len(sys.argv) != 5:
        print 'usage: pull_users_interactions.py <interaction_type> <login_file> <ids_file> <output_directory>'
        sys.exit (1)
        
    interaction = sys.argv[1]
    
    if interaction not in ["badges", "mayorships", "tips", "photos"]:
        print "interaction type must be \"badges\", \"mayorships\" or \"tips\""
        
    #retrieve credentials
    with open(sys.argv[2],'r') as login_file:
        login = login_file.readlines()
        
    #open id file
    with open(sys.argv[3],'r') as ids_file:
        ids = ids_file.read().splitlines();
    
    # Construct the client object
    client = foursquare.Foursquare(client_id=login[0], 
                                   client_secret=login[1], 
                                   redirect_uri=login[2])

    output_directory = sys.argv[4]
    
    i = 0
    while(i < len(ids)):
        user_id = ids[i]
        try:
            user_interaction_path = os.path.join(output_directory, user_id)
            
            if not os.path.isfile(user_interaction_path):
                user_tips = getattr(client.users, interaction)(user_id)
                with open(user_interaction_path,'a') as outfile:
                    json.dump(user_tips, outfile)#, indent=4) #    to make it human readable, but oc use more space
                print 'user_tips '+user_id+' saved in '+ output_directory
            else:
                print user_interaction_path+' already exists'
            i += 1
        except foursquare.RateLimitExceeded:
            print 'Rate limit exceeded...waiting 60s'
            time.sleep(60)
        except foursquare.ServerError:
            print 'Foursquare servers are experiencing problems. Please retry and check status.foursquare.com for updates.'
            print '...waiting 60s'
            time.sleep(60)
        except foursquare.ParamError:
            print 'foursquare.ParamError: Must provide a valid user_tips ID or \'self.\''
            i += 1
        except foursquare.Other:
            print 'error while fetching user_tips...skipped'
            i += 1
        except foursquare.FoursquareException as e:
            print e
            print '...waiting 60s'
            time.sleep(60)
            
            
        
if __name__ == '__main__':
    main()
