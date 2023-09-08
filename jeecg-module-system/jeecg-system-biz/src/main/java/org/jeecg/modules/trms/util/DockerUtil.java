package org.jeecg.modules.trms.util;

import com.alibaba.fastjson.JSONObject;
import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.command.CreateContainerResponse;
import com.github.dockerjava.api.command.InspectContainerResponse;
import com.github.dockerjava.api.model.*;
import com.github.dockerjava.core.DockerClientBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Component
public class DockerUtil {
    public static DockerClient client;

    @PostConstruct
    public void init() {
        client = connectDocker();
    }

    public static void main(String[] args) throws IOException {
        DockerClient client = connectDocker();
//        List<CreateContainerResponse> containers = createContainers(client, "1");
//        getContainerInfo(client, containers);
//        createNetwork(client, "test");
//        System.out.println(slave1Test);
//        int inputPort = 40000;
//        System.out.println("输入端口：" + inputPort + ", 递增递归找到可用端口为：" + getUsablePort(inputPort));
//test();
//        System.out.println(CheckPortAvailability(25123,"192.168.160.100"));
//        System.out.println(CheckPortAvailability(8888,"192.168.160.100"));
//        createContainers(client, "1_" + UUID.randomUUID());

    }

    /**
     * 获取容器列表
     *
     * @param client
     * @return
     */
    public static List<Container> getContainerList(DockerClient client) {
        return client.listContainersCmd().exec();
    }

    /**
     * 获取单个容器信息
     *
     * @param id
     * @return
     */
    public static InspectContainerResponse inspectContainer(String id) {
        return client.inspectContainerCmd(id).exec();
    }

    /**
     * 返回镜像列表
     * @return
     */
    public static List<Image> listImages() {
        return client.listImagesCmd().exec();
    }

    /**
     * 建立连接
     *
     * @return 连接
     */
    public static DockerClient connectDocker() {
//        DockerClient dockerClient = DockerClientBuilder.getInstance("tcp://192.168.160.100:2375").build();
        DockerClient dockerClient = DockerClientBuilder.getInstance("tcp://localhost:2375").build();
        Info info = dockerClient.infoCmd().exec();
        log.info("Init Docker Client");
        return dockerClient;
    }

    /**
     * 创建网络
     *
     * @param client 连接
     * @param name   网络名
     */
    public static void createNetwork(DockerClient client, String name) {
        client.createNetworkCmd()
                .withName(name)
                .withIpam(
                new Network.Ipam().withConfig(
                        new Network.Ipam.Config()
                                .withSubnet("172.36.5.0/29")
                                .withGateway("172.36.5.1")))
                .withDriver("bridge")
                .exec();
    }

    /**
     * 创建集群
     *
     * @param client 连接
     * @param id     创建人id
     * @return 集群信息列表
     */
    public static List<CreateContainerResponse> createContainersHA(DockerClient client, String id, String networkName, String tag) {
        id = id + UUID.randomUUID();
//        String networkName = UUID.randomUUID().toString();
//        createNetwork(client, networkName);
        CreateContainerResponse master = createMaster(client, "master_" + id, "master:" + tag, networkName);
        CreateContainerResponse slave1 = createSlave1(client, "slave1_" + id, "slave1:" + tag, networkName);
        CreateContainerResponse slave2 = createSlave2(client, "slave2_" + id, "slave2:" + tag, networkName);
        List<CreateContainerResponse> list = new ArrayList<>();
        list.add(master);
        list.add(slave1);
        list.add(slave2);
        return list;
    }

