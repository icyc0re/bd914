#!/usr/bin/python

import sys
import os.path
sys.path.append(os.path.join(os.path.dirname(os.path.abspath(__file__)),"foursquare/"))
import foursquare
import math
import time

#add offset in meters to initial lat and lon
#return new lat and lon
def compute_new_geolocation(lat, lon, lat_offset, lon_offset):
    #Earth s radius, sphere
    R=6378137

    #Coordinate offsets in radians
    dLat = lat_offset/R
    dLon = lon_offset/(R*math.cos(math.pi*lat/180))
    
    #OffsetPosition, decimal degrees
    latO = lat + dLat * 180/math.pi
    lonO = lon + dLon * 180/math.pi
    
    return latO, lonO

def main(): 
    
    if len(sys.argv) != 3:
        print 'usage: pull_ids.py <login_file> <output_file>, see README'
        sys.exit (1)
    
    #retrieve credentials
    with open(sys.argv[1],'r') as login_file:
        login = login_file.readlines()
    
    # Construct the client object
    client = foursquare.Foursquare(client_id=login[0], 
                                   client_secret=login[1], 
                                   redirect_uri=login[2])
    
    square_size=50.0 #square size to search in meters
    
    manhattan_brooklin={'n':40.913515,'e':-73.689612,'s':40.534680,'w':-74.039802}
    staten_island={'n':40.647827,'e':-73.702659,'s':40.526851,'w': -74.271888}
    full_New_York={'s':40.487693,'n':40.917923,'w':40.494484,'e':-74.040488}
    
    #start point at north east
    latitude = staten_island['n']
    longitude= staten_island['e']
    start_longitude = staten_island['e']

    min_latitude = staten_island['s']
    min_longitude = staten_island['w']
    
    #I ALREADY PULL UP TO THAT GEOLOCATION, SO START FROM HERE
    #latitude = 40.647827
    #longitude= -73.702659

    #pull venues on 50m^2 squares
    with open(sys.argv[2],'a') as outfile:
        #move vertically
        while latitude > min_latitude:

            #move search area <square_size> to south
            new_latitude,longitude=compute_new_geolocation(latitude, longitude, -square_size, 0)
            mul_factor = 1.0
            
            #move horizontally
            while longitude > min_longitude:
                #move search area <square_size> to west
                latitude,new_longitude = compute_new_geolocation(latitude, longitude, 0, -(square_size * mul_factor))
                print 'pulling from area ne:' + str(latitude)+',' +str(longitude) + ', sw:' + str(new_latitude)+',' + str(new_longitude) + '; square size ' + str(square_size * mul_factor)
                                
                try:
                    search = client.venues.search(params={'sw':str(new_latitude)+','+str(new_longitude),
                                                          'ne':str(latitude)+','+str(longitude),
                                                          'limit':'50', 'intent':'browse'})
                    venues_ids = [venue["id"] for venue in search["venues"]]
                    venues_count = len(venues_ids)
                    print '\t fetched '+ str(venues_count) +' venues ids'
                    if venues_count == 50 and mul_factor > 0.24:
                        mul_factor /=2      
                    else:
                        mul_factor+=1

                        #write ids to file
                        outfile.write('ne:' + str(latitude)+',' +str(longitude) + ', sw:' + str(new_latitude)+',' + str(new_longitude)+'\n')
                        for venue_id in venues_ids:
                            print>>outfile, venue_id
                        longitude=new_longitude

                except foursquare.RateLimitExceeded:
                    print 'Rate limit exceeded...waiting 10s'
                    time.sleep(10);
            
            latitude = new_latitude
            
            #reset longitude to east
            longitude = start_longitude
        
if __name__ == '__main__':
    main()
