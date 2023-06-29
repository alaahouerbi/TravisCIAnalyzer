package com.config;

public class Config {
	// public static String rootDir="/media/AutoBuilder/UnityPerformance/";
	 //public static String
	 //rootDir=".\\Project_Data";
	 public static String
	 rootDir="D:\\Other\\Git Repos\\TravisCIAnalyzer\\";
	 //
	 //text file that contains list of projects to analyze
	 public static String gitProjList=rootDir+"Project_Source.txt";
	 public static String gitProjListNonML=rootDir+"Project_Source_NonML.txt";
	 public static String gitProjListEval=rootDir+"eval_Project_Source.txt";
	
	 

	 //reporDir used for storing Unity Projects
	 public static String repoDirEval= rootDir+"EvalRepos/"; /*rootDir+"GitRepo/";*/	 
	 public static String repoDir = rootDir+"Repository\\";
	 
	 public static String travisRepoDir = "/home/alaa/alexis-project/CloneWithGetPy/travisData";
	
	 public static String csvFile=rootDir+"perf_commit_data_Updated.csv";
	 
	 public static String csvFreqFile=rootDir+"new_new_cmd_frequency_non_ml_unique.csv";
	 
	 public static String csvCmdTypeFile=rootDir+"command_type_new_ML_Non_Ml.csv";
	 
	 //public static String csvCITransitionFile=rootDir+"Tool_transition.csv";
	 public static String csvCITransitionFile="/home/alaa/alexis-project/CloneWithGetPy/travisData/ML.csv";
	 
	 public static String csvBlockCategory=Config.rootDir+"block_type.csv";
			 
	 public static String patchDir=rootDir+"PatchDir\\";

	public static String[] perfCommitToken = { ".." };

	public static int commitid = 1;
	public static int stmtUniqueId = 1;
	
	/* This token expires at the end of 2023, and should be replaced with a token from whoever is working on this then. 
	 * 
	 * To create a secure token like this, go to Github, click your profile picture, and go to 
	 * Settings > Developer Settings > Personal Access Token > Fine-Grained tokens.
	 * 
	 * Only set the required permissions. Cloning public repositories needs only read access to public repositories. 
	 * 
	 * Github accounts can also be used directly, if 2FA is off, but are not recommended. 
	 */
	public static String gitHubUserName="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";
	public static String gitHubPwd="";
	
	public static String[] compoundcmds= {"python","bash", "source", "python3", "sh", "eval", "xvfb-run", "/bin/bash",
			"doit","ruby","unbuffer","catchsegv"};
	
	public static String repoStrDate="08-10-2021";

}