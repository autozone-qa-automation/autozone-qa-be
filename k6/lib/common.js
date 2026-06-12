// Shared configuration and helpers for the k6 performance tests.
//
// Environment variables:
//   BASE_URL  base URL of the backend (default http://localhost:8080)
//   EMAIL     email of a valid user   (required)
//   PASSWORD  password of that user    (required)

import http from 'k6/http';
import { check, fail } from 'k6';
import { textSummary } from 'https://jslib.k6.io/k6-summary/0.1.0/index.js';

export const BASE_URL = __ENV.BASE_URL || 'http://localhost:8080';
const EMAIL = __ENV.EMAIL;
const PASSWORD = __ENV.PASSWORD;

/**
 * Read-only endpoints that only require authentication. Used for load because
 * they never mutate the database, so the tests are safe to repeat.
 */
export const READ_ENDPOINTS = [
  '/api/v1/features',
  '/api/v1/roles',
  '/api/v1/users',
  '/api/v1/releases',
  '/api/v1/reports',
];

/**
 * Authenticates against /api/v1/authentify and returns the JWT. Meant to run
 * once in setup() so the token is shared across all virtual users.
 *
 * @return {string} the bearer token
 */
export function login() {
  if (!EMAIL || !PASSWORD) {
    fail('Provide -e EMAIL=... -e PASSWORD=... with valid credentials');
  }

  const res = http.post(
    `${BASE_URL}/api/v1/authentify`,
    JSON.stringify({ mail: EMAIL, password: PASSWORD }),
    { headers: { 'Content-Type': 'application/json' } },
  );

  const ok = check(res, {
    'login status 200': (r) => r.status === 200,
    'login returns token': (r) => !!r.json('token'),
  });

  if (!ok) {
    fail(`Login failed (status ${res.status}): ${res.body}`);
  }

  return res.json('token');
}

/**
 * Builds authenticated request params from a bearer token.
 *
 * @param {string} token the JWT returned by {@link login}
 * @return {object} k6 request params with the Authorization header
 */
export function authHeaders(token) {
  return {
    headers: {
      'Content-Type': 'application/json',
      Authorization: `Bearer ${token}`,
    },
  };
}

/**
 * Builds a handleSummary callback that prints the report to stdout and also
 * persists it to k6/results/<name>-summary.{txt,json} so the run can be kept
 * as QA evidence.
 *
 * @param {string} name test name used as the file prefix
 * @return {function(object): object} a k6 handleSummary handler
 */
export function summaryReport(name) {
  return (data) => ({
    stdout: textSummary(data, { indent: ' ', enableColors: true }),
    [`k6/results/${name}-summary.txt`]: textSummary(data, { indent: ' ', enableColors: false }),
    [`k6/results/${name}-summary.json`]: JSON.stringify(data, null, 2),
  });
}
