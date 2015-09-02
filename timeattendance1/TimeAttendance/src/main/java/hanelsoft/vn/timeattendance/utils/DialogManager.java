package hanelsoft.vn.timeattendance.utils;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.text.TextUtils;

public class DialogManager {

	// private static boolean isShow = false;
	private static AlertDialog dialog;

	public static void showInfomationMessage(int messageID, Context context) {
		showInfomationMessage(context.getString(messageID), context);
	}

	public static void showInfomationMessage(String message, Context context) {
		showInfomationMessage(message, context, new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		});
	}

	public static void showInfomationMessage(int messageID, Context context,
			OnClickListener listener) {
		showInfomationMessage(context.getString(messageID), context, listener);
	}

	public static void showInfomationMessage(String message, Context context,
			OnClickListener listener) {
		if (dialog != null && dialog.isShowing()) {
			return;
		}
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setTitle("INFORMATION");
		builder.setMessage(message);
		builder.setCancelable(false);
		builder.setPositiveButton("OK", listener);
		dialog = builder.show();
	}

	public static void showErrorMessage(int messageID, Context context) {
		showErrorMessage(context.getString(messageID), context,
				new OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
					}
				});
	}

	public static void showErrorMessage(int messageID, Context context,
			OnClickListener listener) {
		showErrorMessage(context.getString(messageID), context, listener);
	}

	public static void showErrorMessage(String message, Context context) {
		showErrorMessage(message, context, new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		});
	}

	public static void showErrorMessage(String message, Context context,
			OnClickListener listener) {
		if (dialog != null && dialog.isShowing()) {
			return;
		}
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setTitle("ERROR");
		builder.setMessage(message);
		builder.setPositiveButton("OK", listener);
		builder.setCancelable(false);
		dialog = builder.show();
	}

	public static void showSuccessMessage(int messageID, Context context) {
		showSuccessMessage(context.getString(messageID), context,
				new OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
					}
				});
	}

	public static void showSuccessMessage(int messageID, Context context,
			OnClickListener success) {
		showSuccessMessage(context.getString(messageID), context, success);
	}

	public static void showSuccessMessage(String message, Context context,
			OnClickListener success) {
		if (dialog != null && dialog.isShowing()) {
			return;
		}
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setTitle("SUCCESS");
		builder.setMessage(message);
		builder.setPositiveButton("OK", success);
		builder.setCancelable(false);
		dialog = builder.show();
	}

	public static void showConfirmMessage(int messageID, Context context,
			OnClickListener accept) {
		showConfirmMessage(context.getString(messageID), context, accept);
	}

	public static void showConfirmMessage(String message, Context context,
			OnClickListener accept) {
		showConfirmMessage(message, null, context, "", accept);
	}

	public static void showConfirmMessage(int messageID, int titleID,
			Context context, String txtAccept, OnClickListener accept) {
		showConfirmMessage(context.getString(messageID),
				context.getString(titleID), context, txtAccept, accept);
	}

	public static void showConfirmMessage(String message, String title,
			Context context, String txtAccept, OnClickListener accept) {
		showConfirmMessage(message, title, context, txtAccept, accept, "",
				new OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
					}
				});
	}

	public static void showConfirmMessage(int messageID, Context context,
			OnClickListener accept, OnClickListener cancel) {
		showConfirmMessage(context.getString(messageID), context, accept,
				cancel);
	}

	public static void showConfirmMessage(String message, Context context,
			OnClickListener accept, OnClickListener cancel) {
		showConfirmMessage(message, null, context, "", accept, "", cancel);
	}

	public static void showConfirmMessage(String message, String title,
			Context context, String txtAccept, OnClickListener accept,
			String txtCancel, OnClickListener cancel) {
		if (dialog != null && dialog.isShowing()) {
			return;
		}
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		if (title == null || title.equals(""))
			builder.setTitle("CONFIRM");
		else
			builder.setTitle(title);
		builder.setMessage(message);
		if (TextUtils.isEmpty(txtAccept))
			builder.setPositiveButton("Accept", accept);
		else
			builder.setPositiveButton(txtAccept, accept);
		if (TextUtils.isEmpty(txtCancel))
			builder.setNegativeButton("Cancel", cancel);
		else
			builder.setNegativeButton(txtCancel, cancel);
		builder.setCancelable(false);
		dialog = builder.show();
	}

}
