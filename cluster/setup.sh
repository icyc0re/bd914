cd ~

# add master node to authorized_keys
echo "ssh-rsa AAAAB3NzaC1yc2EAAAADAQABAAABAQDd9LddIBEnCjQEC5zgBSeV3Dx+RGtMXRDi62NChMVt/924D4XaP0ElQDjP7l5aYI8CQyfhTO8nLWRrBDL7Az1JjtkLKrUXdkUdGbbW6fFTfiyBTuuGRWvi7RMEvq53aWbhI+wQHn3LUQlZbIFR6wFs73WPT5onFBBelOxu/YU2Bq5rWzUkcmLD9Wh8/jnAzNsCXjW9Po/RueEjvlYXX6UNJyB8UjndyOBgoO4Hds67meWFxIKSp1YQwJwgC11jiydVpCG6H43jYXBK0hc+K2nx/T6RNBorn3FYoIC7ekuQAecK21AOJu2YT5coczZaDacCDKenkmcYXyS6H2YUTos/" >> ~/.ssh/authorized_keys
sudo mv sshd_config /etc/ssh/
sudo chown root:root /etc/ssh/sshd_config

sudo chown -R `whoami` /opt

# install oracle java
sudo add-apt-repository ppa:webupd8team/java
sudo apt-get update
sudo apt-get -f install
sudo apt-get install oracle-java7-installer 

# install scala
sudo apt-get remove scala-library scala
wget www.scala-lang.org/files/archive/scala-2.10.3.deb
sudo dpkg -i scala-2.10.3.deb
sudo apt-get update
sudo apt-get install scala
sudo apt-get -f install
rm scala-2.10.3.deb

# install sbt
wget http://scalasbt.artifactoryonline.com/scalasbt/sbt-native-packages/org/scala-sbt/sbt//0.13.0/sbt.deb
sudo dpkg -i sbt.deb
sudo apt-get update
sudo apt-get install sbt
rm sbt.deb

# install git
sudo apt-get install git

# clone repo
git clone https://github.com/icyc0re/bd914.git

# get spark
wget http://mirror.switch.ch/mirror/apache/dist/incubator/spark/spark-0.9.1/spark-0.9.1-bin-hadoop2.tgz 
mkdir /opt/spark
mv spark-0.9.1-bin-hadoop2.tgz /opt/spark/
cd /opt/spark
tar -xf spark-0.9.1-bin-hadoop2.tgz
cd /opt/spark/spark-0.9.1-bin-hadoop2
rm ../spark-0.9.1-bin-hadoop2.tgz

echo "export SPARK_HOME=/opt/spark/spark-0.9.1-bin-hadoop2" >> ~/.bash_profile
echo 'export PATH=$PATH:$SPARK_HOME/bin:$SPARK_HOME/sbin' >> ~/.bash_profile

# set master node
cp conf/spark-env.sh.template conf/spark-env.sh
echo "SPARK_MASTER_IP=100.88.178.21" >> conf/spark-env.sh

# run spark master and our code
./sbin/start-master.sh
cd ~/bd914/cluster
sbt package
sbt run
cd /opt/spark/spark-0.9.1-bin-hadoop2
./sbin/stop-all.sh