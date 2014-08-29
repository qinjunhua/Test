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
	private boolean isplay = true;// 判断是否为播放图片
	private boolean isPause = false;//
	private int index = 0;// 播放列表的索引
	private ImageButton image_playPause;
	// 准备音乐播放器
	private MediaPlayer mp=null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		lv_media = (ListView) findViewById(R.id.listView1);
		// 初始化方法,一打开界面就呈现出音乐的列表结果
		initMusicList();
		image_playPause = (ImageButton) findViewById(R.id.palyPause);

		// 初始化音乐播放器
		initMediaPlayer();
	}

	// 初始化音乐播放器
	private void initMediaPlayer() {
		mp = new MediaPlayer();
	}

	// 初始化界面
	private void initMusicList() {
		ProgressDialog pd = ProgressDialog.show(this, null, "正在加载音乐...", true);
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

	// 暂停还是播放
	public void playPauseClick(View v) {
		// 是播放图片的话
		if (isplay) {
			image_playPause.setImageResource(R.drawable.ic_media_pause);
			play();// 播放
			isplay = false;
		} else {
			image_playPause.setImageResource(R.drawable.ic_media_play);
			pause();// 暂停
			isplay = true;
		}
	}

	// 暂停方法
	private void pause() {
		mp.pause();
		isPause = true;
	}

	// 播放方法
	private void play() {
		// 不是从播放列表的开始播放
		if (isPause) {
			mp.start();
		} else {
			start();// 自定义的开始方法,需要重新重置播放列表
		}
		isPause = false;

	}
	// 重置后的播放列表的开始播放
	private void start() {

		// 从列表中获取当前播放的歌曲的位置下标
		Music music = musicList.get(index);
		System.out.println(music.toString()+"__________________");
		if (mp.isPlaying()) {
			mp.stop();
		}
		mp.reset();
		// 设置播放当前的音乐
		try {
			mp.setDataSource(music.getUrl());
			mp.prepare();
			mp.start();

		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	// 上一首
	public void previousClick(View v) {
		pervious();
	}

	/**
	 * 上一首
	 */
	private void pervious() {

		if(index-1<=musicList.size()-1)
		{
			index--;
		}
		index=musicList.size()-1;
		mp.start();
	}

	// 下一首
	public void nextClick(View v) {
		next();
	}

	/**
	 * 下一首
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
