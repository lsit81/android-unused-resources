package ca.skennedy.androidunusedresources;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ProjectTreeOfAndroidStudio extends ProjectTree {
	private List<String> mProductFlavors = new ArrayList<String>();
	
	ProjectTreeOfAndroidStudio() {
		super();
	}

	ProjectTreeOfAndroidStudio(final String baseDirectory) {
		super(baseDirectory);
	}

	@Override
	public void find() {
		File gradleFile = findGradleFile();
		if (gradleFile == null) {
			System.err.println("do not find build.gradle");
		}
		
		loadSourceSetInfo(gradleFile);
		loadProductFalvors(gradleFile);
		findRJavaFile();
		findPaths();
	}
	
	private File findGradleFile() {
		final File[] children = mBaseDirectory.listFiles();
		
		if (children == null) {
			return null;
		}
		
		for (final File file : children) {
			 if (file.isDirectory()) {
				 continue;
			 }
			 
			 if (file.getName().equals("build.gradle")) {
				 return file;
			 }
		}
		
		return null;
	}
	
	private void loadSourceSetInfo(File buildGradleFile) {
		try {
			String gradle = FileUtilities.getFileContents(buildGradleFile);
			
			Pattern pattern = Pattern.compile("main.*?\\{.*?srcDirs.*?\\}");
			Matcher matcher = pattern.matcher(gradle);
			
			if (matcher.find() == false) {
				// build.gradle에 sourceSets에 main이 존재하지 않는 경우.
				return;
			}
			
			System.out.println(matcher.group(0));
			mManifestFile = findFilePath("(manifest\\.srcFile.*?').*?(?=')", matcher.group(0));
			System.out.println("manifest file = " + mManifestFile.getAbsolutePath());
			
			mSrcDirectory = findFilePath("(java\\.srcDirs.*?').*?(?=')", matcher.group(0));
			System.out.println("java.srcDirs = " + mSrcDirectory.getAbsolutePath());
			 
			mResDirectory = findFilePath("(res\\.srcDirs.*?').*?(?=')", matcher.group(0));
			System.out.println("res.srcDirs = " + mResDirectory.getAbsolutePath());
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private File findFilePath(String regex, String data) {
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(data);
		
		if (matcher.find() == false) {
			// build.gradle에 sourceSets에 main이 존재하지 않는 경우.
			return null;
		}
		
		String[] split = matcher.group(0).split("'");
		return new File(mBaseDirectory.getAbsolutePath() + "/" + split[1]);
	}
	
	private void findPaths() {
		final File[] children = mBaseDirectory.listFiles();

		if (children == null) {
			return;
		}
	}
	
	private void findRJavaFile() {
		ArrayList<File> searchDirectory = new ArrayList<File>();
		String defaultPath = mBaseDirectory.getAbsolutePath() + "/build/generated/source/r";
		
		searchDirectory.add(new File(defaultPath));
		for (String path : mProductFlavors) {
			searchDirectory.add(new File(defaultPath + "/" + path));
		}
		
		final String packageName = findPackageName(mManifestFile);
		File rFile;
				
		for (File genDirectory : searchDirectory) {
			final File[] children = genDirectory.listFiles();
			
			if (children == null) {
				continue;
			}
			
			for (final File file : children) {
				if (file.isDirectory() == false) {
					continue;
				}
				
				rFile = new File(file.getAbsolutePath() + "/" + packageName.replace(".", "/") + "/R.java");
				System.out.println(rFile.getAbsolutePath());
				if (rFile.exists()) {
					System.out.println("R.java path = " + rFile.getAbsolutePath());
					mRJavaFile = rFile;
					return;
				}
			}
		}
		
		System.err.println("You must first build your project to generate R.java");
		return;
	}
	
	private void loadProductFalvors(File gradleFile) {
		String gradle;
		try {
			gradle = FileUtilities.getFileContents(gradleFile);
			Pattern pattern = Pattern.compile("productFlavors.?\\{.*\\}");
			Matcher matcher = pattern.matcher(gradle);
			
			if (matcher.find() == false) {
				// build.gradle에 sourceSets에 main이 존재하지 않는 경우.
				System.out.println("not find productFalvors");
				return;
			}
			
			System.out.println("product = " + matcher.group(0));
			String productFalvorBlock = getBlock(matcher.group(0));
			if (productFalvorBlock == null) {
				return;
			}
			
			pattern = Pattern.compile("\\w[^{}]+(?=\\{)");
			matcher = pattern.matcher(productFalvorBlock);
			
			while (matcher.find()) {
				if ("productFlavors".equals(matcher.group(0))) {
					continue;
				}
				
				mProductFlavors.add(matcher.group(0).trim());
			}
			
		} catch (IOException e) {
			e.printStackTrace();
		}		
	}
	
	private String getBlock(String data) {
		int length = data.length();
		int blockCount = 0;
		boolean isStartBlock = false;
			
		String charter;
		for (int index = 0; index < length; ++index) {
			charter = data.substring(index, index + 1);
			
			if ("{".equals(charter)) {
				++blockCount;
				
				if (isStartBlock == false) {
					isStartBlock = true;
				}
			} else if ("}".equals(charter)) {
				--blockCount;
			}
			
			if (isStartBlock == true && blockCount == 0) {
				return data.substring(0, index);
			}
		}
		
		return null;
	}
}
