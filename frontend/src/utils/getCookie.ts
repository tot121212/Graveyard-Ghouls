export function getCookie(name: string): string | null {
    const match = document.cookie.match(new RegExp('(^|; )' + name + '=([^;]*)'));
    const result = match ? decodeURIComponent(match[2]) : null;
    return result;
}
