import { Channel } from './common'
export { setLogLevel, setLogOutput, newRemoteChannelTest } from './common';

// java class: org.sudu.experiments.DiffEngineJs
interface DiffEngine {
    dispose(): void;

    startFolderDiff(leftPath: string, rightPath: string, channel: Channel): void;

    testFib(n: string): Promise<number>;

    testFS(path: string, onComplete: () => void): void;

    testFS2(
        path1: string, path2: string,
        onComplete: () => void
    ): void;

    testDiff(
        path1: string, path2: string, withContent: boolean,
        onComplete: () => void
    ): void;
}

export function createDiffEngine(
  workerUrl: string | URL,
  nThreads: number
): Promise<DiffEngine>
