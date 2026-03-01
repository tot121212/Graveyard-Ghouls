<script lang="ts">
    import { RestPaths } from "../paths/RestPaths";
    import { EndpointURIBuilder } from "../utils/EndpointUriBuilder";
    import { postJSON } from "../utils/restApi";
    let gameId = "";

    async function joinGameSession() {
        postJSON<any>(
            new EndpointURIBuilder()
                .addPath(RestPaths.JOIN_GAME_SESSION.path)
                .addQuery("gameId", gameId)
                .build(),
            {},
        ).then(() => {
            // dont actually need the response
            // server will save a cookie of your new playerId
        });
    }
</script>

<p>Have your friend send you the game code!</p>
<input bind:value={gameId} />
<button on:click={joinGameSession}>Join</button>
