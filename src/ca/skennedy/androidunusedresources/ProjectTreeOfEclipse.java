package ca.skennedy.androidunusedresources;

import java.io.File;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ProjectTreeOfEclipse {
	private final File mBaseDirectory;
	
	private File mSrcDirectory = null;
    private File mResDirectory = null;
    private File mGenDirectory = null;
    
    private File mManifestFile = null;
    private File mRJavaFile = null;
    private String mPackageName = null;
    
    public ProjectTreeOfEclipse() {
    	final String baseDirectory = System.getProperty("user.dir");
        mBaseDirectory = new File(baseDirectory);
        
        System.out.println("Running in: " + mBaseDirectory.getAbsolutePath());
    }
    
    public ProjectTreeOfEclipse(final String baseDirectory) {
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
    
    public File getGeneratedDirectory() {
    	return mGenDirectory;
    }
    
    public File getRJavaDirectory() {
    	return mRJavaFile;
    }
    
    public File getManifestFile() {
    	return mManifestFile;
    }
    
    public void find() {
    	findPaths();
    	
    	if (mSrcDirectory == null || mResDirectory == null || mManifestFile == null) {
			System.err.println("The current directory is not a valid Android project root.");
			return;
		}
    	
    	mPackageName = findPackageName(mManifestFile);
    	
		if (mPackageName == null || mPackageName.trim().length() == 0) {
			System.err.println("Unable to determine your application's package name from AndroidManifest.xml.  Please ensure it is set.");
			return;
		}

		if (mGenDirectory == null) {
			System.err.println("You must first build your project to generate R.java");
			return;
		}
		
		mRJavaFile = findRJavaFile(mGenDirectory, mPackageName);

        if (mRJavaFile == null) {
            System.err.println("You must first build your project to generate R.java");
            return;
        }
    }
    
    private void findPaths() {
        final File[] children = mBaseDirectory.listFiles();

        if (children == null) {
            return;
        }

        for (final File file : children) {
            if (file.isDirectory()) {
                if (file.getName().equals("src")) {
                    mSrcDirectory = file;
                } else if (file.getName().equals("res")) {
                    mResDirectory = file;
                } else if (file.getName().equals("gen")) {
                    mGenDirectory = file;
                }
            } else if (file.getName().equals("AndroidManifest.xml")) {
                mManifestFile = file;
            }
        }
    }
    
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
