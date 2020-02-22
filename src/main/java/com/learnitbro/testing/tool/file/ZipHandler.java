package com.learnitbro.testing.tool.file;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

public class ZipHandler {

	private List<String> fileList = new ArrayList<String>();

	public void extractDirectories(String fileZip) {
		System.out.println("Extracting files...");
		// Open the file
		try (ZipFile file = new ZipFile(fileZip)) {
			FileSystem fileSystem = FileSystems.getDefault();
			// Get file entries
			Enumeration<? extends ZipEntry> entries = file.entries();

			// We will unzip files in this folder
			String uncompressedDirectory = (new File(fileZip)).getParent() + "/";
			// Files.createDirectory(fileSystem.getPath(uncompressedDirectory));

			// Iterate over entries
			while (entries.hasMoreElements()) {
				ZipEntry entry = entries.nextElement();
				// If directory then create a new directory in uncompressed folder
				if (entry.isDirectory()) {
					System.out.println("Creating Directory: " + uncompressedDirectory + entry.getName());
					Files.createDirectories(fileSystem.getPath(uncompressedDirectory + entry.getName()));
				}
				// Else create the file
				else {
					InputStream is = file.getInputStream(entry);
					BufferedInputStream bis = new BufferedInputStream(is);
					String uncompressedFileName = uncompressedDirectory + entry.getName();
					Path uncompressedFilePath = fileSystem.getPath(uncompressedFileName);
					Files.createFile(uncompressedFilePath);
					FileOutputStream fileOutput = new FileOutputStream(uncompressedFileName);
					while (bis.available() > 0) {
						fileOutput.write(bis.read());
					}
					fileOutput.close();
					System.out.println("Written :" + entry.getName());
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void extractFiles(String fileZip) {
		System.out.println("Extracting files...");
		try {
			byte[] buffer = new byte[1024];
			ZipInputStream zis = new ZipInputStream(new FileInputStream(fileZip));
			ZipEntry zipEntry = zis.getNextEntry();
			while (zipEntry != null) {
				String fileName = zipEntry.getName();
				String fileParent = (new File(fileZip)).getParent();
				System.out.println("File Extracted : " + fileName);
				File newFile = new File(fileParent + "/" + fileName);
				FileOutputStream fos = new FileOutputStream(newFile);
				int len;
				while ((len = zis.read(buffer)) > 0) {
					fos.write(buffer, 0, len);
				}
				fos.close();
				zipEntry = zis.getNextEntry();
			}

			zis.closeEntry();
			zis.close();
			System.out.println("Done...");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void compressFiles(String name, String src, String dest) {

		String SOURCE_FOLDER = src;
		// Compress the images
		// Generating the zip file.
		generateFileList(new File(SOURCE_FOLDER), SOURCE_FOLDER);
		String OUTPUT_ZIP_FILE = dest + name;
		zipIt(OUTPUT_ZIP_FILE, SOURCE_FOLDER);
		// zipIt(dest + "/" + name, src);
		fileList.clear();
		System.out.println("The file named: " + name + " has been created.");

	}

	public void zipIt(String zipFile, String SOURCE_FOLDER) {

		byte[] buffer = new byte[1024];

		try {

			FileOutputStream fos = new FileOutputStream(zipFile);
			ZipOutputStream zos = new ZipOutputStream(fos);

			System.out.println("Output to Zip : " + zipFile);

			for (String file : this.fileList) {

				System.out.println("File Added : " + file);
				ZipEntry ze = new ZipEntry(file);
				zos.putNextEntry(ze);

				FileInputStream in = new FileInputStream(SOURCE_FOLDER + File.separator + file);

				int len;
				while ((len = in.read(buffer)) > 0) {
					zos.write(buffer, 0, len);
				}

				in.close();
			}

			zos.closeEntry();
			// remember close it
			zos.close();

			System.out.println("Done");
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}

	/**
	 * Traverse a directory and get all files, and add the file into fileList
	 * 
	 * @param node
	 *            file or directory
	 */
	public void generateFileList(File node, String SOURCE_FOLDER) {

		// add file only
		if (node.isFile()) {
			fileList.add(generateZipEntry(node.getAbsoluteFile().toString(), SOURCE_FOLDER));
		}

		if (node.isDirectory()) {
			String[] subNote = node.list();
			for (String filename : subNote) {
				generateFileList(new File(node, filename), SOURCE_FOLDER);
			}
		}

	}

	/**
	 * Format the file path for zip
	 * 
	 * @param file
	 *            file path
	 * @return Formatted file path
	 */
	private String generateZipEntry(String file, String SOURCE_FOLDER) {
		return file.substring(SOURCE_FOLDER.length() + 1, file.length());
	}

}