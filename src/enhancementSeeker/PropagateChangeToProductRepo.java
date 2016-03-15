package enhancementSeeker;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Iterator;

import org.apache.commons.*;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.CreateBranchCommand.SetupUpstreamMode;
import org.eclipse.jgit.api.PushCommand;
import org.eclipse.jgit.lib.Ref;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.transport.CredentialsProvider;
import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider;
import org.eclipse.jgit.util.FileUtils;

import pipeline.PropagateChange;

public class PropagateChangeToProductRepo extends PropagateChange{

	static String coreRepoPath= "./coreRepo/";//args[0];
	static String productRepoPath="./productRepo/";
	
	
	
	static String pathToPropagationUnits="./../EvaluateEnhancements/propagationUnits.csv";//;args[4];
	
	/**
	 * 
	 * Try to use org.apache.commons.io.FileUtils (General file manipulation utilities). Facilities are provided in the following methods:

		(1) FileUtils.moveDirectory(File srcDir, File destDir) => Moves a directory.
		
		(2) FileUtils.moveDirectoryToDirectory(File src, File destDir, boolean createDestDir) => Moves a directory to another directory.
		
		(3) FileUtils.moveFile(File srcFile, File destFile) => Moves a file.
		
		(4) FileUtils.moveFileToDirectory(File srcFile, File destDir, boolean createDestDir) => Moves a file to a directory.
		
		(5) FileUtils.moveToDirectory(File src, File destDir, boolean createDestDir) => Moves a file or directory to the destination directory.
	It's simple, easy and fast.
	 * 
	 * 
	 * 
	 * 
	 * ***/
	
	public static void main(String[] args) {
		
		//<--Paths;readFilesFromLocalFile
		//for each path move File to destination and overwrite
		ArrayList<String> listFilePaths = readListOFilesToPropagateFromLocal(pathToPropagationUnits, coreRepoPath,productRepoPath);
		
		Iterator<String> it=listFilePaths.iterator();
		String filePath;
		  CredentialsProvider cp = new UsernamePasswordCredentialsProvider("jenkinsPropagator", "Florentina88");
		
		System.out.println("Going to create new branch");
		Git git;
		String newBranchPattern="feature/update/CA";
		Ref newBranchaux;
		
		try {
			git = Git.open(new File (productRepoPath));
		
			Repository productRepo = git.getRepository();
			Ref develop = productRepo.findRef("develop"); 
	        if (develop == null){
	        	develop = git.checkout().setCreateBranch(true).
	        			setName("develop").
	        			setUpstreamMode(SetupUpstreamMode.TRACK).
	        			setStartPoint("origin/" + "develop").call();
	        }
			int i=(int) ((int) Math.random()*100-(Math.random())*10);
			while (it.hasNext()){
				i++;
				newBranchaux=git.branchCreate().setName(newBranchPattern+"-"+i).call();
				git.checkout().setName(newBranchPattern+"-"+i).call();
				filePath = it.next().toString();
				//PARSE IT "," and do a for each file to commit
				org.apache.commons.io.FileUtils.copyFile(new File(coreRepoPath+filePath), new File(productRepoPath+filePath));
				
				//COMMIT INTO REPOSITOTORY 
				git.add().addFilepattern(productRepoPath).call();
				git.commit().setMessage("automated propagation").call();
				// PUSH TO REMOTE
				
				PushCommand pc = git.push();
		        pc.setCredentialsProvider(cp)
		                .setForce(true)
		                .setPushAll();
				
				//FALTAN LAS CREDENCIALES!!!
				//git.push().add(newBranchPattern+"-"+i).setPushTags().call();
				//git.push().add(productRepoPath).call();
				
				//OPEN PULL REQUESTS, PRINT PULL REQUEST INTO FILE!
				
				System.out.println("END OF PROPAGATION");
			}
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}

	private static ArrayList<String> readListOFilesToPropagateFromLocal(
			String pathToPropagationUnits2, String coreRepoPath2,
			String productRepoPath2) {
		// TODO Auto-generated method stub
		ArrayList<String> list=new ArrayList<String>();
		list.add("features/ViewMovieDetail/MPEGDecoder/VODClient.java");
		list.add("features/ViewMovieDetail/MPEGDecoder/ListFrame.java");
		return list;
	}

}
