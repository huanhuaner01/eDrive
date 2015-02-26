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
	
	//---------------------------------------学员登录注册-------------------------------------------------
    
	/**
	 * 通过短信获取验证码。 参数 : {@link #PARAM_MOBILE_NUMBER} 返回值:{status:0|1|2} 0 电话号码格式错误 1 发送成功  2 发送异常
	 */
	public static final String METHOD_GET_VERIFY_CODE = "stuMobile/sendStuSM";
	
//	手机短信验证 电话是否存在，不存在就注册 没密码直接登陆 有密码填写密码后登陆:stuMobile/stuMobLoginSubmit
//
//	 1 注册登陆成功
//	 0 账号停用
//	 2 密码不为空 必须输入密码才能登陆
//	 1 登陆成功 
//	 -2 验证码错误
//	 -1 验证码过期
//
//
//	验证密码:stuMobile/stuMobLoginPassWordSubmit
//
//	 0 密码错误
//	 1 登陆成功
//	 -1验证码过期
	/**
	 * 注册登录  参数:phone :18388888888,vcodes=1234  返回值:{status:1|0|2|-1|-2} 0: 账号停用，1:注册登陆成功 2 密码不为空 必须输入密码才能登陆 -2 验证码错误 -1 验证码过期
	 */
	public static final String METHOD_LOGIN = "stuMobile/stuMobLoginSubmit";
	
	/**
	 * 隐形登录  参数：phone:18388888888,MobileFlag:123456 返回值：{未开发接口}
	 */
	public static final String METHOD_LOGIN_PRI = "" ;
//	/**
//	 * 验证手机号是否存在。 参数:mob=18782920468 返回值:{code:0|1} 0:不存在，1:存在
//	 */
//	public static final String METHOD_VERIFY_IF_NUMBER_EXIST = "/adr/vcohMob";

	// ---------------------------------学员登录注册-------------------------------------------
	
	//---------------------------------------教练详情-------------------------------------------------
	/**
	 * 通过学员对教练的评价。 参数 : {@link #PARAM_MOBILE_NUMBER} 返回值:{code:0|1}0:发送成功，1:发送异常
	 */
	public static final String METHOD_GET_COACH_JUDGE = "";
	/**
	 * 通过学员对教练的评价。 参数 : {@link #PARAM_MOBILE_NUMBER} 返回值:{code:0|1}0:发送成功，1:发送异常
	 */
	public static final String METHOD_GET_COACH_MEAL = "";
	
	
}
