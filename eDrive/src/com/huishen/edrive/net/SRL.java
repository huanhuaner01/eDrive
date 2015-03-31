package com.huishen.edrive.net;

/**
 * 该类中的SRL是Server Resource Location 的缩写，表示资源在服务器上的相对位置，为方便客户端代码调用故使用SRL缩写。
 * 该类中只存放常量，不提供其他任何方法实现。
 * <p>
 * 在实际编程中，建议使用 {@link #RESULT_OK}字段进行返回值测试，而不是硬编码其数值。
 * </p>
 * 
 * @author Muyangmin
 * @create 2015-2-7
 */
public final class SRL {
	//请求方法
	public static final class Method{
		
		//---------------------------------------学员登录注册-------------------------------------------------
	    
		/**
		 * 通过短信获取验证码。 参数 : {@link #PARAM_MOBILE_NUMBER} 返回值:{status:0|1|2} 0 电话号码格式错误 1 发送成功  2 发送异常
		 */
		public static final String METHOD_GET_VERIFY_CODE = "stuMobile/sendStuSM";
		
//		手机短信验证 电话是否存在，不存在就注册 没密码直接登陆 有密码填写密码后登陆:stuMobile/stuMobLoginSubmit
	//
//		 1 注册登陆成功
//		 0 账号停用
//		 2 密码不为空 必须输入密码才能登陆
//		 1 登陆成功 
//		 -2 验证码错误
//		 -1 验证码过期
	//
	//
//		验证密码:stuMobile/stuMobLoginPassWordSubmit
	//
//		 0 密码错误
//		 1 登陆成功
//		 -1验证码过期
		/**
		 * 注册登录  参数:phone :18388888888,vcodes=1234  返回值:{status:1|0|2|-1|-2} 0: 账号停用，1:注册登陆成功 2 密码不为空 必须输入密码才能登陆 -2 验证码错误 -1 验证码过期
		 */
		public static final String METHOD_LOGIN = "stuMobile/stuMobLoginSubmit";
		public static final String METHOD_SEND_DEVICETOKEN = "/stuMobile/updateDeviceToken" ;
		/**
		 * 隐形登录  参数：phone:18388888888,MobileFlag:123456 返回值：{未开发接口}
		 */
		public static final String METHOD_LOGIN_PRI = "stuMobile/getCurrLoginStudentInfo" ;
//		/**
//		 * 验证手机号是否存在。 参数:mob=18782920468 返回值:{code:0|1} 0:不存在，1:存在
//		 */
//		public static final String METHOD_VERIFY_IF_NUMBER_EXIST = "/adr/vcohMob";

		// ---------------------------------学员登录注册-------------------------------------------
		
		//---------------------------------------教练详情-------------------------------------------------
		
	
		/**
		 * 根据自己的地理位置，获取周边教练的地理信息
		 */
		public static final String METHOD_GET_ROUND_COACH ="stuMobile/queryDistanceCoachList" ;
		
		/**
		 * 教练详情主页，获取基本信息
		 */
		public static final String METHOD_GET_COACH_DETAIL="stuMobile/queryCoachById" ;
		
		/**
		 * 获取教练个人基本信息（教龄 ，驾校，地区，感言）
		 */
		public static final String METHOD_GET_COACH_INFO = "stuMobile/queryCoachInfoById" ;
		public static final String METHOD_GET_COACH_JUDGE_LIST = "stuMobile/queryCommentInfo"  ;
		public static final String METHOD_GET_COACH_FIELDPIC="stuMobile/queryCoachPicture";
		public static final String METHOD_GET_COACH_FIELD = "stuMobile/queryCoachCampus" ;
		public static final String METHOD_GET_COACH_MEAL = "stuMobile/queryServicebillInfoByCoachID" ;
		
		//------------------------------发布需求----------------------------
		public static final String METHOD_SEND_TXT_ORDER = "stuMobile/publishOrder" ;
		public static final String METHOD_SET_ADDR = "stuMobile/updateStudentAddressInfo" ;
		public static final String METHOD_GET_SUCCESS_ORDER = "stuMobile/queryCoachInfo" ;
		public static final String METHOD_GET_SERVICE_INFO ="stuMobile/queryTermOfService" ;
		public static final String METHOD_SEND_SOUND_ORDER = "stuMobile/uploadAudio" ;
		public static final String METHOD_RESEND_ORDER = "stuMobile/rePublish" ;
		//------------------------------发布需求结束！----------------------------
		
		//-------------------------------绑定教练---------------------------------
		public static final String METHOD_BIND_COACH = "stuMobile/addStudentEnrollInfo" ;
		//-------------------------------绑定教练结束！---------------------------------
		
		//-------------------------------解除绑定教练---------------------------------
		public static final String METHOD_UNBIND_COACH = "stuMobile/updateStuEnrollInfoStatus" ;
		//-------------------------------解除绑定教练结束！---------------------------------
		
		//-------------------------预约学车----------------------------------------
		public static final String METHOD_GET_APPOINT = "stuMobile/queryStuCoachLessonInfo" ;
		public static final String METHOD_GET_SUBJECT = "stuMobile/queryCohLessonInfoCount" ;
		public static final String METHOD_SEND_APPOINT = "stuMobile/addLessonInfo" ;
		public static final String METHOD_CANCEL_APPOINT = "stuMobile/editLesssonInfoStatus" ;
		//-------------------------预约学车结束！-------------------------------------
		
