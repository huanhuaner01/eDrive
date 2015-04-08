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
        String[] projection = new String[]{MessageDbHelper.COLUMN_ID, MessageDbHelper.COLUMN_MSG_CONTENT,
        		MessageDbHelper.COLUMN_MSG_TIME,MessageDbHelper.COLUMN_MSG_ICONPATH ,MessageDbHelper.COLUMN_MSG_TIME,
        		MessageDbHelper.COLUMN_MSG_TYPE};
        Cursor cursor = db.query(MessageDbHelper.TABLE_MESSAGE, projection, null, null, null,
                null, MessageDbHelper.COLUMN_ID);
        ArrayList<AppMessage> list = new ArrayList<AppMessage>();
        while (cursor.moveToNext()) {
            list.add(parseValues(cursor));
        }
        return list;
    }

    /**
     * @return 插入成功则返回true，并且参数中的对象的Id字段会被设置为在数据库中的ID值;否则返回false。
     * @see IMsgStorage#saveAppMessage(AppMessage)
     */
    public boolean saveAppMessage(AppMessage msg) {
        if (msg == null) {
            return false;
        }
        long id = db.insert(MessageDbHelper.TABLE_MESSAGE, null, getContentValues(msg));
        if (id != -1) {
            msg.setId(id);
            return true;
        } else {
            return false;
        }
    }
    public void clearAllMessages() {
        db.delete(MessageDbHelper.TABLE_MESSAGE, null, null);
    }

    private ContentValues getContentValues(AppMessage msg) {
        ContentValues cv = new ContentValues();
        cv.put(MessageDbHelper.COLUMN_MSG_CONTENT, msg.getContent());
        cv.put(MessageDbHelper.COLUMN_MSG_TIME, msg.getTime());
        cv.put(MessageDbHelper.COLUMN_MSG_TYPE, msg.getType()); //存储的是从0开始的序号, int类型
        cv.put(MessageDbHelper.COLUMN_MSG_ICONPATH, msg.getIconPath());
        cv.put(MessageDbHelper.COLUMN_MSG_TITLE, msg.getTime());
        return cv;
    }

    private AppMessage parseValues(Cursor cursor) {
        AppMessage msg = new AppMessage();
        msg.setId(cursor.getLong(cursor.getColumnIndex(MessageDbHelper.COLUMN_ID)));
        msg.setContent(cursor.getString(cursor.getColumnIndex(MessageDbHelper.COLUMN_MSG_CONTENT)));
        msg.setIconPath(cursor.getString(cursor.getColumnIndex(MessageDbHelper.COLUMN_MSG_ICONPATH)));
        msg.setTime(cursor.getString(cursor.getColumnIndex(MessageDbHelper.COLUMN_MSG_TIME)));
        msg.setType(cursor.getInt(cursor.getColumnIndex(MessageDbHelper.COLUMN_MSG_TYPE)));
//        msg.sett(cursor.getInt(cursor.getColumnIndex(MessageDbHelper.COLUMN_MSG_TYPE)));
       // msg.setTime(cursor.getLong(cursor.getColumnIndex(COLUMN_MSG_TIME)));
       // msg.setType(MessageType.values()[cursor.getInt(cursor.getColumnIndex(COLUMN_MSG_TYPE))]);//还原枚举
        return msg;
    }
}
