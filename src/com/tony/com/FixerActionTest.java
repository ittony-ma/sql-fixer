package com.tony.com;

/**
 * @author ittony.ma@gmail.com
 * @desc :
 * @date 2020/6/29
 */
public class FixerActionTest {

    public static void main(String[] args) {
        testJson();
    }

    /**
     * 测试参数中含有json
     */
    private static void testJson() {
        String str = "select Preparing: insert into xxxx (task_id,reference_id,event_key,request,response,url ,call_time,http_code,memo,in_user,in_dtm,result) values (?,?,?,?,?,?,?, ?,?,?,UNIX_TIMESTAMP(),?) \n" +
                "2020-06-29 14:05:00.909 DEBUG 18524 --- [pool-2-thread-1] com.triple.wms.dao.HookDao.insertLog     : ==> Parameters: 456(String), null, INVENTORY_ADJUST_QTY(String), {\"batch_id\":\"15923760178064\",\"list\":[{\"in_user\":\"1004\",\"quantity\":1000,\"in_dtm\":1592376017,\"item_number\":\"56852\",\"type\":701,\"warehouse_number\":\"7\",\"reference_no\":\"56852\"}]}(String), null, https://tb1.sayweee.net/wms_in/inventory(String), 3(Integer), null, null, null, false(Boolean)";
        System.out.println(FixerAction.getSql(str));
    }

}
