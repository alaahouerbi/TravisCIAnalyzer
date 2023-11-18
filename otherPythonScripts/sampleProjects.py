import csv
import random

SAMPLE_SIZE = 343
inputFile='../Alexis-dataset/ML-CommitsFrom-PythonProjects - ML-CommitsFrom-PythonProjects-filtered-from-deleted-projects-onlyIntresting-ML-commits(multipleExtensions).csv'
with open(inputFile, 'r', newline='') as csv_file:
    csv_reader = csv.DictReader(csv_file)
    commits = []
    for line in csv_reader:
        commits.append(line)
    random.shuffle(commits)
    #keep first SAMPLE_SIZE commits
    commits = commits[:SAMPLE_SIZE]
    #add sample keyword to filename before .csv extension
    outputFile = inputFile[:-4] + '-sampled.csv'
    with open(outputFile,'w') as csv_outputfile:
        csv_writer = csv.DictWriter(csv_outputfile, fieldnames=csv_reader.fieldnames)
        csv_writer.writeheader()
        csv_writer.writerows(commits)