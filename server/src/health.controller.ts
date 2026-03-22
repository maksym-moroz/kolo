import { Controller, Get, Inject } from '@nestjs/common';
import { DatabaseService } from './database.service';

@Controller('health')
export class HealthController {
  constructor(@Inject(DatabaseService) private readonly databaseService: DatabaseService) {}

  @Get()
  async getHealth() {
    await this.databaseService.isHealthy();

    return {
      service: 'kolo-server',
      status: 'ok',
      database: 'up',
    };
  }
}
