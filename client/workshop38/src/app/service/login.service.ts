import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable, Subject } from 'rxjs';
import { loginJson } from '../models';

@Injectable({
  providedIn: 'root',
})
export class LoginService {
  isLogin: boolean = false;

  isLogin$ = new Subject<boolean>();

  username!: string;

  constructor(private http: HttpClient) {}

  verifyLogin(username: string, password: string): Observable<loginJson> {
    let userParams = new HttpParams()
      .set('username', username)
      .set('password', password);

    return this.http.get<loginJson>('http://localhost:8080/api/login', {
      params: userParams,
    });
  }

  logout() {
    this.isLogin = false;
  }

  getUsername() {
    return this.username;
  }
}
