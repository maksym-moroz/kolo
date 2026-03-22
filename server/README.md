# Server

This is the standalone NestJS server for Kolo.

It uses:

- NestJS for the HTTP app
- PostgreSQL for persistence
- Flyway for SQL migrations
- Docker Compose for local database tooling

## Prerequisites

- Node.js 22+ recommended
- npm
- Docker Desktop or Docker Engine with Compose support

## Configuration

This folder already supports a local `server/.env` file.

Expected variables:

```env
PORT=3000
POSTGRES_HOST=localhost
POSTGRES_PORT=5432
POSTGRES_USER=user
POSTGRES_PASSWORD=pass
POSTGRES_DB=db
```

The NestJS app reads these variables in [src/app/app.module.ts](/Users/sergijnikitenko/AndroidStudioProjects/kolo/server/src/app/app.module.ts) and [src/database/database.service.ts](/Users/sergijnikitenko/AndroidStudioProjects/kolo/server/src/database/database.service.ts).

## First Run

From this directory:

```bash
cd /AndroidStudioProjects/kolo/server
npm ci
npm run db:up
npm run db:migrate
npm run start:dev
```

If everything is up, try:

- `http://localhost:3000/`
- `http://localhost:3000/health`

## How The App Connects To PostgreSQL

The PostgreSQL client is created in [src/database/database.service.ts](/Users/sergijnikitenko/AndroidStudioProjects/kolo/server/src/database/database.service.ts).

Connection flow:

1. Nest loads env variables through `@nestjs/config`.
2. `DatabaseService` creates a `pg.Pool` using `POSTGRES_HOST`, `POSTGRES_PORT`, `POSTGRES_USER`, `POSTGRES_PASSWORD`, and `POSTGRES_DB`.
3. On startup, [src/main.ts](/Users/sergijnikitenko/AndroidStudioProjects/kolo/server/src/main.ts) calls `databaseService.ensureConnection()`.
4. If PostgreSQL is unavailable, the server exits instead of starting in a broken state.

## Docker Setup

Local Docker services are defined in [docker-compose.yml](/Users/sergijnikitenko/AndroidStudioProjects/kolo/server/docker-compose.yml).

### `postgres`

- uses image `postgres:16-alpine`
- exposes the database on `localhost:${POSTGRES_PORT}`
- creates the database/user from your `.env`
- stores data in the named Docker volume `postgres-data`
- includes a health check with `pg_isready`

### `flyway`

- uses image `flyway/flyway`
- runs only when you invoke it
- waits for the `postgres` service to become healthy
- mounts [db/migration](/Users/sergijnikitenko/AndroidStudioProjects/kolo/server/db/migration) into the container as `/flyway/sql`
- connects with the same database credentials from `.env`

## Docker Commands

These are the main npm wrappers:

```bash
npm run db:up
npm run db:migrate
npm run db:info
npm run db:down
npm run db:reset
```

What they do:

- `npm run db:up`: starts PostgreSQL in the background
- `npm run db:migrate`: runs Flyway once and applies any pending migrations
- `npm run db:info`: shows Flyway migration status
- `npm run db:down`: stops Compose services
- `npm run db:reset`: stops services and deletes the PostgreSQL data volume

If you want the raw Docker Compose equivalents:

```bash
docker compose -f docker-compose.yml up -d postgres
docker compose -f docker-compose.yml run --rm flyway migrate
docker compose -f docker-compose.yml run --rm flyway info
docker compose -f docker-compose.yml down
docker compose -f docker-compose.yml down -v
```

## How Flyway Migrations Work

Flyway reads versioned SQL files from [db/migration](/Users/sergijnikitenko/AndroidStudioProjects/kolo/server/db/migration).

Current example:

- [db/migration/V1__create_users.sql](/Users/sergijnikitenko/AndroidStudioProjects/kolo/server/db/migration/V1__create_users.sql)

Rules:

- migration files must be versioned, for example `V2__add_profiles.sql`
- Flyway applies them in order
- applied migrations are tracked in Flyway's schema history table inside PostgreSQL
- rerunning `npm run db:migrate` is safe and only applies new versions

Typical migration workflow:

1. Create a new file in `db/migration`, for example `V2__add_tasks.sql`.
2. Write forward-only SQL.
3. Run `npm run db:migrate`.
4. Run `npm run db:info` to confirm the new migration is recorded.

If you want to start from a completely clean local database:

```bash
npm run db:reset
npm run db:up
npm run db:migrate
```

## Running The Server

Development mode:

```bash
npm run start:dev
```

Watch mode:

```bash
npm run start:watch
```

Production-style local run:

```bash
npm run build
npm run start
```

## Example Endpoints

- `GET /`
- `GET /health`
- `GET /users`
- `POST /users`

Example request:

```bash
curl -X POST http://localhost:3000/users \
  -H "Content-Type: application/json" \
  -d '{"email":"test@example.com"}'
```

## Troubleshooting

If `npm run start:dev` fails with a DB connection error:

- make sure Docker is running
- make sure `npm run db:up` has completed
- run `npm run db:info` to confirm Flyway can reach the database
- check that `server/.env` matches the exposed Docker port and credentials

If you need to wipe local DB state:

```bash
npm run db:reset
npm run db:up
npm run db:migrate
```
