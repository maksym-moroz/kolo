import { Inject, Injectable, OnModuleDestroy } from '@nestjs/common';
import { ConfigService } from '@nestjs/config';
import { Pool, QueryResultRow } from 'pg';

@Injectable()
export class DatabaseService implements OnModuleDestroy {
  private readonly pool: Pool;

  constructor(@Inject(ConfigService) private readonly configService: ConfigService) {
    this.pool = new Pool({
      host: this.requireEnv('POSTGRES_HOST'),
      port: Number(this.requireEnv('POSTGRES_PORT')),
      user: this.requireEnv('POSTGRES_USER'),
      password: this.requireEnv('POSTGRES_PASSWORD'),
      database: this.requireEnv('POSTGRES_DB'),
    });
  }

  async ensureConnection() {
    await this.pool.query('SELECT 1');
  }

  async isHealthy() {
    await this.ensureConnection();
    return true;
  }

  async query<T extends QueryResultRow>(text: string, values: readonly unknown[] = []) {
    const result = await this.pool.query<T>(text, values as unknown[]);
    return result.rows;
  }

  async onModuleDestroy() {
    await this.pool.end();
  }

  private requireEnv(name: string) {
    const value = this.configService.get<string>(name);

    if (!value) {
      throw new Error(`Missing required environment variable: ${name}`);
    }

    return value;
  }
}
