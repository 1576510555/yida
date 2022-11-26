package com.sipm.weihuzu.editor;

import com.sipm.ui.table.editor.DefaultCellEditor_Table;

public class SelObjsEditor_Table extends DefaultCellEditor_Table {

    public SelObjsEditor_Table() {
        super(new AutoFillMsEditor());
    }

}