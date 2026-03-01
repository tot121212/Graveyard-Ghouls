export const RestPaths = {
    API: "/api",
    GET_GAME_SESSION_PAGE: {
        path: "/getGameSessionPage",
        queries: {
            PAGE_NUMBER: "pageNumber",
            SIZE: "size"
        }
    },
    GET_TOTAL_SESSIONS: {
        path: "getTotalSessions"
    },
    CREATE_GAME_SESSION: {
        path: "createGameSession",
        queries: {
            NAME: "name",
            MAX_PLAYERS: "maxPlayers"
        }
    },
    JOIN_GAME_SESSION: {
        path: "/joinGameSession"
    },
    LEAVE_GAME_SESSION: {
        path: "/leaveGameSession"
    }
} as const;
