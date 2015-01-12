package de.saumya.mojo.mains;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class ExtractedZip {

	static class DeleteDirectory extends SimpleFileVisitor<Path> {

		@Override
		public FileVisitResult visitFile(Path file,
				BasicFileAttributes attrs) throws IOException {
			Files.delete(file); 
			return FileVisitResult.CONTINUE;
		}

		@Override
		public FileVisitResult postVisitDirectory(Path dir,
				IOException exc) throws IOException {
			Files.delete(dir); 
			return FileVisitResult.CONTINUE;
		}
	}

	private final File target;
	
	public ExtractedZip(InputStream zip) throws IOException {
		target = Files.createTempDirectory("jruby-mains-").toFile();
		unzip(zip);
		Runtime.getRuntime().addShutdownHook(new Thread(){

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

	File directory() {
		return target;
	}

	private void unzip(InputStream zip) throws IOException {
        try(ZipInputStream is = new ZipInputStream(zip)){
	        ZipEntry entry = is.getNextEntry();
	        while (entry != null) {
	        	File path = new File(target, entry.getName());
	            if (!entry.isDirectory()) {
	                extractFile(is, path);
	            } else {
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
    	try(BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(path))){
    		int read = 0;
    		while ((read = in.read(bytesIn)) != -1) {
    			bos.write(bytesIn, 0, read);
    		}
    	}    	
    }
}
