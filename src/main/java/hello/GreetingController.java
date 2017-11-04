/*package hello;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@Controller
@RestController
public class GreetingController {

    private SimpMessagingTemplate template;

    @Autowired
    public GreetingController(SimpMessagingTemplate template) {
        this.template = template;
    }

    @MessageMapping("/portfolio")
    @SendTo("/topic/greeting")
    public Greeting greeting(HelloMessage message) throws Exception {
    	System.out.println("ZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZz");
        Thread.sleep(3000); // simulated delay
        return new Greeting("Hello, " + message.getName() + "!");
    }
    
    
    @RequestMapping(path="/greeting", method=RequestMethod.GET)
    public void greet(String greeting) {
        String text = "[" + "TEST" + "]:" + greeting;
        System.out.println(text);
        this.template.convertAndSend("/topic/greeting", text);
    }

}	
*/