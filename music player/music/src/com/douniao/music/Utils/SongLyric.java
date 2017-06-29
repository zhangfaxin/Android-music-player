package com.douniao.music.Utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * ��ʶ���
 * 
 * @author samsung
 * 
 */
public final class SongLyric {

	// ����
	private String title = "";
	// ������
	private String artist = "";
	// ר����
	private String album = "";
	// ƫ��ʱ��
	private long offset = 0;
	// ���ʱ��
	private long maxTime = 0;
	// �������
	private Map<Long, String> lrcs = new HashMap<Long, String>();
	// ��֤�Ƿ�ͨ��
	private boolean valid = false;

	public SongLyric(String url) {
		File file = new File(url);
		if (file.exists()) {
			try {
				// ������ȡ��
				BufferedReader br = new BufferedReader(new InputStreamReader(
						new FileInputStream(file), "gbk"));
				String line = null;
				while ((line = br.readLine()) != null) {
					dealLine(line);
				}
				valid = true;
			} catch (Exception e) {
				System.out.println("Exception");
			}
		}
	}

	public String getTitle() {
		return this.title;
	}

	public String getArtist() {
		return this.artist;
	}

	public String getAlbum() {
		return this.album;
	}

	public boolean isValid() {
		return this.valid;
	}

	public long getMaxTime() {
		return this.maxTime;
	}

	public void setMaxTime(long time) {
		this.maxTime = time;
	}

	/**
	 * ��ȡ��ʱ��Ӧ����ʾ�ĸ��
	 * 
	 * @param ls
	 * @return
	 */
	public String get(long ls) {
		long time = ls + offset;
		Long curr = -1l;
		for (Long l : lrcs.keySet()) {
			curr = l > time ? curr : l < curr ? curr : l;
		}
		return lrcs.get(curr);
	}

	/**
	 * ��ȡ��ʱ����Ҫ��ʾ�ĸ�ʳ�ʼʱ�������
	 * 
	 * @param ls
	 * @return
	 */
	public int getIndex(long ls) {
		Long[] ts = getAllTimes();
		for (int i = 0; i < ts.length - 1; i++) {
			if (ls + offset >= ts[i] && ls + offset < ts[i + 1]) {
				return i;
			}
		}
		return ts.length - 1;
	}

	/**
	 * ��ȡ��ʱ�����ʳ�ʼʱ���ֵ
	 * 
	 * @param ls
	 * @return
	 */
	public int getOffset(long ls) {
		Long[] ts = getAllTimes();
		int index = getIndex(ls);
		if (index < ts.length && index >= 0) {
			return (int) (ls + offset - ts[index]);
		}
		return 0;
	}

	/**
	 * ��ȡ��ʱ��β��ŵĸ�ʹ�����ʱ��
	 * 
	 * @param ls
	 * @return
	 */
	public int getNextTime(long ls) {
		Long[] ts = getAllTimes();
		int index = getIndex(ls);
		if (index < ts.length - 1) {
			return (int) (ts[index + 1] - ts[index]);
		}
		return 0;
	}

	/**
	 * ��������
	 * 
	 * @param line
	 */
	private void dealLine(String line) {
		if (line != null && !line.equals("")) {
			if (line.startsWith("[ti:")) {// ����
				title = line.substring(4, line.length() - 1);
			} else if (line.startsWith("[ar:")) {// ����
				artist = line.substring(4, line.length() - 1);
			} else if (line.startsWith("[al:")) {// ר��
				album = line.substring(4, line.length() - 1);
			} else if (line.startsWith("[offset:")) {// ר��
				offset = Long.parseLong(line.substring(8, line.length() - 1));
			} else {
				// ���и������
				Pattern ptn = Pattern.compile("\\[(\\d{2}:\\d{2}\\.\\d{2})\\]");
				Matcher mth = ptn.matcher(line);
				while (mth.find()) {
					// �õ�ʱ���
					long time = strToLong(mth.group(1));
					// �õ�ʱ���������
					String[] str = ptn.split(line);
					String lrc = str.length > 0 ? str[str.length - 1] : "";
					lrcs.put(time, lrc);
					maxTime = maxTime > time ? maxTime : time;
				}
			}
		}
	}

	/**
	 * ��00:00.00��ʽ�ĸ��ʱ��ת��Ϊlong
	 * 
	 * @param timeStr
	 * @return
	 */
	public static long strToLong(String timeStr) {
		String[] s = timeStr.split(":");
		int min = Integer.parseInt(s[0]);
		String[] ss = s[1].split("\\.");
		int sec = Integer.parseInt(ss[0]);
		int mill = Integer.parseInt(ss[1]);
		return min * 60 * 1000 + sec * 1000 + mill * 10;
	}

	/**
	 * �������������00:00�ķ�ʽ����
	 * 
	 * @param ts
	 * @return
	 */
	public static String longToString(long ts) {
		int time = (int) ts / 1000;
		int ms = time % 60;
		int ss = time / 60;
		ss = ss > 99 ? 99 : ss;
		StringBuffer str = new StringBuffer();
		str.append(ss < 10 ? "0" + ss + ":" : ss + ":");
		str.append(ms < 10 ? "0" + ms : ms + "");
		return str.toString();
	}

	/**
	 * ��ȡ˳��ʱ���������
	 * 
	 * @return
	 */
	public Long[] getAllTimes() {
		Long[] ts = new Long[lrcs.size()];
		int index = 0;
		for (Long l : lrcs.keySet()) {
			ts[index++] = l;
		}
		for (int i = 0; i < ts.length - 1; i++) {
			for (int j = i; j < ts.length; j++) {
				if (ts[i] > ts[j]) {
					Long tmp = ts[i];
					ts[i] = ts[j];
					ts[j] = tmp;
				}
			}
		}
		return ts;
	}

}