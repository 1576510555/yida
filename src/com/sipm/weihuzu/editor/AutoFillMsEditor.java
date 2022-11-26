package com.sipm.weihuzu.editor;

import com.sipm.bcf.BCFObj;
import com.sipm.ui.BaseObjUI;
import com.sipm.ui.editor.components.AbstractTextButtonEditor;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

/**
 * ÎïÁÏÃèÊöÌî³ä
 *
 * @author: zhang
 */
public class AutoFillMsEditor extends AbstractTextButtonEditor {

    public AutoFillMsEditor() {
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
                Object asuser03 = getValue("ASUSER03");
                Object name = getValue("NAME");
                if (asuser03 != null && !"".equals(asuser03)) {
                    value.append(asuser03);
                }
                if (name != null && !"".equals(name)) {
                    if (value.length() > 0) {
                        value.append(" ");
                    }
                    value.append(name);
                }
                BCFObj bcfObj = getEditingObj();
                setValue(value);
                ui.setFieldEditorValue("MAKTX", value);
                bcfObj.setFieldValue("MAKTX", value);
                if (value.length() > 40) {
                    ui.setFieldEditorValue("ZMAKTX", value);
                    bcfObj.setFieldValue("ZMAKTX", value);
                }
            }
        });
        this.txtField.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {

            }

            @Override
            public void keyPressed(KeyEvent event) {

            }

            @Override
            public void keyReleased(KeyEvent e) {

            }
        });
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

    private Object getValue(String fieldName) {
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
}
