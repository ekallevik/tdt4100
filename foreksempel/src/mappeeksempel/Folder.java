package mappeeksempel;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class Folder {

	// referanse til mappa som denne mappa ligger i
	private Folder parentFolder;
	// liste over alle mappene inni denne
	private List<Folder> subFolders = new ArrayList<>();
	// liste over alle filene inni denne mappa
	private List<File> files = new ArrayList<>();

	private String name;

	public Folder(String name, Folder parentFolder) {
		this.name = name;
		this.parentFolder = parentFolder;
		if (parentFolder != null) {
			parentFolder.subFolders.add(this);
		}
	}
	
	// denne skal utvides til å vise hele stien fra rot-noden og ned
	@Override
	public String toString() {
		if (parentFolder == null) {
			return "/";
		}
		String parentToString = parentFolder.toString();
		return parentToString + name + "/";
	}

	// denne brukes av File, for å legge seg til parent-mappa
	// ingen synlighetsmodifikator betyr pakke-synlighet
	void addFile(File file) {
		this.files.add(file);
	}
	// denne brukes av File, for å fjerne seg fra parent-mappa
	// ingen synlighetsmodifikator betyr pakke-synlighet
	void removeFile(File file) {
		this.files.remove(file);
	}

	// sjekker om fileOrFolder ligger inni denne mappa
	public boolean contains(Object fileOrFolder) {
		Folder current = null;
		if (fileOrFolder instanceof Folder) {
			current = ((Folder) fileOrFolder);
		} else if (fileOrFolder instanceof File) {
			current = ((File) fileOrFolder).getParentFolder();			
		}
		while (current != null) {
			if (current == this) {
				return true;
			}
			current = current.parentFolder;
		}
		return false;
	}
	
	// flytter denne mappa over i targetFolder
	public void move(Folder targetFolder) {
		if (parentFolder != null) {
			parentFolder.subFolders.remove(this);
		}
		if (targetFolder != null) {
			targetFolder.subFolders.add(this);
		}
		parentFolder = targetFolder;
	}
	
	// returnerer første fil med angitt navn eller endelse
	public File findFirst(String base, String ext) {
		// TODO
		return null;
	}

	// returnerer alle filene med angitt navn eller endelse
	public Collection<File> findAll(String base, String ext) {
		// TODO
		return null;
	}
	
	public static void main(String[] args) {
		Folder root = new Folder("", null);
		File rootFile = new File("README.txt", root);
		Folder tmp = new Folder("tmp", root);
		Folder folder1 = new Folder("Users", root);
		Folder home1 = new Folder("hal", folder1);
		File myProfileFile = new File("profile.png", home1);	
		
		System.out.println("Skulle være /, var " + root.toString());
		System.out.println("Skulle være /README.TXT, var " + rootFile.toString());
		System.out.println("Skulle være /Users/, var " + folder1.toString());
		System.out.println("Skulle være /Users/hal/, var " + home1.toString());
		System.out.println("Skulle være /Users/hal/profile.png, var " + myProfileFile.toString());

		System.out.println("Skulle være true, var " + root.contains(folder1));
		System.out.println("Skulle være true, var " + root.contains(rootFile));
		System.out.println("Skulle være true, var " + root.contains(home1));

		System.out.println("Skulle være false, var " + tmp.contains(folder1));
		System.out.println("Skulle være false, var " + tmp.contains(rootFile));
		System.out.println("Skulle være false, var " + tmp.contains(home1));
		
		folder1.move(tmp);
		System.out.println("Skulle være /tmp/Users/, var " + folder1.toString());
		myProfileFile.move(tmp);
		System.out.println("Skulle være /tmp/profile.png, var " + myProfileFile.toString());
	}
}