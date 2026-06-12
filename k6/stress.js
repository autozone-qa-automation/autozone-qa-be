// Stress test: finds the point where the API starts to struggle. Steps the
// load up to 50 virtual users to surface where latency spikes or errors begin.
//
//   k6 run -e EMAIL=admin@autozone.com -e PASSWORD=secret k6/stress.js

import http from 'k6/http';
import { check, sleep } from 'k6';
import { BASE_URL, READ_ENDPOINTS, login, authHeaders, summaryReport } from './lib/common.js';

export const handleSummary = summaryReport('stress');

export const options = {
  stages: [
    { duration: '30s', target: 20 },
    { duration: '1m', target: 20 },
    { duration: '30s', target: 50 },
    { duration: '1m', target: 50 },
    { duration: '30s', target: 0 },
  ],
  thresholds: {
    // Informative thresholds: under stress some are expected to break, which
    // tells us where the limit is. abortOnFail=false keeps the run going.
    http_req_duration: [{ threshold: 'p(95)<1500', abortOnFail: false }],
    http_req_failed: [{ threshold: 'rate<0.05', abortOnFail: false }],
  },
};

export function setup() {
  return { token: login() };
}

export default function (data) {
  const params = authHeaders(data.token);

  const path = READ_ENDPOINTS[Math.floor(Math.random() * READ_ENDPOINTS.length)];
  const res = http.get(`${BASE_URL}${path}`, params);

  check(res, {
    'status 200': (r) => r.status === 200,
  });

  sleep(0.5);
}
