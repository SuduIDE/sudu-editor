import { Client } from 'ssh2';

import {testConfig,testSsh} from './testHelper.mjs';

const conn = new Client();

// console.log(conn);

testSsh(conn, testConfig());
