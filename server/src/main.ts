import 'reflect-metadata';
import { Logger } from '@nestjs/common';
import { ConfigService } from '@nestjs/config';
import { NestFactory } from '@nestjs/core';
import { AppModule } from './app/app.module';
import { DatabaseService } from './database/database.service';

async function bootstrap() {
  const app = await NestFactory.create(AppModule);
  app.enableShutdownHooks();

  const databaseService = app.get(DatabaseService);
  await databaseService.ensureConnection();

  const configService = app.get(ConfigService);
  const port = Number(configService.get<string>('PORT') ?? '3000');

  await app.listen(port);
  Logger.log(`Kolo server listening on http://localhost:${port}`, 'Bootstrap');
}

void bootstrap().catch((error: unknown) => {
  Logger.error('Failed to start Kolo server.', error instanceof Error ? error.stack : undefined, 'Bootstrap');
  process.exit(1);
});
