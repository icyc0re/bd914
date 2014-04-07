#!/usr/bin/python

import json
import matplotlib
from numpy import mean, std
import os.path
import pickle
import sys

import matplotlib.pyplot as plt


def save_obj(obj, name ):
    with open(name, 'wb') as f:
        pickle.dump(obj, f, pickle.HIGHEST_PROTOCOL)

def load_obj(name ):
    with open(name, 'r') as f:
        return pickle.load(f)

def main():    

    if len(sys.argv) != 4:
        print 'usage: users_interactions.py <users_dir> <output_raw_stats> <output_stats_file'
        sys.exit (1)
        
    users_paths = [os.path.join(sys.argv[1],f) for f in os.listdir(sys.argv[1])]# if os.path.isfile(f)]
    
    user_interactions = dict()
            
    interactions = ["tips", "photos", "mayorships"]
    interactions_count = dict.fromkeys(interactions+["all"])
    
    counter=0
    
    if not os.path.isfile(sys.argv[2]):
    
        for user_path in users_paths:
            with open(user_path,'r') as outfile:
                try:
                    user=json.load(outfile)
                except ValueError:
                    print "ValueError: No JSON object could be decoded from "+user_path
                    continue
            
            #print os.path.basename(user_path)
            
            interactions_count["all"]=0
            
            for interaction in interactions:
                interactions_count[interaction] = user["user"][interaction]["count"]
                #sys.stdout.write(interaction +"\t"+ str(interactions_count[interaction])+"\t")
                interactions_count["all"] += interactions_count[interaction]
            
            # remove users with more than 100 interactions of unique type, there are very likely to be companies or garbage
            if max([interactions_count[interaction] for interaction in interactions]) < 100:
                user_interactions[os.path.basename(user_path)]=dict(interactions_count)
            
            counter+=1
            if counter % 1000 == 0:
                print counter
        
        save_obj(user_interactions, sys.argv[2])
    
    else:
        user_interactions = load_obj(sys.argv[2])
        
    
    interactions_stats = dict.fromkeys(interactions+["all"]) # list of number of interaction for each user
    friends_count= dict.fromkeys(interactions+["all"]) # aggregated list 
    # init list
    for interaction in interactions+["all"]: 
        interactions_stats[interaction] = list()
        friends_count[interaction] = list()
    
    # get list per user
    for user in user_interactions.keys(): #for each user
        for interaction in interactions+["all"]: #for each interaction of that user
            (interactions_stats[interaction]).append(user_interactions[user][interaction])

    matplotlib.use('Agg') #use png
    means = dict.fromkeys(interactions+["all"])
    stds = dict.fromkeys(interactions+["all"])
    
    # aggregate data
    for interaction in interactions+["all"]:
        # count number of occurrences of value in list
        friends_count[interaction] = dict((interaction_stat,interactions_stats[interaction].count(interaction_stat)) for interaction_stat in interactions_stats[interaction])
        # plot histogram
        plt.hist(friends_count[interaction].values(), bins=range(0,max(friends_count[interaction].keys())))
        plt.savefig(interaction+".png")
        #plt.show()
        # get mean and std
        means[interaction] = mean(interactions_stats[interaction])
        stds[interaction] = std(interactions_stats[interaction])
        print interaction
        print "\tmean: " + str(means[interaction])
        print "\tstd: " + str(stds[interaction])
    
    # print to stats file
    with open(sys.argv[3],'wb') as outfile:
        for interaction in interactions+["all"]:
            print >> outfile, interaction
            print >> outfile, "\tmean: " + str(means[interaction])
            print >> outfile, "\tstd: " + str(stds[interaction])
    
        
        
if __name__ == '__main__':
    main()
