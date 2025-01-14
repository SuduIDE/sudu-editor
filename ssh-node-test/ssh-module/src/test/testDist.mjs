import {newSshClient, OPEN_MODE} from "../../dist/ssh_mjs.mjs";

import {testConfig,testSsh} from './testHelper.mjs';

console.log("OPEN_MODE = ", OPEN_MODE);

const conn = newSshClient();

// console.log(conn);

testSsh(conn, testConfig());
