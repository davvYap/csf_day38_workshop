import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { LoginService } from './service/login.service';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css'],
})
export class AppComponent {
  title = 'workshop38';

  constructor(private router: Router, private loginSvc: LoginService) {}

  logout() {
    this.loginSvc.logout();
    this.router.navigate(['']);
  }
}
