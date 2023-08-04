import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { LoginComponent } from './component/login/login.component';
import { HeaderComponent } from './component/header/header.component';
import { RegisterComponent } from './component/register/register.component';
import { ChatComponent } from './component/chat/chat.component';
import { ReactiveFormsModule } from "@angular/forms";
import { MatInputModule } from "@angular/material/input";
import { MatButtonModule } from "@angular/material/button";
import { provideAnimations } from "@angular/platform-browser/animations";
import { HttpWrapperService } from "./services/http-wrapper.service";
import { UserService } from "./services/user.service";
import { HttpClient, HttpClientModule, HttpHandler } from "@angular/common/http";
import { AuthGuardService } from "./services/auth-guard.service";

@NgModule({
  declarations: [
    AppComponent,
    LoginComponent,
    HeaderComponent,
    RegisterComponent,
    ChatComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    ReactiveFormsModule,
    MatInputModule,
    MatButtonModule,
    HttpClientModule,
  ],
  providers: [
    HttpClient,
    HttpWrapperService,
    UserService,
    AuthGuardService,
    provideAnimations()
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }
