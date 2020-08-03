package jp.co.internous.ocean.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.google.gson.Gson;

import jp.co.internous.ocean.model.domain.MstUser;
import jp.co.internous.ocean.model.domain.dto.LoginDto;
import jp.co.internous.ocean.model.form.UserForm;
import jp.co.internous.ocean.model.mapper.MstUserMapper;
import jp.co.internous.ocean.model.mapper.TblCartMapper;
import jp.co.internous.ocean.model.session.LoginSession;

@RestController
@RequestMapping("/ocean/auth")
public class AuthController {

	@Autowired
	private MstUserMapper userMapper;
	
	@Autowired
	private TblCartMapper cartMapper;
	
	@Autowired
	protected LoginSession loginSession;
	
	private Gson gson = new Gson();
	
	
	@ResponseBody
	@RequestMapping("/login")
	public String login(@RequestBody UserForm form) {
		List<MstUser> user = userMapper.findByUserNameAndPassword(form.getUserName(), form.getPassword());

		
		int tmpUserId = loginSession.getTmpUserId();
		if (user.size() > 0 && tmpUserId != 0) {
			cartMapper.updateUserId(user.get(0).getId(), loginSession.getTmpUserId());
		}
		
		
		LoginDto dto = new LoginDto(0, null, null);
		
		if(user.size() > 0) {
	
			dto = new LoginDto(user.get(0));
			
			loginSession.setUserId(user.get(0).getId());
			loginSession.setTmpUserId(0);
			loginSession.setUserName(user.get(0).getUserName());
			loginSession.setPassword(user.get(0).getPassword());
			loginSession.setLogined(true);
		} else {
			loginSession.setUserId(0);			
			loginSession.setUserName(null);
			loginSession.setPassword(null);
			loginSession.setLogined(false);
		}
		return gson.toJson(dto);
	}
	
	
	
	@RequestMapping("/logout")
	public String logout() {
		loginSession.setUserId(0);
		loginSession.setTmpUserId(0);
		loginSession.setUserName(null);
		loginSession.setPassword(null);
		loginSession.setLogined(false);
	  
	  
		return "";
	}
	
	
	
	@RequestMapping("/resetPassword")
	public String resetPassword(@RequestBody UserForm form) {
		String message = "パスワードが再設定されました。";
		String newPassword = form.getNewPassword();
		String newPasswordConfirm = form.getNewPasswordConfirm();
		
		List<MstUser> user = userMapper.findByUserNameAndPassword(loginSession.getUserName(), form.getPassword());
		if (user.size() == 0) {
			return "現在のパスワードが正しくありません。";
		}
		
		if (user.get(0).getPassword().equals(newPassword)) {
			return "現在のパスワードと同一文字列が入力されました。";
		}
		
		if (!newPassword.equals(newPasswordConfirm)) {
			return "新パスワードと確認用パスワードが一致しません。";
		}
		
		userMapper.update(form.getNewPassword(), user.get(0).getId());
		loginSession.setPassword(form.getNewPassword());
		
		return message;
	}
	
}