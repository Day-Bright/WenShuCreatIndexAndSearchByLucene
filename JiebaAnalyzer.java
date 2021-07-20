import com.huaban.analysis.jieba.WordDictionary;
import org.apache.lucene.analysis.*;
import org.apache.lucene.util.IOUtils;

import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;


public class JiebaAnalyzer extends Analyzer {

    private final CharArraySet stopWords;

    private static final String DEFAULT_STOPWORD_FILE = "stopwords.txt";

    private static final String STOPWORD_FILE_COMMENT = "//";

    /**
     * Returns an unmodifiable instance of the default stop-words set.
     *
     * @return an unmodifiable instance of the default stop-words set.
     */
    public static CharArraySet getDefaultStopSet() {
        return DefaultSetHolder.DEFAULT_STOP_SET;
    }

    /**
     * Atomically loads the DEFAULT_STOP_SET in a lazy fashion once the outer
     * class accesses the static final set the first time.;
     */
    private static class DefaultSetHolder {
        static final CharArraySet DEFAULT_STOP_SET;

        static {
            try {
                DEFAULT_STOP_SET = loadDefaultStopWordSet();
                System.out.println(DEFAULT_STOP_SET);
            } catch (IOException ex) {
                // default set should always be present as it is part of the
                // distribution (JAR)
                throw new RuntimeException(
                        "Unable to load default stopword set");
            }
        }

        static CharArraySet loadDefaultStopWordSet() throws IOException {
            // make sure it is unmodifiable as we expose it in the outer class
            return CharArraySet.unmodifiableSet(WordlistLoader.getWordSet(
                    IOUtils.getDecodingReader(JiebaAnalyzer.class,
                            DEFAULT_STOPWORD_FILE, StandardCharsets.UTF_8),
                    STOPWORD_FILE_COMMENT));
        }
    }

    private String type;

    private CharArraySet loadStopWords(Path dataPath) {
        try {
            return CharArraySet.unmodifiableSet(WordlistLoader.getWordSet(
                    new FileReader(dataPath.resolve("stopwords.txt").toFile()), STOPWORD_FILE_COMMENT));
        } catch (IOException e) {
            return DefaultSetHolder.DEFAULT_STOP_SET;
        }
    }


    public JiebaAnalyzer(String segMode, Path dataPath, boolean isStop) {
        super();

        this.type = segMode;
        WordDictionary.getInstance().init(dataPath);
        this.stopWords = isStop ? this.loadStopWords(dataPath)
                : CharArraySet.EMPTY_SET;
    }

    @Override
    protected TokenStreamComponents createComponents(String fieldName) {
        Tokenizer tokenizer = new SentenceTokenizer();
        System.out.println("fieldName = " + fieldName);
        TokenStream result = new JiebaTokenFilter(type, tokenizer);
        if (!type.equals("other") && !stopWords.isEmpty()) {
            result = new StopFilter(result, stopWords);
        }
        return new TokenStreamComponents(tokenizer, result);
    }
}
