import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, firstValueFrom } from 'rxjs';

@Injectable({
  providedIn: 'root',
})
export class PostService {
  constructor(private http: HttpClient) {}

  image: string = '';

  uploadImage(image: string) {
    this.image = image;
  }

  getImage(): string {
    return this.image;
  }

  // convert image from string to file type
  dataURLtoFile(dataurl: string, filename: string = 'image.jpeg') {
    var arr = dataurl.split(','),
      // @ts-ignore
      mime = arr[0].match(/:(.*?);/)[1],
      bstr = atob(arr[arr.length - 1]),
      n = bstr.length,
      u8arr = new Uint8Array(n);
    while (n--) {
      u8arr[n] = bstr.charCodeAt(n);
    }
    return new File([u8arr], filename, { type: mime });
  }

  // upload image to server
  uploadImageToServer(comments: string, image: string) {
    const imageBlob = this.dataURLtoFile(image, '');
    const formData = new FormData();
    formData.set('comments', comments);
    formData.set('file', imageBlob);
    firstValueFrom(
      this.http.post<string>('http://localhost:8080/upload', formData)
    )
      .then(() => alert('Uploaded'))
      .catch((error) => alert(JSON.stringify(error)));
  }

  uploadFileToServer(comments: string, file: File): Observable<string> {
    const formData = new FormData();
    formData.set('comments', comments);
    formData.set('file', file);
    return this.http.post<string>('http://localhost:8080/upload', formData);
  }
}
