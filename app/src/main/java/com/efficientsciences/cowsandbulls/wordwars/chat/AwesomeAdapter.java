package com.efficientsciences.cowsandbulls.wordwars.chat;

import java.util.ArrayList;

import wei.mark.standout.StandOutWindow;
import wei.mark.standout.ui.Window;
import wei.mark.standout.ui.Window.WindowDataKeys;
import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.efficientsciences.cowsandbulls.wordwars.R;
import com.efficientsciences.cowsandbulls.wordwars.managers.ChatManager;
import com.efficientsciences.cowsandbulls.wordwars.managers.ResourcesManager;
/**
 * AwesomeAdapter is a Custom class to implement custom row in ListView
 * 
 * @author Adil Soomro
 *
 */
public class AwesomeAdapter extends BaseAdapter{
	private Context mContext;
	private ArrayList<Message> mMessages;



	public AwesomeAdapter(Context context, ArrayList<Message> messages) {
		super();
		this.mContext = context;
		this.mMessages = messages;
	}
	@Override
	public int getCount() {
		return mMessages.size();
	}
	@Override
	public Object getItem(int position) {		
		return mMessages.get(position);
	}
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		Message message = (Message) this.getItem(position);

		ViewHolder holder; 
		if(convertView == null)
		{
			holder = new ViewHolder();
			convertView = LayoutInflater.from(mContext).inflate(R.layout.sms_row, parent, false);
			holder.message = (TextView) convertView.findViewById(R.id.message_text);
			convertView.setTag(holder);
		}
		else
			holder = (ViewHolder) convertView.getTag();

		if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
			holder.message.setText(Html.fromHtml(message.getMessage(),Html.FROM_HTML_MODE_LEGACY));
		} else {
			holder.message.setText(Html.fromHtml(message.getMessage()));
		}


		LayoutParams lp = (LayoutParams) holder.message.getLayoutParams();
		//check if it is a status message then remove background, and change text color.
		if(message.isStatusMessage())
		{
			holder.message.setBackgroundDrawable(null);
			//if(message.isMine())
			lp.gravity = Gravity.LEFT;
			/*}
			else{
				lp.gravity = Gravity.CENTER;
			}*/
			holder.message.setTextColor(ResourcesManager.getInstance().activity.getResources().getColor(R.color.textFieldColor));
		}
		else
		{		
			//Check whether message is mine to show green background and align to right
			if(message.isMine())
			{
				holder.message.setBackgroundResource(R.drawable.speech_bubble_white);
				lp.gravity = Gravity.RIGHT;
			}
			//If not mine then it is from sender to show orange background and align to left
			else
			{
				holder.message.setBackgroundResource(R.drawable.speech_bubble_pale);
				lp.gravity = Gravity.LEFT;
			}
			holder.message.setLayoutParams(lp);
			holder.message.setTextColor(ResourcesManager.getInstance().activity.getResources().getColor(R.color.textColor));
		/*	Bundle data = getWindow(0).data;
			boolean isMaximized = data
					.getBoolean(WindowDataKeys.IS_MAXIMIZED);
			
			
			if(android.os.Build.VERSION.SDK_INT >= 11){
				if(isMaximized){
					setHolderMessageScale(holder,1);
					if(null!=ChatManager.getInstance().view){
						LinearLayout lin= (LinearLayout)ChatManager.getInstance().view.findViewById(R.id.bottom_write_bar);
						setBottomBarScale(lin,1);
					}
					if(null!=ChatManager.getInstance().text){
						setEditTextScale(ChatManager.getInstance().text,1);
					}
					if(null!=ChatManager.getInstance().buttonShout){
						setButtonScale(ChatManager.getInstance().buttonShout,1);
					}
				}
				else{
					setHolderMessageScale(holder,ResourcesManager.resourceScaler);
					if(null!=ChatManager.getInstance().view){
						LinearLayout lin= (LinearLayout)ChatManager.getInstance().view.findViewById(R.id.bottom_write_bar);
						setBottomBarScale(lin,ResourcesManager.resourceScaler);
					}
					if(null!=ChatManager.getInstance().text){
						setEditTextScale(ChatManager.getInstance().text,ResourcesManager.resourceScaler);
					}
					if(null!=ChatManager.getInstance().buttonShout){
						setButtonScale(ChatManager.getInstance().buttonShout,ResourcesManager.resourceScaler);
					}
				}
			}*/

		}
		return convertView;
	}

	public final Window getWindow(int id) {
		if(null!=StandOutWindow.sWindowCache){
			return StandOutWindow.sWindowCache.getCache(id, WidgetsWindow.class);
		}
		return null;
	}
	/**
	 * 
	 */
	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	private void setHolderMessageScale(ViewHolder holder,float scaler) {
		holder.message.setScaleX(1/scaler);
		holder.message.setScaleY(1/scaler);
		
	}
	
	/**
	 * 
	 */
	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	private void setEditTextScale(EditText edit,float scaler) {
		if(null!=edit){
			//edit.setScaleX(1/scaler);
			edit.setScaleY(1/scaler);
		}
	}
	
	/**
	 * 
	 */
	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	private void setButtonScale(Button buttonShout,float scaler) {
		if(null!=buttonShout){
			//buttonShout.setScaleX(1/scaler);
			buttonShout.setScaleY(1/scaler);
		}
	}
	

	/**
	 * 
	 */
	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	private void setBottomBarScale(LinearLayout lin,float scaler) {
		if(null!=lin){
			lin.setScaleX(1/scaler);
			lin.setScaleY(1/scaler);
			
			//ViewGroup.LayoutParams lp = lin.getLayoutParams();
			//lp.width  = (int) (lp.width/scaler);
			//lp.height = RelativeLayout.LayoutParams.WRAP_CONTENT;
			//lin.refreshDrawableState();
			//lin.refreshDrawableState();
		}
	}
	private static class ViewHolder
	{
		TextView message;
	}

	@Override
	public long getItemId(int position) {
		//Unimplemented, because we aren't using Sqlite.
		return position;
	}

}
