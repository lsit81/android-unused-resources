package ca.skennedy.androidunusedresources;

import java.io.File;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public abstract class ProjectTree {
	protected final File mBaseDirectory;
	
	protected File mSrcDirectory = null;
	protected File mResDirectory = null;
	protected File mGenDirectory = null;
    
	protected File mManifestFile = null;
	protected File mRJavaFile = null;
    
	protected ProjectTree() {
    	final String baseDirectory = System.getProperty("user.dir");
        mBaseDirectory = new File(baseDirectory);
        
        System.out.println("Running in: " + mBaseDirectory.getAbsolutePath());
    }
    
	protected ProjectTree(final String baseDirectory) {
    	mBaseDirectory = new File(baseDirectory);
    	
    	System.out.println("Running in: " + mBaseDirectory.getAbsolutePath());
    }
	
    public File getBaseDirectory() {
    	return mBaseDirectory;
    }
    
    public File getSourceDirectory() {
    	return mSrcDirectory;
    }
    
    public File getResourceDirectory() {
    	return mResDirectory;
    }
    
    public File getRJavaDirectory() {
    	return mRJavaFile;
    }
    
    public File getManifestFile() {
    	return mManifestFile;
    }
    
    public abstract void find();
        
    public static String findPackageName(final File androidManifestFile) {
        String manifest = "";

        try {
            manifest = FileUtilities.getFileContents(androidManifestFile);
        } catch (final IOException e) {
            e.printStackTrace();
        }

        final Pattern pattern = Pattern.compile("<manifest\\s+.*?package\\s*=\\s*\"([A-Za-z0-9_\\.]+)\".*?>");
        final Matcher matcher = pattern.matcher(manifest);

        if (matcher.find()) {
            return matcher.group(1);
        }

        return null;
    }
    
    public static File findRJavaFile(final File baseDirectory, final String packageName) {
        final File rJava = new File(baseDirectory, packageName.replace('.', '/') + "/R.java");

        if (rJava.exists()) {
            return rJava;
        }

        return null;
    }

	public boolean isValidProject() {
		if (mSrcDirectory == null || mResDirectory == null || mManifestFile == null
				|| mRJavaFile == null) {
			return false;
		}
		
		return true;
	}
}
