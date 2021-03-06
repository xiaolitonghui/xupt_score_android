package com.xy.fy.singleton;

public class Message {
	private int msgId;
	private int collegeId;// 大学Id也是外键
	private int account;// 账号：这是外键
	private String nickname;// 昵称
	private String headPhoto;// 用户头像
	private int kind;// 消息种类
	private String text;// 文本信息
	private String larPic;// 小图片
	private String smaPic;// 图片
	private String time;// 时间,之所以弄成String类型是为了显示的方便
	private String date;// 日期,之所以弄成String类型是为了显示的方便
	private int praNum;// 赞个数
	private int comNum;// 评论个数
	private int colNum;// 收藏个数

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public String getHeadPhoto() {
		return headPhoto;
	}

	public void setHeadPhoto(String headPhoto) {
		this.headPhoto = headPhoto;
	}

	public int getMsgId() {
		return msgId;
	}

	public void setMsgId(int msgId) {
		this.msgId = msgId;
	}

	public int getCollegeId() {
		return collegeId;
	}

	public void setCollegeId(int collegeId) {
		this.collegeId = collegeId;
	}

	public int getAccount() {
		return account;
	}

	public void setAccount(int account) {
		this.account = account;
	}

	public int getKind() {
		return kind;
	}

	public void setKind(int kind) {
		this.kind = kind;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public String getLarPic() {
		return larPic;
	}

	public void setLarPic(String larPic) {
		this.larPic = larPic;
	}

	public String getSmaPic() {
		return smaPic;
	}

	public void setSmaPic(String smaPic) {
		this.smaPic = smaPic;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public int getPraNum() {
		return praNum;
	}

	public void setPraNum(int praNum) {
		this.praNum = praNum;
	}

	public int getComNum() {
		return comNum;
	}

	public void setComNum(int comNum) {
		this.comNum = comNum;
	}

	public int getColNum() {
		return colNum;
	}

	public void setColNum(int colNum) {
		this.colNum = colNum;
	}

	@Override
	public String toString() {
		return "Message [msgId=" + msgId + ", collegeId=" + collegeId
				+ ", account=" + account + ", nickname=" + nickname
				+ ", headPhoto=" + headPhoto + ", kind=" + kind + ", text="
				+ text + ", larPic=" + larPic + ", smaPic=" + smaPic
				+ ", time=" + time + ", date=" + date + ", praNum=" + praNum
				+ ", comNum=" + comNum + ", colNum=" + colNum + "]";
	}

}
