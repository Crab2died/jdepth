# Ruby Install with source on CentOS

---
### 1. Install OpenSSL
 - [Download OpenSSL Source](https://www.openssl.org/source/)
 - Install OpenSSL
 ```
 # tar -zxvf openssl-1.1.1-pre7.tar.gz
 # cd openssl-1.1.1-pre7
 # ./config -fPIC --prefix=/usr/local/openssl enable-shared
 # ./config -t
 # make && make install
 ```
 - Check install
 ```
 # openssl version
 OpenSSL 1.0.2k-fips  26 Jan 2017
 ```
### 2. Install Ruby
 - [Download Ruby Source](http://www.ruby-lang.org/en/downloads/)
 - Install Ruby
 ```
 # tar -zxvf ruby-2.5.1.tar.gz
 # cd ruby-2.5.1
 # ./configure --prefix=/usr/local/ruby --with-opessl-dir=/usr/local/openssl     // important
 # make && make install
 ```
 - Configure environment variable
 ```
 # vi /etc/profile
 
 append => export PATH=/usr/local/ruby/bin:$PATH
 
 # source /etc/profile
 ```
 - Check install
 ```
 # ruby -v
 ruby 2.5.1p57 (2018-03-29 revision 63029) [x86_64-linux]
 
 # gem -v
 2.7.6
 ```
 
