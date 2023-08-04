import { Component } from '@angular/core';
import { FormControl } from "@angular/forms";
import { Router } from "@angular/router";
import { LoginDto } from "../../dto/loginDto";
import { UserService } from "../../services/user.service";

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.scss']
})
export class LoginComponent {

  username: FormControl = new FormControl<string>('');
  password: FormControl = new FormControl<string>('');

  constructor(private route: Router, private userService: UserService) {
  }


  login(): void {
    const loginDto: LoginDto = {
      username: this.username.value,
      password: this.password.value
    };

    this.userService.login(loginDto)
      .subscribe({
        next: () => this.route.navigate(["/", "chat"]),
        error: (error) => console.error(error)
      });
  }

}
