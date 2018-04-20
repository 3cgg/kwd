package me.libme.kwd.fn;

import java.util.Map;

/**
 * Created by J on 2018/4/20.
 */
@FunctionalInterface
public interface Callback {

    void call(boolean pass, String identifier, String text, Map<String,Object> data, KeywordHolder keywordHolder);


}
