// Load test: behavior under expected normal load. Ramps up to 10 virtual
// users, holds for one minute, then ramps down. Each user requests a random
// read endpoint to simulate realistic query traffic.
//
//   k6 run -e EMAIL=admin@autozone.com -e PASSWORD=secret k6/load.js

import http from 'k6/http';
import { check, sleep } from 'k6';
import { BASE_URL, READ_ENDPOINTS, login, authHeaders, summaryReport } from './lib/common.js';

export const handleSummary = summaryReport('load');

export const options = {
  stages: [
    { duration: '30s', target: 10 },
    { duration: '1m', target: 10 },
    { duration: '30s', target: 0 },
  ],
  thresholds: {
    http_req_duration: ['p(95)<500'],
    http_req_failed: ['rate<0.01'],
    checks: ['rate>0.99'],
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
    'body not empty': (r) => r.body.length > 0,
  });

  sleep(1); // think time between a user's requests
}
