package com.sipm.server.action;

import com.sipm.base.exception.UnsupportedException;
import com.sipm.bcf.BCFObj;
import com.sipm.bcf.design.DesignNoObj;
import com.sipm.bcf.design.server.listener.DesignNoChangeListener;
import com.sipm.bcf.exception.MustBreakException;
import com.sipm.bcf.server.ServerInvokeContext;
import com.sipm.bcf.util.BCFObjQueryHelper;
import com.sipm.bcf.util.DBUtil;
import com.sipm.bcf.util.JSONSerialUtil;
import com.sun.istack.internal.NotNull;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

public class ProjectArchiveAction implements DesignNoChangeListener {

    private static final String BIG_PROJECT_NAME = "SIPM86";
    private static final String SMALL_PROJECT_NAME = "SIPM87";
    private static final String RELATION_TABLE_NAME = "SIPM86_SIPM87";
    private static final String WK_PROJ = "WK_PROJ";
    private static final String PROJ_TABLE_NAME = "PROJ";

    private static final String POINT = ".";
    private static final String EQUAL = "=";
    private static final int XNXM_DEFAULT = 0;
    private static final String DESIGN_NO_DEFAULT = "1";

    /**
     * select
     *  SIPM86.ID,
     *  SIPM86.DATAB, 
     *  SIPM86.DATBI, 
     *  SIPM86.BUKRS, 
     *  SIPM86.AUFNR, 
     *  SIPM86.ZYFDXM, 
     *  SIPM86.KTEXT, 
     *  SIPM87.XREF2, 
     *  SIPM87.ZKTEXT, 
     *  SIPM87.XNXM, 
     *  SIPM87.XMJL, 
     *  SIPM87.FZBM, 
     *  SIPM87.CJZ 
     * from SIPM86 
     * left join SIPM86_SIPM87 
     *  on SIPM86.ID = SIPM86_SIPM87.ITEMID1
     *  and SIPM86_SIPM87.DEL = 0
     *  and SIPM86_SIPM87.WKAID = 1
     *  and SIPM86_SIPM87.STATE = 'A'
     * left join SIPM87 
     * on SIPM86_SIPM87.ITEMID2 = SIPM87.ID
     *  and SIPM87.DEL = 0
     *  and SIPM87.WKAID = 1
     *  and SIPM87.STATE = 'A'
     * where SIPM86.AUFNR = #{internalOrderNo}
     *  and SIPM86.DEL = 0
     *  and SIPM86.WKAID = 1
     *  and SIPM86.STATE = 'A'
     * @param internalOrderNo
     * @return
     */
    private String buildSQL(@NotNull String internalOrderNo) {
        StringBuilder sb = new StringBuilder();
        sb.append(" SELECT ")
                .append(BIG_PROJECT_NAME).append(POINT).append("ID")
                .append(" FROM ").append(BIG_PROJECT_NAME)
                .append(" WHERE ").append(BIG_PROJECT_NAME).append(POINT).append("AUFNR").append(EQUAL).append("'").append(internalOrderNo).append("'")
                .append(" AND ").append(BIG_PROJECT_NAME).append(POINT).append("DEL = 0")
                .append(" AND ").append(BIG_PROJECT_NAME).append(POINT).append("WKAID = 1")
                .append(" AND ").append(BIG_PROJECT_NAME).append(POINT).append("STATE = 'A'");
//        sb.append(" SELECT ")
//                .append(BIG_PROJECT_NAME).append(POINT).append("ID").append(", ")
//                .append(BIG_PROJECT_NAME).append(POINT).append("DATAB").append(", ")
//                .append(BIG_PROJECT_NAME).append(POINT).append("DATBI").append(", ")
//                .append(BIG_PROJECT_NAME).append(POINT).append("BUKRS").append(", ")
//                .append(BIG_PROJECT_NAME).append(POINT).append("AUFNR").append(", ")
//                .append(BIG_PROJECT_NAME).append(POINT).append("ZYFDXM").append(", ")
//                .append(BIG_PROJECT_NAME).append(POINT).append("KTEXT").append(", ")
//                .append(SMALL_PROJECT_NAME).append(POINT).append("XREF2").append(", ")
//                .append(SMALL_PROJECT_NAME).append(POINT).append("ZKTEXT").append(", ")
//                .append(SMALL_PROJECT_NAME).append(POINT).append("XNXM").append(", ")
//                .append(SMALL_PROJECT_NAME).append(POINT).append("XMJL").append(", ")
//                .append(SMALL_PROJECT_NAME).append(POINT).append("FZBM").append(", ")
//                .append(SMALL_PROJECT_NAME).append(POINT).append("CJZ")
//                .append(" FROM ").append(BIG_PROJECT_NAME)
//                .append(" LEFT JOIN ").append(RELATION_TABLE_NAME)
//                .append(" ON ").append(BIG_PROJECT_NAME).append(POINT).append("ID").append(EQUAL).append(RELATION_TABLE_NAME).append(POINT).append("ITEMID1")
//                .append(" AND ").append(RELATION_TABLE_NAME).append(POINT).append("DEL = 0")
//                .append(" AND ").append(RELATION_TABLE_NAME).append(POINT).append("WKAID = 1")
//                .append(" AND ").append(RELATION_TABLE_NAME).append(POINT).append("STATE = 'A'")
//                .append(" LEFT JOIN ").append(SMALL_PROJECT_NAME)
//                .append(" ON ").append(SMALL_PROJECT_NAME).append(POINT).append("ID").append(EQUAL).append(RELATION_TABLE_NAME).append(POINT).append("ITEMID2")
//                .append(" AND ").append(SMALL_PROJECT_NAME).append(POINT).append("DEL = 0")
//                .append(" AND ").append(SMALL_PROJECT_NAME).append(POINT).append("WKAID = 1")
//                .append(" AND ").append(SMALL_PROJECT_NAME).append(POINT).append("STATE = 'A'")
//                .append(" WHERE ").append(BIG_PROJECT_NAME).append(POINT).append("AUFNR").append(EQUAL).append("'").append(internalOrderNo).append("'")
//                .append(" AND ").append(BIG_PROJECT_NAME).append(POINT).append("DEL = 0")
//                .append(" AND ").append(BIG_PROJECT_NAME).append(POINT).append("WKAID = 1")
//                .append(" AND ").append(BIG_PROJECT_NAME).append(POINT).append("STATE = 'A'");
        return sb.toString();
    }

