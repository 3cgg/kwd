package test.me.libme.kwd;

import me.libme.kwd.SensitiveKeyword;
import me.libme.kwd.SensitiveKeywordStarter;
import me.libme.kwd.source.ListKeywordSource;

import java.util.HashMap;
import java.util.concurrent.TimeUnit;

/**
 * Created by J on 2018/4/22.
 */
public class TestSensitive {


    public static void main(String[] args) throws InterruptedException {

        ListKeywordSource listKeywordSource=new ListKeywordSource();

        listKeywordSource.add("FUNCK");
        listKeywordSource.add("你是猪");
        listKeywordSource.add("TMD");
        listKeywordSource.add("妈蛋的");

        SensitiveKeywordStarter sensitiveKeywordStarter=SensitiveKeywordStarter.builder()
                .keywordSource(listKeywordSource)
                .build();
        sensitiveKeywordStarter.start();

        SensitiveKeyword sensitiveKeyword=sensitiveKeywordStarter.sensitiveKeyword();

        sensitiveKeyword.submit("OSGi(Open Service Gateway Initiative)技术妈蛋的是Java动态化模块化系你是猪统的一系列规范。OSGi一方面指维护OSGi规范的OFUNCKSGI官方联盟，另一方面指的是该组织",
                new HashMap<>(),(pass, text, data, keywordHolder) -> {

                    keywordHolder.stream().forEach(key->{
                        System.out.println("sensitive : "+key);
                    });

                });


        TimeUnit.SECONDS.sleep(100);




    }


}
