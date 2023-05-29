import { Injectable } from '@angular/core';
import { Dexie } from 'dexie';
import { userInfo } from '../models';
import { v4 as uuidv4 } from 'uuid';

@Injectable({
  providedIn: 'root',
})
export class imageRepository extends Dexie {
  userDb!: Dexie.Table<userInfo, string>;
  isLogin: boolean = false;
  users: userInfo[] = [];

  constructor() {
    super('UserDatabase');
    this.version(2).stores({
      users: 'userId, username',
    });
    this.userDb = this.table('users');
  }

  insertUser(usernameInput: string) {
    let index: string = uuidv4().substring(0, 8);
    let user: userInfo = {
      userId: index,
      username: usernameInput,
    };
    this.userDb.add(user);
  }

  deleteUser(username: string) {
    const deleteCount = this.userDb.where('username').equals(username).delete();
  }

  async getUserArrayPromise(): Promise<userInfo[]> {
    const user: userInfo[] = await this.userDb.toArray();
    return user;
  }

  getUserArray(): userInfo[] {
    this.getUserArrayPromise().then((u) => {
      console.log('From promise >>>', u.length);
      this.users = u;
    });
    console.log('indexedDB Array length >>> ', this.users.length);
    return this.users;
  }
}
