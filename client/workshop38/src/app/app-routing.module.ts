import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { CameraComponent } from './component/camera/camera.component';
import { DisplayComponent } from './component/display/display.component';
import { HomeComponent } from './component/home/home.component';
import { UploadComponent } from './component/upload/upload.component';
import { PhotoComponent } from './component/photo/photo.component';
import { LoginComponent } from './component/login/login.component';
import { loginGuard } from './util';

const routes: Routes = [
  { path: '', component: LoginComponent, title: 'Login' },
  {
    path: 'home',
    component: HomeComponent,
    title: 'Home',
    canActivate: [loginGuard],
  },
  { path: 'camera', component: CameraComponent, title: 'Camera' },
  { path: 'display', component: DisplayComponent, title: 'Display' },
  { path: 'upload', component: UploadComponent, title: 'Upload' },
  { path: 'photo', component: PhotoComponent, title: 'Photo' },
  { path: '**', redirectTo: '', pathMatch: 'full' },
];

@NgModule({
  imports: [RouterModule.forRoot(routes, { useHash: true })],
  exports: [RouterModule],
})
export class AppRoutingModule {}
