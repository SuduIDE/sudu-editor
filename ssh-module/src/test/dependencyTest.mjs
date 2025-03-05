import { Client } from 'ssh2';

import {testConfig,testSsh} from './testHelper.mjs';

const config = testConfig(process.argv);
const conn = new Client();

console.log("new Client:" , conn.constructor.name);
console.log("ssh:", {host: config.host, username: config.username});

testSsh(conn, config);
