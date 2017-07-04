package com.huangyiming.disjob.java.spring;

import com.huangyiming.disjob.java.bean.RpcContainer;
import com.huangyiming.disjob.java.job.AbstractJobAction;
import com.huangyiming.disjob.java.job.EJob;
import com.huangyiming.disjob.quence.Log;

public class SpringJobAction extends AbstractJobAction{

	public SpringJobAction(RpcContainer rpcContainer) {
		super(rpcContainer);
	}

	@Override
	public EJob getEjobAction(String className, String methodName) {
		if("execute".equals(methodName)){
			return SpringWorkFactory.getInstance().getEjob(className);
		}else{
			Log.warn("[ java ] 注册job的 method 参数类型错误，不是 execute。the error method is："+methodName);
			return null ;
		}
	}
}
