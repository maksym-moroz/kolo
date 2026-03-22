import { Inject, Injectable, OnModuleDestroy } from '@nestjs/common';
import { ConfigService } from '@nestjs/config';
import { Pool, QueryResultRow } from 'pg';

@Injectable()
export class DatabaseService implements OnModuleDestroy {
  private readonly pool: Pool;

  constructor(@Inject(ConfigService) private readonly configService: ConfigService) {
    this.pool = new Pool({
      host: this.configService.get<string>('POSTGRES_HOST') ?? 'localhost',
      port: Number(this.configService.get<string>('POSTGRES_PORT') ?? '5432'),
      user: this.configService.get<string>('POSTGRES_USER') ?? 'kolodbuser',
      password: this.configService.get<string>('POSTGRES_PASSWORD') ?? 'kolodbpass',
      database: this.configService.get<string>('POSTGRES_DB') ?? 'kolodb',
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
}
