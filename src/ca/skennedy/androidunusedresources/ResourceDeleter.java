package ca.skennedy.androidunusedresources;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.SortedSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ResourceDeleter {
	public static void deleteResource(Resource res) {
		String type = res.getType();

		try {
			AutoDeletableResourceType deletableResType = AutoDeletableResourceType.valueOf(type.toUpperCase());
	
			if (deletableResType.mHaveToDeleteFileType) {
				deleteFile(res);
			} else {
				deleteResourceInFile(res);
			}
		
//			System.out.println("deleteResource type = " + type + ", deletable = " + deletableResType.mHaveToDeleteFileType);
		} catch (IllegalArgumentException e) {
			return;
		}
	}

	private static void deleteFile(Resource res) {
		SortedSet<String> declaredPaths = res.getDeclaredPaths();
		File shouldDeleteFile;

		for (String path : declaredPaths) {
			shouldDeleteFile = new File(path);
			shouldDeleteFile.delete();

			System.out.println("delete file : " + path);
		}
	}

	private static void deleteResourceInFile(Resource res) {
		SortedSet<String> declaredPaths = res.getDeclaredPaths();
		AutoDeletableResourceType resourceType;
		String readContents;
		String writeContents = null;
		String resourceName = res.getName();

		for (String filePath : declaredPaths) {
			readContents = readFile(filePath);
			resourceType = AutoDeletableResourceType.valueOf(res.getType().toUpperCase());

			if (resourceType == AutoDeletableResourceType.ARRAY) {
				writeContents = removeArrayResource(resourceName, readContents);

			} else if (resourceType == AutoDeletableResourceType.COLOR) {
				writeContents = removeColorResource(resourceName, readContents);

			} else if (resourceType == AutoDeletableResourceType.DIMEN) {
				writeContents = removeDimenResource(resourceName, readContents);

			} else if (resourceType == AutoDeletableResourceType.ID) {
				writeContents = removeIdResource(resourceName, readContents);

			} else if (resourceType == AutoDeletableResourceType.STYLE) {
				writeContents = removeStyleResource(resourceName, readContents);

			} else if (resourceType == AutoDeletableResourceType.STRING) {
				writeContents = removeStringResource(resourceName, readContents);

			}

			if (writeContents != null) {
				saveResource(writeContents, filePath);
				
				System.out.println("\t>>> delete resource : " + res.getName() + ", path = " + filePath);
			}
		}
	}

	private static String removeArrayResource(String resourceName, String contents) {
		final Pattern pattern = Pattern.compile("<([a-z]+\\-)?array\\s+name\\s*=\\s*\"" + resourceName + "\".*?array>", Pattern.DOTALL);

		final Matcher matcher = pattern.matcher(contents);

		if (matcher.find() == false) {
			return null;
		}

		return matcher.replaceAll("");
	}

	private static String removeColorResource(String resourceName, String contents) {
		final Pattern pattern = Pattern.compile("<color\\s+name\\s*=\\s*\"" + resourceName + "\".*?/color>", Pattern.DOTALL);

		final Matcher matcher = pattern.matcher(contents);

		if (matcher.find() == false) {
			return null;
		}

		return matcher.replaceAll("");
	}

	private static String removeDimenResource(String resourceName, String contents) {
		final Pattern pattern = Pattern.compile("<dimen\\s+name\\s*=\\s*\"" + resourceName + "\".*?/dimen>", Pattern.DOTALL);

		final Matcher matcher = pattern.matcher(contents);

		if (matcher.find() == false) {
			return null;
		}

		return matcher.replaceAll("");
	}

	private static String removeIdResource(String resourceName, String contents) {
		final Pattern valuesPattern0 = Pattern.compile("<item\\s+type\\s*=\\s*\"id\".*?name\\s*=\\s*\"" + resourceName + "\".*?/>");
		final Pattern valuesPattern1 = Pattern.compile("<item\\s+name\\s*=\\s*\"" + resourceName + "\".*?type\\s*=\\s*\"id\".*?/>");

		Matcher matcher = valuesPattern0.matcher(contents);

		if (matcher.find()) {
			return matcher.replaceAll("");
		}

		matcher = valuesPattern1.matcher(contents);

		if (matcher.find()) {
			return matcher.replaceAll("");
		}

		return null;
	}

	private static String removeStyleResource(String resourceName, String contents) {
		final Pattern pattern = Pattern.compile("<style[\\s\\w]+name\\s*=\\s*\"" + resourceName + "\".*?style>", Pattern.DOTALL);
		final Matcher matcher = pattern.matcher(contents);

		if (matcher.find() == false) {
			return null;
		}

		return matcher.replaceAll("");
	}

	private static String removeStringResource(String resourceName, String contents) {
		final Pattern pattern = Pattern.compile("<string\\s+name\\s*=\\s*\"" + resourceName + "\".*?/string>", Pattern.DOTALL);

		final Matcher matcher = pattern.matcher(contents);

		if (matcher.find() == false) {
			return null;
		}

		return matcher.replaceAll("");
	}

	private static String readFile(String filePath) {
		BufferedReader reader = null;
		String line, results = "";

		try {
			reader = new BufferedReader(new FileReader(filePath));

			while ((line = reader.readLine()) != null) {
				results += line + "\n";
			}
		} catch (IOException e) {

		} finally {
			if (reader != null) {
				try {
					reader.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return results;
	}

	private static void saveResource(String contents, String filePath) {
		FileWriter writer = null;
		try {
			writer = new FileWriter(filePath);

			writer.write(contents);
		} catch (IOException e) {
			e.printStackTrace();

		} finally {
			if (writer != null) {
				try {
					writer.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	/**
	 * 자동으로 삭제 가능한 리소스 타입.
	 * 
	 * @author Lim SeungTaek
	 * 
	 */
	enum AutoDeletableResourceType {
		ANIM(true),
		ARRAY(false),
		COLOR(false),
		DIMEN(false),
		DRAWABLE(true),
		ID(false),
		LAYOUT(true),
		MENU(true),
		STRING(false),
		STYLE(false);

		public final boolean mHaveToDeleteFileType;

		private AutoDeletableResourceType(boolean haveToDeleteFileType) {
			mHaveToDeleteFileType = haveToDeleteFileType;
		}
	}
}