    /**
     * 移除容器
     *
     * @param client 连接
     * @param name   容器名
     * @return 判断
     */
    public static Boolean removeContainer(DockerClient client, String name) {
        try {
            client.removeContainerCmd(name).exec();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public static Boolean removeImage(DockerClient client, String name) {
        try {
            client.removeImageCmd(name).exec();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 创建master节点
     *
     * @param client        连接
     * @param containerName 容器名
     * @param imageName     镜像名
     * @param network       网络名
     * @return 容器信息
     */
    public static CreateContainerResponse createMaster(DockerClient client, String containerName, String imageName, String network) {

        ExposedPort tcp0022 = ExposedPort.tcp(22);
        ExposedPort tcp8088 = ExposedPort.tcp(8088);
        ExposedPort tcp3306 = ExposedPort.tcp(8088);
        ExposedPort tcp8080 = ExposedPort.tcp(8080);
        ExposedPort tcp8081 = ExposedPort.tcp(8081);
        ExposedPort tcp8082 = ExposedPort.tcp(8082);
        ExposedPort tcp9083 = ExposedPort.tcp(9083);
        ExposedPort tcp9870 = ExposedPort.tcp(9870);
        Ports portBindings = new Ports();
        portBindings.bind(tcp0022, Ports.Binding.bindPort(getRandomPort()));
        portBindings.bind(tcp8088, Ports.Binding.bindPort(getRandomPort()));
        portBindings.bind(tcp3306, Ports.Binding.bindPort(getRandomPort()));
        portBindings.bind(tcp8080, Ports.Binding.bindPort(getRandomPort()));
        portBindings.bind(tcp8081, Ports.Binding.bindPort(getRandomPort()));
        portBindings.bind(tcp8082, Ports.Binding.bindPort(getRandomPort()));
        portBindings.bind(tcp9083, Ports.Binding.bindPort(getRandomPort()));
        portBindings.bind(tcp9870, Ports.Binding.bindPort(getRandomPort()));

        HostConfig hostConfig = HostConfig.newHostConfig()
                .withPrivileged(true)
                .withNetworkMode(network)
                .withPortBindings(portBindings)
//                .withPortBindings(PortBinding.parse("40001:8089"))
//                .withPortBindings(PortBinding.parse("40000:22"))
                .withRestartPolicy(RestartPolicy.alwaysRestart());

//        client.removeContainerCmd(containerName).exec();

        CreateContainerResponse container = client.createContainerCmd(imageName)
                .withName(containerName)
                .withHostName("master")
                .withHostConfig(hostConfig)
                .withExposedPorts(tcp0022, tcp8088, tcp3306, tcp8080, tcp8081, tcp8082, tcp9083, tcp9870)
                .withCmd("--runtime=runc", "--detach=true")
                .withCmd("/usr/sbin/init")
                .exec();
        client.startContainerCmd(containerName).exec();
        return container;
    }

    /**
     * 创建slave1节点
     *
     * @param client        连接
     * @param containerName 容器名
     * @param imageName     镜像名
     * @param network       网络名
     * @return 容器信息
     */
    public static CreateContainerResponse createSlave1(DockerClient client, String containerName, String imageName, String network) {
        ExposedPort tcp22 = ExposedPort.tcp(22);
        ExposedPort tcp8088 = ExposedPort.tcp(8088);
        ExposedPort tcp9870 = ExposedPort.tcp(9870);
        Ports portBindings = new Ports();
        portBindings.bind(tcp22, Ports.Binding.bindPort(getRandomPort()));
        portBindings.bind(tcp8088, Ports.Binding.bindPort(getRandomPort()));
        portBindings.bind(tcp9870, Ports.Binding.bindPort(getRandomPort()));

        HostConfig hostConfig = HostConfig.newHostConfig()
                .withPrivileged(true)
                .withNetworkMode(network)
                .withPortBindings(portBindings)
//                .withPortBindings(PortBinding.parse("40001:8089"))
//                .withPortBindings(PortBinding.parse("40000:22"))
                .withRestartPolicy(RestartPolicy.alwaysRestart());

//        client.removeContainerCmd(containerName).exec();

        CreateContainerResponse container = client.createContainerCmd(imageName)
                .withName(containerName)
                .withHostName("slave1")
                .withHostConfig(hostConfig)
                .withExposedPorts(tcp8088, tcp22, tcp9870)
                .withCmd("--runtime=runc", "--detach=true")
                .withCmd("/usr/sbin/init")
                .exec();
        client.startContainerCmd(containerName).exec();
        return container;
    }

    /**
     * 创建slave2节点
     *
     * @param client        连接
     * @param containerName 容器名
     * @param imageName     镜像名
     * @param network       网络名
     * @return 容器信息
     */
    public static CreateContainerResponse createSlave2(DockerClient client, String containerName, String imageName, String network) {
        ExposedPort tcp22 = ExposedPort.tcp(22);
        ExposedPort tcp8088 = ExposedPort.tcp(8088);
        Ports portBindings = new Ports();
        portBindings.bind(tcp22, Ports.Binding.bindPort(getRandomPort()));
        portBindings.bind(tcp8088, Ports.Binding.bindPort(getRandomPort()));

        HostConfig hostConfig = HostConfig.newHostConfig()
                .withPrivileged(true)
                .withNetworkMode(network)
                .withPortBindings(portBindings)
//                .withPortBindings(PortBinding.parse("40001:8089"))
//                .withPortBindings(PortBinding.parse("40000:22"))
                .withRestartPolicy(RestartPolicy.alwaysRestart());

//        client.removeContainerCmd(containerName).exec();

        CreateContainerResponse container = client.createContainerCmd(imageName)
                .withName(containerName)
                .withHostName("slave2")
                .withHostConfig(hostConfig)
                .withExposedPorts(tcp8088, tcp22)
                .withCmd("--runtime=runc", "--detach=true")
                .withCmd("/usr/sbin/init")
                .exec();
        client.startContainerCmd(containerName).exec();
        return container;
    }

    /**
     * 获取随机40000-60000之间端口
     *
     * @return 端口号
     */
    public static int getRandomPort() {
        double randomDouble = Math.random();

        // 映射到 40000 到 60000 之间的整数范围
        int min = 40000;
        int max = 60000;
        int randomNumber = (int) (randomDouble * (max - min + 1)) + min;
        try {
            return getUsablePortNext(randomNumber);
        } catch (Exception e) {
            return -1;
        }
    }

    /**
     * 获取下一个有用的端口
     *
     * @param port 起始端口
     * @return 端口号
     * @throws IOException 获取失败
     */
    public static int getUsablePortNext(int port) throws IOException {
        boolean flag = false;
        Socket socket = null;
        InetAddress theAddress = InetAddress.getByName("192.168.160.100");
        try {
            socket = new Socket(theAddress, port);
            flag = true;
        } catch (IOException e) {
            //如果测试端口号没有被占用，那么会抛出异常，通过下文flag来返回可用端口
        } finally {
            if (socket != null) {
                //new了socket最好释放
                socket.close();
            }
        }

        if (flag) {
            //端口被占用，port + 1递归
            port = port + 1;
            return getUsablePortNext(port);
        } else {
            //可用端口
            return port;
        }
    }

}