package com.cd.voyager.mobility.webservice;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.cd.voyager.entities.Booking;

@Controller
@RestController
public class NotificationController {

    private SimpMessagingTemplate template;

    @Autowired
    public NotificationController(SimpMessagingTemplate template) {
        this.template = template;
    }

    /*@MessageMapping("/portfolio")
    @SendTo("/topic/greeting")
    public Greeting greeting(HelloMessage message) throws Exception {
    	System.out.println("ZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZz");
        Thread.sleep(3000); // simulated delay
        return new Greeting("Hello, " + message.getName() + "!");
    }
    */
    
    @SendTo("/queue/{userId}")
    @MessageMapping("/notify")
    public Booking userBooking(Booking booking) throws Exception {
    	System.out.println("ZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZz");
        //Thread.sleep(3000); // simulated delay
        return booking;
    }
    
    @RequestMapping(path="/greeting", method=RequestMethod.GET)
    public void greet(String greeting) {
        String text = "[" + "TEST" + "]:" + greeting;
        System.out.println(text);
        this.template.convertAndSend("/topic/greeting", text);
    }

}	
