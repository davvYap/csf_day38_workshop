import { inject } from '@angular/core';
import { CanActivateFn, Router } from '@angular/router';
import { LoginService } from './service/login.service';
import { imageRepository } from './repository/image.repository';
import { userInfo } from './models';

export const loginGuard: CanActivateFn = (route, state) => {
  const loginSvc = inject(LoginService);
  const router = inject(Router);
  const imageRepo = inject(imageRepository);

  const users: userInfo[] = imageRepo.getUserArray();

  if (loginSvc.isLogin) {
    return true;
  }

  return router.createUrlTree(['/']);
};
