import csv
from collections import Counter
import re
#read csv file in ../chris-analysis/Output/
with open("/home/alaa/Research/Thesis/chris-analysis/output_satistical_analysis_expanded_keys_v3.csv", "r") as f:
    reader = csv.reader(f)
    #skip header
    next(reader)
    print(next(reader))
    listOfCommands = list(reader)


lifecyleKeys=['script','install','before_install','after_install','after_success','before_script','deploy','before_deploy'
              ,'after_deploy','after_failure','before_failure','after_script','before_script','before_cache','after_deploy',
              'after_sucess']
normalizedData=[]
for elem in listOfCommands:
    temp=[]
    key=elem[1]
    label=elem[2]

    #if the label starts with key then delete the key part from the label
    if(label.startswith(key+':')):
        label=label[len(key)+1:]
    #if the label starts with key: then delete the key part from the label
    if(len(label.split(' '))>1):
        command=label.split(' ',1)[0]
        params=label.split(' ',1)[1]
    else:
        command=label
        params=''

    #if command is python and param looks like x.y then normalize to python ver
    if((key.lower() == 'python' or key.lower() == 'python-version' or key.lower()=='python_version') and re.match(r'\d+\.\d+',command)):
        command='PYTHON-VERSION'
    normalizedData.append((elem[0],key,command,params,elem[4],elem[5]))


print("len of original data: "+str(len(listOfCommands)))
print("len of normalized data: "+str(len(normalizedData)))


#write result to most_frequent_env_map_changes.csv
with open('normalized_commands_v3.csv', 'w') as f:
    writer = csv.writer(f)
    writer.writerows(normalizedData)
