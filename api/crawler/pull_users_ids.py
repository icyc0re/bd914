#!/usr/bin/python

import json
import os.path
import re
import sys


# extract values from nested dictionaries and lists whose key is k
def extract(DictIn, k, ListOut):
    if isinstance(DictIn, dict):
        for key, value in DictIn.iteritems():
            if isinstance(value, dict): # If value itself is dictionary
                extract(value, k, ListOut)
            elif isinstance(value, list): # If value itself is list
                for i in value:
                    extract(i, k, ListOut)
            elif key == k:
                ListOut.append(value)

def main():    
    
    if len(sys.argv) != 3:
        print 'usage: pull_users_ids.py <venues_dir> <output_file>'
        sys.exit (1)
    
    ids_file=open(sys.argv[2],'a')
        
    users_ids = set()
    
    venues_paths = [os.path.join(sys.argv[1],f) for f in os.listdir(sys.argv[1])]# if os.path.isfile(f)]
    
    counter=0
    
    for venue_path in venues_paths:
        with open(venue_path,'r') as outfile:
            venue=json.load(outfile)

        ids_in_venue=list()
        #extract all values in json whose key is "id"
        extract(venue,"id", ids_in_venue)
        
        #keep only ids of users, which contains only digits as opposed to other ids
        users_ids_in_venue = set(filter((lambda x: re.search(r'^[0-9]+$', x)), ids_in_venue))
        #print 'found '+str(len(users_ids_in_venue))#+' users in venue '+venue["venue"]["id"]
        users_ids.update(users_ids_in_venue)
        counter+=1
        if counter%1000==0:
            print counter
        
    #save ids in file
    for user_id in users_ids:
        ids_file.write(user_id+'\n    ')
     
            
        
if __name__ == '__main__':
    main()
