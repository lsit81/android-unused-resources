package ca.skennedy.androidunusedresources;

public class Loader {
    private Loader() {
        super();
    }

    public static void main(final String[] args) {
        ResourceScanner resourceScanner;
        
        if (args.length == 1) {
        	resourceScanner = new ResourceScanner(args[0]);
        } else if (args.length == 2) {
        	resourceScanner = new ResourceScanner(args[0], Boolean.valueOf(args[1]));
        } else {
        	resourceScanner = new ResourceScanner();
        }
        
        resourceScanner.run();
    }
}
