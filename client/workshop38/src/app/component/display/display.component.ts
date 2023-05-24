import { Component, OnInit } from '@angular/core';
import { PostService } from 'src/app/service/post.service';
import { Location } from '@angular/common';
import { Router } from '@angular/router';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';

@Component({
  selector: 'app-display',
  templateUrl: './display.component.html',
  styleUrls: ['./display.component.css'],
})
export class DisplayComponent implements OnInit {
  image: string = '';
  form!: FormGroup;

  constructor(
    private postSvc: PostService,
    private location: Location,
    private router: Router,
    private fb: FormBuilder
  ) {}

  ngOnInit() {
    this.image = this.postSvc.getImage();
    this.form = this.createForm();
  }

  createForm(): FormGroup {
    return this.fb.group({
      comments: this.fb.control('', Validators.required),
    });
  }

  back(): void {
    this.location.back();
  }

  upload(): void {
    this.postSvc.uploadImageToServer(this.form.value.comments, this.image);
    this.router.navigate(['/camera']);
  }
}
