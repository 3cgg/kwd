package me.libme.kwd;

import me.libme.kernel._c.pubsub.Produce;
import me.libme.kernel._c.pubsub.Publisher;
import me.libme.kernel._c.pubsub.QueuePools;
import me.libme.kernel._c.pubsub.Topic;

import java.util.Map;

/**
 * Created by J on 2018/4/20.
 */
public class SensitiveKeyword implements IKeyword {

    private final Produce produce;

    SensitiveKeyword(Topic topic) {

        Publisher publisher=new Publisher(topic,QueuePools.defaultPool());
        this.produce = publisher.produce();
    }

    @Override
    public void submit(String text, Map<String, Object> data, Callback callback) {

        TextModel textModel=new TextModel();
        textModel.text=text;
        textModel.data=data;
        textModel.callback=callback;
        produce.produce(textModel);
    }


    static class TextModel{

        private String text;

        private Map<String, Object> data;

        private Callback callback;

        private KeywordHolder keywordHolder;


        public String getText() {
            return text;
        }

        void setKeywordHolder(KeywordHolder keywordHolder) {
            this.keywordHolder = keywordHolder;
        }

        public KeywordHolder getKeywordHolder() {
            return keywordHolder;
        }

        public Callback getCallback() {
            return callback;
        }


        public boolean pass(){
            return keywordHolder.stream().count()==0;
        }


        Map<String, Object> getData() {
            return data;
        }
    }



}