    @Override
    public void beforChangeAction(ServerInvokeContext context, DesignNoObj designNoObj, String[] tables, int actionType) throws MustBreakException, UnsupportedException {

    }

    @Override
    public void afterChangeAction(ServerInvokeContext context, DesignNoObj designNoObj, String[] tables, int actionType) {
        System.out.println("------------进入项目归档后处理--------------");
        for (String table : tables) {
            System.out.println(table);
            if (WK_PROJ.equalsIgnoreCase(table)) {
                System.out.println("执行项目归档后处理");
                String condition = " DEL=0 AND WKAID <> '3' AND DESIGNNO='"+designNoObj.getID()+"'";
                Vector vector = BCFObjQueryHelper.queryByCondition(WK_PROJ, condition, null);
                if(CollectionUtils.isEmpty(vector)) {
                    System.out.println("WK_PROJ model query fial");
                    return;
                }
                BCFObj[] objs = new BCFObj[vector.size()];
                vector.toArray(objs);
                // 大项目 内部订单号-ID
                Map<String, String> bigProjectCache = new HashMap();

                for (BCFObj obj : objs) {
                    String internalOrderNo = (String) obj.getFieldValue(PROJ_TABLE_NAME + POINT + "AUFNR");//内部订单号
                    String no = (String) obj.getFieldValue(PROJ_TABLE_NAME + POINT + "NO");
                    if(StringUtils.isNotEmpty(internalOrderNo)){
                        //校验小项目是否存在关系
                        if(checkProjRelationExists(no)){
                            continue;
                        }

                        String bigProjectId = null;
                        if(!bigProjectCache.containsKey(internalOrderNo)){
                            bigProjectId = getBigProjId(internalOrderNo);
                        }else {
                            bigProjectId = bigProjectCache.get(internalOrderNo);
                        }

                        if(bigProjectId == null){
                            System.out.println("查无此内部订单号("+internalOrderNo+")的数据");
                            continue;
                        }

                        HashMap<String, Object> data = new HashMap<>();
                        data.put("XREF2", no);//研发小项目编码
                        data.put("ZKTEXT", obj.getFieldValue(PROJ_TABLE_NAME + POINT + "NAME"));//研发小项目描述
                        data.put("XNXM", XNXM_DEFAULT);//是否虚拟项目
                        data.put("XMJL", obj.getFieldValue(PROJ_TABLE_NAME + POINT + "MANAGER"));//项目经理
                        data.put("FZBM", obj.getFieldValue(PROJ_TABLE_NAME + POINT + "DEPARTMENT"));//负责部门
                        data.put("CJZ", obj.getFieldValue(PROJ_TABLE_NAME + POINT + "CREATOR"));//创建者
                        Map insertObject = null;
                        try {
                            insertObject = DBUtil.insertObject(context, SMALL_PROJECT_NAME, DESIGN_NO_DEFAULT, data);
                            Object insertObjectId = insertObject.get(SMALL_PROJECT_NAME + POINT + "ID");
                            HashMap<String, Object> relationData = new HashMap<>();
                            relationData.put("ITEMID1", bigProjectId);
                            relationData.put("ITEMTN1", BIG_PROJECT_NAME);
                            relationData.put("ITEMID2", insertObjectId);
                            relationData.put("ITEMTN2", SMALL_PROJECT_NAME);
                            DBUtil.insertObject(context, RELATION_TABLE_NAME, DESIGN_NO_DEFAULT, relationData);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
                return;
            }
        }
    }

    private String getBigProjId(String internalOrderNo) {
        String bigProjectId = null;
        String sql = buildSQL(internalOrderNo);
        try {
            Vector<Map> vector = DBUtil.executeQuery(sql);
            if(CollectionUtils.isNotEmpty(vector)){
                bigProjectId = (String) vector.get(0).get(BIG_PROJECT_NAME + POINT + "ID");
                System.out.println("查询到内部订单号为'" + internalOrderNo + "'的数据：\r\n" + JSONSerialUtil.toJSONString(vector));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bigProjectId;
    }

    private boolean checkProjRelationExists(String smallProjectNo) {
        String sql = "SELECT ITEMID2 FROM SIPM87 A,SIPM86_SIPM87 B " +
                "WHERE A.XREF2 = '" + smallProjectNo + "' AND A.ID = B.ITEMID2 AND A.DEL = 0 AND A.WKAID = 1 AND A.STATE = 'A'";
        try {
            Vector vector = DBUtil.executeQuery(sql);
            return vector != null && !vector.isEmpty();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
