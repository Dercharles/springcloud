package com.dzjt.cbs.uam.security.admin.vo;

import com.dzjt.cbs.uam.security.common.vo.TreeNode;

/**
 * ${DESCRIPTION}
 *
 * @author XT
 */
public class GroupTree extends TreeNode {
    String label;

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
