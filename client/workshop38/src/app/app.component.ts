import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { LoginService } from './service/login.service';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css'],
})
export class AppComponent implements OnInit {
  title = 'workshop38';

  isLogin: boolean = false;

  constructor(private router: Router, private loginSvc: LoginService) {}

  ngOnInit(): void {
    this.loginSvc.isLogin$.subscribe((res) => {
      this.isLogin = res;
    });
  }

  logout() {
    this.loginSvc.logout();
    this.isLogin = false;
    this.router.navigate(['']);
  }
}
