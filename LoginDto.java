package jp.co.internous.ocean.model.domain.dto;

import jp.co.internous.ocean.model.domain.MstUser;

public class LoginDto {
	
	private int userId;
	private String userName;
	private String password;
	
	
	public LoginDto() {}
	
	public LoginDto(MstUser user) {
		this.userId = user.getId();
		this.userName = user.getUserName();
		this.password = user.getPassword();
	}
	
	public LoginDto(int id, String userName, String password) {
		this.userId = id;
		this.userName = userName;
		this.password = password;
	}
	
	public int getUserId() {
		return userId;
	}
	public void setUserId(int userId) {
		this.userId = userId;
	}
	
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	
	public String getPassword() {
		return password;
	}	
	public void setPassword(String password) {
		this.password = password;
	}
	
	
}