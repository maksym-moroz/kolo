import { ConflictException, Inject, Injectable } from '@nestjs/common';
import { DatabaseService } from '../database/database.service';

type UserRow = {
  id: string;
  email: string;
  created_at: Date;
};

type User = {
  id: string;
  email: string;
  createdAt: string;
};

@Injectable()
export class UsersService {
  constructor(@Inject(DatabaseService) private readonly databaseService: DatabaseService) {}

  async findAll(): Promise<User[]> {
    const rows = await this.databaseService.query<UserRow>(
      'SELECT id, email, created_at FROM users ORDER BY created_at DESC',
    );

    return rows.map((row: UserRow) => this.toUser(row));
  }

  async create(email: string): Promise<User> {
    try {
      const [row] = await this.databaseService.query<UserRow>(
        'INSERT INTO users (email) VALUES ($1) RETURNING id, email, created_at',
        [email],
      );

      if (!row) {
        throw new Error('User insert did not return a row.');
      }

      return this.toUser(row);
    } catch (error) {
      if (this.isUniqueViolation(error)) {
        throw new ConflictException('A user with that email already exists.');
      }

      throw error;
    }
  }

  private toUser(row: UserRow): User {
    return {
      id: row.id,
      email: row.email,
      createdAt: row.created_at.toISOString(),
    };
  }

  private isUniqueViolation(error: unknown) {
    return typeof error === 'object' && error !== null && 'code' in error && error.code === '23505';
  }
}
