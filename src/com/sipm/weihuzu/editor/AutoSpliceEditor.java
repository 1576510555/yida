package com.sipm.weihuzu.editor;

import com.sipm.bcf.BCFObj;
import com.sipm.ui.BaseObjUI;
import com.sipm.ui.editor.components.AbstractTextButtonEditor;


import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

/**
 * 物料描述
 * @Author Noah
 * @Date 2022/6/8 20:50
 * @Version 1.0
 */
public class AutoSpliceEditor extends AbstractTextButtonEditor {
    private String field = "NAME；QRSRJ；NO；STANDARDNO；SPECS；BBH；ZMC15；MTLMARK；YS；CC；CS；ZMC19；ZMC20；ZMC21；ZMC22；ZMC23；ZMC24×ZMC25×ZMC26；ZMC27；ZMC28；ZMC29；ZMC30；ZMC31；PP；DHH；YXP；QT";

    public AutoSpliceEditor() {
        initUI();
    }

    @Override
    protected void initUI() {
        super.initUI();
        setEditable(true);
        updateMyUI();
    }

    private void updateMyUI() {

        this.btEdit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                BaseObjUI ui = getOwnerUI();
                if (ui == null) {
                    return;
                }
                StringBuffer value = new StringBuffer();
                String[] tempField = field.split("；");
                for (int i = 0; i < tempField.length; i++) {
                    if (tempField[i].indexOf("×")>0) {
                        //ZMC24×ZMC25×ZMC26
                        String[] x = tempField[i].split("×");
                        Object ZMC24 = getValue("ZMC24");
                        Object ZMC25 = getValue("ZMC25");
                        Object ZMC26 = getValue("ZMC26");
                        if (ZMC24 == null || "".equals(ZMC24)
                                ||ZMC25 == null || "".equals(ZMC25)
                                || ZMC26 == null || "".equals(ZMC26) ) {
                            break;
                        }
                        value.append(ZMC24).append("×").append(ZMC25).append("×").append(ZMC26).append("；");
                    }else{
                        Object editor = getValue(tempField[i]);
                        if (editor == null || "".equals(editor)) {
                            continue;
                        }
                        if ("QRSRJ".equals(tempField[i])) {
                            value.append("（含天地自动化").append(editor).append("）").append("；");
                        }else{
                            value.append(editor).append("；");
                        }
                    }
                }
                // 物料状态  △用完为止，禁用※
                Object WLZT = getValue("WLZT");
                if (WLZT != null && "用完为止".equals(WLZT) ) {
                    value.insert(0,"△");
                }
                if (WLZT != null && WLZT.toString().indexOf("禁用")>=0 ) {
                    value.insert(0,"※");
                }
                // 停止使用物料X
                Object TZSY = getValue("TZSY");
                if (TZSY != null && !"".equals(TZSY) ) {
                    value.append("※").append(TZSY).append("；");
                }
                // 用完为止物料△
                Object YWWZ = getValue("YWWZ");
                if (YWWZ != null && !"".equals(YWWZ) ) {
                    value.append("△").append(YWWZ).append("；");
                }
                // 选用物料√
                Object XYWL = getValue("XYWL");
                if (XYWL != null && !"".equals(XYWL) ) {
                    value.append("√").append(XYWL).append("；");
                }
                String str = value.toString();
                if (str.endsWith("；")) {
                    str = str.substring(0,str.length()-1);
                }

                BCFObj bcfObj = getEditingObj();
                if (str.length() >= 38) {
                    str = "$" + str;
                }
                ui.setFieldEditorValue("WLMS",str);
                ui.setFieldEditorValue("WLCWB",str);
                ui.setFieldEditorValue("WLZFS",str.length());
                ui.setFieldEditorValue("CWBZFS",str.length());

                bcfObj.setFieldValue("WLMS",str);
                bcfObj.setFieldValue("WLCWB",str);
                bcfObj.setFieldValue("CWBZFS",str.length());
                bcfObj.setFieldValue("WLZFS",str.length());
                setValue(str);
                ui.setFieldEditorValue("MAKTXN",str);
                bcfObj.setFieldValue("MAKTXN",str);
            }
        });

        this.txtField.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {
                setLength();
            }

            @Override
            public void keyPressed(KeyEvent event) {
                setLength();
            }

            @Override
            public void keyReleased(KeyEvent e) {
                setLength();
            }
        });

    }

    private Object getValue(String fieldName){
        Object editorValue = ui.getFieldEditorValue(fieldName);
        BCFObj bcfObj = getEditingObj();
        if (editorValue == null || "".equals(editorValue)) {
            editorValue = bcfObj.getFieldValueToString(fieldName);
        }
        if (editorValue == null || "".equals(editorValue)) {
            return null;
        }
        return editorValue;
    }
    private void setLength(){
        BaseObjUI ui = getOwnerUI();
        if (ui ==null) {
            return;
        }
        Object WLMS = ui.getFieldEditorValue("WLMS");
        if (WLMS !=null) {
            ui.setFieldEditorValue("WLZFS",String.valueOf(WLMS).length());
        }
    }

    @Override
    public Object getValue() {
        return this.txtField.getText().trim();
    }


    @Override
    public void setValue(Object obj) {
        setOldValue(obj);
        if (obj != null) {
            this.txtField.setText(obj.toString());
        } else {
            this.txtField.setText("");
        }
    }
}
