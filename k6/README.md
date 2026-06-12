# Pruebas de Desempeño con k6

Pruebas de carga para la API de AutoZone QA usando [k6](https://k6.io/).

## Requisitos

- **k6** instalado (`brew install k6` en macOS).
- **El backend corriendo** en `http://localhost:8080` (`./mvnw spring-boot:run`).
- **Credenciales válidas** de un usuario existente en la base de datos.

## Diseño de las pruebas

Todas las pruebas atacan **endpoints de solo lectura (GET)** que únicamente
requieren estar autenticado. Se eligieron así a propósito:

- No escriben en la base de datos → se pueden repetir sin ensuciar datos.
- Representan el tráfico real más frecuente (consultas).

| Archivo | Tipo | Objetivo | Carga |
|---|---|---|---|
| `smoke.js` | Smoke | Validar que la API responde bien con carga mínima | 1 VU, 1 iteración |
| `load.js` | Load | Comportamiento bajo la carga esperada normal | hasta 10 VUs, ~2 min |
| `stress.js` | Stress | Encontrar el punto donde la API empieza a sufrir | hasta 50 VUs, ~3.5 min |

Cada prueba hace **login una sola vez** en `setup()` y reparte el JWT a todos
los usuarios virtuales (VUs), para medir el desempeño de los endpoints de
negocio y no el del login.

### Endpoints bajo prueba

- `GET /api/v1/features`
- `GET /api/v1/roles`
- `GET /api/v1/users`
- `GET /api/v1/releases`
- `GET /api/v1/reports`

## Cómo ejecutarlas

```bash
# 1. Smoke test (siempre empieza por aquí)
k6 run -e EMAIL=tu_correo@az.com -e PASSWORD=tu_password k6/smoke.js

# 2. Load test
k6 run -e EMAIL=tu_correo@az.com -e PASSWORD=tu_password k6/load.js

# 3. Stress test
k6 run -e EMAIL=tu_correo@az.com -e PASSWORD=tu_password k6/stress.js
```

### Variables de entorno

| Variable | Default | Descripción |
|---|---|---|
| `BASE_URL` | `http://localhost:8080` | URL base del backend |
| `EMAIL` | — (obligatorio) | Correo de un usuario válido |
| `PASSWORD` | — (obligatorio) | Contraseña de ese usuario |

Apuntar a otro entorno:

```bash
k6 run -e BASE_URL=https://dev.az.com -e EMAIL=... -e PASSWORD=... k6/load.js
```

## Cómo leer los resultados

Al terminar, k6 imprime un resumen. Las métricas clave:

- **`http_req_duration`** — tiempo de respuesta. Mira el `p(95)` (95% de las
  peticiones estuvieron por debajo de ese valor).
- **`http_req_failed`** — porcentaje de peticiones fallidas. Debe ser ~0%.
- **`checks`** — porcentaje de validaciones (`status 200`, etc.) que pasaron.
- **`iterations` / `http_reqs`** — throughput (peticiones por segundo).

Los **thresholds** definidos en cada prueba aparecen con ✓ (pasó) o ✗ (falló).
Si un threshold falla, k6 termina con código de salida distinto de 0 — útil
para integrarlo en CI/CD.

### Reportes en disco

Cada prueba guarda su resultado automáticamente en `k6/results/` al terminar
(vía `handleSummary()` en `lib/common.js`):

```
k6/results/
├── <prueba>-summary.txt    resumen legible (evidencia de QA)
└── <prueba>-summary.json   métricas crudas (para CI o gráficas)
```

> Los comandos deben ejecutarse desde la raíz del proyecto (`back/`) para que
> los archivos caigan en `k6/results/`.
