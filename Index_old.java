import com.alibaba.fastjson.*;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.*;
import org.apache.lucene.index.*;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.wltea.analyzer.lucene.IKAnalyzer;
import java.io.*;
import java.nio.file.Paths;
import java.util.*;



public class Index_old {


    public static void BuildIndex() throws Exception {

        String creat_path = "C:\\Users\\Me\\Desktop\\a";
        Directory directory = FSDirectory.open(Paths.get(creat_path));
        Analyzer analyzer = new IKAnalyzer(true);
        IndexWriterConfig config = new IndexWriterConfig(analyzer);

//        config.setMaxBufferedDocs(500000);
//        config.setRAMBufferSizeMB(20000);
//        config.setRAMBufferSizeMB(64);
//        config.setUseCompoundFile(false);
//        config.setMaxBufferedDocs(1000);
//        config.setMergePolicy(new LogDocMergePolicy());

        IndexWriter indexWriter = new IndexWriter(directory,config);

        FieldType type1 = new FieldType();
        // 设置是否存储该字段
        type1.setStored(true); // 请试试不存储的结果
        // 设置是否对该字段分词
        type1.setTokenized(true); // 请试试不分词的结果
        // 设置该字段的索引选项
        type1.setIndexOptions(IndexOptions.DOCS); // 反向索引中只保存词项
        // 设置为该字段保存词项向量
        type1.setStoreTermVectors(true);
        type1.setStoreTermVectorPositions(true);
        type1.setStoreTermVectorOffsets(true);
        type1.setStoreTermVectorPayloads(true);
        type1.freeze(); // 使不可更改

        FieldType type2 = new FieldType();
        // 设置是否存储该字段
        type2.setStored(true); // 请试试不存储的结果
        // 设置是否对该字段分词
        type2.setTokenized(false); // 请试试不分词的结果
        // 设置该字段的索引选项
        type2.setIndexOptions(IndexOptions.DOCS); // 反向索引中只保存词项
        // 设置为该字段保存词项向量
        type2.setStoreTermVectors(true);
        type2.setStoreTermVectorPositions(true);
        type2.setStoreTermVectorOffsets(true);
        type2.setStoreTermVectorPayloads(true);
        type2.freeze(); // 使不可更改

        try{
            File file = new File("C:\\Users\\Me\\Desktop\\Index\\src\\testDoc");
            File[] tempList = file.listFiles();
            if(tempList == null){
                System.out.println("路径错误or文件夹无文件");
            }else{
                for(File file_list:tempList){
//                    System.out.println(file_list.getPath());
                    File f = new File(file_list.getPath());
                    BufferedReader br = new BufferedReader(new FileReader(f));
                    String s;
                    while((s = br.readLine())!=null){
                        JSONObject jsonObject = JSONObject.parseObject(s);
//                        System.out.println(jsonObject.toString());
                        Document document=new Document();
                        for (Map.Entry<String, Object> entry : jsonObject.entrySet()) {
                            String key = entry.getKey();
                            String value = entry.getValue().toString();
                            if(key.length()==0){
                                System.out.println(value);
                                continue;
                            }
                            if(value.length()==0){
                                value="null";
                            }
                            if(key.equals("major_text")||key.equals("legal_basis")){
                                document.add(new TextField(key,value, Field.Store.YES));
                            }else{
                                document.add(new StringField(key,value, Field.Store.YES));
                            }
                        }
                        indexWriter.addDocument(document);
                    }
                    indexWriter.flush();
                    indexWriter.commit();
                    br.close();
                }
                indexWriter.close();
            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }


    public static void main(String[] args) throws Exception {
        BuildIndex();
    }

}