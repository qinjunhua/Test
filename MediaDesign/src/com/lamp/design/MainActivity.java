package com.lamp.design;

import java.io.IOException;
import java.util.ArrayList;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.app.Activity;
import android.app.ProgressDialog;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class MainActivity extends Activity {

	private ListView lv_media;
	ArrayList<Music> musicList = null;
	MusicAdapter ma = null;
	private boolean isplay = true;// �ж��Ƿ�Ϊ����ͼƬ
	private boolean isPause = false;//
	private int index = 0;// �����б������
	private ImageButton image_playPause;
	// ׼�����ֲ�����
	private MediaPlayer mp=null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		lv_media = (ListView) findViewById(R.id.listView1);
		// ��ʼ������,һ�򿪽���ͳ��ֳ����ֵ��б���
		initMusicList();
		image_playPause = (ImageButton) findViewById(R.id.palyPause);

		// ��ʼ�����ֲ�����
		initMediaPlayer();
	}

	// ��ʼ�����ֲ�����
	private void initMediaPlayer() {
		mp = new MediaPlayer();
	}

	// ��ʼ������
	private void initMusicList() {
		ProgressDialog pd = ProgressDialog.show(this, null, "���ڼ�������...", true);
		musicList = MusicUtils.getMusicData(this);
		ma = new MusicAdapter();
		lv_media.setAdapter(ma);
		pd.dismiss();
	}

	class MusicAdapter extends BaseAdapter {
		@Override
		public int getCount() {
			return musicList.size();
		}

		@Override
		public Object getItem(int position) {
			return musicList.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder vh;
			if (convertView == null) {
				convertView = getLayoutInflater().inflate(R.layout.musicitem,
						null);
				vh = new ViewHolder();
				vh.iv_icon = (ImageView) convertView
						.findViewById(R.id.imageView1_picture);
				vh.tv_title = (TextView) convertView
						.findViewById(R.id.textView1_song);
				vh.tv_singer = (TextView) convertView
						.findViewById(R.id.textView2_singer);
				vh.tv_time = (TextView) convertView
						.findViewById(R.id.textView3_time);
				convertView.setTag(vh);
			}
			Music music = musicList.get(position);
			vh = (ViewHolder) convertView.getTag();
			vh.iv_icon.setImageResource(R.drawable.ic_launcher1);
			vh.tv_title.setText(music.getTitle());
			
			vh.tv_singer.setText(music.getSinger());
			vh.tv_time.setText(MusicUtils.toTime((int) music.getTime()));
			return convertView;
		}

		class ViewHolder {
			ImageView iv_icon;
			TextView tv_title;
			TextView tv_singer;
			TextView tv_time;
		}

	}

	// ��ͣ���ǲ���
	public void playPauseClick(View v) {
		// �ǲ���ͼƬ�Ļ�
		if (isplay) {
			image_playPause.setImageResource(R.drawable.ic_media_pause);
			play();// ����
			isplay = false;
		} else {
			image_playPause.setImageResource(R.drawable.ic_media_play);
			pause();// ��ͣ
			isplay = true;
		}
	}

	// ��ͣ����
	private void pause() {
		mp.pause();
		isPause = true;
	}

	// ���ŷ���
	private void play() {
		// ���ǴӲ����б�Ŀ�ʼ����
		if (isPause) {
			mp.start();
		} else {
			start();// �Զ���Ŀ�ʼ����,��Ҫ�������ò����б�
		}
		isPause = false;

	}
	// ���ú�Ĳ����б�Ŀ�ʼ����
	private void start() {

		// ���б��л�ȡ��ǰ���ŵĸ�����λ���±�
		Music music = musicList.get(index);
		System.out.println(music.toString()+"__________________");
		if (mp.isPlaying()) {
			mp.stop();
		}
		mp.reset();
		// ���ò��ŵ�ǰ������
		try {
			mp.setDataSource(music.getUrl());
			mp.prepare();
			mp.start();

		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	// ��һ��
	public void previousClick(View v) {
		pervious();
	}

	/**
	 * ��һ��
	 */
	private void pervious() {

		if(index-1<=musicList.size()-1)
		{
			index--;
		}
		index=musicList.size()-1;
		mp.start();
	}

	// ��һ��
	public void nextClick(View v) {
		next();
	}

	/**
	 * ��һ��
	 */
	private void next() {

		if(index+1<musicList.size())
		{
			index++;
		}
		index=0;
		mp.start();
	}

}
