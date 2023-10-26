import csv

# Input CSV file name
input_csv_file = "ML-SampledCommitsFrom-PythonProjects.csv"

# Output text file name
output_text_file = "github_links.txt"

# Function to extract GitHub HTTPS links from project names
def extract_github_links(csv_file):
    github_links = []
    
    with open(csv_file, 'r', newline='') as csvfile:
        reader = csv.reader(csvfile)
        #skip header
        next(reader)
        for row in reader:
            if len(row) >= 2:
                project_name = row[1]  # Assuming the second column contains project names
                #project name format is username/repo
                project_name 
                github_links.append(f"https://github.com/{project_name}.git")
                

    
    return github_links

# Write GitHub links to the output text file
def write_github_links_to_file(links, output_file):
    with open(output_file, 'w') as txtfile:
        for link in links:
            txtfile.write(link + '\n')

if __name__ == "__main__":
    github_links = extract_github_links(input_csv_file)
    
    if github_links:
        write_github_links_to_file(github_links, output_text_file)
        print(f"GitHub links extracted and saved to {output_text_file}")
    else:
        print("No valid GitHub links found in the CSV file.")
