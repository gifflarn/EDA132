package DecisionTree;

import java.util.LinkedList;
import java.util.List;

public class TreeNode<T> {

	T data;
	TreeNode<T> parent;
	List<TreeNode<T>> children;

	public TreeNode(T data) {
		this.data = data;
		this.children = new LinkedList<TreeNode<T>>();
	}
	
	public TreeNode<T> addChild(TreeNode child){
		child.parent = this;
		this.children.add(child);
		return child;
	}
	
	public void print(String pre, boolean rec) {
		System.out.print(pre + " ---NODE--- " + data);
		System.out.println();

		if (rec){
			for (TreeNode t : children){
				t.print(pre + "\t", rec);
			}
		}
	}
	
}