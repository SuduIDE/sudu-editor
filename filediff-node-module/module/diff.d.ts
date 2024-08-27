import { Channel, IDisposable } from './common'
export { Channel, Message, setLogLevel, setLogOutput, newRemoteChannelTest, LogLevel, ChannelTestApi, IDisposable } from './common';

// java class: org.sudu.experiments.DiffEngineJs
export interface DiffEngine extends IDisposable {
    // todo add boolean content
    startFolderDiff(leftPath: string, rightPath: string, channel: Channel): IDisposable;

    testFib(n: string): Promise<number>;

    testFS(path: string, onComplete: () => void): void;

    testDiff(
        path1: string, path2: string, withContent: boolean,
        onComplete: () => void
    ): void;
}

export function createDiffEngine(
  workerUrl: string | URL,
  nThreads: number
): Promise<DiffEngine>
