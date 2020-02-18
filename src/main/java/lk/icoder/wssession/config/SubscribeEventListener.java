package lk.icoder.wssession.config;

import org.springframework.context.ApplicationListener;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessageType;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.AbstractSubProtocolEvent;
import org.springframework.web.socket.messaging.SessionSubscribeEvent;

/**
 * @Project ws-session
 * @Author DILAN on 2/13/2020
 */
@Component
public class SubscribeEventListener implements ApplicationListener<SessionSubscribeEvent> {

    @Override
    public void onApplicationEvent(SessionSubscribeEvent sessionSubscribeEvent) {
        SimpMessageHeaderAccessor headerAccessor = SimpMessageHeaderAccessor.create(SimpMessageType.MESSAGE);

        final String simpSessionId = extractSimpSessionId(sessionSubscribeEvent);
        System.out.println("simpSessionId" + simpSessionId);
    }

    private static final String extractSimpSessionId(final AbstractSubProtocolEvent event) {
        return extractSimpHeaderAsString(event, "simpSessionId");
    }

    private static final String extractSimpHeaderAsString(final AbstractSubProtocolEvent event, final String name) {
        final Object simpDestinationObj = event.getMessage().getHeaders().get(name);
        return simpDestinationObj == null ? null : simpDestinationObj.toString();
    }

}
