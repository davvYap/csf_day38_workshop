import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { LoginService } from 'src/app/service/login.service';

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.css'],
})
export class HomeComponent implements OnInit {
  username: string = 'Guest';
  constructor(private router: Router, private loginSvc: LoginService) {}

  ngOnInit(): void {
    this.username = this.loginSvc.getUsername();
  }

  toSnap() {
    this.router.navigate(['/camera']);
  }

  toUpload() {
    this.router.navigate(['/upload']);
  }

  getPhoto() {
    this.router.navigate(['/photo']);
  }
}
