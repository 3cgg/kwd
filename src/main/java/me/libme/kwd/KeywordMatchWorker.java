package me.libme.kwd;

import me.libme.kernel._c.pubsub.Produce;
import me.libme.kernel._c.pubsub.Publisher;
import me.libme.kwd.source.KeywordSource;
import me.libme.xstream.Compositer;
import me.libme.xstream.ConsumerMeta;
import me.libme.xstream.EntryTupe;
import me.libme.xstream.Tupe;

import java.util.Iterator;

/**
 * Created by J on 2018/4/22.
 */
public class KeywordMatchWorker extends Compositer {

    private final KeywordSource keywordSource;

    private final Publisher publisher;

    private final Produce produce;

    private final ISensitiveWordRecognize sensitiveWordRecognize;

    public KeywordMatchWorker(ConsumerMeta consumerMeta, KeywordSource keywordSource, Publisher publisher, ISensitiveWordRecognize sensitiveWordRecognize) {
        super(consumerMeta);
        this.keywordSource = keywordSource;
        this.publisher = publisher;
        this.produce = publisher.produce();
        this.sensitiveWordRecognize=sensitiveWordRecognize;
    }

    @Override
    protected void doConsume(Tupe tupe) throws Exception {

        Iterator iterator= tupe.iterator();
        SensitiveKeyword.TextModel textModel= (SensitiveKeyword.TextModel) ((EntryTupe.Entry)iterator.next()).getValue();

        KeywordHolder keywordHolder=sensitiveWordRecognize.recognize(textModel.getText());
        textModel.setKeywordHolder(keywordHolder);
        produce.produce(textModel);

    }



}
