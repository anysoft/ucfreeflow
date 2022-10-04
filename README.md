# ucfreeflow
uc浏览器免流key数据uid和token抓取，基于xposed。 

可能高版本Android不支持，因为sdcard文件权限问题
hook到的数据存放在`/sdcard/UCDownloads/freeflow.ini`

## 主要用途
1. 获取`UC浏览器`免流验证的`uid`、`token`、`proxy_ip`、`proxy_port`(每月一号更新)
2. 根据`uid`、`token`、`域名`计算`Proxy-Authorization`认证信息
3. 认证信息填入v2ray 实现基于v2ray+uc(免流代理)免流功能

ps：上述方案其实可以简单手动完成，4G/5G网络下使用httpcanary 对 UC浏览器抓包，UC访问 v2ray/ssr的域名，在请求信息中就可以看到`Proxy-Authorization`了。
此处主要目的还是为了抓取`uid`、`token` 后自行修改编译v2ray源码达到UC浏览器一样的直连代理效果。

## 食用方式
### 方案一
借用http伪装达到http header自定义效果
[代理使用方式](https://github.com/Qv2ray/Qv2ray/issues/483#issuecomment-608985659) (该方式同样适用于王卡，header伪装就行)

### 方案二
修改源码，让v2ray的http认证方式添加method字段，实现uc/王卡 `Proxy-Authorization` 计算和header添加

v2ray修改部分go源码如下：

``` golang
infra/conf/http.go

@@ -10,16 +10,18 @@ import (
 )
 
 type HttpAccount struct {
 	Username string `json:"user"`
 	Password string `json:"pass"`
+	AuthMethod string `json:"method"`
 }
 
 func (v *HttpAccount) Build() *http.Account {
 	return &http.Account{
 		Username: v.Username,
 		Password: v.Password,
+		AuthMethod: v.AuthMethod,
 	}
 }
 
 type HttpServerConfig struct {
 	Timeout     uint32         `json:"timeout"`
  
proxy/http/config.pb.go
@@ -19,10 +19,11 @@ var _ = math.Inf
 const _ = proto.ProtoPackageIsVersion3 // please upgrade the proto package
 
 type Account struct {
 	Username             string   `protobuf:"bytes,1,opt,name=username,proto3" json:"username,omitempty"`
 	Password             string   `protobuf:"bytes,2,opt,name=password,proto3" json:"password,omitempty"`
+	AuthMethod           string   `protobuf:"bytes,3,opt,name=authmethod,proto3" json:"authmethod,omitempty"`
 	XXX_NoUnkeyedLiteral struct{} `json:"-"`
 	XXX_unrecognized     []byte   `json:"-"`
 	XXX_sizecache        int32    `json:"-"`
 }
 
@@ -63,10 +64,17 @@ func (m *Account) GetPassword() string {
 		return m.Password
 	}
 	return ""
 }
 
+func (m *Account) GetAuthMethod() string {
+	if m != nil {
+		return m.AuthMethod
+	}
+	return ""
+}
+
 // Config for HTTP proxy server.
 type ServerConfig struct {
 	Timeout              uint32            `protobuf:"varint,1,opt,name=timeout,proto3" json:"timeout,omitempty"` // Deprecated: Do not use.
 	Accounts             map[string]string `protobuf:"bytes,2,rep,name=accounts,proto3" json:"accounts,omitempty" protobuf_key:"bytes,1,opt,name=key,proto3" protobuf_val:"bytes,2,opt,name=value,proto3"`
 	AllowTransparent     bool              `protobuf:"varint,3,opt,name=allow_transparent,json=allowTransparent,proto3" json:"allow_transparent,omitempty"`

proxy/http/client.go

@@ -2,12 +2,15 @@
 
 package http
 
 import (
 	"context"
+	"crypto/md5"
 	"encoding/base64"
+	"fmt"
 	"io"
+	"net/url"
 	"strings"
 
 	"v2ray.com/core"
 	"v2ray.com/core/common"
 	"v2ray.com/core/common/buf"
@@ -120,14 +123,52 @@ func setUpHttpTunnel(reader io.Reader, writer io.Writer, destination *net.Destin
 	destNetAddr := destination.NetAddr()
 	headers = append(headers, "CONNECT "+destNetAddr+" HTTP/1.1")
 	headers = append(headers, "Host: "+destNetAddr)
 	if user != nil && user.Account != nil {
 		account := user.Account.(*Account)
-		auth := account.GetUsername() + ":" + account.GetPassword()
-		headers = append(headers, "Proxy-Authorization: Basic "+base64.StdEncoding.EncodeToString([]byte(auth)))
+		authmethod := account.GetAuthMethod()
+		var auth string
+		if authmethod == "" {
+			authmethod = "basic"
+		}
+		if authmethod == "basic" {
+			auth = account.GetUsername() + ":" + account.GetPassword()
+			auth = base64.StdEncoding.EncodeToString([]byte(auth))
+			headers = append(headers, "Proxy-Authorization: Basic "+auth)
+			headers = append(headers, "Proxy-Connection: Keep-Alive")
+		} else if authmethod == "uc" {
+			//UC 免流方式有两种，一种直接的Basic认证 另外一种 域名动态认证
+			//因为没root的手机无法获取到免流的uid和token信息，只能抓包获取指定服务器地址的认证信息，故针对特定VPN服务器抓包后填入
+			//此处分为三种模式处理
+			//1. 动态域名认证，且指定了vpn服务器地址的认证信息 。每次更换vpn服务器需要重新抓包对应的认证信息   鱼卡+vpn中转模式
+			//2. Basic Auth认证，且传入的是编码过的认证信息  阿里宝卡模式
+			//3. 动态域名认证，直接传入 uid和token，动态计算认证信息，可以随意切换服务器而无需抓包，只需要每个月1号更新以下uid和token即可。 鱼卡模式
+
+			if strings.Index(account.GetUsername(),"|" ) > 0 && strings.Index(account.GetUsername(),"com.UCMobile" ) >0 {
+				//直接配置的定向vpn免流认证，不动态计算免流认证
+				auth = account.GetUsername()
+			}else if  strings.Index(account.GetUsername(),"Basic " ) > 0 {
+				auth = account.GetUsername()
+			}else {
+				if (strings.Index(destNetAddr,"://") < 0) {
+					destNetAddr = "http://" + destNetAddr;
+				}
+				newUrl, _ := url.Parse(destNetAddr)
+				auth = account.GetUsername() + "|" + account.GetPassword()+"|"+newUrl.Hostname()
+				data := []byte(auth)
+				has := md5.Sum(data)
+				auth = "1|"+account.GetUsername() + "|com.UCMobile|" + fmt.Sprintf("%x", has)
+			}
+			headers = append(headers, "Proxy-Authorization: "+auth)
+			headers = append(headers, "Proxy-Connection: Keep-Alive")
+		} else if authmethod == "qq" {
+			headers = append(headers, "Q-GUID: "+account.GetUsername())
+			headers = append(headers, "Q-Token: "+account.GetPassword())
+		}
+
 	}
-	headers = append(headers, "Proxy-Connection: Keep-Alive")
+
 
 	b := buf.New()
 	b.WriteString(strings.Join(headers, "\r\n") + "\r\n\r\n")
 	if err := buf.WriteAllBytes(writer, b.Bytes()); err != nil {
 		return err




proxy/http/client_test.go

@@ -1 +1,25 @@
 package http
+
+import (
+	"crypto/md5"
+	"fmt"
+	"net/url"
+	"strings"
+	"testing"
+)
+
+func TestLongRequestHeader(t *testing.T) {
+	desturl := "xxx.anysoft.tk:11081"
+	account := "1asd42842484dsad869"
+	token := "f4dsadbf5d8bd3c654dsadf1cb3beasdsa4ab"
+	if (strings.Index(desturl,"://") < 0) {
+		desturl = "http://" + desturl;
+	}
+	newUrl, _ := url.Parse(desturl)
+	fmt.Print(newUrl.Hostname())
+	str := account + "|"+token+"|"+newUrl.Hostname()
+	data := []byte(str)
+	has := md5.Sum(data)
+	md5str := fmt.Sprintf("%x", has)
+	fmt.Print(md5str)
+}
\ No newline at end of file

```


