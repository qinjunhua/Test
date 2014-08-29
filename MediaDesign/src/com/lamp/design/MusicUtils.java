package com.lamp.design;

import java.util.ArrayList;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.provider.MediaStore;

public class MusicUtils {
	public static ArrayList<Music> getMusicData(Context context) {
		ArrayList<Music> musics = new ArrayList<Music>();
		ContentResolver cr = context.getContentResolver();
		//System.out.println(MediaStore.Audio.Media.DEFAULT_SORT_ORDER);
		if (cr != null) {
			Cursor cursor = cr.query(
					MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, null, MediaStore.Audio.Media.DURATION+">=?",
					new String[]{"120000"}, MediaStore.Audio.Media.DEFAULT_SORT_ORDER);
			if (null == cursor) {
				return null;
			}
			while (cursor.moveToNext()) {
				Music m = new Music();
				m.setTitle(cursor.getString(cursor
						.getColumnIndex(MediaStore.Audio.Media.TITLE)));
				m.setName(cursor.getString(cursor
						.getColumnIndex(MediaStore.Audio.Media.DISPLAY_NAME)));
				String singer = cursor.getString(cursor
						.getColumnIndex(MediaStore.Audio.Media.ARTIST));

				if (null==singer || "".equals(singer) || "<unknown>".equals(singer)) {
					singer = "未知艺术家";
				} 
				m.setSinger(singer);
				m.setAlbum(cursor.getString(cursor
						.getColumnIndex(MediaStore.Audio.Media.ALBUM)));
				m.setSize(cursor.getLong(cursor
						.getColumnIndex(MediaStore.Audio.Media.SIZE)));
				m.setTime(cursor.getLong(cursor
						.getColumnIndex(MediaStore.Audio.Media.DURATION)));
				m.setUrl(cursor.getString(cursor
						.getColumnIndex(MediaStore.Audio.Media.DATA)));
				System.out.println(m);
				musics.add(m);
			}
			cursor.close();
		}
		return musics;
	}
	/**
	 * 时间格式转换
	 * 
	 * @param time
	 * @return
	 */
	public static String toTime(int time) {

		time /= 1000;
		int minute = time / 60;
		int hour = minute / 60;
		int second = time % 60;
		minute %= 60;
		return String.format("%02d:%02d", minute, second);
	}
}
