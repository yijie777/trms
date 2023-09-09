package org.jeecg.modules.trms.constant;


import org.jeecg.modules.trms.pojo.HostData;

/**
* @Description: 常量池
* @Author: NoCortY
* @Date: 2020/3/8
*/
public class ConstantPool {
    /**
     * 随机生成uuid的key名
     */
    public static final String USER_UUID_KEY = "user_uuid";
    /**
     * 用户连接的信息
     */
    public static HostData SSH_DATA = null;
    /**
     * 发送指令：连接
     */
    public static final String WEBSSH_OPERATE_CONNECT = "connect";
    /**
     * 发送指令：命令
     */
    public static final String WEBSSH_OPERATE_COMMAND = "command";

    public static final  String[] KEYWORDS_HA_MASTER = {"DataNode", "DFSZKFailoverController", "JournalNode", "QuorumPeerMain", "NameNode", "ResourceManager", "NodeManager"};
    public static final  String[] KEYWORDS_HA_SLAVE2 = {"DataNode", "NodeManager", "JournalNode", "QuorumPeerMain"};

    public static final String CHECK_HA_MASTER = "CHECK_HA_MASTER";
    public static final String CHECK_HA_SLAVE1 = "CHECK_HA_SLAVE1";
    public static final String CHECK_HA_SLAVE2 = "CHECK_HA_SLAVE2";
    public static final String START_HA = "START_HA";

}
