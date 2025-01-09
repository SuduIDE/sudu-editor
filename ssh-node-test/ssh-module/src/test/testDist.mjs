import {newSshClient} from "../../dist/ssh_mjs.mjs";

import {testConfig,testSsh} from './testHelper.mjs';

const conn = newSshClient();

// console.log(conn);

testSsh(conn, testConfig());
