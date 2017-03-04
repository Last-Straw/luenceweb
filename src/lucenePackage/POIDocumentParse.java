package lucenePackage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;

import org.apache.lucene.document.DateTools;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.poi.hslf.extractor.PowerPointExtractor;
import org.apache.poi.hssf.extractor.ExcelExtractor;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hwpf.extractor.WordExtractor;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;

public class POIDocumentParse {
	
	public static Document docDocument(File f)throws Exception{
		Document doc = new Document();
		doc.add(new Field("path", f.getPath(), Field.Store.YES, Field.Index.NOT_ANALYZED));
		doc.add(new Field("modified",
				DateTools.timeToString(f.lastModified(), DateTools.Resolution.MINUTE),
				Field.Store.YES, Field.Index.NOT_ANALYZED));
		FileInputStream is = new FileInputStream(f);
		WordExtractor extractor = new WordExtractor(is);
		doc.add(new Field("contents", extractor.getText(),Field.Store.YES, Field.Index.ANALYZED));
		return doc;
	}
	
	public static Document pptDocument(File f)throws Exception{
		Document doc = new Document();
		doc.add(new Field("path", f.getPath(), Field.Store.YES, Field.Index.NOT_ANALYZED));
		doc.add(new Field("modified",
				DateTools.timeToString(f.lastModified(), DateTools.Resolution.MINUTE),
				Field.Store.YES, Field.Index.NOT_ANALYZED));
		FileInputStream is = new FileInputStream(f);
		PowerPointExtractor extractor=new PowerPointExtractor(is);
		doc.add(new Field("contents", extractor.getText(),Field.Store.YES, Field.Index.ANALYZED));
		return doc;
	}
	
	public static Document xlsDocument(File f)throws Exception{
		Document doc = new Document();
		doc.add(new Field("path", f.getPath(), Field.Store.YES, Field.Index.NOT_ANALYZED));
		doc.add(new Field("modified",
				DateTools.timeToString(f.lastModified(), DateTools.Resolution.MINUTE),
				Field.Store.YES, Field.Index.NOT_ANALYZED));
		FileInputStream is = new FileInputStream(f);
		HSSFWorkbook wb=new HSSFWorkbook(new POIFSFileSystem(is));
		ExcelExtractor extractor=new ExcelExtractor(wb);
		extractor.setFormulasNotResults(false);
		extractor.setIncludeSheetNames(true);
		doc.add(new Field("contents", extractor.getText(),Field.Store.YES, Field.Index.ANALYZED));
		return doc;
	}
	
//	public static void readWord(File file) throws Exception{
//		FileInputStream is = new FileInputStream(file);
//		WordExtractor extractor = new WordExtractor(is);
//		System.out.println(extractor.getText());
//	}
//	
//	public static void readExcel(File file) throws Exception {
//		FileInputStream is = new FileInputStream(file);
//		HSSFWorkbook wb=new HSSFWorkbook(new POIFSFileSystem(is));
//		ExcelExtractor extractor=new ExcelExtractor(wb);
//		extractor.setFormulasNotResults(false);
//		extractor.setIncludeSheetNames(true);
//		System.out.println(extractor.getText());
//	}
//	
//	public static void readPowerPoint(File file) throws Exception {   
//		FileInputStream is = new FileInputStream(file);
//		PowerPointExtractor extractor=new PowerPointExtractor(is);
//		System.out.println(extractor.getText());
//	}
//
//	public static void main(String[] args) throws Exception {
//		File file = new File("D:\\Lucene\\docs\\∫«∫«.doc");
//		readWord(file);
//		file = new File("D:\\Lucene\\docs\\±±” bupt.xls");
//		readExcel(file);
//		file = new File("D:\\Lucene\\docs\\±±” bupt.ppt");
//		readPowerPoint(file);
//	}
}
