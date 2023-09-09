//package cn.wolfcode.ws;
//
//
//import io.netty.handler.codec.http.websocketx.WebSocketClientHandshakeException;
//import reactor.netty.http.client.HttpClient;
//import reactor.netty.http.client.WebsocketClientSpec;
//
//public class WebSocketClientExample {
//    public static void main(String[] args) {
//        String websocketUrl = "wss://example.com/ws";
//
//        HttpClient client = HttpClient.create()
//                .websocket(WebsocketClientSpec.builder().maxFramePayloadLength(1024 * 1024).build());
//
//        client.get()
//                .uri(websocketUrl)
//                .response()
//                .onErrorResume(WebSocketClientHandshakeException.class, error -> {
//                    System.out.println("WebSocket handshake failed: " + error.getMessage());
//                    // 处理握手异常的逻辑
//                    return null;  // 返回适当的响应或执行其他操作
//                })
//                .block();
//    }
//}