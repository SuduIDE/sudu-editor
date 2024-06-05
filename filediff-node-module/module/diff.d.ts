type Message = Array<string | Uint8Array | Uint16Array>

interface Channel {
    sendMessage(message: Message): void
    onMessage?: (message: Message) => void
}

interface DiffEngine {
    terminateWorkers(): void;
    fib(n: string): Promise<number>;
    startFolderDiff(leftPath: string, rightPath: string, channel: Channel): void;
}

export function createDiffEngine(
  workerUrl: string,
  nThreads: number
): Promise<DiffEngine>
