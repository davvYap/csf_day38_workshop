import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { imageRepository } from 'src/app/repository/image.repository';
import { LoginService } from 'src/app/service/login.service';
import { userInfo } from '../../models';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css'],
})
export class LoginComponent implements OnInit {
  form!: FormGroup;
  notAuthenicated: boolean = true;

  constructor(
    private fb: FormBuilder,
    private loginSvc: LoginService,
    private router: Router,
    private userDexieDB: imageRepository
  ) {}

  ngOnInit(): void {
    this.form = this.createForm();
  }

  createForm(): FormGroup {
    return this.fb.group({
      username: this.fb.control('', [Validators.required]),
      password: this.fb.control('', [Validators.required]),
    });
  }

  login() {
    const username = this.form.get('username')?.value;
    const password = this.form.get('password')?.value;

    this.loginSvc.verifyLogin(username, password).subscribe((res) => {
      this.loginSvc.isLogin = res.isLogin;
      this.loginSvc.username = username;
      this.notAuthenicated = res.isLogin;
      this.loginSvc.isLogin$.next(res.isLogin);
      console.log('login >>>> ', this.notAuthenicated);
      if (res.isLogin) {
        this.userDexieDB.insertUser(username);
      }
      this.router.navigate(['/home']);
    });
  }

  invalidField(ctrlName: string): boolean {
    return (
      !!this.form.get(ctrlName)?.invalid && !!this.form.get(ctrlName)?.dirty
    );
  }
}
