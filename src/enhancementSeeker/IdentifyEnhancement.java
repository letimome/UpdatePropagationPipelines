package enhancementSeeker;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.LogCommand;
import org.eclipse.jgit.api.CreateBranchCommand.SetupUpstreamMode;
import org.eclipse.jgit.api.errors.CheckoutConflictException;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.api.errors.InvalidRefNameException;
import org.eclipse.jgit.api.errors.NoHeadException;
import org.eclipse.jgit.api.errors.RefAlreadyExistsException;
import org.eclipse.jgit.api.errors.RefNotFoundException;
import org.eclipse.jgit.diff.DiffEntry;
import org.eclipse.jgit.lib.Constants;
import org.eclipse.jgit.lib.ObjectId;
import org.eclipse.jgit.lib.Ref;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.revwalk.RevSort;
import org.eclipse.jgit.revwalk.RevWalk;
import org.eclipse.jgit.storage.file.FileRepositoryBuilder;

import com.sun.xml.internal.ws.api.pipe.NextAction;

import util.DifferenceComputer;
import pipeline.IdentifyChange;

public class IdentifyEnhancement extends IdentifyChange{
	
static //	/** For desktop develop
	String where="jenkins";//leticia/jenkins
	
	static String coreRepoPath ;
	static String productRepoPath;
	static String pathToIdentifiedChangeCommits;

	static Integer timeFrame= 7;//Integer.parseInt(args[2]);
	static String changeTypeToPropagate="enhancement";//args[3];; //change types considered are: "hotfix", "enhancement", "new feature"
	
	
	static String msgKeyWord = null;
	static String branchToLookInto = "develop";

	public static void main(String[] args) {
		
		
			System.out.println("Inside the algorithm!");
			
			  coreRepoPath= "./coreRepo";//args[0];
			  productRepoPath="./productRepo";
			  pathToIdentifiedChangeCommits="./interestingCommits.csv";//;args[4];

			  
			// coreRepoPath= "/Users/"+where+"/Desktop/VODPlayer-CoreAssets-2";//args[0];
		//productRepoPath="/Users/"+where+"/Desktop/Product-Repository-VODD2";//args[1];
		//pathToIdentifiedChangeCommits="/Users/"+where+"/Desktop/PipelineData/identifyCommits.csv";//;args[4];
//			
		//}
	
		
	/*	if (args.length!=5){
			System.out.println ("Parameters not completed. "+args.length);
			return;
		}	
		*/
		//experimental();
		//extractParameters(args);
		
		msgKeyWord = extractKeywordsForChangeType(changeTypeToPropagate);
		
		/*Identify interesting commits - potential to propagate to products*/
		ArrayList<RevCommit> commitList = new ArrayList<RevCommit>();
		commitList = findEnhancementsInterestingToProduct(coreRepoPath, productRepoPath, msgKeyWord,timeFrame);
		
		System.out.println(commitList.size());
		
		/* post content commitSha array List to a local file*/
		if (!commitList.isEmpty()){
			Iterator<RevCommit> i= commitList.iterator();
			PrintWriter writer;
			try {
				writer = new PrintWriter(pathToIdentifiedChangeCommits, "UTF-8");
				RevCommit com;
				while (i.hasNext()){
					com=i.next();
					writer.println(com.getName());//Prints the commit number // It could also be printed more commit meta-data if needed
				}
				writer.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
	}



	private static void extractParameters(String[] args) {
		coreRepoPath = args[0];
		productRepoPath = args[1];
		timeFrame= Integer.parseInt(args[2]);
		changeTypeToPropagate = args[3]; //change types considered are: "hotfix", "enhancement", "new feature"
		pathToIdentifiedChangeCommits = args[4];
		
	}


	private static String extractKeywordsForChangeType(
			String changeTypeToPropagate2) {
		String key=null;
		if (changeTypeToPropagate=="enhancement")
			key="feature/";
		else if (changeTypeToPropagate=="bugfix")
			key="hotfix/";
		return key;
	}

	private static ArrayList<RevCommit> findEnhancementsInterestingToProduct(String coreRepoPath, String productRepoPath, String msgKeyWord, Integer timeFrame) {
		//1 identify enhancement commits
		//2 get only those that are interesting to products (the enhancements core assets are being reused by products)
		try {
			ArrayList<RevCommit> listOfInterestingCommits=new ArrayList<RevCommit>();
			Git git = Git.open(new File (coreRepoPath));
	        Repository coreRepo = git.getRepository();
	        
	        //Ref master = repo.findRef("master.baseline");	System.out.print(master.getTarget().getObjectId());
			Ref develop = coreRepo.findRef(branchToLookInto); 
			
            if (develop == null){
            	develop = git.checkout().setCreateBranch(true).
            			setName(branchToLookInto).
            			setUpstreamMode(SetupUpstreamMode.TRACK).
            			setStartPoint("origin/" + branchToLookInto).call();
            }
            
			Iterable<RevCommit> logs = git.log().add(develop.getObjectId()).call();
	         for (RevCommit commit : logs) {
	             if(commit.getFullMessage().indexOf(msgKeyWord)!=-1){// if "feature/" is in the commit message
	            	 System.out.println("\n\n\n------Looking in core Asset repo develop branch-------------------"); System.out.println("Interesting commit:"  + commit);
	            	 System.out.println("Message " + commit.getFullMessage()); 
		             //System.out.println("commit Id   "  + commit.toObjectId());System.out.println("commit email "  + commit.getAuthorIdent().getEmailAddress()); System.out.println("commit author "  + commit.getAuthorIdent().getName());
		            // System.out.println("commit time "  + commit.getAuthorIdent().getWhen()); System.out.println("-----------------------------");
		             //System.out.println("commit parent:"+ commit.getParentCount());
		            // System.out.println("commit parents:"+ commit.getParents());
		             
		             if (commitChangesInterestingForProduct(commit,productRepoPath)){// and if enhancements to cores are reusable by products
		            	 System.out.println("Inseting commitId" +commit.getName());
		            	 listOfInterestingCommits.add(commit);
	             }
	            	 
	             }
	         }
	         System.out.println("\n\n\n-------End searching in develop branch -----------------");
	         
	         return listOfInterestingCommits; 
	         } catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		
		
		
		
		
	
	}


	private static  boolean commitChangesInterestingForProduct(RevCommit commit, String productRepoPath2) {
		// get
		Git git;
		
		try {
			git = Git.open(new File (productRepoPath2));
			Repository productRepo = git.getRepository();
	        
	        //Ref master = repo.findRef("master.baseline");	System.out.print(master.getTarget().getObjectId());
			Ref develop = productRepo.findRef("develop"); 
			
			if (develop == null){
	            	develop = git.checkout().setCreateBranch(true).
	            			setName("develop").
	            			setUpstreamMode(SetupUpstreamMode.TRACK).
	            			setStartPoint("origin/" + "develop").call();
	        }
			 
	            
			List<DiffEntry> diff = DifferenceComputer.getChangedFilesBetweenTwoCommits(coreRepoPath,commit.getParents()[0], commit);
			//if (diff==null) return false;
			
			Iterator<DiffEntry> i= diff.iterator();
			/***
			 * HAY QUE IMPLEMENTAR ESTO
			****/
			return true;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false; 
		}
		
		
	}
}
