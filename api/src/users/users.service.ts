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

  async findUser(userId: number) {
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
    const markerUsers = users.filter(user => {
      const now = new Date();
      const expirationTime = new Date(user.expirationTime);

      return user.location !== null
            && user.parkerId === null
            && user.expirationTime !== null
            && expirationTime.getTime() - now.getTime() > 0
          });
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

  async saveParkingLocation(saveParkingLocation: any) {
    await this.usersRepository.save(saveParkingLocation);
    const updatedUser = await this.findUser(saveParkingLocation.id);

    return updatedUser;
  }

  async setExpirationTime(setExpirationTime: any) {
    await this.usersRepository.save(setExpirationTime);
    const updatedUser = await this.findUser(setExpirationTime.id);
    return updatedUser;
  }

  async commitParking(commitParking: any) {
    const parkerUser = await this.findUser(commitParking.id);

    await this.usersRepository.save({
      id: commitParking.ownerId,
      parkerId: parkerUser.id
    });

    const updatedOwnerUser = await this.findUser(commitParking.ownerId);

    // subtract tokens from the parker
    await this.usersRepository.save({
      id: parkerUser.id,
      tokens: parkerUser.tokens - TOKENS_ADDITION
    })

    const updatedParkerUser = await this.findUser(parkerUser.id);

    return {
      parkerUser: updatedParkerUser,
      ownerUser: updatedOwnerUser
    };
  }

  async park(park: any) {
    // add tokens to the parking owner and clean the request:
    const owenrUser = await this.usersRepository.findOne({
      where: {
        parkerId: park.id
      }
    });

    await this.usersRepository.save({
      id: owenrUser.id,
      tokens: owenrUser.tokens + TOKENS_ADDITION,
      location: null,
      parkerId: null,
      expirationTime: null
    });

    const updatedUser = await this.findUser(owenrUser.id);

    return updatedUser;
  }

}
