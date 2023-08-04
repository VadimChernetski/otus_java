import { ActivatedRouteSnapshot, CanActivate, CanActivateChild, Router, RouterStateSnapshot } from "@angular/router";
import { Injectable } from "@angular/core";
import { UserService } from "./user.service";

@Injectable()
export class AuthGuardService implements CanActivate {

  constructor(private authenticationService: UserService,
              private router: Router) {
  }

  async canActivate(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Promise<boolean> {
    const result = await this.authenticationService.getUserinfo()
      .toPromise()
      .catch(console.error);
    if (!result) {
      this.router.navigate(['/', 'login']).then();
    }
    return !!result;
  }

}
