package ldcr.ByeHacker.Utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.jar.JarEntry;
import java.util.jar.JarInputStream;

public class JarScanner {
    public static ArrayList<String> getCrunchifyClassNamesFromJar(final File file) throws IOException {
	final ArrayList<String> listofClasses = new ArrayList<String>();
	final JarInputStream crunchifyJarFile = new JarInputStream(new FileInputStream(file));
	JarEntry crunchifyJar;
	while (true) {
	    crunchifyJar = crunchifyJarFile.getNextJarEntry();
	    if (crunchifyJar == null) {
		break;
	    }
	    if ((crunchifyJar.getName().endsWith(".class"))) {
		final String className = crunchifyJar.getName().replaceAll("/", "\\.");
		final String myClass = className.substring(0, className.lastIndexOf('.')); //remove .class
		listofClasses.add(myClass);
	    }
	}
	crunchifyJarFile.close();
	return listofClasses;
    }
}
