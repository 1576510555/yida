package com.sipm.server.action;

import com.sipm.base.exception.UnsupportedException;
import com.sipm.bcf.connect.BCFServerBeanProvider;
import com.sipm.bcf.design.DesignNoObj;
import com.sipm.bcf.design.server.listener.DesignNoChangeListener;
import com.sipm.bcf.exception.MustBreakException;
import com.sipm.bcf.server.A.S;
import com.sipm.bcf.server.ServerInvokeContext;
import com.sipm.bcf.util.BCFObjQueryHelper;
import com.sipm.bcf.util.DBUtil;
import org.apache.commons.collections.CollectionUtils;

import java.util.HashMap;
import java.util.Vector;

/**
 * @author: zhang
 */
public class MpartBeforeListener implements DesignNoChangeListener {

    private static final String FILED_PRE = "WK_MPART.";

    @Override
    public void beforChangeAction(ServerInvokeContext context, DesignNoObj designNoObj, String[] tables, int i) throws MustBreakException, UnsupportedException {
        System.out.println("------------进入项目提交前处理--------------");
        for (String table : tables) {
            System.out.println(table);
            if ("WK_MPART".equalsIgnoreCase(table)) {
                String condition = " DEL=0 AND WKAID <> '3' AND DESIGNNO='" + designNoObj.getID() + "'";
                Vector vector = BCFObjQueryHelper.queryByCondition("WK_MPART", condition, null);
                if (!CollectionUtils.isEmpty(vector)) {
                    for (Object item : vector) {
                        //查询编码是否已经写入
                        try {
                            HashMap map = (HashMap) item;
                            String no = toStr(map.get(FILED_PRE + "NO"));
                            String sql = "SELECT ID FROM SIPM17 WHERE MATNR = '" + no + "' AND DEL = 0 AND WKAID <> '3'";
                            Vector query = DBUtil.executeQuery(sql);
                            if (query != null) {
                                HashMap map1 = (HashMap) query.get(0);
                                String id = toStr(map1.get("ID"));
                                System.out.println("执行删除sipm17 =====> " + id);
                                BCFServerBeanProvider.getItemBean().deleteDataItem(context.getClientID(), "WK_MPART", id);
                            }
                            //重新写入sipm17
                            HashMap<String, Object> data = new HashMap<>();
                            data.put("MATNR", map.get(FILED_PRE + "NO"));
                            data.put("MTART", map.get(FILED_PRE + "MTART"));
                            data.put("MAKTX", map.get(FILED_PRE + "MAKTX"));
                            data.put("MAKTX_EN", map.get(FILED_PRE + "ENAME"));
                            data.put("ZMAKTX", map.get(FILED_PRE + "ZMAKTX"));
                            data.put("MEINS", map.get(FILED_PRE + "UNIT"));
                            data.put("MATKL", map.get(FILED_PRE + "ITEMGROUP"));
                            data.put("GROES", map.get(FILED_PRE + "SPECS"));
                            data.put("NTGEW", map.get(FILED_PRE + "DSNWEIGHT"));
                            data.put("GEWEI", map.get(FILED_PRE + "GEWEI"));
                            data.put("NORMT", map.get(FILED_PRE + "CREATOR"));
                            data.put("ZEINR", map.get(FILED_PRE + "MTLMARK"));
                            data.put("BESKZ", map.get(FILED_PRE + "BESKZ"));
                            String sobsl = toStr(map.get(FILED_PRE + "SOBSL"));
                            if (!sobsl.isEmpty()) {
                                sobsl = "50";
                            }
                            data.put("SOBSL", sobsl);
                            DBUtil.insertObject(context, "SIPM17", designNoObj.getID(), data);
                            String updateSql = "UPDATE WK_MPART SET TB_FLAG = '1' WEHBER ID = '" + map.get(FILED_PRE + "ID") + "'";
                            DBUtil.executeUpdate(updateSql);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }
    }

    @Override
    public void afterChangeAction(ServerInvokeContext serverInvokeContext, DesignNoObj designNoObj, String[] strings, int i) {

    }

    private String toStr(Object value) {
        return value == null ? "" : String.valueOf(value);
    }
}
