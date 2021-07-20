import java.io.IOException;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.lucene.index.*;
import org.apache.lucene.misc.HighFreqTerms;
import org.apache.lucene.misc.TermStats;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.BytesRef;

import static java.nio.file.Paths.get;


public class new_ {


    public static void count() throws Exception {

        IndexReader reader = DirectoryReader.open(FSDirectory.open(get("C:\\Users\\Me\\Desktop\\a")));
        int numTerms = 100000;
        String [] fieldname = new String[] {"major_text","legal_basis","当事人","审理法院","docid","案由","裁判日期","审理程序","案件类型"};
        double C_num = 0.0;
        for(String field:fieldname){
            TermStats[] stats = HighFreqTerms.getHighFreqTerms(reader, numTerms, field, new HighFreqTerms.DocFreqComparator());
            for (TermStats termStats : stats) {
                String termText = termStats.termtext.utf8ToString();
                System.out.println(termText + " " + termStats.docFreq);
                System.out.println(stats.length);
            }
            double F_term = stats.length;
            C_num += F_term;
            System.out.println(stats.length);
        }
        System.out.println(C_num);

        reader.close();

    }

    //获取term在C中的数量
    public Map<String,Double> test1(String Q) throws IOException {
        Map<String,Double> QinC = new HashMap<>();
        IndexReader reader = DirectoryReader.open(FSDirectory.open(get("C:\\Users\\Me\\Desktop\\a")));
        String [] fieldname = new String[] {"major_text","legal_basis","当事人","审理法院","docid","案由","裁判日期","审理程序","案件类型"};
        long all_tf = 0;
        for(String fn:fieldname){
            long tf = reader.totalTermFreq(new Term(fn,Q));
            all_tf+=tf;
        }
        double All_TF = (double) all_tf;
        QinC.put(Q,All_TF);
        return QinC;
    }


    //获取C的分词数量
    public static long test2() throws IOException {
        IndexReader reader = DirectoryReader.open(FSDirectory.open(get("C:\\Users\\Me\\Desktop\\a")));
        long sumTF = 0;
        String [] fieldname = new String[] {"major_text","legal_basis","当事人","审理法院","docid","案由","裁判日期","审理程序","案件类型"};
        for(String fn:fieldname){
            long TermFreq = reader.getSumTotalTermFreq(fn);
            sumTF+=TermFreq;
        }
//        System.out.println(sumTF);
        return sumTF;
    }

    public Map<Integer, Map<String, Double>> test3(String Q) throws Exception{
        Map<Integer, Map<String, Double>> fuck = new HashMap<>();
        modle_1 m = new modle_1();
        List<String> r_list = m.R_List(Q);
        Search search = new Search();
        IndexSearcher indexSearch = search.getIndexSearch();
        ScoreDoc[] test = search.test(Q, 1000);
        for(ScoreDoc scoreDoc:test){
            Map<String, Double> XinQ = new HashMap<>();
            int i = scoreDoc.doc;
            Fields fields = indexSearch.getIndexReader().getTermVectors(i);
            for(String j:r_list){
                //            Iterator<String> fieldsIterator = fields.iterator();
                double X=0.0;
                double XcD=0.0;
                double  D_count = 0.0;
                for(String f: fields){
//                String field = fieldsIterator.next();
                    Terms terms = fields.terms(f);
                    double F_count = terms.size();
                    D_count+=F_count;
                    TermsEnum termsEnums = terms.iterator();
                    BytesRef byteRef = null;
                    while((byteRef = termsEnums.next()) != null) {
                        String term = new String(byteRef.bytes, byteRef.offset, byteRef.length);
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


    public static Map<Integer, Double> test(String Q) throws Exception {

        Map<Integer, Double> XinQ = new HashMap<>();
        Search search = new Search();
        IndexSearcher indexSearch = search.getIndexSearch();
        ScoreDoc[] test = search.test(Q, 1000);
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
//            System.out.println(D_count);
//            System.out.println(X);
            XcD=X/D_count;
            XinQ.put(i,XcD);
        }
        return XinQ;
    }

    public static void main(String[] args) throws Exception {
        new_ n = new new_();
        System.out.println(n.test3("高人"));
        System.out.println(test("高人"));
    }
}




