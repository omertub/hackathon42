import { User } from 'src/users/users.entity';
import { DataSource } from 'typeorm';

export const databaseProviders = [
  {
    provide: 'DATA_SOURCE',
    useFactory: async () => {
      const dataSource = new DataSource({
        type: 'sqlite',
        database: './data/db.sqlite',
        entities: [
          User
        ],
        // logging: true,
        synchronize: true,
      });

      return dataSource.initialize();
    },
  },
];
