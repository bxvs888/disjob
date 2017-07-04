package com.huangyiming.disjob.common.exception;

/**
 * 异常常量类，主要定义异常码
 * @author Disjob
 *
 */
public class EjobErrorMsgConstant {
    //rpc service error code
    public static final int SERVICE_DEFAULT_ERROR_CODE = 10001;
    public static final int SERVICE_REJECT_ERROR_CODE = 10002;
    public static final int SERVICE_TIMEOUT_ERROR_CODE = 10003;

    //job error code
    public static final int JOB_DEFAULT_ERROR_CODE = 20001;
    
    //console error code
    public static final int CONSOLE_DEFAULT_ERROR_CODE = 30001;
    
    //rpc service error code
    public static final EjobErrorMsg SERVICE_DEFAULT_ERROR = new EjobErrorMsg(503, SERVICE_DEFAULT_ERROR_CODE, "service error");
    public static final EjobErrorMsg SERVICE_TIMEOUT = new EjobErrorMsg(503, SERVICE_TIMEOUT_ERROR_CODE, "service request timeout");
    public static final EjobErrorMsg SERVICE_REJECT = new EjobErrorMsg(503, SERVICE_REJECT_ERROR_CODE, "service reject");
    
    //job error code
    
    
    //console error code
    
}
