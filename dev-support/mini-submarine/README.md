<!--
Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
-->

# Mini-submarine

This is a docker image built for submarine development and quick start test.

**Please Note: don't use the image in production environment. It's only for test purpose.**

## Start mini-submarine

### Use the image we provide

```
docker pull apache/submarine:mini-0.6.0
```

### Create image by yourself

> You may need a VPN if your network is limited

1.Clone the source code of Submarine
```
git clone https://github.com/apache/submarine.git
```

2.Build Submarine
```
cd ./submarine
mvn clean package -DskipTests
```

3.Build image of mini-submarine
>  You can download in advance of these three kind of compressed file for building :
>  zookeeper-3.4.14.tar.gz , hadoop-2.9.2.tar.gz , spark-2.4.4-bin-hadoop2.7.tgz
>  and put  them into "submarine/dev-support/mini-submarine/"

```
cd submarine/dev-support/mini-submarine/
./build_mini-submarine.sh
```
#### Package An Existing Release Candidates
When doing release, the release manager might needs to package a artifact candidates in this docker image and public the image candidate for a vote.
In this scenario, we can do this:

Put submarine candidate artifacts to a folder like "~/releases/submarine-release"
```
$ ls $release_candidates_path
submarine-dist-0.7.0-hadoop-2.9.tar.gz        submarine-dist-0.7.0-src.tar.gz.asc
submarine-dist-0.7.0-hadoop-2.9.tar.gz.asc    submarine-dist-0.7.0-src.tar.gz.sha512
submarine-dist-0.7.0-hadoop-2.9.tar.gz.sha512 submarine-dist-0.7.0-src.tar.gz
```
```
export submarine_version=0.7.0
export release_candidates_path=~/releases/submarine-release
./build_mini-submarine.sh
#docker run -it -h submarine-dev --net=bridge --privileged -P local/mini-submarine:0.7.0 /bin/bash
docker tag local/mini-submarine:0.7.0 apache/mini-submarine:0.7.0-RC0
docker push apache/mini-submarine:0.7.0-RC0
```
In the container, we can verify that the submarine jar version is the expected 0.7.0. Then we can upload this image with a "RC" tag for a vote.

### Run mini-submarine image

```
docker run -it -h submarine-dev --name mini-submarine --net=bridge --privileged -P local/mini-submarine:0.7.0 /bin/bash

# In the container, use root user to bootstrap hdfs and yarn
/tmp/hadoop-config/bootstrap.sh

# Two commands to check if yarn and hdfs is running as expected
yarn node -list -showDetails
```

If you pull the image directly, please replace "local/mini-submarine:0.7.0" with "apache/submarine:mini-0.7.0".

### You should see info like this:

```
Total Nodes:1
         Node-Id      Node-State	Node-Http-Address	Number-of-Running-Containers
submarine-dev:35949         RUNNING	submarine-dev:8042                            0
Detailed Node Information :
  Configured Resources : <memory:8192, vCores:16, nvidia.com/gpu: 1>
  Allocated Resources : <memory:0, vCores:0>
  Resource Utilization by Node : PMem:4144 MB, VMem:4189 MB, VCores:0.25308025
  Resource Utilization by Containers : PMem:0 MB, VMem:0 MB, VCores:0.0
  Node-Labels :
```

```
hdfs dfs -ls /user
```

> drwxr-xr-x   - yarn supergroup          0 2019-07-22 07:59 /user/yarn

## Run workbench server

1. Setup mysql mariadb server
> Because mysql and mariadb use the GPL license, So there is no binary file containing mysql in the image, you need to manually execute the script to install it.

```
/tmp/hadoop-config/setup-mysql.sh
```

You can execute command `mysql -uroot` login mysql mariadb.

2. Start submarine server
```
su yarn
/opt/submarine-current/bin/submarine-daemon.sh start getMysqlJar
```

3. Login submarine workbench

Execute the following command in your host machine, Get the access URL of the submarine workbench running in docker

