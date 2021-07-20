import org.apache.lucene.index.*;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.BytesRef;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.*;


public class modle_1 {

     private final static String [] fieldname= new String[] {"major_text","legal_basis","当事人","审理法院","docid","案由","裁判日期","审理程序","案件类型"};
     private final static String path= "C:\\Users\\Me\\Desktop\\a";

    //获取C的分词数量
    public static long getC() throws IOException {
        IndexReader reader = DirectoryReader.open(FSDirectory.open(Paths.get(path)));
        long sumTF = 0;
        for(String fn:fieldname){
            long TermFreq = reader.getSumTotalTermFreq(fn);
            sumTF+=TermFreq;
        }
        return sumTF;
    }


    //Q分词
    public List<String> R_List(String Q) {
        WordBreak wordBreak = new WordBreak();
        return wordBreak.CWordBreak(Q);
    }


    //获取term在C中的数量
    public Double XinC(String Q) throws IOException {
//        Map<String,Double> QinC = new HashMap<>();
        IndexReader reader = DirectoryReader.open(FSDirectory.open(Paths.get(path)));
        long all_tf = 0;
        for(String fn:fieldname){
            long tf = reader.totalTermFreq(new Term(fn,Q));
            all_tf+=tf;
        }
        return (double) all_tf;
//        QinC.put(Q,All_TF);
    }


    public Map<String,Double> QinC(String Q) throws IOException {
        Map<String,Double> QinC = new HashMap<>();
        long c = getC();
        double C = (double) c;
        List<String> r_list = R_List(Q);
        for(String x:r_list){
            Double xinc = XinC(x)/C;
            QinC.put(x,xinc);
        }
        return QinC;
    }


    //获取Q中每个X在D中的比例
    public Map<Integer, Double> XinD(String Q) throws Exception {
        Map<Integer, Double> XinQ = new HashMap<>();
        Search search = new Search();
        IndexSearcher indexSearch = search.getIndexSearch();
        ScoreDoc[] test = search.test(Q, indexSearch.getIndexReader().maxDoc());
        for(ScoreDoc scoreDoc:test){
            int i = scoreDoc.doc;
            Fields fields = indexSearch.getIndexReader().getTermVectors(i);
            double  D_count = 0.0;
            double X=0.0;
            double XcD=0.0;
//            Iterator<String> fieldsIterator = fields.iterator();
            for(String f: fields){
//                String field = fieldsIterator.next();
                Terms terms = fields.terms(f);
                double F_count = terms.size();
                D_count+=F_count;
                TermsEnum termsEnums = terms.iterator();
                BytesRef byteRef = null;
                while((byteRef = termsEnums.next()) != null) {
                    String term = new String(byteRef.bytes, byteRef.offset, byteRef.length);
                    if(Q.equals(term)){
                        X+=1.0;
                    }
                }
            }
            XcD=X/D_count;
            XinQ.put(i,XcD);
        }
        return XinQ;
    }


    //遍历Q获取每个X在文档中的比例
    public Map<String,Map<Integer, Double>> QinD(String Q) throws Exception {
        List<String> r_list = R_List(Q);
        Map<String,Map<Integer, Double>> kid = new HashMap<>();
        for(String j : r_list){
            Map<Integer, Double> fuck = XinD(j);
            kid.put(j,fuck);
//            System.out.println(fuck);
        }
        return kid;
    }


    public Map<Integer, Map<String, Double>> test(String Q) throws Exception{
        Map<Integer, Map<String, Double>> fuck = new HashMap<>();
        modle_1 m = new modle_1();
        List<String> r_list = m.R_List(Q);
        Search search = new Search();
        IndexSearcher indexSearch = search.getIndexSearch();
        ScoreDoc[] test = search.test(Q, indexSearch.getIndexReader().maxDoc());
        for(ScoreDoc scoreDoc:test){
            Map<String, Double> XinQ = new HashMap<>();
            int i = scoreDoc.doc;
            Fields fields = indexSearch.getIndexReader().getTermVectors(i);
//            System.out.println(fields);
            for(String j:r_list){
                double X=0.0;
                double XcD=0.0;
                double  D_count = 0.0;
                for(String f: fields){
//                    System.out.println(f);
                    Terms terms = fields.terms(f);
                    double F_count = terms.size();
                    D_count+=F_count;
                    TermsEnum termsEnums = terms.iterator();
                    BytesRef byteRef = null;
                    while((byteRef = termsEnums.next()) != null) {
                        String term = new String(byteRef.bytes, byteRef.offset, byteRef.length);
                        System.out.println(term+j);
                        if(j.equals(term)){
                            X+=1.0;
                        }
                    }
                }
                XcD=X/D_count;
                XinQ.put(j,XcD);
            }
            fuck.put(i,XinQ);
        }
        return fuck;
    }

    public List<Double> core(String Q) throws Exception{
        Map<Integer, Map<String, Double>> test = test(Q);
        Map<String, Double> qinC = QinC(Q);
        List<String> r_list = R_List(Q);
        List<Double> result = new ArrayList<>();
        for(Map.Entry<Integer, Map<String, Double>> entry_test:test.entrySet()){
            Map<String, Double> value = entry_test.getValue();
            double result_x = 0.0;
            double result_Q = 1.0;
            for(String x:r_list){
                for(Map.Entry<String, Double> entry_value:value.entrySet()){
                    for(Map.Entry<String, Double> entry_qinC:qinC.entrySet()){
                        if(x.equals(entry_value.getKey())&&x.equals(entry_qinC.getKey())){
                            result_x=entry_value.getValue()+entry_qinC.getValue();
                        }
                    }
                }
                result_Q=result_Q*result_x;
            }
            result.add(result_Q);
        }
        return result;
    }

    public static void main(String[] args) throws Exception {
        modle_1 m = new modle_1();
        String Q = "孔凡军";
//        System.out.println(m.R_List(Q));
        System.out.println(m.test(Q));
//        System.out.println(m.QinC(Q));
//        System.out.println(m.core(Q));
    }

}
