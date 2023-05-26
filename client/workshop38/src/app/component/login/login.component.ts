import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { LoginService } from 'src/app/service/login.service';

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
    private router: Router
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
      this.loginSvc.isLogin = res.login;
      this.loginSvc.username = username;
      this.notAuthenicated = res.login;
      this.loginSvc.isLogin$.next(res.login);
      console.log('login >>>> ', this.notAuthenicated);
      this.router.navigate(['/home']);
    });
  }

  invalidField(ctrlName: string): boolean {
    return (
      !!this.form.get(ctrlName)?.invalid && !!this.form.get(ctrlName)?.dirty
    );
  }
}
