#!/usr/bin/python

import sys
import os.path
sys.path.append(os.path.join(os.path.dirname(os.path.abspath(__file__)),"foursquare/"))
import foursquare
import json
import time

def main():    
    
    if len(sys.argv) != 4:
        print 'usage: pull_venues_from_interactions.py <login_file> <users_path> <output_directory>'
        sys.exit (1)
        
    interactions_types = ["mayorships", "tips", "photos"]
    
    users_path = sys.argv[2]
    #retrieve credentials
    with open(sys.argv[1],'r') as login_file:
        login = login_file.readlines()
        
    # Construct the client object
    client = foursquare.Foursquare(client_id=login[0], 
                                   client_secret=login[1], 
                                   redirect_uri=login[2])

    output_directory = sys.argv[3]
    
    # for each type of interaction 
    for interaction_type in interactions_types:
        
        interactions_path = os.path.join(users_path,interaction_type)
        
        interaction_path = [os.path.join(interactions_path,f) for f in os.listdir(interactions_path)]# if os.path.isfile(f)]
        
        
        # for each user interaction file
        for interaction_file in interaction_path:
            
            try:
                with open(interaction_file,'r') as f:
                    user_interaction = json.load(f)#, indent=4) #    to make it human readable, but oc use more space
        
                # for each venue in user interaction
                i = 0
                while i< len(user_interaction[interaction_type]["items"]):
                    item = user_interaction[interaction_type]["items"][i]
                    venue_id = item["venue"]["id"]
                    if os.path.exists(os.path.join(output_directory, venue_id)):
                        print venue_id+' already exists' 
                        i += 1
                        continue
                    # pull venue
                    venue = client.venues(venue_id);
                    with open(os.path.join(output_directory, venue_id),'w') as outfile:
                        json.dump(venue, outfile, indent=4) #    to make it human readable, but oc use more space
                    
                    print 'saved '+venue_id+' from user '+os.path.basename(interaction_file)+'\'s '+interaction_type
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


# i = 0
#         # for each user interaction file
#         while i <len(interaction_path):
#             
#             try:
#                 with open(interaction_path[i],'r') as f:
#                     user_interaction = json.load(f)#, indent=4) #    to make it human readable, but oc use more space
#         
#                 # for each venue in user interaction
#                 for item in user_interaction[interaction_type]["items"]:
#                     venue_id = item["venue"]["id"]
#                     if os.path.exists(os.path.join(output_directory, venue_id)):
#                         print venue_id+' already exists' 
#                         i += 1
#                         continue
#                     # pull venue
#                     venue = client.venues(venue_id);
#                     with open(os.path.join(output_directory, venue_id),'a') as outfile:
#                         json.dump(venue, outfile, indent=4) #    to make it human readable, but oc use more space
#                     i += 1
#                     print 'saved '+venue_id+' from user '+os.path.basename(interaction_path[i])+'\'s '+interaction_type
#             