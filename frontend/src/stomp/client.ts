import { Client } from '@stomp/stompjs';
import { handleStaticMessage, handlePlayerId } from '../domain/handlers';

export const stompClient = new Client({
    brokerURL: 'ws://localhost:9797/ws',
    reconnectDelay: 5000,
    heartbeatOutgoing: 4000,
});

stompClient.onConnect = () => {
    stompClient.subscribe('/topic/static', message => handleStaticMessage(message));
    stompClient.subscribe('/topic/sendClientPlayerID', message => handlePlayerId(message));

    stompClient.publish({
        destination: '/topic/static',
        body: 'Client: Websocket Connected!',
    });
};

export function activateStomp() {
    stompClient.activate();
}
