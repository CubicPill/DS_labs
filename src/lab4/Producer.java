package lab4;

import javax.jms.Connection;  
import javax.jms.JMSException;  
import javax.jms.MessageProducer;  
import javax.jms.Session;  
import javax.jms.TextMessage;  
  
public class Producer {  
  
    private String name;  
    private String dest;  
    private Connection conn;  
    private MessageProducer producer;  
    private Session session;  
  
    public Producer(Connection conn, String dest, String name) {  
        this.conn = conn;  
        this.dest = dest;  
        this.name = name;  
    }  
  
    public void start() throws JMSException {  
        //conn 可以不连接,当发送消息是会自动建立连接。  
        conn.start();  
        session = conn.createSession(false, Session.AUTO_ACKNOWLEDGE);  
        producer = session.createProducer(session.createTopic(dest));  
    }  
      
    public void send(String text) throws JMSException{  
        TextMessage msg = session.createTextMessage(name + ": " + text);  
        producer.send(msg);  
    }  
}  
