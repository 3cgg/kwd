package me.libme.kwd.fn;

import java.util.Map;

/**
 * Created by J on 2018/4/20.
 */
public interface IKeyword {

    void submit(String identifier,String text,Map<String,Object> data, Callback callback);

}
