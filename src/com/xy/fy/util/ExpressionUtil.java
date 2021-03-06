package com.xy.fy.util;

import java.util.ArrayList;
import java.util.HashMap;

import android.content.Context;
import android.widget.SimpleAdapter;

import com.xy.fy.main.R;

public class ExpressionUtil {

	private Context context;
	public static final HashMap<String, Integer> all = getAllHashMap();

	/**
	 * 用于Adapter的构造方法
	 * 
	 * @param context
	 */
	public ExpressionUtil(Context context) {
		this.context = context;
	}

	/**
	 * 通过名字找到资源的ID
	 * 
	 * @param packageName
	 * @param className
	 * @param name
	 * @return
	 */
	@SuppressWarnings("unused")
	private int getResourseIdByName(Context context, String name) {
		int id = context.getResources().getIdentifier(name, "drawable",
				"com.xy.fy.main");
		return id;
	}

	/**
	 * 得到表情的所有的映射
	 * 
	 * @return
	 */
	public static HashMap<String, Integer> getAllHashMap() {
		Class<com.xy.fy.main.R.drawable> cls = R.drawable.class;
		HashMap<String, Integer> nameToId = new HashMap<String, Integer>();
		// 得到当前页的所有的映射
		for (int i = 1; i <= 140; i++) {
			String name = "expression" + i;// 弄这个的名字只是为了得到Id，其他都不相干
			try {
				int id = cls.getDeclaredField(name).getInt(null);
				nameToId.put(name, id);// 注意这里存放的不是name因为要的键值对
			} catch (Exception e) {
				System.out.println("找不到Id呀");
				e.printStackTrace();
			}
		}
		return nameToId;
	}

	/**
	 * 得到当前所在页的适配器
	 * 
	 * @param pageNumber
	 * @return
	 */
	public ArrayList<HashMap<String, Integer>> getCurrentPageAdapter(
			int pageNumber) {
		Class<com.xy.fy.main.R.drawable> cls = R.drawable.class;
		int start = pageNumber * 35;
		// 从哪里开始，每页有35个表情，5行7列，allNameToId添加一次
		ArrayList<HashMap<String, Integer>> allNameToId = new ArrayList<HashMap<String, Integer>>();
		// 得到当前页的所有的映射
		for (int i = 1; i <= 35; i++) {
			HashMap<String, Integer> nameToId = new HashMap<String, Integer>();
			String name = "expression" + (start + i);// 弄这个的名字只是为了得到Id，其他都不相干
			try {
				int id = cls.getDeclaredField(name).getInt(null);
				nameToId.put("expression", id);// 注意这里存放的不是name因为要的键值对
			} catch (Exception e) {
				System.out.println("找不到Id呀");
				e.printStackTrace();
			}
			allNameToId.add(nameToId);
		}
		return allNameToId;
	}

	/**
	 * 得到适配器
	 */
	public SimpleAdapter getAdapter(int pageNumber) {
		SimpleAdapter simpleAdapter = null;
		simpleAdapter = new SimpleAdapter(context,
				getCurrentPageAdapter(pageNumber), R.layout.view_paper_item,
				new String[] { "expression" }, new int[] { R.id.item_image });
		return simpleAdapter;
	}
}
