<script lang="ts">
    import { onMount } from "svelte";
    import { REST } from "../domain/RestPaths";
    const {
        GameSession: { GET_PAGE, CREATE, GET_TOTAL_SESSIONS },
        User: { LOGIN },
    } = REST;
    import { getJSON } from "../utils/restApi";
    import { EndpointURIBuilder } from "../utils/EndpointUriBuilder";

    const PAGE_SIZE = 10;
    const DEFAULT_PAGE = 1;

    let totalSessions: number = $state(-1);
    let currentPageNumber = $state(-1);
    let sessions: any[] = $state([]);

    const pageItemCountDerived = $derived(() => {
        let min: number = 0;
        let max: number = 0;
        if (totalSessions <= 0) return { min: 0, max: 0 };
        min = (currentPageNumber - 1) * PAGE_SIZE + 1;
        max = Math.min(currentPageNumber * PAGE_SIZE, totalSessions);
        return { min, max };
    });

    function updateTotalSessions() {
        getJSON(
            new EndpointURIBuilder()
                .addPath(REST.path)
                .addPath(GET_TOTAL_SESSIONS.path)
                .build(),
        ).then((data: any) => {
            //set totalSessions
            if (data?.total === null) return;
            if (typeof data.total !== "number") return;
            totalSessions = data.total;
        });
    }

    function queryForPage(pageNumber: number, size: number) {
        updateTotalSessions();
        const query: string = new EndpointURIBuilder()
            .addPath(REST.path)
            .addPath(GET_TOTAL_SESSIONS.path)
            .addQuery(GET_PAGE.queries.PAGE_NUMBER, pageNumber)
            .addQuery(GET_PAGE.queries.SIZE, size)
            .build();
        getJSON(query).then((data: any) => {
            if (data?.page === null || data?.pageNumber === null) return;
            if (typeof data.page !== "object" || data.pageNumber !== "number")
                return;
            console.log("Hello");
            return;
        });
    }

    onMount(async () => {
        queryForPage(DEFAULT_PAGE, PAGE_SIZE);
    });
</script>

<nav>
    <button>New Game</button>

    <p>
        Showing items
        {pageItemCountDerived().min} - {pageItemCountDerived().max}
    </p>

    <table class="border-collapse border border-gray-300 w-full">
        <thead>
            <tr class="bg-gray-200">
                <th class="border border-gray-300 px-4 py-2">ID</th>
                <th class="border border-gray-300 px-4 py-2">Name</th>
            </tr>
        </thead>
        <tbody>
            {#each sessions as session}
                <tr class="hover:bg-gray-100">
                    <td class="border border-gray-300 px-4 py-2"
                        >{session.id}</td
                    >
                    <td class="border border-gray-300 px-4 py-2"
                        >{session.name}</td
                    >
                </tr>
            {/each}
        </tbody>
    </table>
</nav>
