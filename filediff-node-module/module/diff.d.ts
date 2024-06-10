type Message = Array<string | Uint8Array | Uint16Array>

interface Channel {
    sendMessage(message: Message): void
    onMessage?: (message: Message) => void
}

// see org.sudu.experiments.FileDiffNodeMain.DiffEngineJs
interface DiffEngine {
    dispose(): void;
    fib(n: string): Promise<number>;
    startFolderDiff(leftPath: string, rightPath: string, channel: Channel): void;
    testFS(path: string, onComplete: () => void): void;
    testFS2(path1: string, path2: string, onComplete: () => void): void;
}

export function createDiffEngine(
  workerUrl: string | URL,
  nThreads: number
): Promise<DiffEngine>
