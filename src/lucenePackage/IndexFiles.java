package lucenePackage;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.IndexWriter;
import org.wltea.analyzer.lucene.IKAnalyzer;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Date;

/** Index all text files under a directory. */
public class IndexFiles {

	private IndexFiles() {}

	static File INDEX_DIR;
	static int fileAmount=0;

	public static void delFolder(String folderPath) {
		try {
			delAllFile(folderPath); //删除完里面所有内容
			String filePath = folderPath;
			filePath = filePath.toString();
			java.io.File myFilePath = new java.io.File(filePath);
			myFilePath.delete(); //删除空文件夹
		} catch (Exception e) {
			e.printStackTrace(); 
		}
	}
	public static boolean delAllFile(String path) {
		boolean flag = false;
		File file = new File(path);
		if (!file.exists()) {
			return flag;
		}
		if (!file.isDirectory()) {
			return flag;
		}
		String[] tempList = file.list();
		File temp = null;
		for (int i = 0; i < tempList.length; i++) {
			if (path.endsWith(File.separator)) {
				temp = new File(path + tempList[i]);
			} else {
				temp = new File(path + File.separator + tempList[i]);
			}
			if (temp.isFile()) {
				temp.delete();
			}
			if (temp.isDirectory()) {
				delAllFile(path + "/" + tempList[i]);//先删除文件夹里面的文件
				delFolder(path + "/" + tempList[i]);//再删除空文件夹
				flag = true;
			}
		}
		return flag;
	}
	//indexCreateServlet.java文件调用startCreate
	public static int startCreate(String indexDirectory,String docDirectory) throws Exception{
		fileAmount=0;
		INDEX_DIR= new File(indexDirectory);
		if (INDEX_DIR.exists()) {
			delFolder(INDEX_DIR.getPath());
		}

		final File docDir = new File(docDirectory);
		if (!docDir.exists() || !docDir.canRead()) {
			System.out.println("Document directory '" +docDir.getAbsolutePath()+ "' does not exist or is not readable, please check the path");
			System.exit(1);
		}

		Date start = new Date();
		try {
			IndexWriter writer = new IndexWriter(INDEX_DIR, new IKAnalyzer(), true, IndexWriter.MaxFieldLength.LIMITED);
			System.out.println("Indexing to directory '" +INDEX_DIR+ "'...");
			indexDocs(writer, docDir);
			System.out.println("Optimizing...");
			writer.optimize();
			writer.close();

			Date end = new Date();
			System.out.println(end.getTime() - start.getTime() + " total milliseconds");

		} catch (IOException e) {
			System.out.println(" caught a " + e.getClass() +
					"\n with message: " + e.getMessage());
		}
		return fileAmount;
	}

	/** Index all text files under a directory. 
	 * @throws Exception */
	public static void main(String[] args) throws Exception {
		String usage = "java org.apache.lucene.demo.IndexFiles <root_directory>";
		if (args.length == 0) {
			System.err.println("Usage: " + usage);
			System.exit(1);
		}

		if (INDEX_DIR.exists()) {
			delFolder(INDEX_DIR.getPath());
		}

		final File docDir = new File(args[0]);
		if (!docDir.exists() || !docDir.canRead()) {
			System.out.println("Document directory '" +docDir.getAbsolutePath()+ "' does not exist or is not readable, please check the path");
			System.exit(1);
		}

		Date start = new Date();
		try {
			IndexWriter writer = new IndexWriter(INDEX_DIR, new IKAnalyzer(), true, IndexWriter.MaxFieldLength.LIMITED);
			System.out.println("Indexing to directory '" +INDEX_DIR+ "'...");
			indexDocs(writer, docDir);
			System.out.println("Optimizing...");
			writer.optimize();
			writer.close();

			Date end = new Date();
			System.out.println(end.getTime() - start.getTime() + " total milliseconds");

		} catch (IOException e) {
			System.out.println(" caught a " + e.getClass() +
					"\n with message: " + e.getMessage());
		}
	}

	static void indexDocs(IndexWriter writer, File file)
			throws IOException, Exception {
		// do not try to index files that cannot be read
		if (file.canRead()) {
			if (file.isDirectory()) {
				String[] files = file.list();
				// an IO error could occur
				if (files != null) {
					for (int i = 0; i < files.length; i++) {
						indexDocs(writer, new File(file, files[i]));
					}
				}
			} else {
				System.out.println("adding " + file);
				try {
					boolean flags=false;
					Document doc=null;
					if (file.getPath().endsWith(".pdf")){
						doc=LucenePDFDocument.getDocument(file);
						flags=true;
					}
					if (file.getPath().endsWith(".html")){
						doc=HTMLDocument.Document(file);
						flags=true;
					}
					if (file.getPath().endsWith(".txt")){
						doc=HTMLDocument.Document(file);
						flags=true;
					}
					if (file.getPath().endsWith(".doc")){
						doc=POIDocumentParse.docDocument(file);
						flags=true;
					}
					if (file.getPath().endsWith(".ppt")){
						doc=POIDocumentParse.pptDocument(file);
						flags=true;
					}
					if (file.getPath().endsWith(".xls")){
						doc=POIDocumentParse.xlsDocument(file);
						flags=true;
					}
					if(!flags){
						doc = FileDocument.Document(file);
					}
					fileAmount++;
					doc.add(new Field("contents",file.getName(),Field.Store.YES,Field.Index.ANALYZED));
					writer.addDocument(doc);
				}
				// at least on windows, some temporary files raise this exception with an "access denied" message
				// checking if the file can be read doesn't help
				catch (FileNotFoundException fnfe) {
					;
				}
			}
		}
	}

}
