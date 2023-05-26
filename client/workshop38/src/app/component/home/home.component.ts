import { Component } from '@angular/core';
import { Router } from '@angular/router';

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.css'],
})
export class HomeComponent {
  constructor(private router: Router) {}

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
