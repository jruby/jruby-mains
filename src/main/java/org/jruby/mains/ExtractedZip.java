package org.jruby.mains;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.LinkedList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class ExtractedZip {
    
    static class DeleteDirectory extends SimpleFileVisitor<Path> {
        
        @Override
        public FileVisitResult visitFile(Path file, BasicFileAttributes attrs)
                throws IOException {
            Files.delete(file);
            return FileVisitResult.CONTINUE;
        }
        
        @Override
        public FileVisitResult postVisitDirectory(Path dir, IOException exc)
                throws IOException {
            Files.delete(dir);
            return FileVisitResult.CONTINUE;
        }
    }
    
    private final File target;
    private final List<URL> urls = new LinkedList<URL>();
    
    public ExtractedZip(InputStream zip) throws IOException {
        this(zip, false);
    }
    
    public ExtractedZip(InputStream zip, boolean onlyWebInfLibJars)
            throws IOException {
        target = Files.createTempDirectory("jruby-mains-").toFile();
        if (onlyWebInfLibJars) {
            unzipOnlyJars(zip);
        }
        else {
            unzip(zip);
        }
	if (deleteExtractedFiles()) {
	    Runtime.getRuntime().addShutdownHook(new Thread() {

		    @Override
		    public void run() {
			try {
			    Files.walkFileTree(target.toPath(), new DeleteDirectory());
			} catch (IOException e) {
			    e.printStackTrace();
			}
		    }
		});
	}
	else {
	    Runtime.getRuntime().addShutdownHook(new Thread() {

		    @Override
		    public void run() {
			System.err.println("keeping extracted files are in: " + target);
		    }
		});
	}
    }

    boolean deleteExtractedFiles() {
	try {
	    return ! "true".equals(System.getProperty("jruby.mains.keep_extracted"));
	}
	catch(Exception e ) {
	    return true;
	}
    }

    File directory() {
        return target;
    }
    
    List<URL> urls() {
        return urls;
    }
    
    private void unzipOnlyJars(InputStream zip) throws IOException {
        try (ZipInputStream is = new ZipInputStream(zip)) {
            ZipEntry entry = is.getNextEntry();
            while (entry != null) {
                if (!entry.isDirectory()
                        && entry.getName().startsWith("WEB-INF/lib/")
                        && entry.getName().endsWith(".jar")) {
                    File path = new File(target, entry.getName());
                    extractFile(is, path);
                    urls.add(path.toURI().toURL());
                }
                is.closeEntry();
                entry = is.getNextEntry();
            }
        }
    }
    
    private void unzip(InputStream zip) throws IOException {
        try (ZipInputStream is = new ZipInputStream(zip)) {
            ZipEntry entry = is.getNextEntry();
            while (entry != null) {
                File path = new File(target, entry.getName());
                if (!entry.isDirectory()) {
                    extractFile(is, path);
                }
                else {
                    path.mkdir();
                }
                is.closeEntry();
                entry = is.getNextEntry();
            }
        }
    }
    
    private final byte[] bytesIn = new byte[4096];
    
    private void extractFile(ZipInputStream in, File path) throws IOException {
        path.getParentFile().mkdirs();
        try (BufferedOutputStream bos = new BufferedOutputStream(
                new FileOutputStream(path))) {
            int read = 0;
            while ((read = in.read(bytesIn)) != -1) {
                bos.write(bytesIn, 0, read);
            }
        }
        if (path.getPath().contains("jruby.home/bin")) {
            path.setExecutable(true);
        }
    }
}
