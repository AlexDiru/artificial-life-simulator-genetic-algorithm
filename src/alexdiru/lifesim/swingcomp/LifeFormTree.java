package alexdiru.lifesim.swingcomp;

import alexdiru.lifesim.jgap.ga.CATBehaviourTree;

import javax.swing.*;
import javax.swing.event.TreeModelEvent;
import javax.swing.event.TreeModelListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeModel;

/**
 * Simplifies JTree to allow the life form's behaviour to be shown much easier
 */
public class LifeFormTree extends JTree {

    public LifeFormTree() {
        setTree(new DefaultMutableTreeNode("No fittest gene in first generation"));
    }

    private void setupModel(DefaultMutableTreeNode root) {
        TreeModel treeModel = new DefaultTreeModel(root);
        treeModel.addTreeModelListener(new TreeModelListener() {
            public void treeNodesChanged(TreeModelEvent e) {
                DefaultMutableTreeNode node;
                node = (DefaultMutableTreeNode)(e.getTreePath().getLastPathComponent());
                try {
                    int index = e.getChildIndices()[0];
                    node = (DefaultMutableTreeNode)(node.getChildAt(index));
                } catch (NullPointerException ex) {
                }
            }
            public void treeNodesInserted(TreeModelEvent e) {
            }
            public void treeNodesRemoved(TreeModelEvent e) {
            }
            public void treeStructureChanged(TreeModelEvent e) {
            }
        });
        setModel(treeModel);
    }

    public void setTree(DefaultMutableTreeNode root) {
        setupModel(root);
        for (int i = 0; i < getRowCount(); i++)
            expandRow(i);
    }
}
