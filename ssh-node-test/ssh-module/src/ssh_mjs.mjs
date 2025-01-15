
import { Client, utils } from 'ssh2';

export function newSshClient() {
  return new Client();
}

export const OPEN_MODE = utils.sftp.OPEN_MODE;

