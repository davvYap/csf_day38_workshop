import { Component, OnInit } from '@angular/core';
import { WebcamImage, WebcamUtil } from 'ngx-webcam';
import { Observable, Subject } from 'rxjs';
import { PostService } from 'src/app/service/post.service';
import { Router } from '@angular/router';
import { Location } from '@angular/common';

@Component({
  selector: 'app-camera',
  templateUrl: './camera.component.html',
  styleUrls: ['./camera.component.css'],
})
export class CameraComponent implements OnInit {
  allowCameraSwitch = true;
  multipleWebcamsAvailable = false;

  imageCapture!: string;

  // webcam snapshot trigger
  trigger$: Subject<void> = new Subject<void>();

  // switch to next / previous / specific webcam; true/false: forward/backwards, string: deviceId
  private nextWebcam: Subject<boolean | string> = new Subject<
    boolean | string
  >();

  constructor(
    private postService: PostService,
    private router: Router,
    private location: Location
  ) {}

  ngOnInit(): void {
    WebcamUtil.getAvailableVideoInputs().then(
      (mediaDevices: MediaDeviceInfo[]) => {
        this.multipleWebcamsAvailable = mediaDevices && mediaDevices.length > 1;
      }
    );
  }

  handleImage(webcamImage: WebcamImage): void {
    console.info('image captured >>> ', webcamImage);
    this.imageCapture = webcamImage.imageAsDataUrl;
  }

  // the button will trigger the snap
  public triggerSnapshot(): void {
    this.trigger$.next();
    this.postService.uploadImage(this.imageCapture);
    this.router.navigate(['/display']);
  }

  back() {
    this.location.back();
  }
}
