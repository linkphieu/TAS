package hanelsoft.vn.timeattendance.model.DAO;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.a2000.tas.R;

public class adapterProject extends BaseAdapter {
	private Context mContext;
	private daoProject mDaoProject;
	private ArrayList<daoProject> listProject;

	public adapterProject(Context mContext, ArrayList<daoProject> listProject) {
		super();
		this.mContext = mContext;
		this.listProject = listProject;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return listProject.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return listProject.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		final ViewHoder viewHoder;
		if (convertView == null) {
			LayoutInflater inflater = (LayoutInflater) mContext
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = inflater.inflate(R.layout.list_item_project, null);

			viewHoder = new ViewHoder();
			viewHoder.txtNameProject = (TextView) convertView
					.findViewById(R.id.txtNameProject);
			viewHoder.txtAddressProject = (TextView) convertView
					.findViewById(R.id.txtAddressProject);
			convertView.setTag(viewHoder);
		} else {
			viewHoder = (ViewHoder) convertView.getTag();
		}
		// ========================================
		mDaoProject = listProject.get(position);
		viewHoder.txtNameProject.setText(mDaoProject.getNameProject());
		viewHoder.txtAddressProject.setText(" (" + mDaoProject.getAddress()
				+ ")");
		return convertView;
	}

	static class ViewHoder {
		private TextView txtNameProject, txtAddressProject;
	}
}
