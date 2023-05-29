import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { LoginService } from 'src/app/service/login.service';
import { Title } from '@angular/platform-browser';

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.css'],
})
export class HomeComponent implements OnInit {
  username: string = 'Guest';
  constructor(
    private router: Router,
    private loginSvc: LoginService,
    private title: Title
  ) {}

  ngOnInit(): void {
    this.username = this.loginSvc.getUsername();
    this.title.setTitle(`Home | ${this.username}`);
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
