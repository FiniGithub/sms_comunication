package com.dzd.utils.encrypt;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPublicKey;
import java.security.interfaces.RSAPrivateCrtKey;

/**
 * 加密常用类之 RSA 的整理<br>
 * 
 * 代码示例：
 * RSABuilder a=new RSABuilder();
 * String[] keys=a.Skey_RSA(1024);  建立长度为1024的RSA密钥
 * 
 * 密钥的形式
 * keys=new String[]{
 * 		 			"65537",
 * 		 			"16674741447994061997579261012926176985198545353137466547869524933931334669216281403886488806249657753533493784796678932075008897796951226659044486111446984628846619223970429496987152783900603558943283901377227945544410335119101071694816383522163141447982503681246172291300383621513720286248884610576147258016485621489250648917837593780275139754912155428884318997276039619383862279406877174082119956529624688077634886792795580691524271133325132006630535655125602019166066643849661941039894279929940761452985756897460897233027794442977134199513791658521949901800552229593250788933865219571671423906705206397480616695801",
 * 		 			"3862793912804154130449186577021304285964940652628790105280301624226704349421572306846585486923147291059180350653572478855650778741067084595535550729267255453181399408857882732246806438579412594898712730071701095095217017986453338884457670241748642956852928435672664109228716971204377700929712470173597626855094871603943654027297320531629337665611673900378771089801785894455461792034854856015176182462173650327383903313690108011062599754158656150600053013871783598784176241471345920211965621246912947063137634681937198441095293133307266714417941622603760520424139101922480576839485856040508611812539681754574520239873",
 * 		 			"139257415755839907075372353557429280575717223444030211475686606529916611160805128043981968940315351861552425828042923239885345671549037718291068297124954496073441310494838163298991828022193262155600800370412272347510990040400526601009950251042090568504874027415243652865901120219175482121130029084887806358441",
 * 		 			"119740419980433171809218154329889199722271928986361706868747021223883788086127196962482509389897234944460878072557386161887272182346780091140437298053665457915536785251306614459938750178137842356477884721613361136731254008849935311400123839268645183181279287496567960443580978068948787763811699302478837674961"
 * 		 	};
 * 
 * keys[0]，keys[2] 作为公私钥，加密和解密时可以调换使用（即加密用0解密用2或加密用2解密用0）
 * a.Enc_RSA("hello周六", keys[0], keys[1]); 
 * a.Dec_RSA(mt, keys[2], keys[1]);
 * 
 * @date 2011-4-20
 * @author MipatchTeam#chenc
 *
 */
public class RSABuilder {
     //创建密钥对生成器，指定加密和解密算法为RSA
    public String[] Skey_RSA(int keylen) throws NoSuchAlgorithmException{//输入密钥长度      
        String[] output = new String[5]; //用来存储密钥的e n d p q       
        try {
            KeyPairGenerator kpg = KeyPairGenerator.getInstance("RSA");         
            kpg.initialize(keylen); //指定密钥的长度，初始化密钥对生成器          
            KeyPair kp = kpg.generateKeyPair(); //生成密钥对
            RSAPublicKey  puk = (RSAPublicKey)kp.getPublic();
            RSAPrivateCrtKey prk = (RSAPrivateCrtKey)kp.getPrivate();
            BigInteger e = puk.getPublicExponent(); // 公钥
            BigInteger n = puk.getModulus();// 模数
            BigInteger d = prk.getPrivateExponent(); // 私钥
            BigInteger p = prk.getPrimeP(); // 基数1
            BigInteger q = prk.getPrimeQ();// 基数2
            output[0]=e.toString();
            output[1]=n.toString();
            output[2]=d.toString();
            output[3]=p.toString();
            output[4]=q.toString();
        } catch (NoSuchAlgorithmException ex) {
            throw ex;
        }
        return output;
    }
    
    //加密在RSA公钥中包含有两个整数信息：e和n。对于明文数字m,计算密文的公式是m的e次方再与n求模。
    public String Enc_RSA(String mingwen,String eStr,String nStr) throws UnsupportedEncodingException{
        String miwen = new String();
        try {
            BigInteger e = new BigInteger(eStr);
            BigInteger n = new BigInteger(nStr);          
            byte[] ptext = mingwen.getBytes("UTF8"); //获取明文的大整数
            BigInteger m = new BigInteger(ptext);
            BigInteger c = m.modPow(e, n);
            miwen = c.toString();
        } catch (UnsupportedEncodingException ex) {
            throw ex;
        }
        return miwen;
    }
    
    //解密
    public String Dec_RSA(String miwen,String dStr,String nStr) throws UnsupportedEncodingException{
        StringBuffer mingwen= new StringBuffer();       
        BigInteger d=new BigInteger(dStr);//获取私钥的参数d,n
        BigInteger n=new BigInteger(nStr);
        BigInteger c=new BigInteger(miwen);        
        BigInteger m=c.modPow(d,n);//解密明文
        byte[] mt=m.toByteArray();//计算明文对应的字符串并输出
        for(int i=0;i<mt.length;i++){
            mingwen.append((char)mt[i]);
        }
        try {
        	return new String(mt,"UTF8");
		} catch (UnsupportedEncodingException e) {
            throw e;
		}
    }
}