		//---------------------------个人中心-------------------------------------
		public static final String METHOD_GET_CENTER_INFO = "stuMobile/queryStuInfoById" ;
		public static final String METHOD_GET_CENTER_USERINFO = "stuMobile/queryStuinfo" ;
		public static final String METHOD_UPLOAD_PHOTO = "stuMobile/updateStuImg" ;
		public static final String METHOD_EDIT_USERINFO = "stuMobile/updateStuInfo" ;
		public static final String METHOD_GET_MSG_LIST = "stuMobile/queryStuAllPushLogInfo" ;
		public static final String METHOD_GET_ORDER_LIST = "stuMobile/queryTempBillInfoByStuId" ;
		public static final String METHOD_GET_ORDER_DETAIL = "stuMobile/queryTempBillInfoById";
		public static final String METHOD_CANCEL_ORDER = "stuMobile/updateTempBill" ;
		public static final String METHOD_JUDGE = "stuMobile/addComment" ;
		public static final String METHOD_PLUS_JUDGE = "stuMobile/addCommentPlusInfo" ;
		public static final String METHOD_COMPLAIN = "/stuMobile/addComplaintInfo" ;
		public static final String METHOD_ABOUT = "/static/services/aboutUs.html" ;
		public static final String METHOD_OPINION ="/stuMobile/feedback";
		//---------------------------个人中心结束！---------------------------------
		
		//---------------------------侧边栏学车指南-----------------------------------
		public static final String METHOD_GET_MENU_PRO = "/static/services/Fees.html";
		public static final String METHOD_GET_MENU_INFO = "/static/services/Test.html";
		public static final String METHOD_GET_MENU_HOSPITAL = "/static/services/medicalHospital.html";
		public static final String METHOD_GET_MENU_STANDARD = "/static/services/toll.html";
		//---------------------------侧边栏学车指南结束！-------------------------------
		
		/**
		 * 检查软件更新。
		 * 
		 */
		public static final String METHOD_CHECK_UPDATE = "/queryVersion";
		
	}
	//请求参数
	public static final class Param{

		public static final String PARAM_MOBILE_FLAG = "mobileFlag";
		
		/**
		 * 经度
		 */
		public static final String PARAM_LONGITUDE = "lng" ;
		
		/**
		 * 纬度
		 */
		public static final String PARAM_LATITUDE ="lat" ;
//		
//		stuId=1;//学生ID
//		stuRealName=王成;//直接姓名
//		content=包接包送，对学生友好;//订单需求内容
//		lng=104.065656;//当前学生经度
//		lat=30.577716;//当前学生纬度
//		region=成都武候区;//所在区
		public static final String PARAM_STUID = "stuId" ;
		public static final String PARAM_STUREALNAME = "stuRealName" ;
		public static final String PARAM_CONTENT = "content" ;
		public static final String PARAM_STUADDR = "stuAddr" ;
		public static final String PARAM_REGION = "region" ;

		public static final String PARAM_BIND_COACH_PHONE = "coachPhone" ;
		public static final String PARAM_BIND_STU_NAME = "stuRealName" ;
		public static final String PARAM_BIND_CONTENT = "content" ;

		public static final String PARAM_UPDATE_SOFTKEY = "softKey";
	}
		//返回码
	public static final class ReturnCode{
		
	}
	//返回字段
	public static final class ReturnField{
		/**
		 * 获取周边教练信息相关返回值
		 */
		public static final String FIELD_COACH_ID = "coachId" ;
		public static final String FIELD_COACH_NAME = "coachName" ;
		public static final String FIELD_COACH_JUDGE_SCORE = "coachScore" ;
		public static final String FIELD_COACH_PHOTO_PATH ="path" ;
		
		/**
		 * 获取教练训练场地址返回值
		 */
		public static final String FIELD_GPSADDR = "GPSAddress" ;
		public static final String FIELD_ADDR ="address" ;
		public static final String FIELD_SCHOOL = "title" ;
		
		/**
		 * 获取教练套餐信息
		 */
		public static final String COACH_MEAL_TITLE = "title" ;
		public static final String COACH_MEAL_PRIZE = "cash" ;
		public static final String COACH_MEAL_CONTENT = "content" ;
		public static final String COACH_MEAL_CARTYPE ="carTypeContent" ;
		public static final String COACH_MEAL_LICENSETYPE = "licenseType" ;
		public static final String COACH_MEAL_CLASSTYPE = "classType" ;
		/**
		 * 检查版本更新时返回，安装包下载路径。
		 */
		public static final String FIELD_UPDATE_APK_PATH = "path";
		/**
		 * 检查版本更新时返回，版本号。
		 */
		public static final String FIELD_UPDATE_SERVER_VERSIONCODE = "version";
		/**
		 * 检查版本更新时返回，是否强制更新。
		 */
		public static final String FIELD_UPDATE_FORCE_UPDATE = "req";
	}
	

	/**
	 * 通用返回值，代表成功信息。
	 */
	public static final int RESULT_OK = 0;
	/**
	 * 通用返回参数，代表请求结果。
	 */
	protected static final String RESULT_KEY_CODE = "code";
	/**
	 * 上传文件、图片等时服务器返回的代表相对位置的Key。
	 */
	public static final String RESULT_KEY_URL = "url";

}
