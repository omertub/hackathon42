import {
    SubscribeMessage,
    WebSocketGateway,
    OnGatewayInit,
    WebSocketServer,
    OnGatewayConnection,
    OnGatewayDisconnect,
   } from '@nestjs/websockets';
   import { Logger } from '@nestjs/common';
   import { Socket, Server } from 'socket.io';

@WebSocketGateway({ cors: { origin: '*' } })
export class EventsGateway {

    // to publish an event, call server.emit with the event name and payload
    @WebSocketServer() server: Server;
   
    @SubscribeMessage('client2server')
    handleMessage(client: Socket, payload: string): void {
        console.log('client2server', payload);
    }
   
}