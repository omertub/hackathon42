import { Get, Post } from "@nestjs/common";
import { Body } from "@nestjs/common";
import { Query } from "@nestjs/common";
import { Controller } from "@nestjs/common";
import { RESPONSE_STATUS } from "src/consts";
import { EventsGateway } from "src/events.gateway";
import { UserService } from "./users.service";

@Controller()
export class UserController {
    constructor(
        private userService: UserService,
        private eventsGateway: EventsGateway
    ) { }

    // this is a GET endpoint, the full url will look like: http://10.0.2.2:3000/user?userId=1
    // you can access the GET param userId using @Query
    @Get('user')
    async getUser(@Query('id') id: number) {
        const user = await this.userService.findUser(id);

        // if the user is not found
        if (!user) {
            // you always should return the response status with the appropriate code. 
            return {
                status: RESPONSE_STATUS.NOT_FOUND,
                user: null
            };
        }

        // everything is OK, return the user
        return {
            status: RESPONSE_STATUS.OK,
            user: user,
        };
    }

    @Get('users')
    async getUsers() {
        const users = await this.userService.findAll();

        // you always should return the response status. 
        return {
            status: RESPONSE_STATUS.OK,
            users: users,
        };
    }

    // this is a POST endpoint, the full url will look like: http://<IPV6>:3000/signup
    // you can access the request body using @Body.
    @Post('signup')
    async signup(@Body() signup: any) {
        const user = await this.userService.signup(signup);

        if (user === null) {
            return { status: RESPONSE_STATUS.USERNAME_ALREADY_EXISTS, user: null }
        }

        return { status: RESPONSE_STATUS.OK, user: user };
    }

    @Post('login')
    async login(@Body() login: any) {
        const user = await this.userService.login(login)

        if (user === null) {
            return { status: RESPONSE_STATUS.INVALID_USERNAME_OR_PASSWORD, user: null }
        }

        return { status: RESPONSE_STATUS.OK, user: user }
    }

    @Post('saveParkingLocation')
    async saveParkingLocation(@Body() saveParkingLocation: any) {
        const user = await this.userService.saveParkingLocation(saveParkingLocation);
        return {
            status: RESPONSE_STATUS.OK   
        }
    }

    @Post('setExpirationTime')
    async setExpirationTime(@Body() setExpirationTime: any) {
        console.log("🚀 ~ file: users.controller.ts ~ line 83 ~ UserController ~ setExpirationTime ~ setExpirationTime", setExpirationTime)
        const user = await this.userService.setExpirationTime(setExpirationTime);
        return {
            status: RESPONSE_STATUS.OK   
        }
    }

    @Get('markers')
    async markers() {
        const markers = await this.userService.findAllMarkers();
        return {
            status: RESPONSE_STATUS.OK,
            markers: markers,
        };
    }

    @Post('commitParking')
    async commitParking(@Body() commitParking: any) {
        const { ownerUser, parkerUser } = await this.userService.commitParking(commitParking);

        console.log("🚀 ~ file: users.controller.ts ~ line 107 ~ UserController ~ commitParking ~ parkingCommitted", ownerUser.id,)

        this.eventsGateway.server.emit('parkingCommitted', {
            id: ownerUser.id,
            parkerUser: parkerUser
        });

        return {
            status: RESPONSE_STATUS.OK,
        }
    }

    @Post('park')
    async park(@Body() park: any) {
        const ownerUser = await this.userService.park(park);

        this.eventsGateway.server.emit('parkingCompleted', {
            id: ownerUser.id,
        });
        
        return {
            status: RESPONSE_STATUS.OK,
        }
    }
}
