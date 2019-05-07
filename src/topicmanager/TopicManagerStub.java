package topicmanager;

import apiREST.apiREST_TopicManager;
import util.Subscription_check;
import util.Topic;
import util.Topic_check;
import java.util.List;
import publisher.Publisher;
import publisher.PublisherStub;
import subscriber.Subscriber;
import util.Subscription_close;
import webSocketService.WebSocketClient;

public class TopicManagerStub implements TopicManager {

    public String user;

    public TopicManagerStub(String user) {
        WebSocketClient.newInstance();
        this.user = user;
    }

    @Override
    public void close() {
        WebSocketClient.close();
    }

    @Override
    public Publisher addPublisherToTopic(Topic topic) {
        Publisher publisher = new PublisherStub(topic);
        apiREST_TopicManager.addPublisherToTopic(topic);
        return publisher;
    }

    @Override
    public void removePublisherFromTopic(Topic topic) {
        apiREST_TopicManager.removePublisherFromTopic(topic);
    }

    @Override
    public Topic_check isTopic(Topic topic) {
        return apiREST_TopicManager.isTopic(topic);
    }

    @Override
    public List<Topic> topics() {
        return apiREST_TopicManager.topics();
    }

    @Override
    public Subscription_check subscribe(Topic topic, Subscriber subscriber) {
        Subscription_check sc;
        if (isTopic(topic).isOpen) {
            WebSocketClient.addSubscriber(topic, subscriber);
            sc = new Subscription_check(topic, Subscription_check.Result.OKAY);
        } else {
            sc = new Subscription_check(topic, Subscription_check.Result.NO_TOPIC);
        }
        return sc;
    }

    @Override
    public Subscription_check unsubscribe(Topic topic, Subscriber subscriber) {
        Subscription_check sc;
        if (isTopic(topic).isOpen) {
            WebSocketClient.removeSubscriber(topic);
            subscriber.onClose(new Subscription_close(topic, Subscription_close.Cause.SUBSCRIBER));
            sc = new Subscription_check(topic, Subscription_check.Result.OKAY);
        } else {
            sc = new Subscription_check(topic, Subscription_check.Result.NO_TOPIC);
        }
        return sc;
    }

}






