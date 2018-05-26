package lab4;

import javax.jms.Connection;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;
import javax.jms.Session;
import javax.jms.TextMessage;

public class Consumer implements MessageListener {

    private String name;
    private String dest;
    private Connection conn;
    private String client_id;
    private MessageConsumer consumer;
    private Session session;

    public Consumer(Connection conn, String dest, String name, String client_id) {
        this.conn = conn;
        this.dest = dest;
        this.name = name;
        this.client_id = client_id;
    }

    public void start() throws JMSException {
        //使用Consumer之前，必须调用conn的start方法建立连接。  
        conn.setClientID(client_id);
        conn.start();

        session = conn.createSession(false, Session.AUTO_ACKNOWLEDGE);
        consumer = session.createDurableSubscriber(session.createTopic(dest), name);

        consumer.setMessageListener(this);

    }

    @Override
    public void onMessage(Message msg) {
        try {
            System.out.println("User" + name + " received message:" + ((TextMessage) msg).getText());
        } catch (JMSException e) {
            e.printStackTrace();
        }
    }

}
