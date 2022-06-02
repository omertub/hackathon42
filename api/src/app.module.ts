import { Module } from '@nestjs/common';
import { AppController } from './app.controller';
import { AppService } from './app.service';
import { DatabaseModule } from './database/database.module';
import { EventsGateway } from './events.gateway';
import { UserModule } from './users/users.module';

@Module({
  imports: [
    DatabaseModule,
    UserModule
  ],
  controllers: [AppController],
  providers: [
    AppService,
    EventsGateway
  ],
})
export class AppModule {}
