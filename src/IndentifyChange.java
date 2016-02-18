import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;

import org.kohsuke.github.*;


public class IndentifyChange {
	
	

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		
		GitHub github;
		try {
			github = GitHub.connect();
		//	GHRepository repo = github.createRepository( "new-repository","this is my new repository", "",true/*public*/);
			GHRepository repo= github.getRepository("letimome/VODPlayer-CoreAssets-2");
			Map<String, GHBranch> branches=repo.getBranches();
			GHBranch developBranch= branches.get("develop.coreAssets");
			GHCommit latestCommitInDevelop= repo.getCommit(developBranch.getSHA1());
			System.out.println(latestCommitInDevelop.getCommitShortInfo().getMessage().toString());
			
		

			startBugFixPipeline(repo);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		

	}

	private static void startBugFixPipeline(GHRepository coreRepo) {
		ArrayList<String> keys= new ArrayList<String>();
		keys.add("release/"); keys.add("hotfix/");
		String integrationBranchName="develop";
		
		identifyBugFixes(coreRepo, integrationBranchName, keys, 1);
		
	}
	
	private static String[] identifyBugFixes(GHRepository coreRepo, String integrationBranchName, ArrayList<String> keys, int daysFromToday) {
		
		return null;
	}
	
private static String[] identifyEnhancements(GHRepository coreRepo, String integrationBranchName, ArrayList<String> keys, int daysFromToday) {
		
		return null;
	}
private static String[] identifyNewOpportunities(GHRepository coreRepo, String integrationBranchName, ArrayList<String> keys, int daysFromToday) {
	
	return null;
}

private static String[] identifyBrokenProductConfiguration(GHRepository coreRepo, String integrationBranchName, ArrayList<String> keys, int daysFromToday) {
	
	return null;
}


}
