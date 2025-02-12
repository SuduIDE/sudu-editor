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
    path1: FileInput, path2: FileInput, withContent: boolean,
    onComplete: () => void
  ): void;

  testFileWrite(
    path: FileInput, content: string, encoding: string,
    onComplete: () => void,
    onError: (error: string) => void
  ): void;

  testFileReadWrite(
    pathFrom: FileInput,
    pathToS: FileInput,
    pathToJ: FileInput,
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
  testNodeBuffer(onComplete: () => void) : void;

  testSshDir(file: SshInput, onComplete: () => void): void;
  testSshFile(file: SshInput, onComplete: () => void): void;
  testSshDirAsync(file: SshInput, onComplete: () => void): void;
  testSshFileAsync(file: SshInput, onComplete: () => void): void;
}

export interface FolderDiffSession extends AsyncShutdown {
}

export interface FileDiffSession extends AsyncShutdown {
}

export type SshCredentials = { host: string, port: string } & (
  { username: string, password: string } |
  { username: string, privateKey: string });

export type SshInput = { path: string, ssh: SshCredentials }
export type FileInput = { path: string } | { content: string } | SshInput;
export type FolderInput = string | SshInput;

// java class: org.sudu.experiments.DiffEngineJs
export interface DiffEngine extends IDisposable {
  // todo add boolean content
  startFolderDiff(
    leftPath: FolderInput,
    rightPath: FolderInput,
    channel: Channel
  ): FolderDiffSession;

  startFileDiff(
    left: FileInput, right: FileInput,
    channel: Channel,
    folderDiff?: FolderDiffSession
  ): FileDiffSession;

  startFileEdit(file: FileInput, channel: Channel): FileDiffSession;

  testApi(): DiffTestApi;
}

export function createDiffEngine(
  workerUrl: string | URL,
  nThreads: number
): Promise<DiffEngine>
