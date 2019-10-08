FROM registry.redhat.io/rhel7/rhel:7.6-362
COPY jdevSilent.xml /jdevSilent.xml
COPY jdk-6u24-linux-x64.bin /jdk-6u24-linux-x64.bin
COPY OFM_11.1.2.2.tz /OFM_11.1.2.2.tz
RUN chmod +x jdk-6u24-linux-x64.bin
RUN /jdk-6u24-linux-x64.bin
ENV SetJavaHome /jdk1.6.0_24
ENV JAVA_HOME /jdk1.6.0_24
ENV PATH $JAVA_HOME/bin:$PATH
ENV ORACLE_HOME /u01/app/oracle/ofr_app
RUN export ORACLE_HOME ORACLE_HOME=/u01/app/oracle/ofr_app
ENV MW_HOME=/u01/app/oracle/middleware
RUN export MW_HOME=/u01/app/oracle/middleware
ENV WLS_HOME=$MW_HOME/wlserver_10.3
RUN export WLS_HOME=$MW_HOME/wlserver_10.3
ENV WL_HOME=$WLS_HOME

RUN groupadd -g 1000 oinstall
RUN useradd -u 1100 -g oinstall oracle
RUN mkdir -p /u01/app/oracle/middleware
RUN mkdir -p /u01/app/oracle/config/domains
RUN mkdir -p /u01/app/oracle/config/applications
RUN mkdir -p /u01/app/oracle/middleware/wlserver_10.3/samples/domains/wl_server
RUN chown -R oracle:oinstall /u01
RUN chmod -R 775 /u01/

RUN subscription-manager register --username ksummersill --password 'zxasqw12ZXASQW!@' --auto-attach
RUN yum update -y && yum repolist all && yum install nano -y && yum install glibc.i686 -y && yum install git -y
RUN curl -O http://ftp.tu-chemnitz.de/pub/linux/dag/redhat/el7/en/x86_64/rpmforge/RPMS/sshpass-1.05-1.el7.rf.x86_64.rpm
RUN rpm -i sshpass-1.05-1.el7.rf.x86_64.rpm
RUN curl -O http://ftp.tu-chemnitz.de/pub/linux/dag/redhat/el7/en/x86_64/rpmforge/RPMS/fping-3.10-1.el7.rf.x86_64.rpm
RUN rpm -i fping-3.10-1.el7.rf.x86_64.rpm
RUN sshpass -V
RUN java -version

COPY wls1036_generic.jar /wls1036_generic.jar
COPY wlsSilent.xml /wlsSilent.xml
RUN java -Xmx1024m -jar wls1036_generic.jar -mode=silent -silent_xml=wlsSilent.xml
RUN . $WLS_HOME/server/bin/setWLSEnv.sh

COPY V42769-01_1of2.zip /V42769-01_1of2.zip
COPY V42769-01_2of2.zip /V42769-01_2of2.zip
COPY 11gR2_config_inst2.rsp /11gR2_config_inst2.rsp
COPY oraInst.loc /oraInst.loc
COPY install_only.rsp /install_only.rsp

RUN SELINUX=permissive

RUN yum groupinstall "Development Tools" -y
 
RUN yum install unzip -y
RUN yum install binutils -y
RUN yum install elfutils-libelf -y
RUN yum install elfutils-libelf-devel -y
RUN yum install gcc -y
RUN yum install gcc-c++ -y
RUN yum install glibc -y
RUN yum install glibc.i686 -y
RUN yum install glibc-common -y
RUN yum install glibc-devel -y
RUN yum install glibc-headers -y
RUN yum install glibc-devel.i686 -y
RUN yum install ksh -y
RUN yum install libaio -y
RUN yum install libaio.i686 -y
RUN yum install libaio-devel -y
RUN yum install libaio-devel.i686 -y
RUN yum install libgcc -y
RUN yum install libgcc.i686 -y
RUN yum install libstdc++ -y
RUN yum install libstdc++.i686 -y
RUN yum install libstdc++-devel -y
RUN yum install make -y
RUN yum install numactl -y
RUN yum install numactl-devel -y
RUN yum install sysstat -y
RUN yum install unixODBC -y
RUN yum install unixODBC.i686 -y
RUN yum install unixODBC-devel -y
RUN yum install unixODBC-devel.i686 -y

RUN unzip V42769-01_1of2.zip
RUN unzip V42769-01_2of2.zip

RUN usermod -a -G root oracle

RUN mkdir sysman
RUN mkdir /sysman/lib
COPY ins_emagent.mk /sysman/lib/ins_emagent.mk
RUN jar -uvf /Disk4/stage/Components/oracle.sysman.agent/10.2.0.5.6/1/DataFiles/filegroup32.jar /sysman/lib/ins_emagent.mk
RUN chmod +x /Disk4/stage/Components/oracle.sysman.agent/10.2.0.5.6/1/DataFiles/filegroup32.jar
USER oracle
RUN ./Disk1/runInstaller -silent -response /install_only.rsp -invPtrLoc /oraInst.loc -jreLoc /jdk1.6.0_24 && sleep 8m
COPY compile.sh /compile.sh
USER root
RUN rm -Rf Disk1
RUN rm -Rf Disk2
RUN rm -Rf Disk3
RUN rm -Rf Disk4
RUN rm -Rf sysman
RUN rm -f V42769-01_1of2.zip
RUN rm -f V42769-01_2of2.zip
RUN rm -f jdevstudio11124install.bin
RUN rm -f jdk-6u24-linux-x64.bin
RUN rm -f wls1036_generic.jar
RUN echo *******************COMPLETED ORACLE FORMS AND REPORTS*********************
COPY linuxamd64_12102_database_se2_1of2.zip /linuxamd64_12102_database_se2_1of2.zip
COPY linuxamd64_12102_database_se2_2of2.zip /linuxamd64_12102_database_se2_2of2.zip
RUN unzip linuxamd64_12102_database_se2_1of2.zip
RUN unzip linuxamd64_12102_database_se2_2of2.zip
RUN groupadd -g 54322 dba
RUN groupadd -g 54323 oper
RUN usermod -g oinstall -G dba,oper oracle
RUN mkdir -p /ora01/app/oracle/product/11.2.0/db_1
RUN chown -R oracle:oinstall /u01
RUN chmod -R 775 /u01
COPY sysctl.conf /etc/sysctl.conf
COPY limits.conf /etc/security/limits.conf
COPY hosts /etc/hosts
RUN yum install libselinux-utils -y
COPY db_install.rsp /db_install.rsp
RUN groupadd -g 54325 dgdba
RUN groupadd -g 54326 kmdba
RUN groupadd -g 54327 bckpdba
RUN usermod -a -G dgdba oracle
RUN usermod -a -G kmdba oracle
RUN usermod -a -G bckpdba oracle
COPY compat-libcap1-1.10-1.x86_64.rpm /compat-libcap1-1.10-1.x86_64.rpm
COPY compat-libstdc++-33-3.2.3-72.el7.x86_64.rpm /compat-libstdc++-33-3.2.3-72.el7.x86_64.rpm
RUN rpm -i compat-libcap1-1.10-1.x86_64.rpm
RUN rpm -i compat-libstdc++-33-3.2.3-72.el7.x86_64.rpm



