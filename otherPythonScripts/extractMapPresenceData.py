import csv
import csv
from collections import Counter
import re
import json



def parse_string_to_dict(input_string:str):
   #remove first and last elem from input string
   input_string=input_string[1:-1]
   input_string=input_string.replace("true","True")
   input_string=input_string.replace("false",'False')
   result = {i.split('=')[0]: (0 if (i.split('=')[1] == 'False') else 1 ) for i in input_string.split(',')}
   return result

def sum_dictionaries(dict1, dict2):
    # Initialize an accumulator dictionary with the keys from both dictionaries
    accumulator = {key: 0 for key in set(dict1.keys()) | set(dict2.keys())}
    # Iterate over the keys and update the accumulator
    for key in accumulator:
        # Add values if the key is present in the dictionaries
        accumulator[key] = dict1.get(key, 0) + dict2.get(key, 0)

    return accumulator

def write_dicts_to_csv(dictionary , dictionary2:dict, csv_filename):
    with open(csv_filename, 'w', newline='') as csv_file:
        writer = csv.writer(csv_file)
        # Write header
        writer.writerow(['Key', 'occurence','change occurence','change frequency'])
        # Write rows
        for key, value in dictionary.items():
            print(key,value)
            val2=dictionary2.get(key)
            writer.writerow([key, value, val2,0 if val2 is 0 else val2/value])    

#read csv file in ../chris-analysis/Output/
with open("/home/alaa/Research/Thesis/chris-analysis/map_presence_data.csv", "r") as f:
    reader = csv.reader(f)
    #skip header
    next(reader)
    print(next(reader))
    listOfMaps = list(reader)
frequencyMap = {}
changeFrequencyMap = {}

for presenceMap in listOfMaps:
    presenceMapDict=parse_string_to_dict(presenceMap[0])
    changePresenceMapDict=parse_string_to_dict(presenceMap[1])
    frequencyMap=sum_dictionaries(frequencyMap,presenceMapDict)
    changeFrequencyMap=sum_dictionaries(changeFrequencyMap,changePresenceMapDict)    

print(frequencyMap)
#write both frequency mps to csv file
write_dicts_to_csv(frequencyMap,changeFrequencyMap,'occurence_results.csv')

             




