/**
 * <p>存放与友盟推送相关操作的类和接口。</p>
 * <h1>设计思想</h1>
 * <p>
 * 本包下的类和接口是按照低耦合的原则来设计的，本着最大限度隐藏通信细节的原则，
 * 将所有的实现细节与服务器通信字段都封装在包内。
 * 其中，UmengServiceProxy负责处理推送开关、Alias和Tag等的上传和维护操作；
 * UmengPushConst类负责存储与服务器交互的字段信息。
 * </p>
 * <h1>处理流程</h1>
 * <p>
 * 由于服务器下发的是自定义消息而不是notification，因此我们需要使用自定义的UHandler来处理，
 * 这就是CustomUMessageHandler类。该类主要是约定和服务器通信的方式，并将结果转换为应用程序易于使用的Map类型。<br/>
 * AbsActivePushDataReceiver类和DefaultPushDataReceiver类构成了数据的主要处理框架。之所以这样设计是因为同样的数据
 * 可能会有多个地方需要处理，并且往往有的界面希望监听其数据的变更（例如，消息列表界面希望监听新消息的到来）。对于这样
 * 的情形，只需要在合适的地方注册一个AbsActivePushDataReceiver的子类即可。其余一般的、静态的处理，则可以交给
 * DefaultPushDataReceiver来进行操作。关于这些Receiver的Action，将在下一节进行介绍。
 * </p>
 * <h1>数据处理</h1>
 * <p>
 * 每次收到的消息数据的字段根据消息类型的不同而不同，但是所有的消息都必须携带一个msgType字段。<br/>
 * 本包将所有的推送数据设计为PushData类的子类，该类被设计为不可变类，没有一般实体类的getter/setter方法。这样的设计
 * 可以有效提升字段的使用性能，同时由于不可变类的设计，其数据保证不会被破坏。在扩展该类实现新的功能的时候，
 * 应当同样遵循这样的设计。<br/>
 * UmengPushConst类的Action子类比较特殊，它存放了所有消息类型相关的Action，每种消息类型分别对应其中一个。
 * CustomUMessageHandler收到消息后会根据消息类型发送其中一个出来供Receiver接收。请注意，在每次增加消息类型时，你需要：
 * <ul>
 * 	<li>在UmengPushConst里面增加一个子类，其中放置服务器下发的字段名。强烈建议将该类的访问权限置为protected.</li>
 * 	<li>在UmengPushConst#Action类中，增加一个新的ACTION字段。</li>
 * <li>在AndroidManifest文件中，为DefaultPushDataReceiver增加该Action。在onReceive方法中，重写必要逻辑。</li>
 *  <li>在本包下新增一个类继承自PushData，强烈建议也使用不可变类的设计。</li>
 * <ul>
 * 本包在提交的时候保留了两个样例类 {@link com.huishen.edrive.umeng.NewOrderPushData}, 
 * {@link com.huishen.edrive.umeng.UmengPushConst.Action}。可以参考这两个类的设计进行扩展。
 * </p>
 * @author Muyangmin
 * @create 2015-2-27
 */
package com.huishen.edrive.umeng;