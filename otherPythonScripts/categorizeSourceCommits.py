import csv
from git import Repo
import os

fileEndingsWeCareAbout=['.py','.h5','.pkl','.pickle','.pb','.db','.sqlite','.parquet','.onnx','.pmml','.joblib']
directoriesWeCareAbout=['data','model','train','training','test','pipeline','predict','correctness','deploy','inference','preprocess']

def howManyPythonFilesChanged(changedFiles):
    pythonFilesChanged=0
    for file in changedFiles:
        if file.endswith('.py'):
            pythonFilesChanged+=1
    return pythonFilesChanged


def isCommitIntresting(changedFiles):
    #if commit changes 1 file return false
    if len(changedFiles) == 1:
        return False
    #if too many files are changed it is hard to tell what was the purpose of the commit
    if howManyPythonFilesChanged(changedFiles) >= 20:
        return False
    for file in changedFiles:
        #if at least 1 file ends with a good file ending or its in a good directory return true
        if file.endswith(tuple(fileEndingsWeCareAbout)) and any(substring in file for substring in directoriesWeCareAbout):
            return True
    return False


with open('../Alexis-dataset/ML-CommitsFrom-PythonProjects - ML-CommitsFrom-PythonProjects-filtered-from-deleted-projects.csv', 'r', newline='') as csv_file:
    csv_reader = csv.DictReader(csv_file)
    
    intrestingCommits = []
    boringCommits = []
    for line in csv_reader:
        print(line['ProjectName'])
        try:
            if(os.path.isdir('../Alexis-dataset/clonedRepos/'+line['ProjectName'].replace('/', '-')) == False):
                print('Repo not found: '+line['ProjectName'])
            repo = Repo('../Alexis-dataset/clonedRepos/'+line['ProjectName'].replace('/', '-'))
            commit=repo.commit(line['CommitID'])
            changedFiles=commit.stats.files.keys()
            if isCommitIntresting(changedFiles):
                intrestingCommits.append(line)
            else:
                boringCommits.append(line)
        except:
            #write to error log commit not found
            with open('errorLog.txt', 'a') as errorLog:
                errorLog.write('Error getting commit '+ line['CommitID'] +' from '+ line['ProjectName'] +'\n') 

    with open('../Alexis-dataset/ML-CommitsFrom-PythonProjects - ML-CommitsFrom-PythonProjects-filtered-from-deleted-projects-onlyIntresting-ML-commits(multipleExtensions).csv','w') as csv_outputfile:
        csv_writer = csv.DictWriter(csv_outputfile, fieldnames=csv_reader.fieldnames)
        csv_writer.writeheader()
        csv_writer.writerows(intrestingCommits)
    with open('../Alexis-dataset/ML-CommitsFrom-PythonProjects - ML-CommitsFrom-PythonProjects-filtered-from-deleted-projects-onlyBoring-non-ML-commits(multipleExtensions).csv','w') as csv_outputfile:
        csv_writer = csv.DictWriter(csv_outputfile, fieldnames=csv_reader.fieldnames)
        csv_writer.writeheader()
        csv_writer.writerows(boringCommits)