/*******************************************************************************
 * Copyright 2011-2013 Sergey Tarasevich
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *******************************************************************************/
package com.nostra13.universalimageloader.sample;

/**
 * @author Sergey Tarasevich (nostra13[at]gmail[dot]com)
 */
public final class Constants {

	public static final String[] IMAGES = new String[] {
			// Heavy images
			"http://g.hiphotos.baidu.com/image/w%3D310/sign=d20bfb33d01b0ef46ce89e5fedc551a1/b219ebc4b74543a9f4ef49d11c178a82b9011432.jpg",
			"http://f.hiphotos.baidu.com/image/w%3D310/sign=de74fe514836acaf59e090fd4cd88d03/5fdf8db1cb1349541cb8550c554e9258d1094a37.jpg",
			"http://e.hiphotos.baidu.com/image/w%3D310/sign=2524aa1d6259252da3171b05049a032c/a2cc7cd98d1001e9c5dc5c08ba0e7bec55e797d1.jpg",
			"http://b.hiphotos.baidu.com/image/w%3D310/sign=c371b87e718b4710ce2ffbcdf3cfc3b2/72f082025aafa40f322b8e82a864034f79f019fb.jpg",
			"http://d.hiphotos.baidu.com/image/w%3D310/sign=4750453397eef01f4d141ec4d0ff99e0/7e3e6709c93d70cfc818565afbdcd100bba12b83.jpg",
			"http://g.hiphotos.baidu.com/image/w%3D310/sign=d20bfb33d01b0ef46ce89e5fedc551a1/b219ebc4b74543a9f4ef49d11c178a82b9011432.jpg",
            "http://f.hiphotos.baidu.com/image/w%3D310/sign=de74fe514836acaf59e090fd4cd88d03/5fdf8db1cb1349541cb8550c554e9258d1094a37.jpg",
            "http://e.hiphotos.baidu.com/image/w%3D310/sign=2524aa1d6259252da3171b05049a032c/a2cc7cd98d1001e9c5dc5c08ba0e7bec55e797d1.jpg",
            "http://b.hiphotos.baidu.com/image/w%3D310/sign=c371b87e718b4710ce2ffbcdf3cfc3b2/72f082025aafa40f322b8e82a864034f79f019fb.jpg",
            "http://d.hiphotos.baidu.com/image/w%3D310/sign=4750453397eef01f4d141ec4d0ff99e0/7e3e6709c93d70cfc818565afbdcd100bba12b83.jpg",
            "http://g.hiphotos.baidu.com/image/w%3D310/sign=d20bfb33d01b0ef46ce89e5fedc551a1/b219ebc4b74543a9f4ef49d11c178a82b9011432.jpg",
            "http://f.hiphotos.baidu.com/image/w%3D310/sign=de74fe514836acaf59e090fd4cd88d03/5fdf8db1cb1349541cb8550c554e9258d1094a37.jpg",
            "http://e.hiphotos.baidu.com/image/w%3D310/sign=2524aa1d6259252da3171b05049a032c/a2cc7cd98d1001e9c5dc5c08ba0e7bec55e797d1.jpg",
            "http://b.hiphotos.baidu.com/image/w%3D310/sign=c371b87e718b4710ce2ffbcdf3cfc3b2/72f082025aafa40f322b8e82a864034f79f019fb.jpg",
            "http://d.hiphotos.baidu.com/image/w%3D310/sign=4750453397eef01f4d141ec4d0ff99e0/7e3e6709c93d70cfc818565afbdcd100bba12b83.jpg",
            "http://g.hiphotos.baidu.com/image/w%3D310/sign=d20bfb33d01b0ef46ce89e5fedc551a1/b219ebc4b74543a9f4ef49d11c178a82b9011432.jpg",
            "http://f.hiphotos.baidu.com/image/w%3D310/sign=de74fe514836acaf59e090fd4cd88d03/5fdf8db1cb1349541cb8550c554e9258d1094a37.jpg",
            "http://e.hiphotos.baidu.com/image/w%3D310/sign=2524aa1d6259252da3171b05049a032c/a2cc7cd98d1001e9c5dc5c08ba0e7bec55e797d1.jpg",
            "http://b.hiphotos.baidu.com/image/w%3D310/sign=c371b87e718b4710ce2ffbcdf3cfc3b2/72f082025aafa40f322b8e82a864034f79f019fb.jpg",
            "http://d.hiphotos.baidu.com/image/w%3D310/sign=4750453397eef01f4d141ec4d0ff99e0/7e3e6709c93d70cfc818565afbdcd100bba12b83.jpg",
            "file:///sdcard/Universal Image Loader @#&=+-_.,!()~'%20.png", // Image from SD card with encoded symbols
			"assets://Living Things @#&=+-_.,!()~'%20.jpg", // Image from assets
			"drawable://" + R.drawable.ic_launcher, // Image from drawables
			"http://upload.wikimedia.org/wikipedia/ru/b/b6/Как_кот_с_мышами_воевал.png", // Link with UTF-8
			"https://www.eff.org/sites/default/files/chrome150_0.jpg", // Image from HTTPS
			"http://bit.ly/soBiXr", // Redirect link
			"http://img001.us.expono.com/100001/100001-1bc30-2d736f_m.jpg", // EXIF
			"", // Empty link
			"http://wrong.site.com/corruptedLink", // Wrong link
	};

	private Constants() {
	}

	public static class Config {
		public static final boolean DEVELOPER_MODE = false;
	}
	
	public static class Extra {
		public static final String FRAGMENT_INDEX = "com.nostra13.example.universalimageloader.FRAGMENT_INDEX";
		public static final String IMAGE_POSITION = "com.nostra13.example.universalimageloader.IMAGE_POSITION";
	}
}
