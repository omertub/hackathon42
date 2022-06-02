
import { EventsGateway } from 'src/events.gateway';
import { DataSource } from 'typeorm';
import { User } from './users.entity';


export const usersProviders = [
  {
    provide: 'USERS_REPOSITORY',
    useFactory: (dataSource: DataSource) => dataSource.getRepository(User),
    inject: ['DATA_SOURCE'],
  },
  EventsGateway
];
