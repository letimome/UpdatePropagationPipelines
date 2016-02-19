package pipeline;

import java.io.File;
import java.nio.file.Files;

public class PropagateChangeToProductRepo {
	
	
	
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
	
	
	static String coreRepoPath= "/Users/Onekin/Desktop/VODPlayer-CoreAssets-2";//args[0];
	static String productRepoPath="/Users/Onekin/Desktop/Product-Repository-VODD2";//args[1];
	static String pathToIdentifiedChangeCommits="/Users/Onekin/Desktop/PipelineData/identifyCommits.csv";//;args[4];
	
	public static void main(String[] args) {
		
		
		
		Files.copy(in, target, options);
		
		

	}

}
