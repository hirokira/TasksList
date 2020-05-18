package com.sample.component;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import com.sample.entity.Task;
import com.sample.entity.User;

@Component
public class TimeFilterLogic {

	//---残り日数をms(Long型)で返すメソッド(User)
	public Long remnantMs(User user) {
		Date sqlDate = new Date(System.currentTimeMillis());
		Long ms = ((Date.valueOf(user.getActive_to()).getTime()) - sqlDate.getTime());
		return ms;
	}

	//---残り日数をms(Long型)で返すメソッド(Task)
	public Long remnantMs(Task task) {
		Date sqlDate = new Date(System.currentTimeMillis());
		Long ms = ((Date.valueOf(task.getDue_date()).getTime()) - sqlDate.getTime());
		return ms;
	}

	/*--- 1分は60,000ms、1時間は3,600,000ms、1日は86,400,000ms、365日は31,536,000,000ms ---*/
	/*--- int型は21億までの値しか格納できない為、時刻の計算は基本Long型を使用する ---*/

	public List<User> TimeFilter(List<User> list,boolean check1,boolean check2){

		List<User> lists = new ArrayList<>();
		//---check1,check2が共にtrueの場合、listを全てlistsに格納する。
		if(check1 && check2) {  //---check1,check2が共にtrueの場合、listを全てlistsに格納する。
			for(int i = 0; i<list.size();i++) {
				lists.add(list.get(i));
			}
			//---check1のみtrueの場合、listのオブジェクトの残日数が"0日以上30日未満"のものをlistsに格納する。
		}else if(check1) {  //---check1のみtrueの場合、listのオブジェクトの残日数が"0日以上30日未満"のものをlistsに格納する。
			for(int i = 0; i<list.size();i++) {
				if(0<=remnantMs(list.get(i)) && remnantMs(list.get(i))< (86400000L * 31L) ) {
					lists.add(list.get(i));
				}else {//---check1のみtrueでlistのオブジェクトの残日数が"0日以上30日未満"のものがない場合listsにnullを格納する。
					lists=null;
				}
			}
			//---check2のみtrueの場合、listのオブジェクトの残日数が30日以上のものをlistsに格納する。
		}else if(check2) {  //---check2のみtrueの場合、listのオブジェクトの残日数が30日以上のものをlistsに格納する。
			for(int i = 0; i<list.size(); i++) {
				if(remnantMs(list.get(i)) > (86400000L * 31L) ) {
					lists.add(list.get(i));
				}else {//---check2のみtrueでlistのオブジェクトの残日数が30日以上のものがない場合listsにnullを格納する。
					lists = null;
				}
			}
		}else { //それ以外の場合(check1,check2が共にfalse)
			lists = null;
		}
		return lists;
	}

	//---ToDo(5/7追加):24時間以内、完了パターンを作成する。
	/*--check1(通常)
	 * check2(7日以内)
	 * check3(24時間以内)
	 * check4(完了)
	 */
	public List<Task> timeFilter(List<Task> list,boolean check1,boolean check2,boolean check3,boolean check4){
		List<Task> lists = new ArrayList<>();
		//---check1がtrueの場合、listを全てlistsに格納する。
		if(check1) {  //---check1がtrueの場合、listを全てlistsに格納する。
			for(int i = 0; i<list.size();i++) {
				lists.add(list.get(i));
			}
		//---check2のみtrueの場合、listのオブジェクトの残日数が"0日以上7日未満"のものをlistsに格納する。
		}else if(check2) {  //---check2のみtrueの場合、listのオブジェクトの残日数が"0日以上7日未満"のものをlistsに格納する。
			for(int i = 0; i<list.size();i++) {
				if(0<=(remnantMs(list.get(i)))&& (remnantMs(list.get(i))) < (86400000L * 7L) ) {
					lists.add(list.get(i));
				}
			}
		//---check3のみtrueの場合、listのオブジェクトの残日数が24時間以内のものをlistsに格納する。
		}else if(check3) {  //---check2のみtrueの場合、listのオブジェクトの残日数が24時間以内のものをlistsに格納する。
			for(int i = 0; i<list.size(); i++) {
				if(remnantMs(list.get(i)) <= 86400000L) {
					lists.add(list.get(i));
				}
			}
		}else if(check4) {  //---check4のみtrueの場合、listのオブジェクトが完了のものをlistsに格納する。
			for(int i = 0;i<list.size();i++) {
				if(!list.get(i).getCompletion_date().equals("")) {
					lists.add(list.get(i));
				}
			}
		}else { //全部のflagがfalseの場合、nullを格納する。
			lists = null;
		}
		//フィルターをかけたが1件もHitしなかた場合、nullを格納する。
		if(lists.size()==0) {lists=null;}
		return lists;
	}

	//---ユーザー毎の有効期限までの残日時分を算出するメソッド
	public String remnantTimeUser(User user) {
		//残り日付の算出方法
		Long day = remnantMs(user) / 86400000 ;
		Long hour = remnantMs(user) % 86400000 / 3600000;
		Long min = remnantMs(user) % 86400000 % 3600000/60000;
		if(day<=0 && hour<0 && min<0) {
			return "期限切れ";
		}else {
			return "残有効期限："+day+"日"+hour+"時"+min+"分";
		}
	}

	//---タスク毎の有効期限までの残日時分を算出するメソッド
	public String remnantTimeTask(Task task) {
		//残り日付の算出方法
		Long day = remnantMs(task) / 86400000 ;
		Long hour = remnantMs(task) % 86400000 / 3600000;
		Long min = remnantMs(task) % 86400000 % 3600000/60000;
		if(day<=0 && hour<0 && min<0) {
			return "期限切れ";
		}else {
			return "残有効期限："+day+"日"+hour+"時"+min+"分";
		}
	}

}
