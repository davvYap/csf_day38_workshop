import { Component, OnInit, OnDestroy } from '@angular/core';
import { Router } from '@angular/router';
import { Observable, Subject, Subscription, debounceTime, map } from 'rxjs';
import { image, imageJson, userImage } from 'src/app/models';
import { GetService } from 'src/app/service/get.service';
import { LoginService } from 'src/app/service/login.service';
import { PostService } from 'src/app/service/post.service';

@Component({
  selector: 'app-photo',
  templateUrl: './photo.component.html',
  styleUrls: ['./photo.component.css'],
})
export class PhotoComponent implements OnInit, OnDestroy {
  image$!: Subscription;
  image!: string;
  key: string = '';
  key$!: Subscription;
  userImg!: userImage;
  comments: string = '';
  images: image[] = [];
  totalImages: number = 0;
  index: number = 0;
  event$ = new Subject<image>();

  likesSubscription$!: Subscription;
  likesSub$ = new Subject<number>();
  likes: number = 0;
  unlikes: number = 0;

  constructor(
    private getSvc: GetService,
    private router: Router,
    private postSvc: PostService
  ) {}

  ngOnInit(): void {
    // First Image
    this.getSvc.getUserImage().subscribe((data: userImage) => {
      this.userImg = data;
      console.log('userImg >>> ', this.userImg);
      this.images = this.userImg.images;
      console.log('images >>> ', this.userImg.images);
      this.totalImages = this.images.length;
      console.log('length of keys >>>', this.totalImages);

      // get comments
      this.comments = data.images[0].comments;

      // get key
      this.key = data.images[0].image_key;

      // get image
      this.getSvc
        .getImage(data.images[0].image_key)
        .subscribe((img: imageJson) => {
          this.image = img.image;
        });

      // get image likes and unlikes
      this.likesSubscription$ = this.getSvc
        .getImageLikesUnlikes(this.key)
        .subscribe((imgLikes) => {
          this.likes = imgLikes.likes;
          this.unlikes = imgLikes.unlikes;
        });
    });

    // Prev and Next key
    this.key$ = this.event$.subscribe((image) => {
      this.key = image.image_key;
      this.comments = image.comments;
      console.log('key in subcription >>> ', image.image_key);

      this.getSvc.getImage(this.key).subscribe((img: imageJson) => {
        this.image = img.image;
      });

      this.getSvc.getImageLikesUnlikes(this.key).subscribe((imgLikes) => {
        this.likes = imgLikes.likes;
        this.unlikes = imgLikes.unlikes;
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
    if (this.likesSubscription$) {
      this.likesSubscription$.unsubscribe();
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
    this.subcribeUserImage(this.index);
  }

  nextImg() {
    this.index === this.totalImages - 1 ? this.index : this.index++;
    this.subcribeUserImage(this.index);
  }

  private subcribeUserImage(index: number): void {
    this.getSvc
      .getUserImage()
      .pipe(
        debounceTime(2000),
        map((v: userImage) => {
          return v;
        })
      )
      .subscribe((data: userImage) => {
        console.log('data >>> ', data);
        this.userImg = data;
        this.event$.next(data.images[this.index]);
        console.log('data key >>> ', data.images[this.index].image_key);
      });
  }

  like() {
    this.likes++;
    this.postSvc
      .updateImageLikes(this.key, this.likes, this.unlikes)
      .subscribe();
  }

  unlike() {
    this.unlikes++;
    this.postSvc
      .updateImageLikes(this.key, this.likes, this.unlikes)
      .subscribe();
  }
}
