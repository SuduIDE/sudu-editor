// noinspection DuplicatedCode

import {createDiffEngine, setLogLevel, setLogOutput} from "./../src/diffEngine.mjs";

import fs from 'node:fs';
import path from 'node:path';
import url from 'node:url';

const nThreads = 3;
const timeoutTime = 1000;

const __dirname = path.dirname(
    url.fileURLToPath(new URL(import.meta.url)));

function logHandler(logLevel, text) {
  console.log("Logging at level " + logLevel + ": " + text);
}

setLogOutput(logHandler);
setLogLevel(5);

const diffEngineWorker = path.join(__dirname, "../src/diffEngineWorker.mjs")

let diffEngine = await createDiffEngine(diffEngineWorker, nThreads);

console.log("got module: ", diffEngine.constructor.name);

let jobCount = 0;

function mayBeExit() {
  if (--jobCount <= 0) {
    if (timeoutTime === 0)
        diffEngine.dispose();
    else
      setTimeout( () => diffEngine.dispose(), timeoutTime);
  }
}

function sshFile(ssh, path) {
  return { path, ssh };
}

function onError(title) {
  return (error) => {
    console.log(title + ".onError: ", error.message);
    mayBeExit();
  };
}

function testListRemoteDirectory(args) {
  const ssh = sshConfig(args, 3);
  const dir = args[3 + 4];
  const withFiles = args[3 + 5] === 'true';

  if (!ssh || !dir) {
    console.log("args: ssh[4] dir");
    mayBeExit();
    return;
  }

  console.log("ssh", ssh);
  console.log("dir", dir);
  console.log("withFiles", withFiles);

  jobCount++;
  diffEngine.listRemoteDirectory(sshFile(ssh, dir), withFiles).then(
      list => {
        console.log("listRemoteDirectory: ", list);
        mayBeExit();
      }, error => {
        console.error("listRemoteDirectory error: ", error);
        mayBeExit();
      }
  );
}

function testStats(args) {
  if (!args[3]) {
    mayBeExit();
    return "usage: testStats file1 [file2] ..."
  }
  for (let i = 3; i < args.length; i++) {
    const file = args[i];
    console.log("file", file);
    jobCount++;
    diffEngine.stat(file).then(
        stats => {
          console.log("testStats: file", file, ", stats", stats);
          mayBeExit();
        },
        onError("testStats")
    );
  }
}

function testReadFile(args) {
  if (!args[3]) {
    mayBeExit();
    return "usage: testReadFile file1 [file2] ..."
  }
  for (let i = 3; i < args.length; i++) {
    const file = args[i];
    console.log("file", file);
    jobCount++;
    diffEngine.readFile(file).then(
        content => {
          console.log("testReadFile: file", file, ", content", content);
          mayBeExit();
        },
        onError("testReadFile")
    );
  }
}

const fileContentOther = 0;
const fileContentUtf8 = 1;
const fileContentGbk = 2;

function decorateContentType(number) {
  switch (number) {
    case fileContentOther: return "fileContentOther";
    case fileContentUtf8: return "fileContentUtf8";
    case fileContentGbk: return "fileContentGbk";
    default: return "bad file content: " + number;
  }
}

function testDetectFileContent(args) {
  if (!args[3]) {
    mayBeExit();
    return "usage: testReadFile file1 [file2] ..."
  }
  for (let i = 3; i < args.length; i++) {
    const file = args[i];
    console.log("file", file);
    jobCount++;
    diffEngine.detectFileContent(file).then(
        content => {
          console.log("testDetectFileContent: file", file,
              ", content type", decorateContentType(content));
          mayBeExit();
        },
        onError("testDetectFileContent")
    );
  }
}


const sshPathTestUsage = "args: ssh[4] file1 [file2 ...]";

function testStatsSsh(args) {
  const ssh = sshConfig(args, 3);
  const file0 = args[3+4];

  if (!args || !file0) {
    mayBeExit();
    return sshPathTestUsage;
  }

  console.log("ssh:", {host: ssh.host, username: ssh.username});

  for (let i = 3+4; i < args.length; i++) {
    const file = args[i];
    console.log("file", file);
    jobCount++;
    diffEngine.stat(sshFile(ssh, file)).then(
        stats => {
          console.log("testStatsSsh: file", file, ", stats", stats);
          mayBeExit();
        },
        onError("testStatsSsh")
    );
  }
}

function testReadFileSsh(args) {
  const ssh = sshConfig(args, 3);
  const file0 = args[3+4];

  if (!args || !file0) {
    mayBeExit();
    return sshPathTestUsage;
  }

  console.log("ssh:", {host: ssh.host, username: ssh.username});

  for (let i = 3+4; i < args.length; i++) {
    const file = args[i];
    console.log("file", file);
    jobCount++;
    diffEngine.readFile(sshFile(ssh, file)).then(
        content => {
          console.log("testReadFileSsh: file", file, ", content", content);
          mayBeExit();
        },
        onError("testReadFileSsh")
    );
  }
}

function runTest() {
  let args = process.argv;
  const cmd = args[2];
  switch (cmd) {
    case "testListRemoteDirectory": return testListRemoteDirectory(args);
    case "testStats": return testStats(args);
    case "testStatsSsh": return testStatsSsh(args);
    case "testReadFile": return testReadFile(args);
    case "testReadFileSsh": return testReadFileSsh(args);
    case "testDetectFileContent": return testDetectFileContent(args);

    default:
      mayBeExit();
      return "not running any test";
  }
}

const r = runTest();

if (r !== undefined) {
  console.log(r);
}

function sshConfig(args, s) {
  if (args.length < s + 4) {
    console.log("args: host port user password");
    return null;
  }

  const keyOrPass = args[s + 3];
  const stats = fs.statSync(keyOrPass, {throwIfNoEntry:false});
  if (stats && stats.isFile()) {
    const key = fs.readFileSync(keyOrPass, 'utf8');
    console.log("using key file " + keyOrPass +
        ", " + key.length + " bytes");
    return {
      host: args[s],
      port: args[s + 1],
      username: args[s + 2],
      privateKey: key
    };
  }

  return {
    host: args[s],
    port: args[s + 1],
    username: args[s + 2],
    password: keyOrPass
  };
}
