package lucenePackage;


/**
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Date;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.index.FilterIndexReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.HitCollector;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.Searcher;
import org.apache.lucene.search.TopDocCollector;
import org.wltea.analyzer.lucene.IKAnalyzer;
import org.wltea.analyzer.lucene.IKQueryParser;

/** Simple command-line based search demo. */
public class SearchFiles {

  /** Use the norms from one field for all fields.  Norms are read into memory,
   * using a byte of memory per document per searched field.  This can cause
   * search of large collections with a large number of fields to run out of
   * memory.  If all of the fields contain only a single token, then the norms
   * are all identical, then single norm vector may be shared. */
  public static Results result;
  private static class OneNormsReader extends FilterIndexReader {
    private String field;

    public OneNormsReader(IndexReader in, String field) {
      super(in);
      this.field = field;
    }

    public byte[] norms(String field) throws IOException {
      return in.norms(this.field);
    }
  }

  private SearchFiles() {}
  
  public static Results doSearch(String indexDirectory,String queryString,String maxresults,int totalAlldoc) throws Exception{
	  result=new Results();
	  String index = indexDirectory;
	  String field = "contents";
	  int repeat = 0;
	  boolean raw = false;
	  String normsField = null;
	  boolean paging = true;
	  int hitsPerPage = Integer.parseInt(maxresults);
	  IndexReader reader = IndexReader.open(index);

	  if (normsField != null)
		  reader = new OneNormsReader(reader, normsField);

	  Searcher searcher = new IndexSearcher(reader);
	  Analyzer analyzer = new IKAnalyzer();

	  String line = queryString;

	  line = line.trim();
	  Query query = IKQueryParser.parse(field, line);
//	  QueryParser parser = new QueryParser(field, analyzer);
//	  Query query = parser.parse(line);

	  if (paging) {
		  doPagingSearch(searcher, query, hitsPerPage, totalAlldoc);
	  } else {
		  doStreamingSearch(searcher, query);
	  }
	  reader.close();
	  return result;
}

  /** Simple command-line based search demo. */
  public static void main(String[] args) throws Exception {
  }
  
  /**
   * This method uses a custom HitCollector implementation which simply prints out
   * the docId and score of every matching document. 
   * 
   *  This simulates the streaming search use case, where all hits are supposed to
   *  be processed, regardless of their relevance.
   */
  public static void doStreamingSearch(final Searcher searcher, Query query) throws IOException {
    HitCollector streamingHitCollector = new HitCollector() {
      
      // simply print docId and score of every matching document
      public void collect(int doc, float score) {
        System.out.println("doc="+doc+" score="+score);
      }
      
    };
    searcher.search(query, streamingHitCollector);
  }

  /**
   * This demonstrates a typical paging search scenario, where the search engine presents 
   * pages of size n to the user. The user can then go to the next page if interested in
   * the next hits.
   * 
   * When the query is executed for the first time, then only enough results are collected
   * to fill 5 result pages. If the user wants to page beyond this limit, then the query
   * is executed another time and all hits are collected.
   * 
   */
  public static void doPagingSearch(Searcher searcher, Query query, 
                                     int hitsPerPage, int totalAlldoc) throws IOException {
 
    TopDocCollector collector = new TopDocCollector(totalAlldoc);
    searcher.search(query, collector);
    ScoreDoc[] hits = collector.topDocs().scoreDocs;
    
    int numTotalHits = collector.getTotalHits();
    System.out.println(numTotalHits + " total matching documents");

    int start = 0;
    int end = numTotalHits;
    result.totalHits=numTotalHits;
    for (int i = start; i < end; i++) {
    	Document doc = searcher.doc(hits[i].doc);
    	String path = doc.get("path");      
    	if (path != null) {
    		System.out.println((i+1) + ". " + path+"  score="+hits[i].score);
    		result.dir.add(path);                     //查询到后加入相关路径
    		result.score.add(""+hits[i].score);       //查询到后加入相关评分
    	}         
    }
    
  }
}
