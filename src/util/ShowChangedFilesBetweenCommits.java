package util;
/*
   Copyright 2013, 2014 Dominik Stadler
   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at
     http://www.apache.org/licenses/LICENSE-2.0
   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
 */

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.diff.DiffEntry;
import org.eclipse.jgit.errors.IncorrectObjectTypeException;
import org.eclipse.jgit.errors.MissingObjectException;
import org.eclipse.jgit.lib.ObjectId;
import org.eclipse.jgit.lib.ObjectReader;
import org.eclipse.jgit.lib.Ref;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.revwalk.RevTree;
import org.eclipse.jgit.revwalk.RevWalk;
import org.eclipse.jgit.treewalk.AbstractTreeIterator;
import org.eclipse.jgit.treewalk.CanonicalTreeParser;



/**
 * Snippet which shows how to show diffs between two commits.
 *
 * @author dominik.stadler at gmx.at
 */
public class ShowChangedFilesBetweenCommits {

    public static void main(String[] args) throws IOException, GitAPIException {
    	///
    	String coreRepoPath= "/Users/Onekin/Desktop/VODPlayer-CoreAssets-2";//args[0];
    	String branchToLookInto="develop.coreAssets";
    	Git git = Git.open (new File (coreRepoPath));
        Repository repository  = git.getRepository();
        
        ObjectId parentCommit = repository.resolve(repository.findRef(branchToLookInto).getObjectId().getName()+"^^{commit}");//http://download.eclipse.org/jgit/site/4.2.0.201601211800-r/apidocs/org/eclipse/jgit/lib/Repository.html#resolve(java.lang.String)
        ObjectId currentCommit = repository.resolve(repository.findRef(branchToLookInto).getObjectId().getName()+"^{commit}");
        
        List<DiffEntry> asda= getChangedFilesBetweenTwoCommits( coreRepoPath, currentCommit, parentCommit);
    }
    
    

	public static List<DiffEntry> getChangedFilesBetweenTwoCommits(String coreRepoPath, ObjectId commitSha1, ObjectId commitSha2){
    	List<DiffEntry> diffs = null;
    	
    	try{
	        // The {tree} will return the underlying tree-id instead of the commit-id itself!
	        // For a description of what the carets do see e.g. http://www.paulboxley.com/blog/2011/06/git-caret-and-tilde
	        // This means we are selecting the parent of the parent of the parent of the parent of current HEAD and
	        // take the tree-ish of it
	    	
	    	Git git = Git.open (new File (coreRepoPath));
	        Repository repository  = git.getRepository();
	        
	    	//ObjectId oldHead = repository.resolve(repository.findRef(branchToLookInto).getObjectId().getName()+"^^{tree}");//http://download.eclipse.org/jgit/site/4.2.0.201601211800-r/apidocs/org/eclipse/jgit/lib/Repository.html#resolve(java.lang.String)
	        //ObjectId head = repository.resolve(repository.findRef(branchToLookInto).getObjectId().getName()+"^{tree}");
	        
	        ObjectId oldHead = repository.resolve(commitSha1.getName()+"^{tree}");//http://download.eclipse.org/jgit/site/4.2.0.201601211800-r/apidocs/org/eclipse/jgit/lib/Repository.html#resolve(java.lang.String)
	        ObjectId head = repository.resolve(commitSha2.getName()+"^{tree}");
	        
	        System.out.println("Printing diff between tree: " + oldHead + "and" + head);
	
	        // prepare the two iterators to compute the diff between
	
			ObjectReader reader = repository.newObjectReader(); 
			
	    	CanonicalTreeParser oldTreeIter = new CanonicalTreeParser();
	    	oldTreeIter.reset(reader, oldHead);
	    	CanonicalTreeParser newTreeIter = new CanonicalTreeParser();
	    	newTreeIter.reset(reader, head);
	
	    	// finally get the list of changed files
	    	git = new Git(repository);
	        
	    	diffs= git.diff().setNewTree(newTreeIter).setOldTree(oldTreeIter).call();
	        int changedFiles=0;
	    	for (DiffEntry diff : diffs) {
	           System.out.println("Entry: " + diff);
	           System.out.println("getNewPath: " + diff.getNewPath());
	           System.out.println("getScore: " + diff.getScore());
	           System.out.println("getChangeType: " + diff.getChangeType());
	           System.out.println("getNewMode: " + diff.getNewMode());
	           System.out.println("getPath: " + diff.getPath(null));
	           changedFiles++;
	        }
	    	  System.out.println("Changed Files: " + changedFiles);	
			
	    }catch (Exception e){
	    	// TODO Auto-generated catch block
	    	e.printStackTrace();
	    }
	
	    System.out.println("Done");
	    
		return diffs;
}
    
    
    
   
}
