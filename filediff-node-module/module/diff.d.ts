import {Channel, IDisposable} from './common'

export {
  Channel, Message,
  setLogLevel, setLogOutput, newRemoteChannelTest,
  LogLevel, ChannelTestApi, IDisposable
} from './common';

export interface AsyncShutdown {
  shutdown(): Promise<void>;
}

export interface DiffTestApi {
  testFib(n: string): Promise<number>;

  testFS(path: string, onComplete: () => void): void;

  testDiff(
    path1: string, path2: string, withContent: boolean,
    onComplete: () => void
  ): void;

  testFileWrite(
    path: string, content: string, encoding: string,
    onComplete: () => void,
    onError: (error: string) => void
  ): void;

  testFileReadWrite(
    pathFrom: string,
    pathToS: string,
    pathToJ: string,
    onComplete: () => void,
    onError: (error: string) => void
  ) : void;

  testFileCopy(
    pathFrom: string, pathTo: string,
    onComplete: () => void,
    onError: (error: string) => void
  ): void;

  testDirCopy(
    pathFrom: string, pathTo: string,
    onComplete: () => void,
    onError: (error: string) => void
  ): void;

  testGbkEncoder() : void;
}

export interface FolderDiffSession extends AsyncShutdown {
}

export interface FileDiffSession extends AsyncShutdown {
}

export type FileInput = { path: string } | { content: string }

// java class: org.sudu.experiments.DiffEngineJs
export interface DiffEngine extends IDisposable {
  // todo add boolean content
  startFolderDiff(leftPath: string, rightPath: string, channel: Channel): FolderDiffSession;

  startFileDiff(
    left: FileInput, right: FileInput,
    channel: Channel,
    folderDiff?: FolderDiffSession
  ): FileDiffSession;

  startFileEdit(path: string, channel: Channel): FileDiffSession;

  testApi(): DiffTestApi;
}

export function createDiffEngine(
  workerUrl: string | URL,
  nThreads: number
): Promise<DiffEngine>
