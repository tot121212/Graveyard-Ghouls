export class EndpointURIBuilder {
    private pathSegments: string[] = [];
    private queryParams: Record<string, string> = {};

    addPath(segment: string) {
        this.pathSegments.push(segment.replace(/^\/+|\/+$/g, "")); // strip slashes
        return this;
    }

    addQuery(key: string, value: string | number | boolean) {
        this.queryParams[key] = String(value);
        return this;
    }

    addQueries(params: Record<string, string | number | boolean>) {
        for (const key in params) {
            if (params[key] != null) this.queryParams[key] = String(params[key]);
        }
        return this;
    }

    build(): string {
        const path = "/" + this.pathSegments.join("/");
        const query = Object.entries(this.queryParams)
            .map(([k, v]) => `${encodeURIComponent(k)}=${encodeURIComponent(v)}`)
            .join("&");
        return path + (query ? "?" + query : "");
    }
}
