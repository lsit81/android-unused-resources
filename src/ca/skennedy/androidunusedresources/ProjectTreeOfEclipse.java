package ca.skennedy.androidunusedresources;

import java.io.File;

public class ProjectTreeOfEclipse extends ProjectTree {
    private String mPackageName = null;
    
    ProjectTreeOfEclipse() {
    	super();
    }
    
    ProjectTreeOfEclipse(final String baseDirectory) {
    	super(baseDirectory);
    }
    
    public File getBaseDirectory() {
    	return mBaseDirectory;
    }
    
    @Override
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
}
