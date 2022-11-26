package com.sipm.bcf.codeman.app.editor;//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//


import com.sipm.bcf.BCFObj;
import com.sipm.bcf.action.ActionParamParser;
import com.sipm.bcf.codeman.CodeServerBeanProvider;
import com.sipm.bcf.codeman.app.gui.JDlgSIPCodeCreator;
import com.sipm.bcf.codeman.server.CodeBean;
import com.sipm.bcf.commandparam.ParamDefine;
import com.sipm.bcf.connect.BCFServerBeanProvider;
import com.sipm.bcf.nmetadata.MetadataUtil;
import com.sipm.bcf.nmetadata.element.FieldType;
import com.sipm.bcf.util.DBUtil;
import com.sipm.ui.BaseObjUI;
import com.sipm.ui.editor.components.AbstractTextButtonEditor;
import com.sipm.ui.editor.components.delegate.EditableListener;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;
import javax.swing.JPanel;

public class CodeEditor extends AbstractTextButtonEditor {
    @ParamDefine(
            editor = "TextFieldEditor",
            captionKey = "com.sipm.bcf.codeman.CodeEditor_CODEKEY"
    )
    private String CODEKEY = null;
    @ParamDefine(
            editor = "CheckBoxStringEditor",
            captionKey = "com.sipm.bcf.app.editor.SQLQueryEditor_MANUAL"
    )
    private String MANUAL = null;
    @ParamDefine(
            editor = "DefaultDialogNumberEditor",
            captionKey = "com.sipm.bcf.codeman.CodeEditor_WIDTH"
    )
    private String WIDTH = null;
    @ParamDefine(
            editor = "DefaultDialogNumberEditor",
            captionKey = "com.sipm.bcf.codeman.CodeEditor_HEIGHT"
    )
    private String HEIGHT = null;
    private int width = 500;
    private int height = 300;

    public String getCODEKEY() {
        return this.CODEKEY;
    }

    public void setCODEKEY(String var1) {
        this.CODEKEY = var1;
    }

    public String getMANUAL() {
        return this.MANUAL;
    }

    public void setMANUAL(String var1) {
        this.MANUAL = var1;
    }

    public String getWIDTH() {
        return this.WIDTH;
    }

    public void setWIDTH(String var1) {
        this.WIDTH = var1;
    }

    public String getHEIGHT() {
        return this.HEIGHT;
    }

    public void setHEIGHT(String var1) {
        this.HEIGHT = var1;
    }

    public CodeEditor() {
        this.initUI();
    }

