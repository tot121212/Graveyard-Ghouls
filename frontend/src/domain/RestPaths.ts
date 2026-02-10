export const REST = {
    path: "api",
    GameSession: {
        GET_PAGE: {
            path: "getGameSessionPage",
            queries: {
                PAGE_NUMBER: "pageNumber",
                SIZE: "size"
            }
        },
        GET_TOTAL_SESSIONS: {
            path: "getTotalSessions"
        },
        CREATE: {
            path: "createGameSession",
            queries: {
                NAME: "name",
                MAX_PLAYERS: "maxPlayers"
            }
        }
    },
    User: {
        LOGIN: {
            path: "login",
            queries: {
                USERNAME: "username",
                PASSWORD: "password"
            }
        },
        LIST: {
            path: "usersList"
            // no queries
        }
    }
} as const;
