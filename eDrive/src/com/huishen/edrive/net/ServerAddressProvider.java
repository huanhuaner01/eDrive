package com.huishen.edrive.net;

/**
 * 该类提供系统服务器的地址。
 * 
 * @author Muyangmin
 * @create 2015-2-7
 */
@SuppressWarnings("unused")
final class ServerAddressProvider {
	
	//全局变量，服务器路径，只需要修改这里
	private static final IServer CURRENT_SERVER = new InternalTestServer();
	
	protected static final String getServerAddress(){
		return CURRENT_SERVER.getServerAddress();
	}
	
	//公开服务器
	private static final class PublicServer implements IServer{

		@Override
		public String getServerAddress() {
			return "http://bind.ejxc.com.cn:8081/";
		}
	}
	
	//内部测试服务器
	private static class InternalTestServer implements IServer{

		@Override 
		public String getServerAddress() {
			return "http://192.168.0.210:8080/enrollsystem/";
		}
	  	
	}
	
	private static interface IServer {
		/**
		 * 获得服务器的地址。
		 * 
		 * @return 返回服务器的地址（通常是域名，有时还包含端口号）。一个典型值是这样：
		 * <code>http://www.huishen.com</code>
		 */
		String getServerAddress();
	}
}
