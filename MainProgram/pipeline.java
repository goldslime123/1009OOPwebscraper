package MainProgram;

import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import java.util.Properties;

public class pipeline {

    private static Properties properties;
    private static String propertiesName = "tokenize, sspilt, pos. lemma, ner, parse, sentiment";
    private static StanfordCoreNLP stanfordCoreNLP;

    private pipeline() {}

    static {
        properties = new Properties();
        properties.setProperty("annotators", propertiesName);

    }

    public static StanfordCoreNLP getPipeline() {
        if(stanfordCoreNLP == null) {
            stanfordCoreNLP= new StanfordCoreNLP(properties);
        }
        return stanfordCoreNLP;
    }

    
}
