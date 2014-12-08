package ca.skennedy.androidunusedresources;

public class ProjectTreeFactory {
	public static ProjectTree createProjectTree() {
		ProjectTree projectTree = new ProjectTreeOfAndroidStudio();
		if (isValidProject(projectTree) == true) {
			System.out.println("This is Android Studio Project");
			return projectTree;
		}
		
		projectTree = new ProjectTreeOfEclipse();
		if (isValidProject(projectTree) == true) {
			System.out.println("This is Eclipse Project");
			return projectTree;
		}
		
		return null;
	}
	
	public static ProjectTree createProjectTree(final String baseDirectory) {
		ProjectTree projectTree = new ProjectTreeOfAndroidStudio(baseDirectory);
		if (isValidProject(projectTree) == true) {
			System.out.println("This is Android Studio Project");
			return projectTree;
		}
		
		projectTree = new ProjectTreeOfEclipse(baseDirectory);
		if (isValidProject(projectTree) == true) {
			System.out.println("This is Eclipse Project");
			return projectTree;
		}
		
		return null;
	}
	
	private static boolean isValidProject(ProjectTree projectTree) {
		projectTree.find();
		return projectTree.isValidProject();
	}
}
