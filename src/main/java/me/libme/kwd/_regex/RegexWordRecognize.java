package me.libme.kwd._regex;

import me.libme.kwd.ISensitiveWordRecognize;
import me.libme.kwd.KeywordHolder;
import me.libme.kwd.ListKeywordHolder;
import me.libme.kwd.source.KeywordSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.regex.Pattern;

import static java.util.regex.Pattern.CASE_INSENSITIVE;
import static java.util.regex.Pattern.LITERAL;

/**
 * Created by J on 2018/4/22.
 */
public class RegexWordRecognize implements ISensitiveWordRecognize {

    private static Logger LOGGER= LoggerFactory.getLogger(RegexWordRecognize.class);

    private final KeywordSource keywordSource;

    public RegexWordRecognize(KeywordSource keywordSource) {
        this.keywordSource = keywordSource;
    }

    @Override
    public KeywordHolder recognize(String text) {
        ListKeywordHolder keywordHolder=new ListKeywordHolder();
        keywordSource.forEach(keyword->{
            try {
                Pattern pattern=Pattern.compile(keyword,CASE_INSENSITIVE|LITERAL);
                if(pattern.matcher(text).find()){
                    keywordHolder.add(keyword);
                }
            }catch (Exception e){
                LOGGER.error(e.getMessage(),e);
            }
        });
        return keywordHolder;
    }


}
