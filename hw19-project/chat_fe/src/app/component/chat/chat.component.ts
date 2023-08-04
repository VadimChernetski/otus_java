import { Component, OnDestroy, OnInit } from '@angular/core';
import { UserService } from "../../services/user.service";
import { UserDto } from "../../dto/userDto";
import { ChatService } from "../../services/chat.service";
import { FormControl } from "@angular/forms";
import { MessageDto } from "../../dto/messageDto";
import { IMessage, RxStomp } from "@stomp/rx-stomp";

@Component({
  selector: 'app-chat',
  templateUrl: './chat.component.html',
  styleUrls: ['./chat.component.scss']
})
export class ChatComponent implements OnInit, OnDestroy {

  openedChat: number;
  chatMessages: MessageDto[] = [];
  massage: FormControl = new FormControl<string>('');
  users: UserDto[] = [];
  currentUser: UserDto;

  stompClient: RxStomp;

  constructor(private userService: UserService, private chatService: ChatService) {
  }

  ngOnInit(): void {
    this.userService.getUserinfo().subscribe(user => this.currentUser = user);
    this.userService.getAllUsers().subscribe(users => this.users = users);
    this.initWs()
  }

  ngOnDestroy(): void {
    this.stompClient.deactivate();
  }

  startChat(id: number): void {
    this.chatService.startChat(id).subscribe(response => {
      this.openedChat = response;
      this.chatService.getAllPreviousMessages(this.openedChat)
        .subscribe(messages => this.chatMessages = messages);
      this.stompClient.watch('/topic/message.' + this.openedChat)
        .subscribe( (message: IMessage) =>
          this.chatMessages.push(JSON.parse(message.body)));
    });
  }

  private initWs(): void {
    this.stompClient = new RxStomp();
    this.stompClient.configure({brokerURL: 'ws://localhost:8080/chat/connect'});
    this.stompClient.activate();
  }

  sendMessage(): void {
    this.stompClient.publish({
      destination: '/ws/message.' + this.openedChat,
      body: JSON.stringify({
        authorId: this.currentUser.id,
        chatId: this.openedChat,
        text: this.massage.value
      }),
    });
    this.massage.setValue(null);
  }
}
