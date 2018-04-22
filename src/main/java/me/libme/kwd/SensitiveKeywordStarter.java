package me.libme.kwd;

import me.libme.kernel._c.pubsub.Publisher;
import me.libme.kernel._c.pubsub.QueuePool;
import me.libme.kernel._c.pubsub.QueuePools;
import me.libme.kernel._c.pubsub.Topic;
import me.libme.kernel._c.util.ThreadUtil;
import me.libme.kwd._regex.RegexWordRecognize;
import me.libme.kwd.source.KeywordSource;
import me.libme.xstream.ConsumerMeta;
import me.libme.xstream.QueueWindowSourcer;
import me.libme.xstream.Topology;
import me.libme.xstream.WindowTopology;

import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

/**
 * Created by J on 2018/4/22.
 */
public class SensitiveKeywordStarter implements Topology {


    private static ExecutorService keywordExecutor= Executors.newFixedThreadPool(ThreadUtil.recommendCount(),
            r->new Thread(r,"real thread on executing topology[sensitive keyword filter]"));

    private static ScheduledExecutorService windowExecutor=Executors.newScheduledThreadPool(1,
            r->new Thread(r,"sensitive-keyword-window-topology-scheduler"));

    private static ExecutorService callbackExecutor= Executors.newFixedThreadPool(ThreadUtil.recommendCount(),
            r->new Thread(r,"real thread on executing topology[sensitive keyword callback]"));



    private Builder builder;


    @Override
    public void shutdown() {

        keywordExecutor.shutdown();
        windowExecutor.shutdown();
        callbackExecutor.shutdown();
    }


    public SensitiveKeyword sensitiveKeyword(){
        SensitiveKeyword sensitiveKeyword=new SensitiveKeyword(builder.unreadTextModelTopic);
        return sensitiveKeyword;
    }

    @Override
    public void start() {
        QueuePool queuePool= QueuePools.defaultPool();

        Topic unreadTextModelTopic= builder.unreadTextModelTopic;// new Topic("Keyword");
        QueueWindowSourcer keywordQueueSourcer=new QueueWindowSourcer(queuePool.queue(unreadTextModelTopic));

        Topic readTextModelTopic=builder.readTextModelTopic;//new Topic("text-model-data");
        Publisher publisher=new Publisher(readTextModelTopic,queuePool);

        RegexWordRecognize regexWordRecognize=new RegexWordRecognize(builder.keywordSource);

        ConsumerMeta keywordMatchWorkerMeta=new ConsumerMeta("KeywordMatchWorker");
        KeywordMatchWorker keywordMatchWorker=new KeywordMatchWorker(keywordMatchWorkerMeta,builder.keywordSource,publisher,
                builder.sensitiveWordRecognize==null?regexWordRecognize:builder.sensitiveWordRecognize);


        // sensitive recognize worker
        WindowTopology.builder().setName("Sensitive Keyword Search")
                .setSourcer(keywordQueueSourcer)
                .addConsumer(keywordMatchWorker)
                .windowExecutor(windowExecutor)
                .executor(keywordExecutor)
                .setSchedule(builder.schedule)
                .build().start();

        //callback worker

        //source
        QueueWindowSourcer callbackQueueSourcer=new QueueWindowSourcer(queuePool.queue(readTextModelTopic));
        //consume
        ConsumerMeta callbackWorkerMeta=new ConsumerMeta("Callback Worker");
        CallbackWorker callbackWorker=new CallbackWorker(callbackWorkerMeta);

        WindowTopology.builder().setName("Sensitive Keyword Callback")
                .setSourcer(callbackQueueSourcer)
                .addConsumer(callbackWorker)
                .windowExecutor(windowExecutor)
                .executor(callbackExecutor)
                .setSchedule(builder.schedule)
                .build().start();
    }


    public static Builder builder(){
        return new Builder();
    }


    public static class Builder{

        private KeywordSource keywordSource;

        private Topic readTextModelTopic=new Topic("readTextModelTopic");

        private Topic unreadTextModelTopic=new Topic("unreadTextModelTopic");;

        private ISensitiveWordRecognize sensitiveWordRecognize;

        private int schedule=500;

        public Builder keywordSource(KeywordSource keywordSource) {
            this.keywordSource = keywordSource;
            return this;
        }

        public Builder schedule(int schedule) {
            this.schedule = schedule;
            return this;
        }

        public Builder unreadTextModelTopic(Topic unreadTextModelTopic) {
            this.unreadTextModelTopic = unreadTextModelTopic;
            return this;
        }

        public Builder readTextModelTopic(Topic readTextModelTopic) {
            this.readTextModelTopic = readTextModelTopic;
            return this;
        }

        public Builder sensitiveWordRecognize(ISensitiveWordRecognize sensitiveWordRecognize) {
            this.sensitiveWordRecognize = sensitiveWordRecognize;
            return this;
        }

        public SensitiveKeywordStarter build(){
            Objects.requireNonNull(keywordSource);
            Objects.requireNonNull(unreadTextModelTopic);
            Objects.requireNonNull(readTextModelTopic);
//            Objects.requireNonNull(sensitiveWordRecognize);

            SensitiveKeywordStarter keywordStarter=new SensitiveKeywordStarter();
            keywordStarter.builder=this;
            return keywordStarter;
        }

    }


}