```shell
echo "http://localhost:$(docker inspect --format='{{(index (index .NetworkSettings.Ports "8080/tcp") 0).HostPort}}' mini-submarine)"
```
The URL returned by the command (like to: http://localhost:32819) is opened through a browser. The username and initial password of the workbench are both `admin`.

## Run a submarine job

### Switch to user yarn

```
su yarn
```

### Navigate to submarine example directory
```
cd /home/yarn/submarine/
```

### Run a mnist TF job with submarine + TonY runtime
```
# run TF 1 distributed training job
./run_submarine_mnist_tony.sh

# run TF 2 distributed training job
./run_submarine_mnist_tf2_tony.sh
```
When run_submarine_mnist_tony.sh is executed, mnist data is download from the url, [google mnist](https://storage.googleapis.com/cvdf-datasets/mnist/), by default. If the url is unaccessible, you can use parameter "-d" to specify a customized url.
For example, if you are in mainland China, you can use the following command
```
./run_submarine_mnist_tony.sh -d http://yann.lecun.com/exdb/mnist/
```

### Run a mnist TF job via submarine server

Submarine server is supposed to manage jobs lifecycle. Clients can just submit
job parameters or yaml file to submarine server instead of submitting jobs
directly by themselves. Submarine server can handle the rest of the work.

Set submarine.server.rpc.enabled to true in the file of
/opt/submarine-current/conf/submarine-site
```
  <property>
    <name>submarine.server.rpc.enabled</name>
    <value>true</value>
    <description>Run jobs using rpc server.</description>
  </property>
```
Run the following command to submit a job via submarine server
```
./run_submarine_mnist_tony_rpc.sh
```

### Try your own submarine program

Run container with your source code. You can also use "docker cp" to an existing running container

1. `docker run -it -h submarine-dev --net=bridge --privileged -v pathToMyScrit.py:/home/yarn/submarine/myScript.py local/hadoop-docker:submarine /bin/bash`

2. Refer to the `run_submarine_mnist_tony.sh` and modify the script to your script

3. Try to run it. Since this is a single node environment, keep in mind that the workers could have conflicts with each other. For instance, the mnist_distributed.py example has a workaround to fix the conflicts when two workers are using same "data_dir" to download data set.


## Update Submarine Version

You can follow the documentation instructions to update your own modified and compiled submarine package to the submarine container.

### Build Submarine

```
cd submarine-project-dir/
mvn clean package -DskipTests
```

### Copy submarine jar to mini-submarine container

```
docker cp submarine-all/target/submarine-all-<SUBMARINE_VERSION>-hadoop-<HADOOP_VERSION>.jar <container-id>:/tmp/
```

### Modify environment variables

```
cd /home/yarn/submarine
vi run_customized_submarine-all_mnist.sh

# Need to modify environment variables based on hadoop and submarine version numbers
SUBMARINE_VERSION=<submarine-version-number>
HADOOP_VERSION=<hadoop-version-number> # default 2.9
```

### Test submarine jar package in container

```
cd /home/yarn/submarine
./run_customized_submarine-all_mnist.sh
```

## Debug Submarine

When using mini-submarine, you can debug submarine client, applicationMaster and executor for trouble shooting.

### <span id="debug">Debug submarine client</span>

Run the following command to start mini-submarine.

```
docker run -it -P -h submarine-dev --net=bridge --expose=8000 --privileged local/mini-submarine:<REPLACE_VERSION> /bin/bash
```

Debug submarine client with the parameter "--debug"

```
./run_submarine_mnist_tony.sh --debug
```

Port 8000 is used in the mini-submarine.
You need to find the debug port mapping between mini-submarine and the host on which run mini-subamrine.

```
docker port <SUBMARINE_CONTAINER_ID>
```

For example, we can get some info like this

```
8000/tcp -> 0.0.0.0:32804
```

Then port 32804 can be used for remote debug.

### Debug submarine job applicationMaster

Run the following command to start mini-submarine.

```
docker run -it -P -h submarine-dev --net=bridge --expose=8001 --privileged local/mini-submarine:<REPLACE_VERSION> /bin/bash
```

Add the following configuration in the file /usr/local/hadoop/etc/hadoop/tony.xml.

```
<property>
  <name>tony.task.am.jvm.opts</name>
  <value>-agentlib:jdwp=transport=dt_socket,server=y,suspend=y,address=8001</value>
</property>
```

You can use run_submarine_mnist_tony.sh to submit a job. Port 8001 is used for AM debugging in mini-submarine.
And the debug port mapping can be obtained using the way as [Debug submarine client](#debug) shows.

### Debug submarine job executor

Run the following command to start mini-submarine.

```
docker run -it -P -h submarine-dev --net=bridge --expose=8002 --privileged local/mini-submarine:<REPLACE_VERSION> /bin/bash
```

Add the following configuration in the file /usr/local/hadoop/etc/hadoop/tony.xml.

```
<property>
  <name>tony.task.executor.jvm.opts</name>
  <value>-agentlib:jdwp=transport=dt_socket,server=y,suspend=y,address=8002</value>
</property>
```

Port 8002 is used for executor debugging in mini-submarine.
To avoid port confliction, you need to use only one executor, which means the parameter of
submarine job should be like this

```
--num_workers 1 \
--num_ps 0 \
```

You can get the debug port mapping using the way as [Debug submarine client](#debug) shows.

## Run a distributedShell job with docker container

You can also run a distributedShell job in mini-submarine.

```
cd && ./yarn-ds-docker.sh
```

## Run a spark job

Spark jobs are supported as well.

```
cd && cd spark-script && ./run_spark.sh
```

## Question and answer

1. Submarine package name error

   Because the package name of submarine 0.3.0 or higher has been changed from `apache.hadoop.yarn.submarine` to `apache.submarine`, So you need to set the Runtime settings in the `/usr/local/hadoop/etc/hadoop/submarine-site.xml` file.

   ```
   <configuration>
      <property>
        <name>submarine.runtime.class</name>
        <value>org.apache.submarine.server.submitter.yarn.YarnRuntimeFactory</value>
      </property>
   </configuration>
   ```
