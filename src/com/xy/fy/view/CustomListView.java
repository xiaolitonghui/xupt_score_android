package com.xy.fy.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.xy.fy.main.R;

/**
 * 自定义ListView
 * 
 * @author Administrator
 * 
 */
public class CustomListView extends ListView implements OnScrollListener {

	private final static int RELEASE_To_REFRESH = 0; // 释放刷新状态
	private final static int PULL_To_REFRESH = 1; // 下拉刷新状态
	private final static int REFRESHING = 2; // 正在刷新状态
	private final static int DONE = 3; // 完成状态
	private final static int LOADING = 4; // 正在加载数据状态

	private LinearLayout headView;// 代表头部视图
	private ImageView imageArrow;// 代表箭头
	private ProgressBar progressHead;// 头部进度圈
	private ProgressBar progressFoot;// 尾部进度圈
	private int headContentHeight;
	private TextView butMore;// 加载更多
	private RotateAnimation animation;// 动画效果
	private RotateAnimation reverseAnimation;
	private int state;// 状态
	private boolean isRefreshable;// 是否可以刷新
	private int firstItemIndex; // ListView的第一个Item
	private boolean isRecored;// 用于保证startY的值在一个完整的touch事件中只被记录一次
	private int startY; // 开始的Y坐标
	private boolean isBack; // 是否返回
	private final static int RATIO = 3; // 实际的padding的距离与界面上偏移距离的比例
	private OnRefreshListener refreshListener;
	@SuppressWarnings("unused")
	private LayoutInflater inflater;

	public CustomListView(Context context) {
		super(context);
		this.init(context);
	}

	public CustomListView(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.init(context);
	}

	/**
	 * 初始化数据
	 * 
	 * @param context
	 *        上下文
	 */
	public void init(Context context) {
		inflater = LayoutInflater.from(context);
		this.headView = (LinearLayout) inflate(context, R.layout.pull_down_head_refresh, null);
		this.imageArrow = (ImageView) headView.findViewById(R.id.imageHeadArrow);
		this.progressHead = (ProgressBar) headView.findViewById(R.id.progressBarHead);

		measureView(headView); // 测量headView的大小
		headContentHeight = headView.getMeasuredHeight(); // headView的高度
		headView.setPadding(0, -1 * headContentHeight, 0, 0); // 将headView置于ListView的上方-
		headView.invalidate(); // 重绘组件
		addHeaderView(headView, null, false); // 将headView添加到ListView的上方

		LinearLayout footView = (LinearLayout) inflate(context, R.layout.pull_up_foot_more, null);
		this.butMore = (TextView) footView.findViewById(R.id.butMore);
		this.progressFoot = (ProgressBar) footView.findViewById(R.id.progressBarFoot);
		this.progressFoot.setVisibility(View.GONE);
		addFooterView(footView);

		setOnScrollListener(this); // 设置监听器

		animation = new RotateAnimation(0, -180, // 动画效果
				RotateAnimation.RELATIVE_TO_SELF, 0.5f, RotateAnimation.RELATIVE_TO_SELF, 0.5f);
		animation.setInterpolator(new LinearInterpolator());
		animation.setDuration(250);
		animation.setFillAfter(true);

		reverseAnimation = new RotateAnimation(-180, 0, RotateAnimation.RELATIVE_TO_SELF, 0.5f, RotateAnimation.RELATIVE_TO_SELF, 0.5f);
		reverseAnimation.setInterpolator(new LinearInterpolator());
		reverseAnimation.setDuration(200);
		reverseAnimation.setFillAfter(true);

		state = DONE; // 初始化state和isRefreshable
		isRefreshable = false;
	}

