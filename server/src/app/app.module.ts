import { Module } from '@nestjs/common';
import { ConfigModule } from '@nestjs/config';
import { DatabaseService } from '../database/database.service';
import { HealthController } from '../health/health.controller';
import { UsersController } from '../users/users.controller';
import { UsersService } from '../users/users.service';
import { AppController } from './app.controller';

@Module({
  imports: [
    ConfigModule.forRoot({
      isGlobal: true,
      envFilePath: ['.env', '../.env'],
    }),
  ],
  controllers: [AppController, HealthController, UsersController],
  providers: [DatabaseService, UsersService],
})
export class AppModule {}
