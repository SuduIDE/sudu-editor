import { Channel } from './common'

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
