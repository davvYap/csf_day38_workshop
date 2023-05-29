export interface imageJson {
  image: string;
}

export interface loginJson {
  isLogin: boolean;
}

export interface image {
  image_key: string;
  comments: string;
}

export interface userImage {
  username: string;
  images: image[];
}

export interface imageLikes {
  key: string;
  likes: number;
  unlikes: number;
}

export interface userInfo {
  userId: string;
  username: string;
}
