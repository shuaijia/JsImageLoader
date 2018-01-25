package com.jia.jsloader.cache;

import android.util.Log;


import com.jia.jsloader.domain.RequestType;
import com.jia.jsloader.domain.RequestVo;
import com.jia.jsloader.net.Net;
import com.jia.jsloader.utils.EncryptUtil;
import com.jia.jsloader.utils.FileUtil;
import com.jia.jsloader.utils.NetCheckUtil;
import com.jia.jsloader.utils.StreamTool;
import com.jia.jsloader.utils.ToastUtil;

import java.io.IOException;
import java.io.InputStream;

/**
 *
 *  数据的缓存
 */
public class JsonCacheUtil {


    /**
     * 这里完成的操作是关于网络的不同的请求类型的分拣操作，
     * 根据不同网络请求方式和类型执行不同的代码逻辑
     * 该方法利用RequestVo封装的解析器Parser完成将字符串解析成为JavaBean的过程
     * @param vo
     * @return
     */
    public static Object getBeanByNetORCache(RequestVo vo) throws IOException {

        //无论get还是post都进行参数的拼接，使用这个拼接内容作为保存数据的标志
        String netUrl1 = NetMapUtil.getTotalUrlFromVo(vo);
        // System.out.println("DOWNLOADBBB网络的URL：：" + netUrl1);
        Log.e("Tag", "url================="+netUrl1);
        Log.i("TAG", "请求的接口----------" + netUrl1);
        //利用整体的Url作为标志生成一个MD5串，使用该加密串作为保存的Json文件的文件名称
        final String md5Url = EncryptUtil.md5(netUrl1);

        /**
         * 关于网络连接正常的情况下的缓存设置与获取！
         * 1、如果选择了缓存数据，首先从缓存中获取数据，并对数据进行判断和操作
         * 2、如果没有选择缓存数据，直接请求网络，获取数据后返回
         *
         * 关于网络未正常连接的情况缓存操作与获取
         * 1、此时选择了缓存数据，直接获取
         *    a、时间未超时，返回数据正常处理
         *    b、时间超时，进行对应的提示
         * 2、没有选择数据缓存，进行对应的提示！
         *
         *
         *
         */
        String result = null;


        if (vo.isSaveDataWithNet()) {
            //利用加密后的网络连接标志从文件中获取数据
            result = FileUtil.getStringFromFile(vo.getDataSaveTimeNoNet(), md5Url);
            if (null != result) {
                return vo.getJsonParser().parseJSON(result);

            } else {
                //此时表示文件中不存在对应的数据，或者文件存储时间已经超时。需要进行网络请求
                result = getStringByNet(vo);
                if (null != result) {
                    final String finalResult = result;
                    new Thread() {
                        @Override
                        public void run() {
                            try {
                                FileUtil.writeJsonToFile(md5Url, finalResult);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }.start();

                    return vo.getJsonParser().parseJSON(result);
                } else {
                    //如果网络返回的数据为null说明网络存在问题！
                    return vo.getJsonParser().parseJSON(null);
                }
            }
        } else {
            //此时没有选择数据缓存，直接请求网络
            result = getStringByNet(vo);
            if (null != result) {
                //如果选择了缓存数据
                if (vo.isSaveDataWithNet()) {
                    final String finalResult = result;
                    new Thread() {
                        @Override
                        public void run() {
                            try {
                                FileUtil.writeJsonToFile(md5Url, finalResult);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }.start();
                }
                return vo.getJsonParser().parseJSON(result);
            } else {
                //如果网络返回的数据为null说明网络存在问题！
                return vo.getJsonParser().parseJSON(null);
            }
        }


    }
//            } else {
//                ToastUtils.showToast("您的手机没有连接网络，请先连接网络！");
////                ToastUtil.showToastSafe(vo.getmContext(), "您的手机没有连接网络，请先连接网络！");
//                /**
//                 * 当前不存在网络连接，但是假如本地数据中具有缓存，仍然可以获取数据
//                 */
//                if(vo.isSaveDataWithNet()){
//                    //获取缓存的数据
//                    result=FileUtil.getStringFromFile(vo.getDataSaveTimeNoNet(),md5Url);
//                    if(null==result){
//                        ToastUtils.showToast("您的缓存数据已经超时，请连接网络！");
//                        return vo.getJsonParser().parseJSON(null);
//                    }else{
//                        return vo.getJsonParser().parseJSON(result);
//                    }
//                }else{
//                    ToastUtils.showToast("您的手机没有连接网络，请先连接网络！");
//                    return vo.getJsonParser().parseJSON(null);
//                }
//
//            }





    private static String getStringByNet(RequestVo vo) throws IOException {
        //无论get还是post都进行参数的拼接，使用这个拼接内容作为保存数据的标志
        String netUrl2= NetMapUtil.getTotalUrlFromVo(vo);
        String result=null;
        InputStream is=null;
        switch (vo.getRequestType()) {

            case RequestType.HTTP_GET:

//                	if(NetworkUtils.isConnectInternet(vo.getmContext())){
                // 进行Http的Get请求操作的时候进行文件操作的时候出现IO异常
                is= Net.getHttpIOByGet(netUrl2);
                if(null==is){
                    System.out.println("数据流为NULL！！");
                }else{
                    result = StreamTool.getStringByIO(is);
                }
//                	}else{
                //ToastUtils.showToastSafe(vo.getmContext(), "现在没有网络！");
//                	}



                break;
            case RequestType.HTTP_POST:

//                	if(NetworkUtils.isConnectInternet(vo.getmContext())){
                if(NetCheckUtil.checkNetworkAvailable(vo.getmContext())){
                    // 进行Http的Post请求操作的时候进行文件操作的时候出现IO异常
                    is= Net.getHttpIOByPost(vo);
                    if(is!=null)
                        result= StreamTool.getStringByIO(is);
                }else{
                    ToastUtil.showToastSafe(vo.getmContext(), "现在没有网络！");
                }


                break;
            case RequestType.HTTPS_GET:

//                	if(NetworkUtils.isConnectInternet(vo.getmContext())){

                // 进行Https的Get请求操作的时候进行文件操作的时候出现IO异常
                // 进行Http的Get请求操作的时候进行文件操作的时候出现IO异常
                is= Net.getHttpIOByGet(netUrl2);
                result = StreamTool.getStringByIO(is);
//                	}else{
//                		ToastUtils.showToastSafe(vo.getmContext(), "现在没有网络！");
//                	}


                break;
            case RequestType.HTTPS_POST:
//                	if(NetworkUtils.isConnectInternet(vo.getmContext())){
                // 进行Https的Post请求操作的时候进行文件操作的时候出现IO异常
                is=Net.getHttpsIOByPost(vo);
                result=StreamTool.getStringByIO(is);
//                	}else{
//                		ToastUtils.showToastSafe(vo.getmContext(), "现在没有网络！");
//                	}


                break;
        }
        return result;
    }

}
