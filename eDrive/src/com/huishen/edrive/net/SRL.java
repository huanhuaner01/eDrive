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
	 * 通过短信获取验证码。 参数 : {@link #PARAM_MOBILE_NUMBER} 返回值:{code:0|1}0:发送成功，1:发送异常
	 */
	public static final String METHOD_GET_VERIFY_CODE = "/adr/isPhoneNumber";
	
	/**
	 * 注册登录  参数:vcode:4456 返回值:{code:0|1} 0:匹配成功，1:匹配失败
	 */
	public static final String METHOD_LOGIN = "/adr/stuMobileLoginSubmit";
	
	/**
	 * 验证手机号是否存在。 参数:mob=18782920468 返回值:{code:0|1} 0:不存在，1:存在
	 */
	public static final String METHOD_VERIFY_IF_NUMBER_EXIST = "/adr/vcohMob";

	// ---------------------------------学员登录注册-------------------------------------------
}
