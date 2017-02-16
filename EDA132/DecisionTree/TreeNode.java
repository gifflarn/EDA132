package DecisionTree;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Spliterator;
import java.util.function.Consumer;

public class TreeNode<T> implements Iterable<TreeNode<T>> {

	T data;
	TreeNode<T> parent;
	List<TreeNode<T>> children;

	public TreeNode(T data) {
		this.data = data;
		this.children = new LinkedList<TreeNode<T>>();
	}

	public TreeNode<T> addChild(T child) {
		TreeNode<T> childNode = new TreeNode<T>(child);
		childNode.parent = this;
		this.children.add(childNode);
		return childNode;
	}
	
	public TreeNode<T> addChild(TreeNode child){
		TreeNode<T> childNode = new TreeNode<T>((T) child.data);
		childNode.parent = this;
		this.children.add(childNode);
		return childNode;
	}

	@Override
	public void forEach(Consumer<? super TreeNode<T>> arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public Iterator<TreeNode<T>> iterator() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Spliterator<TreeNode<T>> spliterator() {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public String toString(){
		return (String) data;
	}
	
	public static String toStringTree(TreeNode node) {
		  final StringBuilder buffer = new StringBuilder();
		  return toStringTreeHelper(node, buffer, new LinkedList<Iterator<TreeNode>>()).toString();
		}
		 
		private static String toStringTreeDrawLines(List<Iterator<TreeNode>> parentIterators, boolean amLast) {
		  StringBuilder result = new StringBuilder();
		  Iterator<Iterator<TreeNode>> it = parentIterators.iterator();
		  while (it.hasNext()) {
		    Iterator<TreeNode> anIt = it.next();
		    if (anIt.hasNext() || (!it.hasNext() && amLast)) {
		      result.append("   |");
		    }
		    else {
		      result.append("    ");
		    }
		  }
		  return result.toString();
		}
		 
		private static StringBuilder toStringTreeHelper(TreeNode node, StringBuilder buffer, List<Iterator<TreeNode>>
		    parentIterators) {
		  if (!parentIterators.isEmpty()) {
		    boolean amLast = !parentIterators.get(parentIterators.size() - 1).hasNext();
		    buffer.append("\n");
		    String lines = toStringTreeDrawLines(parentIterators, amLast);
		    buffer.append(lines);
		    buffer.append("\n");
		    buffer.append(lines);
		    buffer.append("- ");
		  }
		  buffer.append(node.toString());
		  if (node.hasChildren()) {
		    Iterator<TreeNode> it = node.getChildNodes().iterator();
		    parentIterators.add(it);
		    while (it.hasNext()) {
		      TreeNode<String> child = it.next();
		      toStringTreeHelper(child, buffer, parentIterators);
		    }
		    parentIterators.remove(it);
		  }
		  return buffer;
		}

		private Iterable<TreeNode<T>> getChildNodes() {
			
			return children;
		}

		private boolean hasChildren() {
			// TODO Auto-generated method stub
			return !children.isEmpty();
		}
}