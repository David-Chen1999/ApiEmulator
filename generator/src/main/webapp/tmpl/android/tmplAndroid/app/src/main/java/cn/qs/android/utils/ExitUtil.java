package cn.qs.android.utils;

import android.app.Activity;
import java.util.LinkedList;
import java.util.List;

public class ExitUtil {

	private List<Activity> mList = new LinkedList<>();
	private static ExitUtil instance;


	private ExitUtil() {
	}

	public synchronized static ExitUtil getInstance() {
		if (null == instance) {
			instance = new ExitUtil();
		}
		return instance;
	}

	// add Activity
	public void addActivity(Activity activity) {
		mList.add(activity);
	}

	public void exit() {
		try {
			for (Activity activity : mList) {
				if (activity != null)
					activity.finish();
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			System.exit(0);
		}
	}
//	public void logout() {
//		try {
//			for (Activity activity : mList) {
//				if (activity != null && !(activity instanceof InputTmplActivity))
//					activity.finish();
//			}
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//	}

}
