package me.libme.kwd;

import me.libme.xstream.Compositer;
import me.libme.xstream.ConsumerMeta;
import me.libme.xstream.EntryTupe;
import me.libme.xstream.Tupe;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Iterator;

/**
 * Created by J on 2018/4/22.
 */
public class CallbackWorker extends Compositer {

    private static Logger LOGGER= LoggerFactory.getLogger(CallbackWorker.class);

    public CallbackWorker(ConsumerMeta consumerMeta) {
        super(consumerMeta);
    }

    @Override
    protected void doConsume(Tupe tupe) throws Exception {

        Iterator iterator= tupe.iterator();
        SensitiveKeyword.TextModel textModel= (SensitiveKeyword.TextModel) ((EntryTupe.Entry)iterator.next()).getValue();
        try {
            textModel.getCallback().call(textModel.pass(), textModel.getText(), textModel.getData(), textModel.getKeywordHolder());
        }catch (Exception e) {
            LOGGER.error(e.getMessage(),e);
        }

    }



}
