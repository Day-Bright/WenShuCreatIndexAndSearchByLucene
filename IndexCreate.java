

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field.Store;
import org.apache.lucene.document.StringField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.wltea.analyzer.lucene.IKAnalyzer;

public class IndexCreate {

    public static void main(String[] args) {
//        makeIndexs();
        mergeIndexFiles();
//        searchIndexFileSize();

    }

    /**
     * 合并索引文件
     */
    public static void mergeIndexFiles() {
        IndexWriter writer = null;
        Directory directory = null;
        Directory tempDir = null;
        try {
            Path path = new File("C:\\Users\\Me\\Desktop\\a1").toPath(); //将a合并到a1
            directory = FSDirectory.open(path);
            Path temp = new File("C:\\Users\\Me\\Desktop\\a").toPath();
            tempDir = FSDirectory.open(temp);
            writer = new IndexWriter(directory,
                    new IndexWriterConfig(new IKAnalyzer()));
            writer.addIndexes(tempDir);
            writer.commit();
        } catch (IOException e) {
            e.printStackTrace();
        } finally{
            try {
                writer.close();
                directory.close();
                tempDir.close();
                System.out.println("索引文件合并成功!");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}