#!/usr/bin/python

import json
import os.path
import re
import sys

# extract values from nested dictionaries and lists 
def extract(DictIn, stats, prevKey):
    if isinstance(DictIn, dict):
        for key, value in DictIn.iteritems():
            if isinstance(value, dict): # If value itself is dictionary
                extract(value, stats,key)
            elif isinstance(value, list): # If value itself is list
                for i in value:
                    if key == 'categories':
                        if 'categoriesCount' in stats:
                            stats['categoriesCount'] += 1
                        else:
                            stats['categoriesCount'] = 1
                    elif key == 'tags':
                        if 'tagsCount' in stats:
                            stats['tagsCount'] += 1
                        else:
                            stats['tagsCount'] = 1
                    extract(i, stats, key)
            elif key == 'tipCount' and prevKey == 'stats':
                stats['tipsCount'] = value
            elif key == 'checkinsCount' and prevKey == 'stats':
                stats['checkinsCount'] = value
            elif key == 'usersCount' and prevKey == 'stats':
                stats['usersCount'] = value
            

def main():    
    
    if len(sys.argv) != 2:
        print 'usage: pull_users_ids.py <venues_dir>'
        sys.exit (1)
     
    venues_paths = [os.path.join(sys.argv[1],f) for f in os.listdir(sys.argv[1])]# if os.path.isfile(f)]
    
    counter = 0
    all_stats = dict()
    
    for venue_path in venues_paths:
        with open(venue_path,'r') as outfile:
            venue=json.load(outfile)


        stats = dict()
        # Extract stats for a venue
        extract(venue, stats,'dummyPrevKey')
        
        # Store stats of a venue in dictornaries of dictionaries
        # Stats contain frequencies of specific value occurances
        for key, value in stats.iteritems():
            if key not in all_stats:
                all_stats[key] = dict()
            if value in all_stats[key]:
                all_stats[key][value] += 1
            else:
                all_stats[key][value] = 1
    
        counter+=1
        if counter%1000==0:
            print counter
    
    # Create folder with result (stats_results)
    for stat_name, stat_dict in all_stats.iteritems():
        file=open('stats_results/' + stat_name,'w')
        for key, value in stat_dict.iteritems():
                file.write(str(key) + ' '+ str(value) + '\n')
               
if __name__ == '__main__':
    main()
