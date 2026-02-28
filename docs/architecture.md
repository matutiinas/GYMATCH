# Gymatch Platform Architecture (Escalable + Marketplace + Microservicios)

## 1) Diseño final de arquitectura

```text
Clients (Android/iOS KMP)
  └─ BFF/API Gateway
      ├─ Auth Service
      ├─ Profile Service
      ├─ Matching Service
      ├─ Recommendation Service (optional AI microservice)
      ├─ Training Service (TrainingEngine, optional LLM adapter)
      ├─ Ranking Service
      ├─ Marketplace Service
      ├─ Events Service
      ├─ Payment Service
      ├─ Notification Service
      └─ Analytics/Event Ingestion
```

### Separación por bounded context
- **Auth**: identidad, tokens, sesiones.
- **Matching**: swipes, match rules, límites.
- **TrainingEngine**: generación dinámica de entrenamiento.
- **Ranking**: score deportivo, ELO, leaderboards.
- **Marketplace**: coaches verificados, sesiones, comisiones.
- **Events**: creación, inscripción, asistencia.

## 2) Arquitectura modular KMP (shared)

```text
shared/
├── core/common/di
├── domain/
│   ├── marketplace/
│   ├── events/
│   ├── i18n/
│   ├── recommendation/
│   ├── ranking/
│   ├── training/
│   └── ...
├── marketplaceengine/
├── recommendationengine/
├── rankingengine/
└── trainingengine/
```

## 3) Diagrama de microservicios

```text
[API Gateway]
   ├──> [Auth Service] --------------------> SQL Auth DB
   ├──> [Matching Service] ----------------> NoSQL Swipe/Match Store
   ├──> [Recommendation Service] ----------> Feature Store + Vector/NoSQL
   ├──> [Training Service] ----------------> SQL Plans DB
   ├──> [Ranking Service] -----------------> Redis + Columnar Analytics
   ├──> [Marketplace Service] -------------> SQL Commerce DB
   ├──> [Payment Service] -----------------> PSP (Stripe/Adyen/etc)
   ├──> [Events Service] ------------------> SQL Events DB
   └──> [Notification Service] ------------> Push Providers

[CDC / Event Bus (Kafka/PubSub)]
   <- emits from all services for analytics, anti-fraud and recommendation retraining.
```

## 4) Base de datos híbrida (SQL + NoSQL)

### SQL (consistencia transaccional)
- usuarios, coaches verificados, órdenes de sesiones, pagos, eventos, asistencias.

### NoSQL (alto volumen / baja latencia)
- swipes/matches stream,
- recommendation events,
- caches de feed,
- feature vectors de usuario.

### Modelo sugerido (extracto)

```sql
CREATE TABLE coaches (
  coach_id TEXT PRIMARY KEY,
  verification_status TEXT NOT NULL,
  display_name TEXT NOT NULL,
  country_code TEXT NOT NULL,
  created_at INTEGER NOT NULL
);

CREATE TABLE marketplace_orders (
  order_id TEXT PRIMARY KEY,
  user_id TEXT NOT NULL,
  coach_id TEXT NOT NULL,
  gross_amount BIGINT NOT NULL,
  commission_amount BIGINT NOT NULL,
  net_coach_amount BIGINT NOT NULL,
  currency TEXT NOT NULL,
  payment_status TEXT NOT NULL,
  created_at INTEGER NOT NULL
);

CREATE TABLE sport_events (
  event_id TEXT PRIMARY KEY,
  creator_user_id TEXT NOT NULL,
  city_code TEXT NOT NULL,
  sport_code TEXT NOT NULL,
  starts_at INTEGER NOT NULL,
  capacity INTEGER NOT NULL
);

CREATE TABLE event_registrations (
  event_id TEXT NOT NULL,
  user_id TEXT NOT NULL,
  attendance_status TEXT NOT NULL,
  registered_at INTEGER NOT NULL,
  PRIMARY KEY(event_id, user_id)
);

CREATE TABLE region_config (
  region_code TEXT PRIMARY KEY,
  language_tag TEXT NOT NULL,
  currency_code TEXT NOT NULL,
  timezone_id TEXT NOT NULL,
  tax_percent REAL NOT NULL
);
```

## 5) Internacionalización
- Multi-idioma por `language_tag` y catálogos i18n key-based.
- Multi-moneda por `currency_code` + pricing por región.
- Config por región centralizada en `RegionConfigRepository`.

## 6) Estrategia de escalado horizontal
1. Servicios stateless detrás de load balancer.
2. Sharding por región para matching/recommendation/events.
3. Redis para caches hot-path (discover feed, rankings).
4. Asíncrono por eventos (outbox + Kafka) para desacoplar escrituras.
5. Read replicas SQL + auto-scaling NoSQL.
6. Circuit breakers y retries para servicios externos (PSP/IA/LLM).

## 7) Estrategia de migración

### Fase 1 (Monolito modular KMP + backend modular)
- Mantener contratos de dominio.
- Introducir `Marketplace`, `Events`, `i18n` en shared domain.

### Fase 2 (Strangler pattern)
- Extraer `Marketplace Service` y `Events Service` primero.
- Gateway enruta endpoints legacy y nuevos.

### Fase 3 (Dominio crítico)
- Extraer `Matching`, `Recommendation`, `Ranking`.
- Añadir Feature Store y pipeline de entrenamiento.

### Fase 4 (Optimización masiva)
- Multi-región activa-activa.
- Data mesh analítico y control de costes por tenant/región.

## 8) Plan de despliegue
1. **Entornos**: dev → staging → canary → production.
2. **Entrega**: blue/green para servicios core; canary en recommendation.
3. **Observabilidad**: OpenTelemetry + tracing distribuido + SLO por servicio.
4. **Seguridad**: OAuth2/OIDC, mTLS interno, secrets manager.
5. **FinOps**: auto-scaling basado en QPS/latencia/cola.
6. **DR**: backups PITR SQL + snapshots NoSQL + runbooks de failover.

## 9) Seed de datos para desarrollo (DEBUG)

- Se agrega `SeedService` en capa **Data** (no en Domain).
- Al iniciar app se llama `AppContainer.initializeData(isDebug)`.
- Flujo:
  1. Si `isDebug == false` ⇒ no hace nada.
  2. Si `profiles` ya tiene datos ⇒ no inserta nada.
  3. Si está vacío ⇒ genera **50 perfiles** con datos variados y realistas.
- Persistencia desacoplada vía `ProfileSeedDataSource`.
- Adapter listo para Firestore: `FirestoreProfileSeedDataSource` (`count` + `upsertBatch`).

Campos seed incluidos:
- nombre, edad
- 2-4 fotos placeholder
- deportes variados (gym/running/crossfit/swimming/cycling)
- nivel y objetivo
- marcas personales realistas (bench, 5k, etc.)
- ubicación cercana
- estado online aleatorio
- última actividad aleatoria
