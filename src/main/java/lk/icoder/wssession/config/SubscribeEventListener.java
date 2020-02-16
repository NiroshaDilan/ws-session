package lk.icoder.wssession.config;

import org.springframework.context.event.EventListener;
import org.springframework.messaging.Message;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectedEvent;

/**
 * @Project ws-session
 * @Author DILAN on 2/13/2020
 */
@Component
public class SubscribeEventListener {

//    @EventListener
    public void onApplicationEvent(SessionConnectedEvent sessionConnectedEvent) {

        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(sessionConnectedEvent.getMessage());

        System.out.println(headerAccessor.getSessionAttributes().get("sessionId").toString());
    }
}