	@Override
	public boolean onTouchEvent(MotionEvent ev) {
		if (isRefreshable) {
			switch (ev.getAction()) {
			case MotionEvent.ACTION_DOWN: // 当手指按下时，记录startY,并isRecored标志为已记录
				if (firstItemIndex == 0 && !isRecored) {
					isRecored = true;
					startY = (int) ev.getY();
				}
				break;
			case MotionEvent.ACTION_MOVE:
				int temY = (int) ev.getY(); // 记录当前的Y坐标
				if (!isRecored && firstItemIndex == 0) { // 若还没记录startY，将其记录
					isRecored = true;
					startY = temY;
				}
				if (state != REFRESHING && state != LOADING && isRecored) {

					if (state == DONE) { // 当前状态为DONE
						if ((temY - startY) > 0) {
							state = PULL_To_REFRESH;
							changeHeaderViewByState();
						}
					}
					if (state == PULL_To_REFRESH) {
						setSelection(0);

						// 下拉到headView显示出来，进入释放刷新状态
						if ((temY - startY) / RATIO >= headContentHeight) {
							state = RELEASE_To_REFRESH;
							isBack = true;
							changeHeaderViewByState();
						}
						// 上推到顶了
						else if (temY - startY <= 0) {
							state = DONE;
							changeHeaderViewByState();
						}
					}
					if (state == RELEASE_To_REFRESH) {
						setSelection(0);
						// 往上推了，推到了屏幕足够掩盖head的程度，但是还没有推到全部掩盖的地步
						if (((temY - startY) / RATIO < headContentHeight) && (temY - startY) > 0) {
							state = PULL_To_REFRESH;
							changeHeaderViewByState();
						}
						// 一下子推到顶了
						else if (temY - startY <= 0) {
							state = DONE;
							changeHeaderViewByState();
						}
					}
				}
				// 更新headView的size
				if (state == PULL_To_REFRESH) {
					headView.setPadding(0, -1 * headContentHeight + (temY - startY) / RATIO, 0, 0);
				}
				// 更新headView的paddingTop
				if (state == RELEASE_To_REFRESH) {
					headView.setPadding(0, (temY - startY) / RATIO - headContentHeight, 0, 0);
				}
				break;
			case MotionEvent.ACTION_UP:
				if (state != REFRESHING && state != LOADING) {
					if (state == DONE) {
						// 什么都不做
					}
					if (state == PULL_To_REFRESH) {
						state = DONE;
						changeHeaderViewByState();
					}
					if (state == RELEASE_To_REFRESH) {
						state = REFRESHING;
						changeHeaderViewByState();
						onRefresh();
					}
				}
				isRecored = false;
				isBack = false;
				break;
			}
		}
		return super.onTouchEvent(ev);
	}

	/**
	 * 更新完毕
	 */
	public void refreshComplete() {
		state = DONE;
		changeHeaderViewByState();
	}

	/**
	 * 刷新监听器
	 * 
	 * @param refreshListener
	 */
	public void setOnRefreshListener(OnRefreshListener refreshListener) {
		this.refreshListener = refreshListener;
		isRefreshable = true;
	}

	/**
	 * 更多监听器
	 * 
	 * @param moreBTNListener
	 */
	public void setOnMoreListener(OnMoreButtonListener moreButtonListener) {
		butMore.setOnClickListener(moreButtonListener);
	}

	/**
	 * 开始更多
	 */
	public void start() {
		butMore.setVisibility(View.GONE);
		progressFoot.setVisibility(View.VISIBLE);
	}

	/**
	 * 结束更多
	 */
	public void finish() {
		butMore.setVisibility(View.VISIBLE);
		progressFoot.setVisibility(View.GONE);
	}

	public interface OnMoreButtonListener extends OnClickListener {
	} // 更多按钮监听器

	private void onRefresh() {
		if (refreshListener != null) {
			refreshListener.onRefresh();
		}
	}

	// 当状态改变时候，调用该方法，以更新界面
	private void changeHeaderViewByState() {
		switch (state) {
		case RELEASE_To_REFRESH:
			imageArrow.setVisibility(View.VISIBLE);
			progressHead.setVisibility(View.GONE);
			imageArrow.clearAnimation();
			imageArrow.startAnimation(animation);
			break;
		case PULL_To_REFRESH:
			progressHead.setVisibility(View.GONE);
			imageArrow.clearAnimation();
			imageArrow.setVisibility(View.VISIBLE);
			// 是由RELEASE_To_REFRESH状态转变来的
			if (isBack) {
				isBack = false;
				imageArrow.clearAnimation();
				imageArrow.startAnimation(reverseAnimation);
			}
			break;
		case REFRESHING:
			headView.setPadding(0, 0, 0, 0);
			progressHead.setVisibility(View.VISIBLE);
			imageArrow.clearAnimation();
			imageArrow.setVisibility(View.GONE);
			break;
		case DONE:
			headView.setPadding(0, -1 * headContentHeight, 0, 0);
			progressHead.setVisibility(View.GONE);
			imageArrow.clearAnimation();
			imageArrow.setImageResource(R.drawable.pull_down_refresh_arrow);
			break;
		}
	}

	/**
	 * 测量头部长宽
	 */
	@SuppressWarnings("deprecation")
	private void measureView(View child) {
		ViewGroup.LayoutParams p = child.getLayoutParams();
		if (p == null) {
			p = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
		}
		int childWidthSpec = ViewGroup.getChildMeasureSpec(0, 0 + 0, p.width);
		int lpHeight = p.height;
		int childHeightSpec;
		if (lpHeight > 0) {
			childHeightSpec = MeasureSpec.makeMeasureSpec(lpHeight, MeasureSpec.EXACTLY);
		} else {
			childHeightSpec = MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED);
		}
		child.measure(childWidthSpec, childHeightSpec);
	}

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
		this.firstItemIndex = firstVisibleItem;
	}

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
	}

	public interface OnRefreshListener { // 刷新监听器
		public void onRefresh();
	}

	@Override
	public void setAdapter(ListAdapter adapter) {
		super.setAdapter(adapter);
	}
}
