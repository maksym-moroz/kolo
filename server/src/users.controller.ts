import { BadRequestException, Body, Controller, Get, HttpCode, HttpStatus, Inject, Post } from '@nestjs/common';
import { UsersService } from './users.service';

type CreateUserBody = {
  email?: unknown;
};

@Controller('users')
export class UsersController {
  constructor(@Inject(UsersService) private readonly usersService: UsersService) {}

  @Get()
  async getUsers() {
    return this.usersService.findAll();
  }

  @Post()
  @HttpCode(HttpStatus.CREATED)
  async createUser(@Body() body: CreateUserBody) {
    if (typeof body.email !== 'string' || !body.email.includes('@')) {
      throw new BadRequestException('A valid email is required.');
    }

    return this.usersService.create(body.email.trim().toLowerCase());
  }
}
