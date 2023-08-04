import { Injectable } from "@angular/core";
import { HttpWrapperService } from "./http-wrapper.service";
import { Observable } from "rxjs";
import { HttpParams } from "@angular/common/http";
import { MessageDto } from "../dto/messageDto";

@Injectable({
  providedIn: 'root'
})
export class ChatService {

  constructor(private httpWrapper: HttpWrapperService) {
  }

  public startChat(id: number): Observable<number> {
    const param: HttpParams = new HttpParams().set('id', id);
    return this.httpWrapper.get<number>(this.httpWrapper.endpointPrefix + '/start', { params: param });
  }

  public getAllPreviousMessages(id: number): Observable<MessageDto[]> {
    const param: HttpParams = new HttpParams().set('id', id);
    return this.httpWrapper.get<MessageDto[]>(this.httpWrapper.endpointPrefix + '/messages', { params: param})
  }

}
