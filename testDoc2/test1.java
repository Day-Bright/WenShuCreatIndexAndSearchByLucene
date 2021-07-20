//import java.io.BufferedReader;
//import java.io.File;
//import java.io.FileInputStream;
//import java.io.InputStreamReader;
//
//import org.apache.lucene.analysis.Analyzer;
//import org.apache.lucene.analysis.standard.StandardAnalyzer;
//import org.apache.lucene.document.Document;
//import org.apache.lucene.document.Field;
//import org.apache.lucene.document.NumberTools;
//import org.apache.lucene.document.Field.Index;
//import org.apache.lucene.document.Field.Store;
//import org.apache.lucene.index.IndexWriter;
//import org.apache.lucene.index.IndexWriter.MaxFieldLength;
//import org.apache.lucene.queryParser.MultiFieldQueryParser;
//import org.apache.lucene.queryParser.QueryParser;
//import org.apache.lucene.search.Filter;
//import org.apache.lucene.search.IndexSearcher;
//import org.apache.lucene.search.Query;
//import org.apache.lucene.search.ScoreDoc;
//import org.apache.lucene.search.TopDocs;
//import org.apache.lucene.store.Directory;
//import org.apache.lucene.store.FSDirectory;
//import org.apache.lucene.store.RAMDirectory;
//
//public class test1 {
//
//    public void createIndex2() throws Exception {
//        Directory fsDir = FSDirectory.getDirectory(indexpath);
//        //1、启动时读取
//        Directory ramDir = new RAMDirectory(fsDir);
//
//        // 运行程序时操作ramDir
//        IndexWriter ramIndexWriter = new IndexWriter(ramDir, analyzer, MaxFieldLength.LIMITED);
//
//        //数据源
//        File file = new File(dspath);
//        // 添加 Document
//        Document doc = new Document();
//        //文件名称
//        doc.add(new Field("name", file.getName(), Store.YES, Index.ANALYZED));
//        //检索到的内容
//        doc.add(new Field("content", readFileContent(file), Store.YES, Index.ANALYZED));
//        //文件大小
//        doc.add(new Field("size", NumberTools.longToString(file.length()), Store.YES, Index.NOT_ANALYZED));
//        //检索到的文件位置
//        doc.add(new Field("path", file.getAbsolutePath(), Store.YES, Index.NOT_ANALYZED));
//        ramIndexWriter.addDocument(doc);
//        ramIndexWriter.close();
//
//        //2、退出时保存
//        IndexWriter fsIndexWriter = new IndexWriter(fsDir, analyzer, true, MaxFieldLength.LIMITED);
//        fsIndexWriter.addIndexesNoOptimize(new Directory[]{ramDir});
//
//        // 优化操作
//        fsIndexWriter.commit();
//        fsIndexWriter.optimize();
//
//        fsIndexWriter.close();
//    }
//
//}
