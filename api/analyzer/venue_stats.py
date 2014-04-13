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
                if key == 'hours' and prevKey == 'venue':                    
                    stats['hours'] = 1
                elif key == 'popular' and prevKey == 'venue':
                    stats['popular'] = 1
                elif key == 'price' and prevKey == 'venue':
                    stats['price'] = 1
                elif key == 'location' and prevKey == 'venue':
                    stats['location'] = 1
                extract(value, stats,key)
            elif isinstance(value, list): # If value itself is list
                for i in value:
                    if key == 'categories':
                        stats['haveCategories'] = 1
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
            elif key == 'count' and prevKey == 'likes':
                stats['likes'] = value
            elif key == 'rating' and prevKey == 'venue':
                stats['ratingCount'] = 1
                stats['ratingValue'] = value
            elif key == 'name' and prevKey == 'categories' :
                if 'categories' not in stats:
                    stats['categories'] = list()
                stats['categories'].append(value)
            elif key == 'tier' and prevKey == 'price' :
                if 'priceTiers' not in stats:
                    stats['priceTiers'] = list()
                stats['priceTiers'].append(value)
            
            
                
            

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
            if type(value) is list :
                for list_value in value :
                    if list_value in all_stats[key] :
                        all_stats[key][list_value] += 1
                    else :
                        all_stats[key][list_value] = 1
            else :
                if value in all_stats[key]:
                    all_stats[key][value] += 1
                else:
                    all_stats[key][value] = 1
        counter+=1
        if counter%1000==0:
            print counter
    print 'Total number of venues: ' + str(counter)
    # Create folder with result (stats_results) and calculate average and mean
    average_and_median_file=open('stats_results/average_and_median','w')
    for stat_name, stat_dict in all_stats.iteritems():
        valueSum = 0.0
        timesSum = 0.0
        median = 0.0
        is_median_set = 0
        key_stored_for_median_calc = -1.0;
        file=open('stats_results/' + stat_name,'w')
        
        # key is the value of json object and value is the times of its accurance
        for key, value in sorted(stat_dict.iteritems()):
            # print str(key) + ' '+ str(value) + '\n'
            
            if type(key) is int :
                if median == -2 :
                    print stat_name + str(key)
                    median =  (key_stored_for_median_calc + key)/2
                    is_median_set = 1
                if is_median_set == 0 :
                    timesSum += value
                    if timesSum > counter/2 :
                        print stat_name + str(key)
                        median = key
                        is_median_set = 1
                    elif timesSum == counter/2 :
                        median = -2
                        key_stored_for_median_calc = key
                valueSum += value*key
                file.write(str(key) + ' '+ str(value) + '\n')
            elif type(key) is unicode :
                file.write(key.encode('utf-8') + ' '+ str(value) + '\n')
        
        #Calculate the average
        average = valueSum/counter
        average_and_median_file.write(stat_name + '| average: ' + str(average) + ' median: ' + str(median) + '\n')
        
        
            
               
if __name__ == '__main__':
    main()
