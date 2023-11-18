import csv
from collections import Counter
from re import sub

#read csv file in ../chris-analysis/Output/
with open("normalized_commands_v3.csv", "r") as f:
    reader = csv.reader(f)
    print(next(reader))
    listOfCommands = list(reader)

#for every list  in listOfCommands turns in into a tuple
#turn a list of list str to a list of tuple str
newListOfCommands = []
for elem in listOfCommands:
    #if lem
    #remove last 2 elements
    elem=elem[:-2]
    label=elem[2]
    key=elem[1]
    newListOfCommands.append(tuple(elem))
    #label=sub(r'(\[addons:apt:packages:)[a-zA-Z_$0-9].*\]','$1]',label) #normalize addons:apt:packages... to addons:apt:packages
    #label=sub(r'(\[apt:packages:)[a-zA-Z_$0-9].*\]', '$1]',label) #normalize apt:packages... to apt:packages
    #label=sub(r'(\[addons:([a-zA-Z\-]+):)(\d+\.\d+)\]', '$1]',label) #normalize addons:mariadb:9.3 to addons:mariadb:
"""   if(label.startswith('addons:apt:packages:')):
        label='addons:apt:packages:PACKAGE-NAME'
    elif(label.startswith('addons:apt:')):
        label='addons:apt:NORMALIZED'
    elif(label.startswith('addons:') and len(label.split(':'))>1):
        #remove last part of label      
        temp=label.split(':')[:-1]
        label=':'.join(temp)
    elif(label.startswith('secure:')):
        label='secure:NORMALIZED'
    elif(key=='branch'):
        label='BRANCH-NAME'
    elif(key=='password'):
        label= 'PASSWORD'
    elif(key=='slack'):
        label= 'SLACK-CHANNEL'
    elif(key=='if'):
        label='IF-CONDITION'
    elif(key=='python'):
        label='PYTHON-VERSION'
    elif(key=='cache'):
        label='CACHE-DIRECTORY'
    elif(key=='apt' and len(label.split(':'))>1):
        temp=label.split(':')[:-1]
        label=':'.join(temp)
    elif(key=='packages'):
        label='PACKAGE-NAME'
    elif(key=='script' or key=='install'):
        if(elem[3].startswith('[') and elem[3].endswith(']')):
            label=extract_text_between_brackets(elem[3])
        elif(elem[3].startswith(' then')and elem[2].startswith('if')):
            label='IF-THEN'

"""
    

most_common_commands = Counter(newListOfCommands).most_common(200)

#write result to most_frequent_env_map_changes.csv
with open('most_frequent_command_changes_expanded_v3.csv', 'w') as f:
    writer = csv.writer(f)
    writer.writerows(most_common_commands)
