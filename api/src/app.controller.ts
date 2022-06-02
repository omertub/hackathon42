import { Controller, Get, NotFoundException, Post, Query, Req, Res } from '@nestjs/common';
import { existsSync, statSync } from 'fs';
import { AppService } from './app.service';
import { EventsGateway } from './events.gateway';
import { Response } from 'express';
import { RESPONSE_STATUS } from './consts';

@Controller()
export class AppController {
  constructor(
    private readonly appService: AppService,
    private readonly eventsGateway: EventsGateway
    ) {}

  @Get()
  getHello(): string {
    return this.appService.getHello();
  }

  @Post('/pushEvent')
  pushEvent() {
    this.eventsGateway.server.emit('server2client', {
      hello: 'Hello there ;)'
    });

    return { status: RESPONSE_STATUS.OK };
  }
}
