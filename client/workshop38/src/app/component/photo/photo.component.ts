import { Component, OnInit } from '@angular/core';
import { Observable, Subscription } from 'rxjs';
import { imageJson } from 'src/app/models';
import { GetService } from 'src/app/service/get.service';

@Component({
  selector: 'app-photo',
  templateUrl: './photo.component.html',
  styleUrls: ['./photo.component.css'],
})
export class PhotoComponent implements OnInit {
  image$!: Subscription;
  image!: string;
  key: string = 'b3e77264';
  constructor(private getSvc: GetService) {}
  ngOnInit(): void {
    this.image$ = this.getSvc
      .getImage(this.key)
      .subscribe((data: imageJson) => {
        this.image = data.image;
      });
  }

  getImageSrc(): string {
    return this.image;
  }
}
