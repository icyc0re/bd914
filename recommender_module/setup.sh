cd ~
# add master node to authorized_keys
echo "ssh-rsa AAAAB3NzaC1yc2EAAAADAQABAAABAQDgJT/wBEqZISAvzkTmT4IZ25XeQO4VJS6+3+6ZiNKYTB7TQ9sv+H9EsxWKRHAZ/ZUqLyyedeyP6AvYLAV8ej2zyLsXmn0LH0YQ74tZUZcsaXYpSx3ACCo3X2aIQaenr3u9Q9vf7LrjiFH7LeaRb9d8H9xsZP3GT0EvdEMPk+ipekiKOie3Qm/Hj0CJHHzPWu/t0vKuE3rO9RAClwJG9OumSuu4wx+WAyDJ9V2dLzn+2ya8SJnumBLPWXX8wStVtDBNax70bU/JbEhiXMMq/BqIQUNSA/L53ItuHuDtym7BZGQK6JWsPBHKkM2FzpNpS3JhzPn/e3RkDyMn+y63hNSn azureuser@bigdataig1" >> ~/.ssh/authorized_keys
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
echo "SPARK_MASTER_IP=bigdataivan.cloudapp.net" >> conf/spark-env.sh

# run spark master and our code
./sbin/start-master.sh
cd ~/bd914/cluster
sbt package
sbt run
cd /opt/spark/spark-0.9.1-bin-hadoop2
./sbin/stop-all.sh