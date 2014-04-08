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
        print 'usage: users_info.py <users_dir> <output_raw_stats> <output_stats_file'
        sys.exit (1)
        
    users_paths = [os.path.join(sys.argv[1],f) for f in os.listdir(sys.argv[1])]# if os.path.isfile(f)]
    
    user_infos = dict()
            
    infos = ["female", "male", "none", "homeCity", "facebook", "twitter", "non-standard", "friends"]
    genders = ["female", "male", "none"]
    contacts = ["facebook", "twitter"]
    
    infos_count = dict.fromkeys(infos)
    for info in infos:
        infos_count[info]=0
    
    infos_count["friends"]=list()
    
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
            for gender in genders:           
                if user["user"]["gender"] == gender:
                    infos_count[gender] +=1;

            
            if user["user"]["homeCity"] != "":    
                infos_count["homeCity"] +=1
            
            for contact in contacts:
                if contact in user["user"]["contact"]:
                    infos_count[contact] +=1

            if user["user"]["type"] != "user":
                infos_count["non-standard"] +=1
                
            infos_count["friends"].append(user["user"]["friends"]["count"])
                #sys.stdout.write(info +"\t"+ str(infos_count[info])+"\t")
            
            counter+=1
            if counter % 1000 == 0:
                print counter
        
        save_obj(user_infos, sys.argv[2])
    
    else:
        user_infos = load_obj(sys.argv[2])
        
    matplotlib.use('Agg') #use png

# aggregate data

    # count number of occurrences of value in list
    friends_count = dict((number,infos_count["friends"].count(number)) for number in infos_count["friends"])
    # plot histogram
    plt.hist(friends_count.values(), bins=range(0,max(friends_count.keys())))
    plt.savefig("friends.png")
    #plt.show()
    # get mean and std
    means = mean(infos_count["friends"])
    stds = std(infos_count["friends"])

    
    # print to stats file
    with open(sys.argv[3],'wb') as outfile:
        print >> outfile, "gender"
        for gender in genders:           
            print >> outfile, "\t" +gender+ " " + str(infos_count[gender])
        print >> outfile, ""
        print >> outfile, "homeCity: " + str(infos_count["homeCity"])
        print >> outfile, "contact"
        for contact in contacts:
            print >> outfile, "\t" +contact+ " " + str(infos_count[contact])
        print >> outfile, ""
        print >> outfile, "non-standard: " + str(infos_count["non-standard"])
        
        print >> outfile, "friends stats"
        print >> outfile, "\tmean: " + str(means)
        print >> outfile, "\tstd: " + str(stds)


    
        
        
if __name__ == '__main__':
    main()
