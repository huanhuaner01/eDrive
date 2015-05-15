package com.huishen.edrive.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.util.Log;

import java.util.ArrayList;

/**
 * @author zhanghuan
 * @create 2015-4-8
 */
public final class MeaasgeDbManager {

    private static final String LOG_TAG = "DbManager";

    private MessageDbHelper helper;
    private SQLiteDatabase db;


    public MeaasgeDbManager(Context context) {
        helper = new MessageDbHelper(context);
        try {
            db = helper.getWritableDatabase();
        } catch (SQLiteException e) {
            Log.e(LOG_TAG, "Cannot get sqlite database." + e.getMessage());
            e.printStackTrace();
        }
    }


    public ArrayList<AppMessage> getAllMessage() {
//        Cursor cursor = db.query(MessageDbHelper.TABLE_MESSAGE, projection, null, null, null,
//                null, MessageDbHelper.COLUMN_ID);
        Cursor cursor = db.query(MessageDbHelper.TABLE_MESSAGE, null, null, null, null,
              null, "_id desc");
        ArrayList<AppMessage> list = new ArrayList<AppMessage>();
        while (cursor.moveToNext()) {
            list.add(parseValues(cursor));
        }
        return list;
    }
    /**
     * 根据id查询详细信息
     * @param id
     * @return
     */
    public AppMessage queryMessage(int id){
    	AppMessage msg = new AppMessage();
    	String select = "_id=?" ;
    	String[] selectionArgs={id+""};//具体的条件,注意要对应条件字段  
    	Cursor cursor = db.query(MessageDbHelper.TABLE_MESSAGE, null, select, selectionArgs, null,
                null, null);
    	 while (cursor.moveToNext()) {
    		 msg = parseValues(cursor);
         }
    	return msg ;
    }
    /**
     * @return 插入成功则返回true，并且参数中的对象的Id字段会被设置为在数据库中的ID值;否则返回false。
     * @see IMsgStorage#saveAppMessage(AppMessage)
     */
    public boolean saveAppMessage(AppMessage msg) {
    	
        if (msg == null) {
            return false;
        }
        Log.i(LOG_TAG, "msg is "+msg.toString());
        long id = db.insert(MessageDbHelper.TABLE_MESSAGE, null, getContentValues(msg));
        if (id != -1) {
            msg.setId(id);
            return true;
        } else {
            return false;
        }
    }
    
    /**
     * 清空数据表
     */
    public void clearAllMessages() {
        db.delete(MessageDbHelper.TABLE_MESSAGE, null, null);
    }
    
    /**
     * 删除_id为id的数据
     */
    public void deleteMessages(int id ) {
        db.delete(MessageDbHelper.TABLE_MESSAGE, "_id = ?", new String[]{id+""});
    }
    /**
     * 将消息bean转换为ContentValues进行数据插入
     * @param msg
     * @return
     */
    private ContentValues getContentValues(AppMessage msg) {
    	Log.i(LOG_TAG, msg.toString());
        ContentValues cv = new ContentValues();
        cv.put(MessageDbHelper.COLUMN_MSG_CONTENT, msg.getContent());
        cv.put(MessageDbHelper.COLUMN_MSG_TIME, msg.getTime());
        cv.put(MessageDbHelper.COLUMN_MSG_TYPE, msg.getType()); //存储的是从0开始的序号, int类型
        cv.put(MessageDbHelper.COLUMN_MSG_ICONPATH, msg.getIconPath());
        cv.put(MessageDbHelper.COLUMN_MSG_TITLE, msg.getTitle());
        return cv;
    }
    
    /**
     * 将数据库中的游标数据转换为消息bean
     * @param cursor
     * @return
     */
    private AppMessage parseValues(Cursor cursor) {
        AppMessage msg = new AppMessage();
        msg.setId(cursor.getLong(cursor.getColumnIndex(MessageDbHelper.COLUMN_ID)));
        msg.setContent(cursor.getString(cursor.getColumnIndex(MessageDbHelper.COLUMN_MSG_CONTENT)));
        msg.setIconPath(cursor.getString(cursor.getColumnIndex(MessageDbHelper.COLUMN_MSG_ICONPATH)));
        msg.setTime(cursor.getString(cursor.getColumnIndex(MessageDbHelper.COLUMN_MSG_TIME)));
        msg.setType(cursor.getInt(cursor.getColumnIndex(MessageDbHelper.COLUMN_MSG_TYPE)));
        msg.setTitle(cursor.getString(cursor.getColumnIndex(MessageDbHelper.COLUMN_MSG_TITLE)));
//        msg.sett(cursor.getInt(cursor.getColumnIndex(MessageDbHelper.COLUMN_MSG_TYPE)));
       // msg.setTime(cursor.getLong(cursor.getColumnIndex(COLUMN_MSG_TIME)));
       // msg.setType(MessageType.values()[cursor.getInt(cursor.getColumnIndex(COLUMN_MSG_TYPE))]);//还原枚举
        return msg;
    }
}
