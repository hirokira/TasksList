package com.sample.service;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.stereotype.Service;

import com.sample.bean.UserBean;
import com.sample.component.TimeFilterLogic;
import com.sample.entity.User;
import com.sample.mapper.UserMapper;

@Service
public class UserService {

	@Autowired
	private UserMapper userMapper;

	@Autowired
	private TimeFilterLogic timeFilter;

	//---全てのユーザーを表示
	public List<User> findAll(){
		return userMapper.findAll();
	}

	//---全てのユーザーを表示(Bean化)
	public List<UserBean> findAllBean(){
		List<User> list = userMapper.findAll();
		List<UserBean> listBean = new ArrayList<>();

		if(list!=null) {
			for(int i = 0;i<list.size(); i++) {
				listBean.add(new UserBean(list.get(i)));
				listBean.get(i).setRemnant_date(timeFilter.remnantTimeUser(list.get(i)));
			}
		}
		return listBean;
	}

	//---新規ユーザー登録
	public void insert(User user) {
		userMapper.insert(user);
	}

	//---ユーザー情報アップデート 2020/06/08 add 楽観ロック処理とModelMapperを使用したマッピング追記。
	public void update(User userInfo) {
		User target = userMapper.select(userInfo.getId());

		//ここで表示したときのversionと、現在のversionを比較
		if(target.getVersion() != userInfo.getVersion()) {
			throw new OptimisticLockingFailureException(null);
		}else {
			//更新内容をマッピングする処理を行う。
			ModelMapper modelMapper = new ModelMapper();
			target = modelMapper.map(userInfo, User.class);

			//更新処理を行う。
			userMapper.update(target);

		}
	}

	//---選択したIDのユーザー情報取得
	public User select(int id) {
		return userMapper.select(id);
	}

	//---選択したIDのユーザー情報をUserBeanにラッピングして取得
	public UserBean selectBean(int id) {
		UserBean bean = new UserBean(userMapper.select(id));
		return bean;
	}

	//---選択したNAMEのユーザー情報一覧を取得
	public List<User> selectName(String name) {
		return userMapper.selectName(name);
	}

	//---選択したNAMEのユーザー情報一覧を抽出。(Nameが空白("")だった場合はnullを返す。
	/*public List<User> selectDate(String name ,boolean check1,boolean check2) {
		List<User> list = new ArrayList<>();
		List<User> lists = new ArrayList<>();

		if(!name.equals("")) {  //---nameが空白("")以外のみ、以下の処理を行う。
			list = userMapper.selectNameOnly(name);  //---選択したNameのユーザー情報一覧抽出。
			lists = timeFilter.TimeFilter(list, check1, check2);//---check1,check2のFlagに法って、有効期限までの日数が該当するものを抽出。
		}else {
			lists = null;
		}
		return lists;
	}
*/
	//---選択したNAMEのユーザー情報一覧を抽出。(Nameが空白("")だった場合はnullを返す。
	public List<UserBean> selectDateBean(String name ,boolean check1,boolean check2) {
		List<User> list = new ArrayList<>();
		List<User> lists = new ArrayList<>();
		List<UserBean> listBean = new ArrayList<>();

		if(!name.equals("")) {  //---nameが空白("")以外のみ、以下の処理を行う。
			list = userMapper.selectNameOnly(name);  //---選択したNameのユーザー情報一覧抽出。
			lists = timeFilter.TimeFilter(list, check1, check2);//---check1,check2のFlagに法って、有効期限までの日数が該当するものを抽出。
			if(lists!=null) {
				for(int i = 0;i<lists.size(); i++) {
					listBean.add(new UserBean(lists.get(i)));
					listBean.get(i).setRemnant_date(timeFilter.remnantTimeUser(lists.get(i)));
				}
			}
		}else {
			listBean = null;
		}
		return listBean;
	}

	//---選択したIDのユーザー情報を削除
	public void delete(int id) {
		userMapper.delete(id);
	}


	//---ログインユーザー、パスワードチェック
	public User checkResult(String name,String password) {
		return userMapper.checkResult(name,password);
	}

	//---ID登録済みチェック
	public boolean checkIdResult(int id) {
		if(userMapper.checkIdResult(id)==null) {
			return false;
		}else {
			return true;
		}
	}

	//---Name登録済みチェック
	public boolean checkNameResult(String name) {
		if(userMapper.checkNameResult(name)==null) {
			return false;
		}else {
			return true;
		}
	}


	//---UserBean　→　Userに変換ロジック
	public User changeUser(UserBean bean) {
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		//DateFormat df2 = new SimpleDateFormat("yyyy-MM-dd hh:mm");
		User user = new User();
		user.setId(Integer.parseInt(bean.getId()));
		user.setName(bean.getName());
		if(!bean.getNickName().equals("")) {
			user.setNickName(bean.getNickName());
		}else {
			user.setNickName(bean.getName());
		}
		user.setPassword(bean.getPassword());
		user.setActive_from(df.format(bean.getActive_from()));
		user.setActive_to(df.format(bean.getActive_to()));

		//----2020/04/25 add
		user.setUpdate_user(bean.getUpdate_user());
		user.setUpdate_date(df.format(bean.getUpdate_date()));
		user.setInsert_user(bean.getInsert_user());
		user.setInsert_date(df.format(bean.getInsert_date()));

		//---2020/06/08 add 楽観ロック実装の為、バージョン追加。
		user.setVersion(bean.getVersion());
		return user;
	}

}
