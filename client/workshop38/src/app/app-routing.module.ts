import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { CameraComponent } from './component/camera/camera.component';
import { DisplayComponent } from './component/display/display.component';
import { HomeComponent } from './component/home/home.component';
import { UploadComponent } from './component/upload/upload.component';

const routes: Routes = [
  { path: '', component: HomeComponent, title: 'Home' },
  { path: 'camera', component: CameraComponent, title: 'Camera' },
  { path: 'display', component: DisplayComponent, title: 'Display' },
  { path: 'upload', component: UploadComponent, title: 'Upload' },
  { path: '**', redirectTo: '', pathMatch: 'full' },
];

@NgModule({
  imports: [RouterModule.forRoot(routes, { useHash: true })],
  exports: [RouterModule],
})
export class AppRoutingModule {}
