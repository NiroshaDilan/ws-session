package lk.icoder.wssession.controller;

import com.google.gson.Gson;
import org.springframework.messaging.handler.annotation.MessageExceptionHandler;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.SimpMessageType;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.messaging.simp.annotation.SubscribeMapping;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Controller;
import org.springframework.util.MimeTypeUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.HashMap;
import java.util.Map;

/**
 * @Project ws-session
 * @Author DILAN on 2/15/2020
 */
@Controller
public class WebSocketController {

    private SimpMessageSendingOperations messagingTemplate;

    public WebSocketController(SimpMessageSendingOperations messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    @MessageMapping("/message")
//    @SubscribeMapping("/message")
    public void processMessageFromClient(@Payload String message,
                                         SimpMessageHeaderAccessor headerAccessor) throws Exception {

        String sessionId = headerAccessor.getSessionAttributes().get("sessionId").toString();
        System.out.println("---processMessageFromClient -----" +  sessionId);
        headerAccessor.setSessionId(sessionId);
//        messagingTemplate.convertAndSend("/topic/reply", new Gson().fromJson(message, Map.class).get("name"));
        messagingTemplate
                .convertAndSendToUser(sessionId, "/topic/reply", new Gson().fromJson(message, Map.class).get("name"),
                        headerAccessor.getMessageHeaders());
//        testMessage(message, sessionId);
    }

    @PostMapping("/push/{message}/{sessionId}")
    public void pushMessageListener(@PathVariable("message") String message,
                                    @PathVariable("sessionId") String sessionId) {

        SimpMessageHeaderAccessor headerAccessor = SimpMessageHeaderAccessor.create();
        headerAccessor.setContentType(MimeTypeUtils.TEXT_PLAIN);
        headerAccessor.setSessionId(sessionId);
        headerAccessor.setLeaveMutable(true);
//        messagingTemplate.convertAndSend("/topic/reply", message);
        messagingTemplate.convertAndSendToUser(sessionId, "/topic/reply", message, headerAccessor.getMessageHeaders());
    }

    private void testMessage(String message, Object sessionId) {
        Map<String, Object> attributes = new HashMap();
        attributes.put("sessionId", sessionId);
        SimpMessageHeaderAccessor headerAccessor = SimpMessageHeaderAccessor.create(SimpMessageType.MESSAGE);
        headerAccessor.setSessionId(sessionId.toString());
        headerAccessor.setLeaveMutable(true);
//        messagingTemplate.convertAndSendToUser(sessionId, "/topic/reply", message, headerAccessor.getMessageHeaders());
        messagingTemplate.convertAndSend("/topic/reply",  new Gson().fromJson(message, Map.class).get("name"), headerAccessor.getMessageHeaders());
    }

    @MessageExceptionHandler
    @SendToUser("/queue/errors")
    public String handleException(Throwable exception) {
        return exception.getMessage();
    }
}
