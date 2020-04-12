package com.sample.component;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import com.sample.entity.User;

@Component
public class TimeFilterLogic {



	public List<User> remnantTimeList(List<User> list){
		List<User> lists = new ArrayList<>();
		Date sqlDate = new Date(System.currentTimeMillis());

		for(int i = 0; i<list.size();i++) {
			lists.add(list.get(i));
		}
		return lists;
	}

	/*--- 1分は60,000ms、1時間は3,600,000ms、1日は86,400,000ms、365日は31,536,000,000ms ---*/
	/*--- int型は21億までの値しか格納できない為、時刻の計算は基本Long型を使用する ---*/

	public List<User> TimeFilter(List<User> list,boolean check1,boolean check2){

		List<User> lists = new ArrayList<>();
		Date sqlDate = new Date(System.currentTimeMillis());

		//---check1,check2が共にtrueの場合、listを全てlistsに格納する。
		if(check1 && check2) {  //---check1,check2が共にtrueの場合、listを全てlistsに格納する。
			for(int i = 0; i<list.size();i++) {
				lists.add(list.get(i));
			}
			//---check1のみtrueの場合、listのオブジェクトの残日数が30日未満のものをlistsに格納する。
			//---check1のみtrueでlistのオブジェクトの残日数が30日未満のものがない場合listsにnullを格納する。
		}else if(check1) {  //---check1のみtrueの場合、listのオブジェクトの残日数が30日未満のものをlistsに格納する。
			for(int i = 0; i<list.size();i++) {
				if(((Date.valueOf(list.get(i).getActive_to()).getTime()) - sqlDate.getTime()) < (86400000L * 31L) ) {
					lists.add(list.get(i));
				}else {//---check1のみtrueでlistのオブジェクトの残日数が30日未満のものがない場合listsにnullを格納する。
					lists=null;
				}
			}
			//---check2のみtrueの場合、listのオブジェクトの残日数が30日以上のものをlistsに格納する。
			//---check2のみtrueでlistのオブジェクトの残日数が30日以上のものがない場合listsにnullを格納する。
		}else if(check2) {  //---check2のみtrueの場合、listのオブジェクトの残日数が30日以上のものをlistsに格納する。
			for(int i = 0; i<list.size(); i++) {
				if((Date.valueOf(list.get(i).getActive_to()).getTime() - sqlDate.getTime()) > (86400000L * 31L) ) {
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



	//---ユーザー毎の有効期限までの残日時分を算出するメソッド
	public String remnantTime(List<User> lists) {

		Date sqlDate = new Date(System.currentTimeMillis());

		//残り日付の算出方法
		Long day = ((Date.valueOf(lists.get(0).getActive_to()).getTime()) - sqlDate.getTime()) / 86400000 ;
		Long hour = ((Date.valueOf(lists.get(0).getActive_to()).getTime()) - sqlDate.getTime()) % 86400000 / 3600000;
		Long min = ((Date.valueOf(lists.get(0).getActive_to()).getTime()) - sqlDate.getTime()) % 86400000 % 3600000/60000;

		return "残有効期限："+day+"日"+hour+"時"+min+"分";

	}

}
