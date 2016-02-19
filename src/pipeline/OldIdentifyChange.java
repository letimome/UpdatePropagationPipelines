package pipeline;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;

//import org.kohsuke.github.*;

//import edu.nyu.cs.javagit.api.*;
//import edu.nyu.cs.javagit.api.commands.GitLogResponse.Commit;


public class OldIdentifyChange {
	
	//private static ArrayList<edu.nyu.cs.javagit.api.commands.GitLogResponse.Commit> listOfIdentifiedCommits;

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
	/*	if (args.length!=6){
			System.out.println ("Parameters not completed");
			return;
		}
		*/
		//Parameters
		String coreRepoPath= "/Users/Onekin/Desktop/VODPlayer-CoreAssets-2";//args[0];
		String productRepoPath="/Users/Onekin/Desktop/Product-Repository-VODD2";//args[1];
		Integer TimeFrame= 10;//Integer.parseInt(args[2]);
		
		String changeTypeToPropagate="enhancement";//args[3];; //change types considered are: "hotfix", "enhancement", "new feature"
		String pathToIdentifiedChangeCommits="/Users/Onekin/Desktop/identifyChange";//;args[4];
		
		String msgKeyWord = null;
		if (changeTypeToPropagate=="enhancement")
			msgKeyWord="feature/";
		else if (changeTypeToPropagate=="bugfix")
			msgKeyWord="hotfix/";
		
		
	//	try {
			/*
			
			System.out.println(JavaGitConfiguration.getGitVersion());
			
			// Access core repository
			File coreAssetRepoDir = new File(coreRepoPath);
			DotGit dotGit = DotGit.getInstance(coreAssetRepoDir);

			// Get the current working tree from the git repository
			WorkingTree wt = dotGit.getWorkingTree();

			//Look for master and develop branches
			Ref master = null,develop=null;
			Iterator<Ref> branches = dotGit.getBranches();
			Ref aux;
			while (branches.hasNext()){
				aux=branches.next();
				if ( aux.getName().indexOf("master") != -1)
					master=aux;
				else if (aux.getName().indexOf("develop") != -1)
					develop=aux;
				
			}
			
			
			listOfIdentifiedCommits = new ArrayList<edu.nyu.cs.javagit.api.commands.GitLogResponse.Commit>() ;
			System.out.println(develop);
			wt.checkout(develop);
			
			for (edu.nyu.cs.javagit.api.commands.GitLogResponse.Commit c : dotGit.getLog()) {
				
				
				if (c.getMessage().indexOf(msgKeyWord) !=-1 && (c.getMergeDetails()!=null) && changesApealProduct(c,productRepoPath) ){
					listOfIdentifiedCommits.add(c);
					System.out.print(c.getSha());
					System.out.println("  ---"+ c.getMessage()+" ---"+c.getMergeDetails()); // if get details is null is not a merge commit
					System.out.println(	c.getAuthor());
					System.out.println(c.getFiles());
					
				}
			}
			
		System.out.println("Size of listedCommits = "+ listOfIdentifiedCommits.size());	    
		
		} catch (JavaGitException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
	}

	private static boolean changesApealProduct(Commit c, String productRepoPath) {
		
		return true;
	}
*/


}}