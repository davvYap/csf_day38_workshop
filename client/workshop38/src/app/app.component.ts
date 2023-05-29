import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { LoginService } from './service/login.service';
import { imageRepository } from './repository/image.repository';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css'],
})
export class AppComponent implements OnInit {
  title = 'workshop38';

  isLogin: boolean = false;

  constructor(
    private router: Router,
    private loginSvc: LoginService,
    private imgRepo: imageRepository
  ) {}

  ngOnInit(): void {
    this.loginSvc.isLogin$.subscribe((res) => {
      this.isLogin = res;
    });
  }

  logout() {
    this.imgRepo.deleteUser(this.loginSvc.getUsername());
    this.loginSvc.logout();
    this.isLogin = false;
    this.router.navigate(['']);
  }
}
