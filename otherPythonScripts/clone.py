import csv
from git import Repo

with open('../Alexis-dataset/ML-PythonProjects-WithTravisCI - ML-PythonProjects-WithTravisCI.csv', 'r', newline='') as csv_file:
    csv_reader = csv.DictReader(csv_file)
    clonedProjects = []
    header = ['RepoName', 'path']
    for line in csv_reader:
        print(line)
        #replace forward slash with dash
        pathToRepo = '../Alexis-dataset/clonedRepos/' + line['RepoName'].replace('/', '-')
        #convert https url to ssh url
        sshURL = line['GitHubURL'].replace('https://github.com/', 'git@github.com:')
        try:
            Repo.clone_from(sshURL, pathToRepo)
            print('Cloned ' + line['RepoName'] + ' to ' + pathToRepo+' from '+sshURL)
        except:
            #repo was probably deleted add to error log
            with open('errorLog.txt', 'a') as errorLog:
                errorLog.write('Error cloning '+ line['RepoName'] +'\n')
        
        #attempt to to init the repo if it fails add to error log   
        try:
            repo = Repo(pathToRepo)
            #check out head commit
            repo.git.checkout(repo.head.commit)
            clonedProjects.append({"RepoName": line['RepoName'], "path": pathToRepo})
        except:
            with open('errorLog.txt', 'a') as errorLog:
                errorLog.write('Error checking out '+ line['RepoName'] +'\n')

with open('clonedProjects.csv', 'w', newline='') as csv_file:
    csv_writer = csv.DictWriter(csv_file, fieldnames=header)
    csv_writer.writeheader()
    csv_writer.writerows(clonedProjects)
