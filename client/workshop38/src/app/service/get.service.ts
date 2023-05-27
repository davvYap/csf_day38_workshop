import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable, firstValueFrom } from 'rxjs';
import { imageJson, imageLikes, userImage } from '../models';
import { LoginService } from './login.service';

@Injectable({
  providedIn: 'root',
})
export class GetService {
  constructor(private http: HttpClient, private loginSvc: LoginService) {}

  // async getImage(key: string): Promise<imageJson> {
  //   let s3Key = new HttpParams().set('key', key);

  //   const response = await firstValueFrom(
  //     this.http.get<imageJson>('http://localhost:8080/image', {
  //       params: s3Key,
  //     })
  //   );
  //   return response;
  // }

  // get image from s3 bucket
  getImage(key: string): Observable<imageJson> {
    let s3Key = new HttpParams().set('key', key);
    return this.http.get<imageJson>('http://localhost:8080/api/image', {
      params: s3Key,
    });
  }

  // get user images from mongo
  getUserImage(): Observable<userImage> {
    console.log('getSvc >>> ', this.loginSvc.getUsername());
    let usernameParam = new HttpParams().set(
      'username',
      this.loginSvc.getUsername()
    );
    return this.http.get<userImage>('http://localhost:8080/api/userImage', {
      params: usernameParam,
    });
  }

  // get user images likes and unlikes from redis
  getImageLikesUnlikes(key: string): Observable<imageLikes> {
    const keyParam = new HttpParams().set('key', key);
    return this.http.get<imageLikes>('http://localhost:8080/api/likes', {
      params: keyParam,
    });
  }
}
