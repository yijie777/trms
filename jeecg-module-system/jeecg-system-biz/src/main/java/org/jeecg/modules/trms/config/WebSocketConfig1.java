package org.jeecg.modules.trms.config;

import io.swagger.models.auth.In;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.config.filter.WebsocketFilter;
import org.jeecg.modules.trms.service.WebSSHService;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.config.annotation.*;
import org.springframework.web.socket.handler.WebSocketHandlerDecorator;
import org.springframework.web.socket.handler.WebSocketHandlerDecoratorFactory;
import org.springframework.web.socket.server.standard.ServerEndpointExporter;

import java.util.Objects;

/**
* @Description: websocket配置
* @Author: NoCortY
* @Date: 2020/3/8
*/
@Configuration
@Slf4j
@AllArgsConstructor
@EnableWebSocketMessageBroker
public class WebSocketConfig1 implements WebSocketMessageBrokerConfigurer {
    private WebSSHService webSSHService;

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry ) {
        //路径"/web-terminal"被注册为STOMP端点，对外暴露，客户端通过该路径接入WebSocket服务
        // addHandler：添加处理器   addInterceptors：添加拦截器
        registry.addEndpoint("web-terminal").setAllowedOrigins("*");
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        // 用户可以订阅来自以"/topic"为前缀的消息，客户端只可以订阅这个前缀的主题
        config.enableSimpleBroker("/topic");
    }

    @Override
    public void configureWebSocketTransport(final WebSocketTransportRegistration registration) {
        registration.addDecoratorFactory(new WebSocketHandlerDecoratorFactory() {
            @Override
            public WebSocketHandler decorate(final WebSocketHandler handler) {
                return new WebSocketHandlerDecorator(handler) {
                    // 上线相关操作
                    @Override
                    public void afterConnectionEstablished(final WebSocketSession session) throws Exception {
                        // 通过创建连接的url解析出userId
                        String query = Objects.requireNonNull(session.getUri()).getQuery();
                        //调用初始化连接（后面改为创建容器）
                        webSSHService.initConnection(query);
                        log.info("-------------【系统 WebSocket】有新的连接");
                        //上线相关操作
                        super.afterConnectionEstablished(session);
                    }
                    // 离线相关操作
                    @Override
                    public void afterConnectionClosed(WebSocketSession session, CloseStatus closeStatus) throws Exception {
                        // 通过创建连接的url解析出userId
                        String query = Objects.requireNonNull(session.getUri()).getQuery();
                        // 移除连接
                        webSSHService.close(query);
                        log.info("-------------【系统 WebSocket】连接断开");
                        //离线相关操作
                        super.afterConnectionClosed(session, closeStatus);
                    }
                };
            }
        });
    }

}