    protected void initUI() {
        super.initUI();
        CodeEditor.MyActionListener var1 = new CodeEditor.MyActionListener();
        this.btEdit.setActionCommand("EDIT");
        this.btEdit.addActionListener(var1);
        this.txtField.addKeyListener(new KeyAdapter() {
            public void keyReleased(KeyEvent var1) {
                if (127 == var1.getKeyCode()) {
                    if (!CodeEditor.this.txtField.isEditable()) {
                        if (CodeEditor.this.btEdit.isEnabled()) {
                            CodeEditor.this.txtField.setText("");
                        }
                    }
                }
            }
        });
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

    public Object getValue() {
        String var1 = this.txtField.getText();
        var1 = var1.trim();
        return var1;
    }

    public void setDirectEditable(boolean var1) {
        this.txtField.setEditable(var1);
    }

    public void initialEditor(String var1) {
        super.initialEditor(var1);
        String var2 = this.getParamValue("WIDTH");

        try {
            this.width = Integer.parseInt(var2);
        } catch (Exception var5) {
        }

        var2 = ActionParamParser.getCfgValue(this.strParam, "HEIGHT");

        try {
            this.height = Integer.parseInt(var2);
        } catch (Exception var4) {
        }

        this.addPropertyListeners();
    }

    public void setValue(Object var1) {
        this.setOldValue(var1);
        if (var1 != null) {
            this.txtField.setText(var1.toString());
        } else {
            this.txtField.setText("");
        }

    }

    public String getCodeKey() {
        return this.getParamValue("CODEKEY");
    }

    protected void addPropertyListeners() {
        this.getProperties().addPropertyChangeListener("EDITABLE", new EditableListener(this.btEdit));
        this.getProperties().addPropertyChangeListener("EDITABLE", new EditableListener(this.txtField));
    }

    public void setEditable(boolean var1) {
        super.setEditable(var1);
        this.btEdit.setEnabled(var1);
        this.txtField.setEditable(this.isDirectEditable && var1);
    }

    public boolean isSequenceEditor() {
        return true;
    }

    class DLgCode extends JPanel {
        private static final long serialVersionUID = 7682353560318734485L;

        DLgCode() {
        }
    }

    class MyActionListener implements ActionListener {
        public MyActionListener() {
        }

        public void actionPerformed(ActionEvent var1) {
            if (var1.getActionCommand().equalsIgnoreCase("EDIT")) {
                String var2 = null;
                BaseObjUI var3 = CodeEditor.this.getOwnerUI();
                if (var3 != null) {
                    FieldType var4 = CodeEditor.this.getAdvanceFieldInfo();
                    var2 = MetadataUtil.getTableName(var4.getTableField());
                }

                String var11 = CodeEditor.this.getCodeKey();
                String var5 = JDlgSIPCodeCreator.showMe((Component) var1.getSource(), var11, CodeEditor.this.width, CodeEditor.this.height);
                if (var5 != null) {
                    try {
                        CodeBean var6 = CodeServerBeanProvider.getCodeManBean();
                        var6.addNewCodeObj(var2, var11, var5);
                        String var7 = "";
                        if (CodeEditor.this.oldValue != null && "".equals(CodeEditor.this.oldValue)) {
                            var7 = "'" + DBUtil.convertSQLQueryTransferCode(CodeEditor.this.oldValue.toString(), "=") + "'";
                        }

                        String var8 = CodeEditor.this.txtField.getText();
                        if (var8 != null && var8.length() > 0) {
                            if (var7.length() > 0) {
                                var7 = var7 + ",";
                            }

                            var8 = DBUtil.convertSQLQueryTransferCode(var8, "=");
                            var7 = var7 + "'" + var8 + "'";
                        }

                        if (var7 != null && var7.length() > 0) {
                            StringBuffer var9 = new StringBuffer();
                            var9.append("update PLMCODEOBJS set TYPE='UNUSER'");
                            var9.append(" where OBJTABLE='" + var2 + "'");
                            var9.append(" and TYPE='NEW'");
                            var9.append(" and CODENAME in ( " + var7 + ")");
                            BCFServerBeanProvider.getDBBean().executeUpdate(var9.toString());
                        }
                    } catch (Exception var10) {
                        var10.printStackTrace();
                    }
                    //自动带出物料组
                    BaseObjUI ui = getOwnerUI();
                    if (ui != null) {
                        String fjId = var5.substring(0, var5.length() - 3);
                        String sql = "SELECT MATKL,MTART FROM SIPM23  WHERE PRECODE = '" + fjId + "' AND DEL = 0 AND WKAID <> '3'";
                        try {
                            Vector vector = DBUtil.executeQuery(sql);
                            if (vector != null && vector.size() > 0) {
                                Map map = (Map) vector.get(0);
                                String matkl = toStr(map.get("MATKL"));
                                String mtart = toStr(map.get("MTART"));
                                BCFObj bcfObj = getEditingObj();
                                ui.setFieldEditorValue("ITEMGROUP", matkl);
                                ui.setFieldEditorValue("MTART", mtart);
                                bcfObj.setFieldValue("ITEMGROUP", matkl);
                                bcfObj.setFieldValue("MTART", mtart);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    CodeEditor.this.txtField.setText(var5);

                }
            }

        }
    }

    private String toStr(Object value) {
        return value == null ? "" : String.valueOf(value);
    }

}
