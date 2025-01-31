import {newSshClient, OPEN_MODE} from "../../dist/sshLib.mjs";

import {testConfig,testSsh} from './testHelper.mjs';

console.log("OPEN_MODE = ", OPEN_MODE);

const config = testConfig(process.argv);
const conn = newSshClient();

console.log("newSshClient: ", conn.constructor.name);

testSsh(conn, config);
