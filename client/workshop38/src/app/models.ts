export interface imageJson {
  image: string;
}

export interface loginJson {
  login: boolean;
}

export interface image {
  image_key: string;
  comments: string;
}

export interface userImage {
  username: string;
  images: image[];
}
