import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexableField;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryparser.classic.MultiFieldQueryParser;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.*;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.wltea.analyzer.lucene.IKAnalyzer;
import java.nio.file.Paths;
import java.util.ArrayList;


class Search {

    public IndexSearcher getIndexSearch() throws Exception{
        String creat_path = "C:\\Users\\Me\\Desktop\\s";
        Directory directory = FSDirectory.open(Paths.get(creat_path));
        IndexReader indexReader = DirectoryReader.open(directory);
        return new IndexSearcher(indexReader);
    }

    private ArrayList<StringBuilder> getResult(ScoreDoc[] scoreDocs,IndexSearcher indexSearcher) throws Exception{

        ArrayList<StringBuilder> result = new ArrayList<>();
        for(ScoreDoc scoreDoc : scoreDocs){

            int doc = scoreDoc.doc;
            Document document = indexSearcher.doc(doc);
            StringBuilder part = new StringBuilder();
//            List<IndexableField> fields = document.getFields();
            for ( IndexableField indexableField : document.getFields() )
            {

                String key = indexableField.name();
                String str = key + ":" + document.get(key);
                part.append(str);
                part.append("\n");
            }
            result.add(part);
        }
        return result;
    }


    public ArrayList<StringBuilder> Search_TermQuery(String fld,String text,int n) throws Exception {
        IndexSearcher indexSearcher = getIndexSearch();
        Query query = new TermQuery(new Term(fld,text));
        TopDocs topDocs = indexSearcher.search(query,n);
        ScoreDoc[] scoreDocs = topDocs.scoreDocs;
        ArrayList<StringBuilder> result = getResult(scoreDocs,indexSearcher);
        indexSearcher.getIndexReader().close();
        return result;
    }

    public ArrayList<StringBuilder> Search_BooleanQuery(int n) throws Exception{
        IndexSearcher indexSearcher = getIndexSearch();
        Query query1 = new TermQuery(new Term("", ""));
		Query query2 = new TermQuery(new Term("", ""));
		BooleanQuery.Builder booleanQueryBuilder = new BooleanQuery.Builder();
		booleanQueryBuilder.add(query1, BooleanClause.Occur.MUST);
		booleanQueryBuilder.add(query2, BooleanClause.Occur.MUST);
		BooleanQuery query = booleanQueryBuilder.build();
        TopDocs topDocs = indexSearcher.search(query,n);
        ScoreDoc[] scoreDocs = topDocs.scoreDocs;
        ArrayList<StringBuilder> result = getResult(scoreDocs,indexSearcher);
        indexSearcher.getIndexReader().close();
        return result;
    }

    public ArrayList<StringBuilder> Search_QueryParser(int n) throws Exception{
        IndexSearcher indexSearcher = getIndexSearch();
        QueryParser queryParser = new QueryParser("2",new StandardAnalyzer());//默认检索案件类型
        Query query = queryParser.parse("2:nu");
        TopDocs topDocs = indexSearcher.search(query,n);
        ScoreDoc[] scoreDocs = topDocs.scoreDocs;
        ArrayList<StringBuilder> result = getResult(scoreDocs,indexSearcher);
        indexSearcher.getIndexReader().close();
        return result;
    }

    public ArrayList<StringBuilder> Search_MultiFieldQueryParser(String text,int n) throws Exception{
        IndexSearcher indexSearcher = getIndexSearch();
        String [] fields = {"当事人",
                            "审理程序",
                            "案件类型",
                            "legal_basis",
                            "major_text",
                            "案由",
                            "审理法院",
                            "裁判日期",
                            "docid"};
        MultiFieldQueryParser multiFieldQueryParser = new MultiFieldQueryParser(fields,new IKAnalyzer());

        Query query = multiFieldQueryParser.parse(text);
        TopDocs topDocs = indexSearcher.search(query,n);
        ScoreDoc[] scoreDocs = topDocs.scoreDocs;
        ArrayList<StringBuilder> result = getResult(scoreDocs,indexSearcher);
        indexSearcher.getIndexReader().close();
        return result;
    }

    public ScoreDoc[] test(String text,int n) throws Exception {
        IndexSearcher indexSearcher = getIndexSearch();
        String [] fields = {"当事人",
                "审理程序",
                "案件类型",
                "legal_basis",
                "major_text",
                "案由",
                "审理法院",
                "裁判日期",
                "docid"};
        MultiFieldQueryParser multiFieldQueryParser = new MultiFieldQueryParser(fields,new IKAnalyzer());
        Query query = multiFieldQueryParser.parse(text);
        TopDocs topDocs = indexSearcher.search(query,n);
        return topDocs.scoreDocs;
    }

    /**
     *通配符查询
     * '?' 代表一个字符， '*' 代表0个或多个字符
     * 查询当事人，案由等
     */
    public ArrayList<StringBuilder> Search_WildcardQuery(String fld,String text,int n) throws Exception{
        String new_text = '*' + text +'*';
        IndexSearcher indexSearcher = getIndexSearch();
        Query query = new WildcardQuery(new Term(fld,new_text));
        TopDocs topDocs = indexSearcher.search(query,n);
        ScoreDoc[] scoreDocs = topDocs.scoreDocs;
        ArrayList<StringBuilder> result = getResult(scoreDocs,indexSearcher);
        indexSearcher.getIndexReader().close();
        return result;
    }

    /**
     * 模糊检索不支持中文
     */
    public ArrayList<StringBuilder> Search_FuzzyQuery(String fld,String text,int n) throws Exception{
        IndexSearcher indexSearcher = getIndexSearch();
        FuzzyQuery query = new FuzzyQuery(new Term(fld,text));
        TopDocs topDocs = indexSearcher.search(query,n);
        ScoreDoc[] scoreDocs = topDocs.scoreDocs;
        ArrayList<StringBuilder> result = getResult(scoreDocs,indexSearcher);
        indexSearcher.getIndexReader().close();
        return result;
    }

    public static void main(String[] args) throws Exception {
        Search search = new Search();
        ArrayList<StringBuilder> qq = search.Search_QueryParser(10);
//        System.out.println(qq.size());
        for(StringBuilder i :qq){
            System.out.println(i.toString());
            System.out.println("==================================================");
        }
//        System.out.println(search.Search_WildcardQuery("当事人","孔",10).toString());
//        System.out.println(search.Search_MultiFieldQueryParser("民事案件",100).toString());
    }

}
