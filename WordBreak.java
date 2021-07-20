import com.huaban.analysis.jieba.*;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.wltea.analyzer.core.IKSegmenter;
import org.wltea.analyzer.core.Lexeme;
import org.wltea.analyzer.lucene.IKAnalyzer;
import java.io.*;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;


public class WordBreak {

    //中文分词
    public List<String> CWordBreak(String CNstr){
        // 词典路径为Resource/dicts/jieba.dict
//        WordDictionary.getInstance().loadUserDict(new File("src\\jar\\lawdicts.dict").toPath());

        JiebaSegmenter segmenter = new JiebaSegmenter();
        return segmenter.sentenceProcess(CNstr);
    }

    //英文分词
    public List<String> EWordBreak(String ENstr) throws IOException {
        List<String> EList = new ArrayList<>();
        Analyzer analyzer=new IKAnalyzer();
        StringReader reader=new StringReader(ENstr);
        TokenStream tokenStream=analyzer.tokenStream("", reader);
        CharTermAttribute term=tokenStream.getAttribute(CharTermAttribute.class);
        tokenStream.reset();
        while(tokenStream.incrementToken()){
            EList.add(term.toString());
        }
        reader.close();
        analyzer.close();
        return EList;
    }


    //中英文分词
    public List<String> CEWordBreak(String CEstr){
        StringBuilder CN_str = new StringBuilder();
        StringBuilder EN_str = new StringBuilder();
        char[] char_str =CEstr.toCharArray();
        for(char i_char:char_str){
            if(Character.toString(i_char).matches("[\\u4E00-\\u9FA5]+")){
                CN_str.append(i_char);
            }else{
                EN_str.append(i_char);
            }
        }
        List<String> CList = CWordBreak(CN_str.toString());
        List<String> EList = CWordBreak(EN_str.toString());
        CList.addAll(EList);
        return CList;
    }


    public List<String> StandardList(String QueryStr) throws IOException {
        QueryStr = QueryStr.replaceAll("[1234567890]"," ");
        List<String> QueryList = new ArrayList<>();
        Analyzer analyzer=new StandardAnalyzer(
                new FileReader(new File("C:\\Users\\Me\\Desktop\\Index\\src\\新建文件夹\\stopwords.txt"))
        );
        StringReader reader=new StringReader(QueryStr);
        TokenStream tokenStream=analyzer.tokenStream("", reader);
        CharTermAttribute term=tokenStream.getAttribute(CharTermAttribute.class);
        tokenStream.reset();
        while(tokenStream.incrementToken()){
            QueryList.add(term.toString());
        }
        reader.close();
        analyzer.close();
        return QueryList;
    }

    public String StandardStr(String QueryStr) throws IOException {
        QueryStr = QueryStr.replaceAll("[1234567890]"," ");

        Analyzer analyzer=new StandardAnalyzer(
                new FileReader(new File("C:\\Users\\Me\\Desktop\\Index\\src\\新建文件夹\\stopwords.txt"))
        );
        StringReader reader=new StringReader(QueryStr);
        TokenStream tokenStream=analyzer.tokenStream("", reader);
        CharTermAttribute term=tokenStream.getAttribute(CharTermAttribute.class);
        tokenStream.reset();
        String QueryString="";
        while(tokenStream.incrementToken()){
            QueryString=term.toString()+" "+QueryString;
        }
        reader.close();
        analyzer.close();
        return QueryString;
    }

    public static void main(String[] args) throws IOException {
        String test_str = "qq ww eer rt";
        WordBreak wordBreak = new WordBreak();
        System.out.println(wordBreak.StandardList(test_str));
        System.out.println(wordBreak.StandardStr(test_str));
    }

}
