package top.jwxiang.websocket.handler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.*;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
@Slf4j
public class TestWebSocketHandler implements WebSocketHandler {
    // 定义一个线程安全的静态 Map 常量 SESSIONS，用于存储 WebSocket 会话信息。其中：
    // ConcurrentHashMap 保证多线程环境下的并发安全，key 为字符串类型，value 为 WebSocketSession 对象
    // 用于管理和维护所有活跃的 WebSocket 连接会话
    private static final Map<String, WebSocketSession> SESSIONS = new ConcurrentHashMap<>();

    /**
     * 连接建立处理
     */
    @Override
    public void afterConnectionEstablished(WebSocketSession session) {
        // 获取连接的会话 ID
        String clientId = session.getId();
        log.info("客户端 {} 连接成功", clientId);
        //将本次连接的会话保存到 SESSIONS 中
        SESSIONS.put(clientId, session);
        // 调用封装的私有方法，向该会话发送欢迎消息
        sendMessage(session, "欢迎连接到WebSocket服务");
    }

    /**
     * 接收消息处理
     */
    @Override
    public void handleMessage(WebSocketSession session, WebSocketMessage<?> message) {
        String clientMessage = message.getPayload().toString();
        log.info("收到消息: {}", clientMessage);
        // 根据消息内容进行处理
        if ("ping".equals(clientMessage)) {
            sendMessage(session, "pong");
        }
    }

    /**
     * 连接异常处理
     */
    @Override
    public void handleTransportError(WebSocketSession session, Throwable exception) {
        log.error("连接异常: {}", session.getId(), exception);
        // 调用私有方法，清理该会话的资源
        cleanup(session);
    }

    /**
     * 连接关闭处理
     */
    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus closeStatus) {
        log.info("客户端 {} 断开连接，原因: {}", session.getId(), closeStatus.getReason());
        // 调用私有方法，清理该会话的资源
        cleanup(session);
    }

    /**
     * 不支持部分消息
     */
    @Override
    public boolean supportsPartialMessages() {
        return false;
    }

    /**
     * 私有方法，向某个会话发送消息
     */
    private void sendMessage(WebSocketSession session, String message) {
        try {
            if (session.isOpen()) {
                session.sendMessage(new TextMessage(message));
            }
        } catch (Exception e) {
            log.error("发送消息失败: {}", e.getMessage());
        }
    }

    /**
     * 私有方法，清理某个会话的资源
     */
    private void cleanup(WebSocketSession session) {
        // 清理连接相关资源
        SESSIONS.remove(session.getId());
    }

}
