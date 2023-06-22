export function equalModels(a, b) {
    if (a.language !== b.language) throw `Assertion error on model language: ${a.language} != ${b.language}`;
    equalUri(a.uri, b.uri);
}

export function equalUri(a, b) {
    if (a.scheme !== b.scheme) throw `Assertion error on uri scheme: ${a.scheme} != ${b.scheme}`;
    if (a.path !== b.path) throw `Assertion error on uri path: ${a.path} != ${b.path}`;
    if (a.authority !== b.authority) throw `Assertion error on uri authority: ${a.authority} != ${b.authority}`;
}

export function throws(f, message) {
    try {
        f();
    } catch (e) {
        if (e.message !== message)
            throw `Assertion error on catching exception: ${e.message} != ${message}`;
    }
}
