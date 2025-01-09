
import { Client } from 'ssh2';

export function newSshClient() {
  return new Client();
}

