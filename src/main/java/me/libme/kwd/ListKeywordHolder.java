package me.libme.kwd;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

/**
 * Created by J on 2018/4/22.
 */
public class ListKeywordHolder implements KeywordHolder{

    private final List<String> words=new ArrayList<>();


    public boolean add(String word){
        return words.add(word);
    }

    @Override
    public Stream stream() {
        return words.stream();
    }



}
