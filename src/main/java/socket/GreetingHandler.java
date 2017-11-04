package socket;

import java.io.IOException;

import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

public class GreetingHandler extends TextWebSocketHandler {

    @Override
    public void handleTextMessage(WebSocketSession session, TextMessage message) {
        try {
			Thread.sleep(100);
	        TextMessage msg = new TextMessage("Hello, " + message.getPayload() + "!");
	        session.sendMessage(msg);
	        
        } catch (InterruptedException | IOException e) {
        	// TODO Auto-generated catch block
        	e.printStackTrace();
        } // simulated delay
    }
    
    
}
