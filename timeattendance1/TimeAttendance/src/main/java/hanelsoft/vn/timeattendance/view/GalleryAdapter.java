package hanelsoft.vn.timeattendance.view;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Gallery;
import android.widget.ImageView;

import com.a2000.tas.R;

public class GalleryAdapter extends BaseAdapter {
	private Context context;
	private ArrayList<String> photos;

	public GalleryAdapter(Context context, ArrayList<String> photos) {
		this.context = context;
		this.photos = photos;
		System.out.println(photos.size());

	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return photos.size();
	}

	@Override
	public Object getItem(int position) {
		return photos.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ImageView imgView = new ImageView(context);
		imgView.setPadding(10, 0, 10, 0);
		imgView.setScaleType(ImageView.ScaleType.FIT_XY);
		imgView.setLayoutParams(new Gallery.LayoutParams(context.getResources()
				.getDimensionPixelSize(R.dimen.image_galery_width), context
				.getResources().getDimensionPixelSize(
						R.dimen.image_galery_height)));
		Bitmap bmImg = BitmapFactory.decodeFile(photos.get(position));
		imgView.setImageBitmap(bmImg);
		return imgView;
	}

}
