import { Component, OnInit, OnDestroy } from '@angular/core';
import { Router } from '@angular/router';
import { Observable, Subject, Subscription } from 'rxjs';
import { image, imageJson, userImage } from 'src/app/models';
import { GetService } from 'src/app/service/get.service';
import { LoginService } from 'src/app/service/login.service';

@Component({
  selector: 'app-photo',
  templateUrl: './photo.component.html',
  styleUrls: ['./photo.component.css'],
})
export class PhotoComponent implements OnInit, OnDestroy {
  image$!: Subscription;
  image!: string;
  key: string = '95aaa2d3';
  key$!: Subscription;
  userImg!: userImage;
  images: image[] = [];
  totalImages: number = 0;
  index: number = 0;
  event$ = new Subject<string>();

  constructor(private getSvc: GetService, private router: Router) {}

  ngOnInit(): void {
    // this.image$ = this.getSvc.getImage(this.key).subscribe((img: imageJson) => {
    //   this.image = img.image;
    // });

    this.getSvc.getUserImage().subscribe((data: userImage) => {
      this.userImg = data;
      console.log('userImg >>> ', this.userImg);
      this.images = this.userImg.images;
      console.log('images >>> ', this.userImg.images);
      this.totalImages = this.images.length;
      console.log('length of keys >>>', this.totalImages);

      this.getSvc
        .getImage(data.images[0].image_key)
        .subscribe((img: imageJson) => {
          this.image = img.image;
        });
    });

    this.key$ = this.event$.subscribe((key) => {
      this.key = key;
      console.log('key in subcription >>> ', key);

      this.getSvc.getImage(key).subscribe((img: imageJson) => {
        this.image = img.image;
      });
    });
  }

  ngOnDestroy(): void {
    if (this.image$) {
      this.image$.unsubscribe();
    }
    if (this.key$) {
      this.key$.unsubscribe();
    }
  }

  getImageSrc(): string {
    return this.image;
  }

  back() {
    this.router.navigate(['/home']);
  }

  prevImg() {
    this.index === 0 ? 0 : this.index--;
    this.getSvc.getUserImage().subscribe((data: userImage) => {
      console.log('data >>> ', data);
      this.userImg = data;
      this.event$.next(data.images[this.index].image_key);
      console.log('data key >>> ', data.images[this.index].image_key);
    });
  }

  nextImg() {
    this.index === this.totalImages - 1 ? this.index : this.index++;
    this.getSvc.getUserImage().subscribe((data: userImage) => {
      console.log('data >>> ', data);
      this.userImg = data;
      this.event$.next(data.images[this.index].image_key);
      console.log('data key >>> ', data.images[this.index].image_key);
    });
  }
}
