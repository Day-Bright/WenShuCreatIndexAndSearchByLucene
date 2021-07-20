import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

public class ngram {

    public LinkedList<String> Ngram(String QueryStr,int N) throws IOException {
        WordBreak wordBreak = new WordBreak();
        List<String> QueryList = wordBreak.StandardList(QueryStr);
        LinkedList<String> new_QueryList = new LinkedList<>();
        for(int i=0;i<QueryList.size();i++){
            String words = "";
            if(QueryList.size()-i<N){
                break;
            }else{
                for(int j=0;j<N;j++){
                    words+=QueryList.get(i+j);
                }
                new_QueryList.add(words);
            }
        }
        return new_QueryList;
    }

}

