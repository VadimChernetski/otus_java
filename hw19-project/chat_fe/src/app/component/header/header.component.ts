import { Component, OnInit } from '@angular/core';
import { Router, Scroll } from "@angular/router";
import { UserService } from "../../services/user.service";

@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.scss']
})
export class HeaderComponent implements OnInit{

  isAuthenticationPage: boolean = true;

  constructor(private route: Router, private userService: UserService) {
  }

  ngOnInit(): void {
    this.route.events.subscribe(event => {
      if (event instanceof Scroll) {
        const scroll: Scroll = event as Scroll;
        this.isAuthenticationPage = scroll.routerEvent.url === '/login' || scroll.routerEvent.url === '/register';
      }
    })
  }

  onLoginClick(): void {
    this.route.navigate(['/', 'login'])
      .then(() => console.log('navigated to login page'))
  }

  onRegisterClick(): void {
    this.route.navigate(['/', 'register'])
      .then(() => console.log('navigated to register page'))
  }

  onLogoutClick(): void {
    this.userService.logout()
      .subscribe({
        next: () => this.route.navigate(['/', 'login']),
        error: (error) => console.error(error)
      });
  }
}
