import { inject } from '@angular/core';
import { CanActivateFn, Router } from '@angular/router';
import { LoginService } from './service/login.service';

export const loginGuard: CanActivateFn = (route, state) => {
  const loginSvc = inject(LoginService);
  const router = inject(Router);

  if (loginSvc.isLogin) return true;

  return router.createUrlTree(['/']);
};
