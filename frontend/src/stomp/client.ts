import { Client } from '@stomp/stompjs';
import { handlePlayerToken, handleGameToken } from '../domain/handlers';

function getCookie(name: string): string | null {
    const match = document.cookie.match(new RegExp('(^|; )' + name + '=([^;]*)'));
    const result = match ? decodeURIComponent(match[2]) : null;
    return result;
}

export const stompClient = new Client({
    brokerURL: 'ws://localhost:9797/ws',
    // validate your websocket connection is a valid player session
    connectHeaders: {
        playerPublicId: getCookie('playerPublicId') || '',
        playerPrivateId: getCookie('playerPrivateId') || '',
    },
    debug: function (str) {
        console.log(str);
    },
    reconnectDelay: 5000,
    heartbeatIncoming: 4000,
    heartbeatOutgoing: 4000,
});

stompClient.onConnect = () => {
    stompClient.subscribe('/topic/sendPlayerSessionToken', message => handlePlayerToken(message));
    stompClient.subscribe('/topic/sendGameSessionToken', message => handleGameToken(message));
}

export function activateStomp() {
    stompClient.activate();
}
export function deactivateStomp() {
    stompClient.deactivate();
}