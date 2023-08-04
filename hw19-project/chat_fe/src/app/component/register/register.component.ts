import { Component } from '@angular/core';
import { FormControl } from "@angular/forms";
import { Router } from "@angular/router";
import { UserService } from "../../services/user.service";
import { RegisterDto } from "../../dto/registerDto";

@Component({
  selector: 'app-register',
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.scss']
})
export class RegisterComponent {

  email: FormControl = new FormControl<string>('');
  nickname: FormControl = new FormControl<string>('');
  password: FormControl = new FormControl<string>('');

  constructor(private route: Router, private authenticationService: UserService) {
  }

  register(): void {
    const registerDto: RegisterDto = {
      email: this.email.value,
      nickname: this.nickname.value,
      password: this.password.value
    };

    this.authenticationService.register(registerDto)
      .subscribe({
        next: () => this.route.navigate(["/", "login"]),
        error: (error) => console.error(error)
      });
  }

}
