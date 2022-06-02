import { Entity, Column, PrimaryGeneratedColumn, Unique } from 'typeorm';

@Unique(['username'])
@Entity()
export class User {
  @PrimaryGeneratedColumn()
  id: number;

  @Column({ length: 30 })
  username: string;

  @Column({ length: 64 })
  password: string;

  @Column( { default: 100 })
  tokens: number;

  @Column({ default: null })
  location: string;

  @Column({ default: null })
  parkerId: number;
}
