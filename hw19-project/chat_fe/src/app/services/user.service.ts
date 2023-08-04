import { Injectable } from "@angular/core";
import { HttpWrapperService } from "./http-wrapper.service";
import { Observable } from "rxjs";
import { LoginDto } from "../dto/loginDto";
import { RegisterDto } from "../dto/registerDto";
import { UserDto } from "../dto/userDto";

@Injectable({
  providedIn: 'root'
})
export class UserService {

  constructor(private httpWrapper: HttpWrapperService) {
  }

  public login(loginDto: LoginDto): Observable<void> {
    const formData: FormData = new FormData();
    formData.set('username', loginDto.username);
    formData.set('password', loginDto.password);
    return this.httpWrapper
      .post<void>(this.httpWrapper.endpointPrefix + '/login', formData);
  }

  public register(registerDto: RegisterDto): Observable<void> {
    return this.httpWrapper.post<void>(this.httpWrapper.endpointPrefix + '/register', registerDto);
  }

  public getUserinfo(): Observable<UserDto> {
    return this.httpWrapper.get<UserDto>(this.httpWrapper.endpointPrefix + '/user-info');
  }

  public getAllUsers(): Observable<UserDto[]> {
    return this.httpWrapper.get<UserDto[]>(this.httpWrapper.endpointPrefix + '/user')
  }

  public logout(): Observable<void> {
    return this.httpWrapper.post<void>(this.httpWrapper.endpointPrefix + '/logout', null);
  }

}
