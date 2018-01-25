package com.jia.jsloader.cache;



import com.jia.jsloader.domain.RequestVo;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * Created by hm on 2016/3/3.
 *
 *  当前类的操作完成的是协助网络交互中关于Map集合的简单曹组
 */
public class NetMapUtil {

    /**
     * 这个方法的作用
     * 1、生成完整的Get请求连接
     *    无论是get还是post方法，在执行操作的时候会出现多个参数的情况，在RequestVo中定义了参数的集合
     *    为了方便使用，直接利用集合的遍历思路完成关于Get请求的链接拼装
     * 2、对于Get和Post根据其BaseUrl和参数列表生成一个缓存数据的唯一标志
     *
     * 方法过程分析：
     * a、关于数据的缓存操作，需要一个文件名称，对于这个名称是否会出现
     *    同样一个BaseUrl由于参数的不同代表的是两个不同的接口-----会（Get）
     *    对于Post请求会不会出现这种情况呢？？----暂时感觉不会出现
     *
     *    上面的是错误的解释：
     *    正确的解释，只要接口的名称相同，无论传递的参数是多少个请求的是同一个接口，但是这个时候返回的数据
     *    可能存在不同，那么问题出现：：
     *    在进行数据缓存操作的时候怎么操作
     *    利用BaseUrl和参数列表生成一个保存数据的文件标志，这样就能很大程度保证每一份数据都被缓存下来了！
     *
     * @param vo  封装的请求基础类
     * @return
     */
    public static String getTotalUrlFromVo(RequestVo vo){

       // System.out.println("Vo中封装的Map"+vo.getParamStringHashMap().toString());
        Map<String, String> paramsMap=null;
        paramsMap=vo.getParamStringHashMap();
        String encodedParams = "";
        String resultUrl="";
        if (paramsMap!=null&&!paramsMap.isEmpty()) {

            StringBuffer buf = new StringBuffer();
            Set<String> set = paramsMap.keySet();
            Iterator<String> iterator = set.iterator();
            int i = 0;
            while (iterator.hasNext()) {
                String key = (String) iterator.next();
                String value = (String) paramsMap.get(key);

                if ((key == null) || ("".equals(key)) || (value == null)
                        || ("".equals(value))) {
                    continue;
                }
                if (i != 0)
                    buf.append("&");
                try {
                    buf.append(URLEncoder.encode(key, "UTF-8")).append("=")
                            .append(URLEncoder.encode(value, "UTF-8"));
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }

                i++;
            }
            encodedParams=buf.toString();
        }
        if (encodedParams.length() > 0) {
            if (-1 == vo.getBaseUrl().indexOf("?"))
                resultUrl=vo.getBaseUrl()+ "?" + encodedParams;
                //下面代码存在错误
//                vo.setBaseUrl( vo.getBaseUrl() + "?" + encodedParams);
            else {

                resultUrl=vo.getBaseUrl()+ "&" + encodedParams;
                //下面代码存在错误
//                vo.setBaseUrl(vo.getBaseUrl() + "&" + encodedParams);
            }
        }
       // Log.i("TAG", "请求的接口------" + resultUrl);
        return resultUrl;
    }
}
