/*

package hello;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;

import ch.rasc.wampspring.config.WampEndpointRegistry;
import ch.rasc.wampspring.user.AbstractUserWampConfigurer;

@Configuration
@EnableWebSocketMessageBroker
@ComponentScan(basePackages = { "hello" })
public class WebSocketConfig extends AbstractUserWampConfigurer  {

	@Override
	public void configureMessageBroker(MessageBrokerRegistry config) {
		config.enableSimpleBroker("/topic");
		config.setApplicationDestinationPrefixes("/app");
	}

	@Override
	public void registerWampEndpoints(WampEndpointRegistry registry) {
		registry.addEndpoint("/hello").withSockJS();
	}
	
}
 */