// Smoke test: verifies the API responds correctly under minimal load.
// One virtual user hits every read endpoint once. Run this first to validate
// the environment and credentials before launching heavier tests.
//
//   k6 run -e EMAIL=admin@autozone.com -e PASSWORD=secret k6/smoke.js

import http from 'k6/http';
import { check, sleep } from 'k6';
import { BASE_URL, READ_ENDPOINTS, login, authHeaders, summaryReport } from './lib/common.js';

export const handleSummary = summaryReport('smoke');

export const options = {
  vus: 1,
  iterations: 1,
  thresholds: {
    http_req_failed: ['rate==0'],
    // Loose bound: the first request pays the JVM cold start (JIT, cold
    // connections, first Hibernate hit), typically around one second.
    http_req_duration: ['p(95)<1500'],
  },
};

export function setup() {
  return { token: login() };
}

export default function (data) {
  const params = authHeaders(data.token);

  for (const path of READ_ENDPOINTS) {
    const res = http.get(`${BASE_URL}${path}`, params);
    check(res, {
      [`GET ${path} -> 200`]: (r) => r.status === 200,
    });
    sleep(0.5);
  }
}
