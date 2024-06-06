type Message = Array<string | Uint8Array | Uint16Array>

interface Channel {
    sendMessage(message: Message): void
    onMessage?: (message: Message) => void
}

// see org.sudu.experiments.FileDiffNodeMain.DiffEngineJs
interface DiffEngine {
    terminateWorkers(): void;
    fib(n: string): Promise<number>;
    startFolderDiff(leftPath: string, rightPath: string, channel: Channel): void;
    testFS(path: string): void;
}

export function createDiffEngine(
  workerUrl: string,
  nThreads: number
): Promise<DiffEngine>
