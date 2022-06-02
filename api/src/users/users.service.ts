import { UnauthorizedException } from '@nestjs/common';
import { Injectable, Inject } from '@nestjs/common';
import { TOKENS_ADDITION } from 'src/consts';
import { Repository } from 'typeorm';
import { User } from './users.entity';

@Injectable()
export class UserService {
  constructor(
    // this object is the 'middle man' between the backend and the users table on the database
    @Inject('USERS_REPOSITORY') private usersRepository: Repository<User>
  ) {}

  async findOne(userId: number) {
    // we need to await because find is async operation(like Future in Java)
    const user = await this.usersRepository.findOne({
      where: {
        id: userId
      }
    });
    return user;
  }

  async findAll() {    
    const users = await this.usersRepository.find();
    return users;
  }

  async findAllMarkers() {    
    const users = await this.usersRepository.find();
    const markerUsers = users.filter(user => user.location !== null && user.parkerId === null);
    return markerUsers;
  }

  async signup(signup: any): Promise<User> {
    try {
      // will throw an exception when the username already exists,
      // because the username colume is Unique.
      const user = await this.usersRepository.save(signup);

      return user;
    } 
    catch {
      return null;
    }
  }

  async login(login: any) {
    const user = await this.usersRepository.findOne({
      where: {
        username: login.username
      }
    });

    if (!user) {
      return null;
    } 

    // TODO: better security...
    if (user.password !== login.password) {
      return null;
    }

    return user;
  }

  async postParking(postParking: any) {
    const user = await this.usersRepository.save(postParking);
    return user;
  }

  async commitParking(commitParking: any) {
    let parkerUser = await this.usersRepository.findOne({
      where: {
        id: commitParking.id
      }
    })

    const ownerUser = await this.usersRepository.save({
      id: commitParking.ownerId,
      parkerId: parkerUser.id
    });

    // subtract tokens from the parker
    parkerUser = await this.usersRepository.save({
      id: parkerUser.id,
      tokens: parkerUser.tokens - TOKENS_ADDITION
    })

    return {
      parkerUser: parkerUser,
      ownerUser: ownerUser
    };
  }

  async park(park: any) {
    // add tokens to the parking owner and clean the request:
    const owenrUser = await this.usersRepository.findOne({
      where: {
        parkerId: park.id
      }
    });

    const updatedUser = await this.usersRepository.save({
      id: owenrUser.id,
      tokens: owenrUser.tokens + TOKENS_ADDITION,
      location: null,
      parkerId: null
    });

    return updatedUser;
  }

}
