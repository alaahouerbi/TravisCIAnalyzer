
import csv
import sys
import re
import pickle as pkl
import pandas as pd
import json 
#define class changeData to store the json data
class changeData:
    def __init__(self, changeType, changeLocation, changeContent):
        self.changeType = changeType
        self.changeLocation = changeLocation
        self.changeContent = changeContent
    
    #define hashing function
    def __hash__(self):
        return hash((self.changeType, self.changeLocation, self.changeContent))

#read csv file in ../chris-analysis/Output/
with open("/home/alaa/Research/Thesis/chris-analysis/Output/ML-SampledCommitsFrom-PythonProjects_Output.csv", "r") as f:
    reader = csv.reader(f)
    listOfCommits = list(reader)

#remove header
listOfCommits.pop(0)


allTransactions = []
for row in listOfCommits:
    print(row)
    if(row[8]==''):
        continue

    #read file in row[8] and translate the json to an object
    with open(row[8], "r") as f:
        change = json.load(f)

        arrayForDataFrame = []
        for elem in change:
            parent = elem['parent']
            action = elem['action']
            changeContent = elem['change']
            print(elem)
            #take whatever is in change remove all linebreaks and grab whatever is between the outermost quotes
            changeContent = re.sub(r'\n', '', changeContent)
            #remove all linebreaks and double spaces and tabs 
            changeContent = re.sub(r'\t', '', changeContent)
            changeContent = re.sub(r'  ', '', changeContent)
            changeContent = re.sub(r'\r', '', changeContent)
            changeContent = re.sub(r'\"\"\"', '', changeContent)

            arrayForDataFrame.append((parent,action,changeContent))
        
        allTransactions.append(arrayForDataFrame)


#save allTransactions in a pickle file
with open('allTransactions.pkl', 'wb') as f:
    pkl.dump(allTransactions, f)


        
        



