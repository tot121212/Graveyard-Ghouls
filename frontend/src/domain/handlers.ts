export function handleStaticMessage(message: any) {
    console.log(`Received: ${message.body}`);
}

export function handlePlayerId(message: any) {
    const playerId = message.body;
    // store clientPlayerId in a cookie or state
}
