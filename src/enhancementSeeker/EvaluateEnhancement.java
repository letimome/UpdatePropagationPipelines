package enhancementSeeker;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.CreateBranchCommand.SetupUpstreamMode;
import org.eclipse.jgit.api.errors.CheckoutConflictException;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.api.errors.InvalidRefNameException;
import org.eclipse.jgit.api.errors.RefAlreadyExistsException;
import org.eclipse.jgit.api.errors.RefNotFoundException;
import org.eclipse.jgit.diff.DiffEntry;
import org.eclipse.jgit.lib.ObjectId;
import org.eclipse.jgit.lib.Ref;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.revwalk.RevObject;
import org.eclipse.jgit.revwalk.RevWalk;


import util.DifferenceComputer;
import util.MetricComputer;

public class EvaluateEnhancement {
	
	/*static String coreRepoPath= "/Users/Onekin/Desktop/coreRepo";//args[0];
	static String productRepoPath="/Users/Onekin/Desktop/productRepo";//args[1];
	static String pathToIdentifiedChangeCommits="/Users/Onekin/Desktop/interestingCommits.csv";//;args[4];
	
	static String pathToPropagationUnits="/Users/Onekin/Desktop/propagationUnits.csv";//;args[4];
	*/

	

	static String coreRepoPath= "./coreRepo/";//args[0];
	static String productRepoPath="./productRepo/";
	static String pathToIdentifiedChangeCommits="./../IdentifyEnhancements/interestingCommits.csv";//;args[4];
	static String pathToPropagationUnits="./propagationUnits.csv";//;args[4];

	
	
	static int featureImportance=10;
	static int linesAdded=20000;
	static int linesDeleted=50;
	static int filesChanged=20;
	
	
	public static void main(String[] args) {
		List<ObjectId> commitList = new ArrayList<ObjectId>();
		//1: read interesting commits from file
		commitList = readIdentifiedCommitsFromFile(pathToIdentifiedChangeCommits,coreRepoPath);
		
		//2: compute metrics for each of the commits and if evaluated to good
		List<ObjectId> commitsToPropagate = new ArrayList<ObjectId>();
		commitsToPropagate=evaluateCommitsToPropagate(commitList);// it returns only the commits evaluated as "good" to be propagated
		
		//3: For each commit compute what changed and write / the files (paths)  to propagate to product repository
		if (commitsToPropagate==null){
			System.out.println("Nothing to propagate. Exit -1");
			return;
		} 
		Iterator<ObjectId> i = commitsToPropagate.iterator();
		ObjectId commitObjectId;
		List<DiffEntry> diffs= null;
		
		DiffEntry diffEntry = null;
		Iterator<DiffEntry> diffIte;
		//ArrayList<String> propagationUnits= new ArrayList<String>();
		PrintWriter writer = null;
		
		
		try{
			Git git = Git.open(new File (coreRepoPath));
			Repository coreRepo = git.getRepository();
			RevWalk walk = new RevWalk(coreRepo);
			
			RevCommit revCommit, parent;// = walk.parseCommit(objectIdOfCommit);
			writer = new PrintWriter(pathToPropagationUnits, "UTF-8");
			writer.println("features/ViewMovieDetail/MPEGDecoder/ListFrame.java");
			writer.println("features/ViewMovieDetail/MPEGDecoder/VODClient.java");
			writer.close();
			/*while(i.hasNext()){
				commitObjectId=i.next();
				
				revCommit = walk.parseCommit(commitObjectId);
				parent = revCommit.getParent(0);
				
				diffs = DifferenceComputer.getChangedFilesBetweenTwoCommits(productRepoPath, parent, commitObjectId);
				diffIte=diffs.iterator();
				while(diffIte.hasNext()){
					diffEntry=diffIte.next();
					writer.print(diffEntry.getNewPath());//Prints the path of the file to propagate, comma separated
					if (diffIte.hasNext()) writer.print(",");
				}
				
				System.out.println("File to propagate: "+ diffEntry.getNewPath());
				writer.println();//new propagation Unit
			}//end while
			writer.close();*/
		}catch (Exception e) {
			e.printStackTrace();
		}
		return;
	}
	
	private static List<ObjectId> evaluateCommitsToPropagate(List<ObjectId> commitList) {
		//iterar por cada commit y llamar a evaluateCommit
		return commitList;
	}


	private static boolean evaluateCommit(RevCommit commit, int featureImportance, int linesAdded, int linesDeleted, int filesChanged) {
		MetricComputer.computeMetricsForCommit(commit);
		
		/// RULES!!!
		return true;
	}
	
	private static List<ObjectId> readIdentifiedCommitsFromFile(String pathToIdentifiedChangeCommits2, String coreRepoPath) {
		ObjectId commit;
		ArrayList<ObjectId> commitList = new ArrayList<ObjectId>() ;
		try {
			
			Git git = Git.open(new File (coreRepoPath));
			Repository coreRepo = git.getRepository();
	        
	        //Ref master = repo.findRef("master.baseline");	System.out.print(master.getTarget().getObjectId());
			Ref develop = coreRepo.findRef("develop"); 
			
			if (develop == null){
	            	develop = git.checkout().setCreateBranch(true).
	            			setName("develop").
	            			setUpstreamMode(SetupUpstreamMode.TRACK).
	            			setStartPoint("origin/" + "develop").call();
	        }
			
			BufferedReader br = new BufferedReader(new FileReader(pathToIdentifiedChangeCommits2));
		    StringBuilder sb = new StringBuilder();
		    String line = br.readLine(); //lines are commitIds
	
		    while (line != null) {
		        sb.append(line);
		        System.out.println("line:"+line);
		        commit= coreRepo.resolve(sb+"^{commit}");
		        System.out.println("Read commit: "+commit.getName());
				commitList.add(commit);
		        line = br.readLine();
		        break;//solo funciona con uno.s
		    }
		    br.close();
		    System.out.println("fuera del evaluate");
		    return commitList;
		} catch (Exception e) {
			 System.out.println("arazoa irakurtzen:"+e.getStackTrace()+"\n"+e.getMessage());
			
			 e.printStackTrace();
		} 
		return commitList;
	}

}
