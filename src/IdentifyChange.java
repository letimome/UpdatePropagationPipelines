import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.LogCommand;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.api.errors.NoHeadException;
import org.eclipse.jgit.lib.Constants;
import org.eclipse.jgit.lib.ObjectId;
import org.eclipse.jgit.lib.Ref;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.revwalk.RevSort;
import org.eclipse.jgit.revwalk.RevWalk;
import org.eclipse.jgit.storage.file.FileRepositoryBuilder;


public class IdentifyChange {
	
	static String coreRepoPath= "/Users/Onekin/Desktop/VODPlayer-CoreAssets-2";//args[0];
	static String productRepoPath="/Users/Onekin/Desktop/Product-Repository-VODD2";//args[1];
	static Integer timeFrame= 10;//Integer.parseInt(args[2]);
	static String changeTypeToPropagate="enhancement";//args[3];; //change types considered are: "hotfix", "enhancement", "new feature"
	static String pathToIdentifiedChangeCommits="/Users/Onekin/Desktop/identifyChange";//;args[4];
	static String msgKeyWord = null;

	public static void main(String[] args) {
	/*	if (args.length!=6){
			System.out.println ("Parameters not completed");
			return;
		}
		*/
		//Parameters
		
		
		
		if (changeTypeToPropagate=="enhancement")
			msgKeyWord="feature/";
		else if (changeTypeToPropagate=="bugfix")
			msgKeyWord="hotfix/";
		
		String pathToInterestingCommitShasString;
		
		experimental();
		
		ArrayList<String> commitSha = findCommitsInterestingToProduct(coreRepoPath, productRepoPath, msgKeyWord,timeFrame);
		//post content commitSha array List to a local file	
	}


	private static ArrayList<String> findCommitsInterestingToProduct(String coreRepoPath, String productRepoPath, String msgKeyWord, Integer timeFrame) {
		// TODO Auto-generated method stub
		return null;
	}
	

	
	private static void experimental() {
		try {

			Git git = Git.open(new File (coreRepoPath));
	        Repository coreRepo = git.getRepository();
	        
	        //Ref master = repo.findRef("master.baseline");	System.out.print(master.getTarget().getObjectId());
			Ref develop = coreRepo.findRef("develop.coreAssets"); System.out.print(develop.getTarget().getObjectId());
		
			
			Iterable<RevCommit> logs = git.log().add(develop.getObjectId()).call();
	         for (RevCommit commit : logs) {
	             if(commit.getFullMessage().indexOf(msgKeyWord)!=-1){
	            	 System.out.println("\n\n\n----------------------------------------");System.out.println("commit    "  + commit);
		             System.out.println("commit Id   "  + commit.toObjectId());System.out.println("commit email "  + commit.getAuthorIdent().getEmailAddress()); System.out.println("commit author "  + commit.getAuthorIdent().getName());
		             System.out.println("commit time "  + commit.getAuthorIdent().getWhen());System.out.println(" commit Message " + commit.getFullMessage());  System.out.println("-----------------------------");
	             }
	        	 
	             
	         }

	         } catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		
	}

}
