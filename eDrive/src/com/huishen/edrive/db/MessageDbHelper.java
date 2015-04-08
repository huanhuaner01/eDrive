package com.huishen.edrive.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * @author zhanghuan
 * @create 2015-4-8
 */
public final class MessageDbHelper extends SQLiteOpenHelper {

	private static final String LOG_TAG = "DbHelper";
	
	protected static final String DATABASE_NAME = "edrive.db";
	private static final int DATABASE_VERSION = 1;
    
	 protected static final String TABLE_MESSAGE = "t_appmsg";        //消息表
	 protected static final String COLUMN_ID = "_id";                //主键
	 protected static final String COLUMN_MSG_TIME = "time";            //时间列
	 protected static final String COLUMN_MSG_CONTENT = "content";    //内容列
	 protected static final String COLUMN_MSG_TITLE = "title";        //标题列
	 protected static final String COLUMN_MSG_ICONPATH = "iconpath";    //消息图片路径
	 protected static final String COLUMN_MSG_TYPE = "msgtype";        //类型列
	
	public MessageDbHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	public MessageDbHelper(Context context, String dbname) {
		super(context, dbname, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		String sqlmsg = buildMessageTable();
		Log.d(LOG_TAG, sqlmsg);
		try {
			db.beginTransaction();
			db.execSQL(sqlmsg);	
			db.setTransactionSuccessful();
			db.endTransaction();
		} catch (SQLiteException e) {
			Log.e(LOG_TAG, "Cannot create tables.");
			e.printStackTrace();
		}
	}

	private final String buildMessageTable(){
		StringBuilder sb = new StringBuilder();
		sb.append("create table IF NOT EXISTS ").append(TABLE_MESSAGE)
				.append("(")
				.append(COLUMN_ID).append(" integer primary key autoincrement, ")
				.append(COLUMN_MSG_TIME).append(" varchar, ")
				.append(COLUMN_MSG_TITLE).append(" varchar, ")
				.append(COLUMN_MSG_TYPE).append(" integer, ")
				.append(COLUMN_MSG_CONTENT).append(" varchar, ")
				.append(COLUMN_MSG_ICONPATH).append(" varchar")
				.append(")");
		return sb.toString();
	}

	@Override
	public void onUpgrade(SQLiteDatabase arg0, int arg1, int arg2) {
		// current version:empty
	}

}
