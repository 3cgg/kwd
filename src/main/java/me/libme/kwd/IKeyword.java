package me.libme.kwd;

import java.util.Map;

/**
 * Created by J on 2018/4/20.
 */
public interface IKeyword {

    void submit(String text,Map<String,Object> map, Callback callback);

}
