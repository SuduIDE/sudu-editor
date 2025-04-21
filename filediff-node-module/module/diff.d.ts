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
    pathFrom: FileInput, pathTo: FileInput,
    onComplete: () => void,
    onError: (error: string) => void
  ) : void;

  testNodeFsCopyFile(
    pathFrom: string, pathTo: string,
    onComplete: () => void,
    onError: (error: string) => void
  ): void;

  testNodeFsCopyDirectory(
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

  testDeleteFile(path: FileInput, onComplete: () => void): void;

  testCopyFileToFolder(
    from: FileInput, destDir: FolderInput, destFile: FileInput,
    onComplete: () => void, onError: (error: string) => void): void;

  testFileAppend(
    file: FileInput, str1: string, str2: string,
    onComplete: () => void, onError: (error: string) => void): void;

  testMkDir(
    dir: FolderInput, name: string,
    onComplete: () => void, onError: (error: string) => void): void;

  testRemoveDir(
    dir: FolderInput,
    onComplete: () => void, onError: (error: string) => void): void;

  // returns: [equals(), ssh1.hashCode(), ssh2.hashCode
  testSshHash(ssh1: SshCredentials, ssh2: SshCredentials) : number[];
}

export interface FolderDiffSession extends AsyncShutdown {
}

export interface FileDiffSession extends AsyncShutdown {
}

export type SshCredentials = { host: string, port: string } & (
  { username: string, password: string } |
  { username: string, privateKey: string });

export type SshInput = { path: string, ssh: SshCredentials };
export type ContentInput = { path: string, content: string };
export type FileInput = { path: string } | ContentInput | SshInput;
export type FolderInput = string | SshInput;

export type FolderListingEntry = { name: string, isFile: boolean };

export type FStats = {
  isDirectory: boolean,
  isFile: boolean,
  isSymbolicLink: boolean,
  size: number;
};

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

  startFileEdit(
    file: FileInput, channel: Channel,
    folderDiff?: FolderDiffSession
  ): FileDiffSession;

  listRemoteDirectory(sshInput: SshInput, withFiles: boolean): Promise<FolderListingEntry[]>;

  stat(fileOrFolder: FileInput) : Promise<FStats>;

  testApi(): DiffTestApi;
}

export function createDiffEngine(
  workerUrl: string | URL,
  nThreads: number
): Promise<DiffEngine>
