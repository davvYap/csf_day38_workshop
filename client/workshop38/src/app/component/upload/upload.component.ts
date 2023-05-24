import { Component, ElementRef, OnInit, ViewChild } from '@angular/core';
import {
  FormGroup,
  FormControl,
  Validators,
  FormBuilder,
} from '@angular/forms';
import { Router } from '@angular/router';
import { Observable } from 'rxjs';
import { PostService } from 'src/app/service/post.service';
import { Location } from '@angular/common';

@Component({
  selector: 'app-upload',
  templateUrl: './upload.component.html',
  styleUrls: ['./upload.component.css'],
})
export class UploadComponent implements OnInit {
  form!: FormGroup;

  uploadMessage$!: Observable<string>;

  @ViewChild('file')
  file!: ElementRef;

  constructor(
    private fb: FormBuilder,
    private postSvc: PostService,
    private router: Router,
    private location: Location
  ) {}

  ngOnInit(): void {
    this.form = this.createForm();
  }

  createForm(): FormGroup {
    return this.fb.group({
      comments: this.fb.control('', [Validators.required]),
    });
  }

  uploadPic(): void {
    this.uploadMessage$ = this.postSvc.uploadFileToServer(
      this.form.value.comments,
      this.file.nativeElement.files[0]
    );
  }

  formInvalid(): boolean {
    return this.form.invalid || this.file.nativeElement.files.length === 0;
  }

  done(): void {
    this.router.navigate(['/']);
  }
}
