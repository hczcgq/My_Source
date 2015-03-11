package com.roger.quickviewpage.imagecache;

import java.io.File;

public interface OnDownloadListener {

	void onStart();

	void onProgress(int i);

	void onFinish(File file);

	void onError();
}
